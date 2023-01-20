package biogenesis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BioFile {
  public enum Type {
    REGULAR,
    WORLD,
    STATS
  }

  private final File file;

  public BioFile(File file) {
    this.file = file;
  }

  public File getCsvFile() {
    return new File(getFile().getName().replaceFirst("." + BioFileFilter.WORLD_EXTENSION + "$", ".csv"));
  }

  public File getFile() {
    return file;
  }

  public File getFileForTime(final long time, final Type type) {
    final String baseFilename = baseFilename(getFile().getPath(), time);
    final String suffix;
    switch (type) {
      case REGULAR:
        suffix = BioFileFilter.WORLD_EXTENSION;
        break;
      case WORLD:
        suffix = "world.png";
        break;
      case STATS:
        suffix = "stats.png";
        break;
      default:
        throw new IllegalArgumentException("Type " + type + " is not supported");
    }

    return new File(baseFilename + "." + suffix);
  }

  public boolean fileNameContainsTime() {
    return getFile().getName().matches(".*?@[0-9]*." + BioFileFilter.WORLD_EXTENSION + "$");
  }

  public void appendToCsv(long time, int population, int distinctClades, double O2, double CO2, double CH4) {
    File csvFile = getCsvFile();
    FileWriter fw = null;
    try {
      try {
        if (csvFile.exists() && csvFile.isFile()) {
          fw = new FileWriter(csvFile, true);
        } else {
          fw = new FileWriter(csvFile);
          fw.write("time,population,clades,o2,co2,ch4\n");
        }
        fw.write(
          String.format(
            "%d,%d,%d,%.2f,%.2f,%.2f\n",
            time,
            population,
            distinctClades,
            O2,
            CO2,
            CH4
          )
        );
      } finally {
        if (fw != null) {
          fw.close();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Replace any string of format "filename@#####.ext" with "filename@TIME".
  // Files without '@#####' ending also become "filename@TIME".
  public static String baseFilename(String filename, long time) {
    return filename.replaceFirst(
        "(@[0-9]*)?." + BioFileFilter.WORLD_EXTENSION + "$",
        "@" + formatTime(time));
  }

  private static String formatTime(long time) {
    if (time < 1000000) {
      return String.format("%1$05d", time);
    }

    return Long.toString(time, 10);
  }
}
