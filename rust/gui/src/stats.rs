//! The statistics window: Java's StatisticsWindow charts (population,
//! atmosphere, generations) and a table of the main numbers.

use eframe::egui::{self, Color32, RichText};
use egui_plot::{Bar, BarChart, Legend, Line, Plot, PlotPoints};

use crate::sim::Stats;

pub const VIEWPORT: &str = "statistics";

/// Shows the window while `open`; clears it when the window is closed.
/// Returns whether the window has the keyboard focus.
pub fn show(ctx: &egui::Context, stats: Option<&Stats>, open: &mut bool) -> bool {
    let builder = egui::ViewportBuilder::default().with_title("Statistics (Rust)").with_inner_size([1000.0, 760.0]);
    ctx.show_viewport_immediate(egui::ViewportId::from_hash_of(VIEWPORT), builder, |ui, _class| {
        if ui.input(|i| i.viewport().close_requested()) {
            *open = false;
        }
        egui::Panel::left("table").resizable(false).show(ui, |ui| {
            ui.add_space(6.0);
            match stats {
                Some(s) => table(ui, s),
                None => {
                    ui.label("Waiting for the simulation...");
                }
            }
        });
        egui::CentralPanel::default_margins().show(ui, |ui| {
            if let Some(s) = stats {
                charts(ui, s);
            }
        });
        ui.input(|i| i.focused)
    })
}

fn table(ui: &mut egui::Ui, s: &Stats) {
    egui::Grid::new("numbers").num_columns(2).striped(true).show(ui, |ui| {
        ui.label("Time");
        ui.label(s.time.to_string());
        ui.end_row();
        for (k, v) in &s.table {
            ui.label(*k);
            ui.label(v);
            ui.end_row();
        }
    });
}

/// Points of a list holding the last values, one a time unit, up to `time`.
fn series(time: i64, values: &[f64]) -> PlotPoints<'static> {
    let first = time - values.len() as i64 + 1;
    values.iter().enumerate().map(|(i, &v)| [(first + i as i64) as f64, v]).collect()
}

fn rgb(c: u32) -> Color32 {
    Color32::from_rgb((c >> 16) as u8, (c >> 8) as u8, c as u8)
}

fn charts(ui: &mut egui::Ui, s: &Stats) {
    let h = ((ui.available_height() - 120.0) / 3.5).max(100.0);
    let plot = |id: &str, height: f32| {
        Plot::new(id.to_owned())
            .height(height)
            .legend(Legend::default())
            .include_y(0.0)
            .allow_scroll(false)
            .allow_drag(false)
            .allow_zoom(false)
    };
    let orange = rgb(0xffc800);
    ui.label(RichText::new("Population").strong());
    plot("population", h).x_axis_label("time").show(ui, |p| {
        p.line(Line::new("Deaths", series(s.time, &s.deaths)).color(Color32::RED));
        p.line(Line::new("Births", series(s.time, &s.births)).color(Color32::GREEN));
        p.line(Line::new("Population", series(s.time, &s.population)).color(Color32::WHITE));
        p.line(Line::new("Clades", series(s.time, &s.clades)).color(orange));
    });
    ui.label(RichText::new("Clades").strong());
    plot("clades", h / 2.0).x_axis_label("time").show(ui, |p| {
        p.line(Line::new("Clades", series(s.time, &s.clades)).color(orange));
    });
    ui.label(RichText::new("Atmosphere").strong());
    plot("atmosphere", h).x_axis_label("time").y_axis_label("log10(amount / 100)").show(ui, |p| {
        for (name, color, values) in &s.atmosphere {
            p.line(Line::new(*name, series(s.time, values)).color(rgb(*color)));
        }
    });
    ui.label(RichText::new("Generations").strong());
    plot("generations", h).x_axis_label("generation").show(ui, |p| {
        let bars = s.generations.iter().enumerate()
            .map(|(i, &v)| Bar::new((s.min_generation + i as i32) as f64, v).width(0.9))
            .collect();
        p.bar_chart(BarChart::new("Organisms", bars).color(Color32::WHITE));
    });
}
