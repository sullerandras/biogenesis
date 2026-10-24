//! The simulation thread. As in Java (MainWindow's "Main Worker Thread"),
//! the world advances on its own thread and the window only shows it. The
//! translated code is single-threaded (`Rc` objects, statics), so all of it
//! runs on this thread; the window gets copies: the lines of the organisms in
//! view (drawn by the translated `Organism.draw`) and the statistics.

use std::path::PathBuf;
use std::sync::{Arc, Mutex};
use std::time::{Duration, Instant};

use biogenesis_core::generated::{Organism, World, WorldStatistics, utils};
use biogenesis_core::rt::{Color, Graphics, JRef, Random, Stub};
use biogenesis_core::save;

#[derive(Clone, Copy, Debug)]
pub struct NewWorld {
    pub seed: i64,
    pub width: i32,
    pub height: i32,
    pub organisms: i32,
}

pub enum Cmd {
    New(NewWorld),
    Load(PathBuf),
    Save(PathBuf),
    Run(bool),
    Step,
}

/// One `Graphics.drawLine`, in world coordinates.
#[derive(Clone, Copy)]
pub struct Line {
    pub x1: i32,
    pub y1: i32,
    pub x2: i32,
    pub y2: i32,
    pub rgb: u32,
}

/// The part of the world the window shows, in world coordinates.
#[derive(Clone, Copy, PartialEq, Debug)]
pub struct View {
    pub x0: f32,
    pub y0: f32,
    pub x1: f32,
    pub y1: f32,
}

#[derive(Clone, Default)]
pub struct Status {
    pub width: i32,
    pub height: i32,
    pub time: i64,
    pub frame: i64,
    pub population: i32,
    pub organisms: i32,
    pub running: bool,
    /// Simulated frames per second.
    pub fps: f64,
    /// The seed of a world made here (None: loaded).
    pub seed: Option<i64>,
    pub file: Option<PathBuf>,
    /// The last save/load result.
    pub message: String,
    /// The simulation stopped (a Java exception, or a bad save file).
    pub error: Option<String>,
}

/// A copy of `WorldStatistics` for the charts.
#[derive(Default)]
pub struct Stats {
    pub time: i64,
    pub population: Vec<f64>,
    pub clades: Vec<f64>,
    pub births: Vec<f64>,
    pub deaths: Vec<f64>,
    /// (name, Java color, log10(amount / 100) as in Java)
    pub atmosphere: Vec<(&'static str, u32, Vec<f64>)>,
    pub generations: Vec<f64>,
    /// The generation of `generations[0]`.
    pub min_generation: i32,
    /// (label, value) for the table next to the charts.
    pub table: Vec<(&'static str, String)>,
}

/// What the window and the simulation thread exchange.
#[derive(Default)]
pub struct Shared {
    pub cmds: Vec<Cmd>,
    /// Set while the window repaints the world: the simulation draws the
    /// organisms in this part into `picture`.
    pub view: Option<View>,
    /// Utils.DELAY: while the world is repainted, a frame takes at least this.
    pub delay_ms: u32,
    /// The latest frame's lines; the window takes it.
    pub picture: Option<Vec<Line>>,
    /// A used picture handed back, to draw the next one into.
    pub spare: Vec<Line>,
    pub want_stats: bool,
    pub stats: Option<Arc<Stats>>,
    pub status: Status,
}

pub type SharedRef = Arc<Mutex<Shared>>;

/// The Java defaults (Utils.DEF_WORLD_WIDTH...). Called before the
/// simulation thread starts: after that, only that thread touches the statics.
pub fn defaults(seed: i64) -> NewWorld {
    NewWorld { seed, width: utils::WORLD_WIDTH(), height: utils::WORLD_HEIGHT(), organisms: utils::INITIAL_ORGANISMS() }
}

pub fn start(ctx: eframe::egui::Context, first: Vec<Cmd>) -> SharedRef {
    let shared: SharedRef = Arc::new(Mutex::new(Shared { cmds: first, ..Default::default() }));
    let s = shared.clone();
    std::thread::Builder::new()
        .name("simulation".into())
        .stack_size(64 << 20)
        .spawn(move || {
            let _activity = user_initiated();
            let r = std::panic::catch_unwind(std::panic::AssertUnwindSafe(|| run(&s, &ctx)));
            if let Err(e) = r {
                let what = e.downcast_ref::<&str>().map(|s| s.to_string())
                    .or_else(|| e.downcast_ref::<String>().cloned())
                    .unwrap_or_else(|| "panic".into());
                let mut s = s.lock().unwrap_or_else(|p| p.into_inner());
                s.status.error = Some(what);
                s.status.running = false;
                ctx.request_repaint();
            }
        })
        .expect("spawn the simulation thread");
    shared
}

/// macOS may slow an app down that is in the background (hidden, or behind
/// other windows: App Nap; a background priority makes the simulation 5
/// times slower). The simulation is what the user asked for, also while
/// they look elsewhere. Returns what keeps it so.
fn user_initiated() -> impl Sized {
    #[cfg(target_os = "macos")]
    {
        use objc2_foundation::{NSActivityOptions, NSProcessInfo, NSString};
        NSProcessInfo::processInfo().beginActivityWithOptions_reason(
            NSActivityOptions::UserInitiatedAllowingIdleSystemSleep,
            &NSString::from_str("Running the simulation"),
        )
    }
}

fn new_world(n: &NewWorld) -> JRef<World> {
    utils::set_WORLD_WIDTH(n.width);
    utils::set_WORLD_HEIGHT(n.height);
    utils::set_INITIAL_ORGANISMS(n.organisms);
    utils::set_random(Random::new_seeded(n.seed));
    let world = World::new(Stub::NONNULL);
    World::genesis(&world);
    world
}

fn run(shared: &SharedRef, ctx: &eframe::egui::Context) {
    let mut world: Option<JRef<World>> = None;
    let (mut seed, mut file, mut message) = (None, None, String::new());
    let mut running = false;
    // The view the last picture was drawn for, and the stats' time.
    let mut drawn: Option<View> = None;
    let mut stats_time = -1;
    let mut fps = (Instant::now(), 0u32, 0.0);
    let mut prev = Instant::now();
    let mut ahead_nanos: i64 = 0;
    let log_fps = std::env::var_os("BIOGENESIS_LOG_FPS").is_some();
    // For the log: the time spent advancing the world, drawing, and on the statistics.
    let mut spent = [Duration::ZERO; 3];
    loop {
        let (cmds, view, delay_ms, want_stats) = {
            let mut s = shared.lock().unwrap();
            (std::mem::take(&mut s.cmds), s.view, s.delay_ms, s.want_stats)
        };
        let mut changed = false;
        for c in cmds {
            match c {
                Cmd::New(n) => {
                    world = Some(new_world(&n));
                    (seed, file, message) = (Some(n.seed), None, String::new());
                    running = true;
                }
                Cmd::Load(p) => match std::fs::read_to_string(&p) {
                    Ok(text) => {
                        world = Some(save::load(&text));
                        message = format!("Loaded {}", p.display());
                        (seed, file) = (None, Some(p));
                    }
                    Err(e) => message = format!("Can't read {}: {e}", p.display()),
                },
                Cmd::Save(p) => {
                    if let Some(w) = &world {
                        message = match std::fs::write(&p, save::save(w)) {
                            Ok(()) => format!("Saved {}", p.display()),
                            Err(e) => format!("Can't write {}: {e}", p.display()),
                        };
                        file = Some(p);
                    }
                }
                Cmd::Run(r) => running = r,
                Cmd::Step => {
                    running = false;
                    if let Some(w) = &world {
                        World::time(w);
                    }
                }
            }
            changed = true;
            stats_time = -1;
        }
        let Some(w) = world.clone() else {
            std::thread::sleep(Duration::from_millis(10));
            continue;
        };
        if running {
            let t0 = Instant::now();
            World::time(&w);
            spent[0] += t0.elapsed();
            changed = true;
            fps.1 += 1;
            let t = fps.0.elapsed().as_secs_f64();
            if t >= 1.0 {
                if log_fps {
                    let ms = |d: Duration| d.as_secs_f64() * 1000.0 / fps.1 as f64;
                    eprintln!("{:.1} frames/s, population {}, ms a frame: world {:.1} drawing {:.1} statistics {:.1}",
                        fps.1 as f64 / t, World::getPopulation(&w), ms(spent[0]), ms(spent[1]), ms(spent[2]));
                }
                fps = (Instant::now(), 0, fps.1 as f64 / t);
                spent = [Duration::ZERO; 3];
            }
        } else {
            fps.2 = 0.0;
            std::thread::sleep(Duration::from_millis(10));
        }

        // A picture, if the window wants one and has taken the last.
        let picture = match view {
            Some(v) if changed || drawn != Some(v) => {
                let mut s = shared.lock().unwrap();
                if s.picture.is_none() { Some((v, std::mem::take(&mut s.spare))) } else { None }
            }
            _ => None,
        };
        let t0 = Instant::now();
        let picture = picture.map(|(v, mut lines)| {
            lines.clear();
            draw(&w, v, &mut lines);
            drawn = Some(v);
            lines
        });
        let t1 = Instant::now();
        spent[1] += t1 - t0;
        let ws: JRef<WorldStatistics> = w.get(|o| o.worldStatistics.clone());
        let stats = (want_stats && WorldStatistics::getTime(&ws) != stats_time).then(|| {
            stats_time = WorldStatistics::getTime(&ws);
            Arc::new(statistics(&w, &ws))
        });
        spent[2] += t1.elapsed();
        // Wake the window only for something new to show; it refreshes the
        // status line on its own.
        let wake = picture.is_some() || stats.is_some();
        {
            let mut s = shared.lock().unwrap();
            s.status = Status {
                width: World::getWidth(&w),
                height: World::getHeight(&w),
                time: World::getTime(&w),
                frame: World::getFrame(&w),
                population: World::getPopulation(&w),
                organisms: w.get(|o| o._organisms.clone()).size(),
                running,
                fps: fps.2,
                seed,
                file: file.clone(),
                message: message.clone(),
                error: None,
            };
            if picture.is_some() {
                s.picture = picture;
            }
            if stats.is_some() {
                s.stats = stats;
            }
        }
        if wake {
            ctx.request_repaint();
        }

        // As Java's worker: while the world is repainted, a frame takes
        // DELAY ms (time saved on fast frames pays for slow ones).
        let now = Instant::now();
        let frame_nanos = (now - prev).as_nanos() as i64;
        // The next frame's time includes this sleep, as in Java.
        prev = now;
        if running && view.is_some() {
            ahead_nanos += delay_ms as i64 * 1_000_000 - frame_nanos;
            if ahead_nanos > 100_000_000 {
                ahead_nanos = 0;
            } else if ahead_nanos > 10_000_000 {
                std::thread::sleep(Duration::from_nanos(ahead_nanos as u64));
            } else if ahead_nanos < -10_000_000 {
                ahead_nanos = 0;
            }
        }
        if !running {
            ahead_nanos = 0;
        }
    }
}

/// `Graphics` that records the lines inside a view.
struct Recorder<'a> {
    lines: &'a mut Vec<Line>,
    rgb: u32,
    view: (i32, i32, i32, i32),
}

impl Graphics for Recorder<'_> {
    fn setColor(&mut self, c: Color) {
        self.rgb = c.getRGB() as u32;
    }

    fn drawLine(&mut self, x1: i32, y1: i32, x2: i32, y2: i32) {
        let (vx0, vy0, vx1, vy1) = self.view;
        if x1.max(x2) < vx0 || x1.min(x2) > vx1 || y1.max(y2) < vy0 || y1.min(y2) > vy1 {
            return;
        }
        self.lines.push(Line { x1, y1, x2, y2, rgb: self.rgb });
    }
}

/// World.draw, without Swing: the organisms whose bounds meet the view.
fn draw(world: &JRef<World>, v: View, lines: &mut Vec<Line>) {
    // A margin, so that panning before the next picture shows no gap.
    let m = 64.0;
    let (x0, y0, x1, y1) = ((v.x0 - m) as i32, (v.y0 - m) as i32, (v.x1 + m) as i32, (v.y1 + m) as i32);
    let mut g = Recorder { lines, rgb: 0, view: (x0, y0, x1, y1) };
    let it = world.get(|o| o._organisms.clone()).iterator();
    while it.hasNext() {
        let o: JRef<Organism> = it.next();
        let (ox, oy, ow, oh) = o.get(|o| (o.__super.x, o.__super.y, o.__super.width, o.__super.height));
        if ox + ow < x0 || ox > x1 || oy + oh < y0 || oy > y1 {
            continue;
        }
        Organism::draw(&o, &mut g);
    }
}

fn statistics(w: &JRef<World>, ws: &JRef<WorldStatistics>) -> Stats {
    let list = |l: biogenesis_core::rt::JList<f64>| l.get(|v| v.clone());
    let t = |v: i64| format!("(time {v})");
    // As Java's StatisticsWindow: the alive organisms' records and the generation histogram.
    WorldStatistics::findBestAliveBeings(ws, w.get(|o| o._organisms.clone()));
    let table = vec![
        ("Population", format!("{}", World::getPopulation(w))),
        ("Max population", format!("{} {}", WorldStatistics::getMaxPopulation(ws), t(WorldStatistics::getMaxPopulationTime(ws)))),
        ("Min population", format!("{} {}", WorldStatistics::getMinPopulation(ws), t(WorldStatistics::getMinPopulationTime(ws)))),
        ("Average population", format!("{:.1}", WorldStatistics::getAveragePopulation(ws))),
        ("Average births", format!("{:.1}", WorldStatistics::getAverageBirths(ws))),
        ("Average deaths", format!("{:.1}", WorldStatistics::getAverageDeaths(ws))),
        ("Average infections", format!("{:.1}", WorldStatistics::getAverageInfections(ws))),
        ("Mass extinctions", format!("{}", WorldStatistics::getMassExtintions(ws))),
        ("Created organisms", format!("{}", WorldStatistics::getCreatedOrganisms(ws))),
        ("Corpses", format!("{}", World::getNCorpses(w))),
        ("Total mass", format!("{:.0}", WorldStatistics::getTotalMass(ws))),
        ("Total energy", format!("{:.0}", WorldStatistics::getTotalEnergy(ws))),
        ("Most children (alive)", format!("{}", WorldStatistics::getAliveOrganismMostChildrenNumber(ws))),
        ("Most kills (alive)", format!("{}", WorldStatistics::getAliveOrganismMostKillsNumber(ws))),
        ("Most infections (alive)", format!("{}", WorldStatistics::getAliveOrganismMostInfectionsNumber(ws))),
        ("Oldest (alive)", format!("{}", WorldStatistics::getAliveOrganismOldestNumber(ws))),
        ("Generations (alive)", format!("{} - {}", WorldStatistics::getMinGeneration(ws), WorldStatistics::getMaxGeneration(ws))),
        ("Oxygen", format!("{:.1}", World::getO2(w))),
        ("Carbon dioxide", format!("{:.1}", World::getCO2(w))),
        ("Carbon monoxide", format!("{:.1}", World::getCO1(w))),
        ("Methane", format!("{:.1}", World::getCH4(w))),
        ("Detritus", format!("{:.1}", World::getDetritus(w))),
        ("Nitrogen", format!("{:.1}", World::getN2(w))),
        ("Ammonia", format!("{:.1}", World::getNH3(w))),
    ];
    Stats {
        time: WorldStatistics::getTime(ws),
        population: list(WorldStatistics::getPopulationList(ws)),
        clades: list(WorldStatistics::getDistinctCladesList(ws)),
        births: list(WorldStatistics::getBirthList(ws)),
        deaths: list(WorldStatistics::getDeathList(ws)),
        // The colors of Java's StatisticsWindow.
        atmosphere: vec![
            ("Oxygen", 0x0000ff, list(WorldStatistics::getOxygenList(ws))),
            ("Carbon dioxide", 0xffffff, list(WorldStatistics::getCarbonDioxideList(ws))),
            ("Carbon monoxide", 0x808080, list(WorldStatistics::getCarbonMonoxideList(ws))),
            ("Methane", 0xff00ff, list(WorldStatistics::getMethaneList(ws))),
            ("Detritus", 0xffff00, list(WorldStatistics::getDetritusList(ws))),
            ("Nitrogen", 0x00ffff, list(WorldStatistics::getNitrogenList(ws))),
            ("Ammonia", 0xffafaf, list(WorldStatistics::getAmmoniaList(ws))),
        ],
        generations: list(WorldStatistics::getGenerationHistogramList(ws)),
        min_generation: WorldStatistics::getMinGeneration(ws),
        table,
    }
}
