//! Biogenesis window for the Rust translation: the world, a status line, the
//! statistics window. The simulation runs on its own thread (sim.rs).
//!
//! biogenesis-rust [random seed | save file]  (Java: java -jar biogenesis.jar [random seed])

mod sim;
mod stats;

use std::path::PathBuf;
use std::sync::Arc;
use std::time::Duration;

use eframe::egui::{self, Color32, Key, Pos2, Rect, Sense, Vec2, pos2, vec2};
use sim::{Cmd, NewWorld, SharedRef, View};

#[global_allocator]
static GLOBAL: mimalloc::MiMalloc = mimalloc::MiMalloc;

/// Java's RepaintWorldStrategy.
#[derive(Clone, Copy, PartialEq)]
enum Strategy {
    Always,
    OnlyWhenMainWindowIsInFocus,
    WhenAnyAppWindowIsInFocus,
}

/// What part of the world the view shows.
struct Camera {
    /// The world point at the view's top left corner.
    x: f32,
    y: f32,
    /// Points per world pixel (Java's VisibleWorld.zoomFactor).
    zoom: f32,
}

impl Camera {
    const MIN_ZOOM: f32 = 0.02;
    const MAX_ZOOM: f32 = 16.0;

    /// Keeps the view inside the world, or the world centered if it is smaller.
    fn clamp(&mut self, view: Vec2, world: Vec2) {
        let (vw, vh) = (view.x / self.zoom, view.y / self.zoom);
        self.x = if vw >= world.x { (world.x - vw) / 2.0 } else { self.x.clamp(0.0, world.x - vw) };
        self.y = if vh >= world.y { (world.y - vh) / 2.0 } else { self.y.clamp(0.0, world.y - vh) };
    }

    /// Zooms by `f`, keeping the world point under `at` (relative to the view) in place.
    fn zoom_by(&mut self, f: f32, at: Vec2) {
        let z = (self.zoom * f).clamp(Self::MIN_ZOOM, Self::MAX_ZOOM);
        self.x += at.x / self.zoom - at.x / z;
        self.y += at.y / self.zoom - at.y / z;
        self.zoom = z;
    }

    fn fit(&mut self, view: Vec2, world: Vec2) {
        self.zoom = (view.x / world.x).min(view.y / world.y).min(1.0);
    }

    fn to_screen(&self, origin: Pos2, x: f32, y: f32) -> Pos2 {
        pos2(origin.x + (x - self.x) * self.zoom, origin.y + (y - self.y) * self.zoom)
    }
}

struct App {
    shared: SharedRef,
    status: sim::Status,
    lines: Vec<sim::Line>,
    cam: Camera,
    world_size: Vec2,
    repaint_world: bool,
    strategy: Strategy,
    delay_ms: u32,
    stats_open: bool,
    stats_focused: bool,
    stats: Option<Arc<sim::Stats>>,
    new_world: Option<NewWorld>,
    defaults: NewWorld,
    /// BIOGENESIS_SCREENSHOT=<dir>: the window's picture goes there, once, after a while.
    screenshot: Option<(PathBuf, std::time::Instant)>,
}

/// Writes a window's picture as a binary PPM.
fn write_ppm(path: PathBuf, image: &egui::ColorImage) {
    let mut out = format!("P6 {} {} 255\n", image.size[0], image.size[1]).into_bytes();
    for p in &image.pixels {
        out.extend_from_slice(&[p.r(), p.g(), p.b()]);
    }
    if let Err(e) = std::fs::write(&path, out) {
        eprintln!("{}: {e}", path.display());
    }
}

fn main() -> eframe::Result {
    let arg = std::env::args_os().nth(1);
    let given_seed = arg.as_ref().and_then(|a| a.to_str()?.parse::<i64>().ok());
    let seed = given_seed.unwrap_or_else(|| {
        std::time::SystemTime::now().duration_since(std::time::UNIX_EPOCH).map_or(0, |d| d.as_millis() as i64)
    });
    // Before the simulation thread starts (see sim::defaults).
    let defaults = sim::defaults(seed);
    let first = match arg {
        Some(p) if given_seed.is_none() => vec![Cmd::Load(PathBuf::from(p)), Cmd::Run(true)],
        _ => vec![Cmd::New(defaults)],
    };
    // Taking screenshots for checking a change: the windows stay hidden and
    // the app doesn't take the focus.
    let background = std::env::var_os("BIOGENESIS_SCREENSHOT").is_some();
    let mut options = eframe::NativeOptions {
        viewport: egui::ViewportBuilder::default()
            .with_title("Biogenesis (Rust)")
            .with_inner_size([1280.0, 860.0])
            .with_visible(!background)
            .with_active(!background),
        ..Default::default()
    };
    #[cfg(target_os = "macos")]
    if background {
        use winit::platform::macos::{ActivationPolicy, EventLoopBuilderExtMacOS};
        options.event_loop_builder = Some(Box::new(|b| {
            b.with_activation_policy(ActivationPolicy::Prohibited).with_activate_ignoring_other_apps(false);
        }));
    }
    eframe::run_native(
        "Biogenesis",
        options,
        Box::new(move |cc| {
            cc.egui_ctx.set_theme(egui::Theme::Dark);
            // Hidden windows aren't painted: the statistics go inside the main window.
            cc.egui_ctx.set_embed_viewports(background);
            let shared = sim::start(cc.egui_ctx.clone(), first);
            Ok(Box::new(App {
                shared,
                status: Default::default(),
                lines: Vec::new(),
                cam: Camera { x: 0.0, y: 0.0, zoom: 1.0 },
                world_size: Vec2::ZERO,
                repaint_world: true,
                strategy: Strategy::Always,
                delay_ms: std::env::var("BIOGENESIS_DELAY_MS").ok().and_then(|d| d.parse().ok()).unwrap_or(20),
                stats_open: std::env::var_os("BIOGENESIS_STATS").is_some(),
                stats_focused: false,
                stats: None,
                new_world: None,
                defaults,
                screenshot: std::env::var_os("BIOGENESIS_SCREENSHOT")
                    .map(|d| (PathBuf::from(d), std::time::Instant::now() + Duration::from_secs(8))),
            }))
        }),
    )
}

impl App {
    fn send(&self, c: Cmd) {
        self.shared.lock().unwrap().cmds.push(c);
    }

    fn open(&self) {
        if let Some(p) = rfd::FileDialog::new().add_filter("Biogenesis save", &["save"]).pick_file() {
            self.send(Cmd::Load(p));
        }
    }

    fn save_as(&self) {
        let mut d = rfd::FileDialog::new().add_filter("Biogenesis save", &["save"]).set_file_name("world.save");
        if let Some(f) = &self.status.file {
            if let (Some(dir), Some(name)) = (f.parent(), f.file_name()) {
                d = d.set_directory(dir).set_file_name(name.to_string_lossy());
            }
        }
        if let Some(p) = d.save_file() {
            self.send(Cmd::Save(p));
        }
    }

    fn save(&self) {
        match &self.status.file {
            Some(f) => self.send(Cmd::Save(f.clone())),
            None => self.save_as(),
        }
    }

    /// Utils.repaintWorld()
    fn repaint(&self, main_focused: bool) -> bool {
        self.repaint_world
            && match self.strategy {
                Strategy::Always => true,
                Strategy::OnlyWhenMainWindowIsInFocus => main_focused,
                Strategy::WhenAnyAppWindowIsInFocus => main_focused || (self.stats_open && self.stats_focused),
            }
    }

    fn menu(&mut self, ui: &mut egui::Ui) {
        egui::MenuBar::new().ui(ui, |ui| {
            ui.menu_button("World", |ui| {
                if ui.button("New world...").clicked() {
                    let seed = self.defaults.seed.wrapping_add(self.status.time).wrapping_add(self.status.frame + 1);
                    self.new_world = Some(NewWorld { seed, ..self.defaults });
                }
                if ui.button("Open...").clicked() {
                    self.open();
                }
                if ui.button("Save").clicked() {
                    self.save();
                }
                if ui.button("Save as...").clicked() {
                    self.save_as();
                }
                ui.separator();
                if ui.button("Quit").clicked() {
                    ui.send_viewport_cmd(egui::ViewportCommand::Close);
                }
            });
            ui.menu_button("Simulation", |ui| {
                let running = self.status.running;
                if ui.button(if running { "Pause (space)" } else { "Run (space)" }).clicked() {
                    self.send(Cmd::Run(!running));
                }
                if ui.button("Step one frame (.)").clicked() {
                    self.send(Cmd::Step);
                }
            });
            ui.menu_button("View", |ui| {
                ui.checkbox(&mut self.stats_open, "Statistics window");
                ui.separator();
                ui.checkbox(&mut self.repaint_world, "Repaint the world");
                ui.add_enabled_ui(self.repaint_world, |ui| {
                    ui.radio_value(&mut self.strategy, Strategy::Always, "Always");
                    ui.radio_value(&mut self.strategy, Strategy::OnlyWhenMainWindowIsInFocus, "Only when the main window is in focus");
                    ui.radio_value(&mut self.strategy, Strategy::WhenAnyAppWindowIsInFocus, "When any window of the app is in focus");
                });
                ui.add(egui::Slider::new(&mut self.delay_ms, 0..=100).text("ms per frame while repainting"));
                ui.separator();
                if ui.button("Zoom 1:1 (R)").clicked() {
                    self.cam.zoom = 1.0;
                }
                if ui.button("Fit the world (F)").clicked() {
                    self.cam.fit(ui.ctx().content_rect().size(), self.world_size);
                }
            });
        });
    }

    fn status_line(&self, ui: &mut egui::Ui, repaint: bool) {
        let s = &self.status;
        ui.horizontal(|ui| {
            if let Some(e) = &s.error {
                ui.colored_label(Color32::RED, format!("The simulation stopped: {e}"));
                return;
            }
            ui.label(format!(
                "Time {}  Frame {:3}  Population {} ({} with corpses)  {:.0} frames/s  Zoom x{:.2}",
                s.time, s.frame, s.population, s.organisms, s.fps, self.cam.zoom
            ));
            ui.separator();
            ui.label(if s.running { "running" } else { "paused" });
            if !repaint {
                ui.separator();
                ui.colored_label(Color32::YELLOW, "not repainting");
            }
            ui.separator();
            match (&s.file, s.seed) {
                (_, Some(seed)) => ui.label(format!("{}x{} seed {seed}", s.width, s.height)),
                (Some(f), None) => ui.label(f.display().to_string()),
                _ => ui.label(""),
            };
            if !s.message.is_empty() {
                ui.separator();
                ui.label(&s.message);
            }
            ui.with_layout(egui::Layout::right_to_left(egui::Align::Center), |ui| {
                ui.weak("drag/scroll/WASD: move  Q/E/pinch: zoom  R: 1:1  F: fit  space: pause");
            });
        });
    }

    fn keys(&mut self, ui: &egui::Ui, view: Vec2) {
        if ui.ctx().egui_wants_keyboard_input() {
            return;
        }
        let (dt, shift) = ui.input(|i| (i.stable_dt.min(0.1), i.modifiers.shift));
        let down = |k| ui.input(|i| i.key_down(k));
        let pressed = |k| ui.input(|i| i.key_pressed(k));
        let speed = if shift { 2400.0 } else { 600.0 } * dt / self.cam.zoom;
        let mut moving = false;
        for (k, dx, dy) in [(Key::W, 0.0, -1.0), (Key::A, -1.0, 0.0), (Key::S, 0.0, 1.0), (Key::D, 1.0, 0.0)] {
            if down(k) {
                self.cam.x += dx * speed;
                self.cam.y += dy * speed;
                moving = true;
            }
        }
        for (k, f) in [(Key::Q, 1.04f32), (Key::E, 1.0 / 1.04)] {
            if down(k) {
                self.cam.zoom_by(f.powf(dt * 60.0), view / 2.0);
                moving = true;
            }
        }
        if pressed(Key::R) {
            self.cam.zoom_by(1.0 / self.cam.zoom, view / 2.0);
        }
        if pressed(Key::F) {
            self.cam.fit(view, self.world_size);
        }
        if pressed(Key::Space) {
            self.send(Cmd::Run(!self.status.running));
        }
        if pressed(Key::Period) {
            self.send(Cmd::Step);
        }
        if moving {
            ui.ctx().request_repaint();
        }
    }

    fn world_view(&mut self, ui: &mut egui::Ui, focused: bool) -> Rect {
        let (rect, resp) = ui.allocate_exact_size(ui.available_size(), Sense::click_and_drag());
        if resp.dragged() {
            let d = resp.drag_delta() / self.cam.zoom;
            self.cam.x -= d.x;
            self.cam.y -= d.y;
        }
        if resp.hovered() {
            let (scroll, zoom, pointer) = ui.input(|i| (i.smooth_scroll_delta, i.zoom_delta(), i.pointer.hover_pos()));
            if zoom != 1.0 {
                self.cam.zoom_by(zoom, pointer.map_or(rect.size() / 2.0, |p| p - rect.min));
            } else {
                self.cam.x -= scroll.x / self.cam.zoom;
                self.cam.y -= scroll.y / self.cam.zoom;
            }
        }
        if focused {
            self.keys(ui, rect.size());
        }
        self.cam.clamp(rect.size(), self.world_size);

        let painter = ui.painter_at(rect);
        painter.rect_filled(rect, 0.0, Color32::from_gray(48));
        let world = Rect::from_min_max(
            self.cam.to_screen(rect.min, 0.0, 0.0),
            self.cam.to_screen(rect.min, self.world_size.x, self.world_size.y),
        );
        painter.rect_filled(world, 0.0, Color32::BLACK);
        painter.add(egui::Shape::mesh(self.mesh(rect.min, ui.ctx().pixels_per_point())));
        rect
    }

    /// The lines as one mesh: a quad each, `zoom` wide (at least a pixel),
    /// covering the pixels Java's 1-pixel drawLine would (square ends).
    fn mesh(&self, origin: Pos2, pixels_per_point: f32) -> egui::Mesh {
        let mut mesh = egui::Mesh::default();
        mesh.reserve_vertices(self.lines.len() * 4);
        mesh.reserve_triangles(self.lines.len() * 2);
        let half = self.cam.zoom.max(1.0 / pixels_per_point) / 2.0;
        for l in &self.lines {
            let a = self.cam.to_screen(origin, l.x1 as f32 + 0.5, l.y1 as f32 + 0.5);
            let b = self.cam.to_screen(origin, l.x2 as f32 + 0.5, l.y2 as f32 + 0.5);
            let d = b - a;
            let len = d.length();
            let u = if len > 1e-3 { d * (half / len) } else { vec2(half, 0.0) };
            let n = vec2(-u.y, u.x);
            let c = Color32::from_rgb((l.rgb >> 16) as u8, (l.rgb >> 8) as u8, l.rgb as u8);
            let i = mesh.vertices.len() as u32;
            mesh.colored_vertex(a - u + n, c);
            mesh.colored_vertex(a - u - n, c);
            mesh.colored_vertex(b + u - n, c);
            mesh.colored_vertex(b + u + n, c);
            mesh.add_triangle(i, i + 1, i + 2);
            mesh.add_triangle(i, i + 2, i + 3);
        }
        mesh
    }

    fn new_world_dialog(&mut self, ctx: &egui::Context) {
        let Some(n) = &mut self.new_world else { return };
        let (mut create, mut cancel) = (false, false);
        egui::Window::new("New world").collapsible(false).resizable(false).show(ctx, |ui| {
            egui::Grid::new("new world").num_columns(2).show(ui, |ui| {
                ui.label("Seed");
                ui.add(egui::DragValue::new(&mut n.seed));
                ui.end_row();
                ui.label("Width");
                ui.add(egui::DragValue::new(&mut n.width).range(100..=100_000));
                ui.end_row();
                ui.label("Height");
                ui.add(egui::DragValue::new(&mut n.height).range(100..=100_000));
                ui.end_row();
                ui.label("Initial organisms");
                ui.add(egui::DragValue::new(&mut n.organisms).range(1..=1_000_000));
                ui.end_row();
            });
            ui.horizontal(|ui| {
                create = ui.button("Create").clicked();
                cancel = ui.button("Cancel").clicked();
            });
        });
        if create {
            let n = *n;
            self.send(Cmd::New(n));
        }
        if create || cancel {
            self.new_world = None;
        }
    }
}

impl eframe::App for App {
    /// Runs while the window is hidden too.
    fn logic(&mut self, ctx: &egui::Context, _frame: &mut eframe::Frame) {
        // Hidden, the window isn't drawn, unless asked: measuring what drawing costs.
        if std::env::var_os("BIOGENESIS_PAINT_HIDDEN").is_some() {
            ctx.send_viewport_cmd(egui::ViewportCommand::RequestPaintWhileHidden);
        }
        if let Some((dir, at)) = &self.screenshot {
            if std::time::Instant::now() >= *at {
                let main = dir.join("main.ppm");
                ctx.request_screenshot(move |image| write_ppm(main, &image));
                self.screenshot = None;
            } else {
                ctx.request_repaint_after(Duration::from_millis(250));
            }
        }
    }

    fn ui(&mut self, ui: &mut egui::Ui, _frame: &mut eframe::Frame) {
        let ctx = ui.ctx().clone();
        let main_focused = ui.input(|i| i.focused);
        let repaint = self.repaint(main_focused);
        {
            let mut s = self.shared.lock().unwrap();
            self.status = s.status.clone();
            if let Some(p) = s.picture.take() {
                s.spare = std::mem::replace(&mut self.lines, p);
            }
            if let Some(st) = s.stats.take() {
                self.stats = Some(st);
            }
            s.want_stats = self.stats_open;
            s.delay_ms = self.delay_ms;
        }
        let size = vec2(self.status.width as f32, self.status.height as f32);
        if size != self.world_size {
            self.world_size = size;
            self.cam = Camera { x: 0.0, y: 0.0, zoom: 1.0 };
        }

        egui::Panel::top("menu").show(ui, |ui| self.menu(ui));
        egui::Panel::bottom("status").show(ui, |ui| self.status_line(ui, repaint));
        let rect = egui::CentralPanel::default().show(ui, |ui| self.world_view(ui, main_focused)).inner;
        if !repaint {
            let why = if self.repaint_world { "Not repainting: the window is not in focus" } else { "Not repainting" };
            ui.painter().text(rect.left_top() + vec2(8.0, 8.0), egui::Align2::LEFT_TOP, why,
                egui::FontId::proportional(14.0), Color32::YELLOW);
        }
        self.new_world_dialog(&ctx);
        if self.stats_open {
            self.stats_focused = stats::show(&ctx, self.stats.as_deref(), &mut self.stats_open);
        } else {
            self.stats_focused = false;
        }

        // Ask for pictures of what the view shows now.
        let view = View {
            x0: self.cam.x,
            y0: self.cam.y,
            x1: self.cam.x + rect.width() / self.cam.zoom,
            y1: self.cam.y + rect.height() / self.cam.zoom,
        };
        self.shared.lock().unwrap().view = repaint.then_some(view);
        // The status line, while no pictures come.
        ctx.request_repaint_after(Duration::from_millis(250));
    }
}
