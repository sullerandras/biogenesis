package biogenesis;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import biogenesis.csv.CSV;
import biogenesis.csv.Row;

public class BioFile {
  public enum Type {
    REGULAR,
    WORLD,
    STATS,
    JSON,
    CLADES,
    SQLITE
  }

  private final File file;

  public BioFile(File file) {
    this.file = file;
  }

  public File getCsvFile() {
    return new File(getFile().getPath().replaceFirst("\\." + BioFileFilter.WORLD_EXTENSION + "(\\.gz)?$", ".csv"));
  }

  public File getSqliteFile() {
    return new File(getFile().getPath().replaceFirst("\\." + BioFileFilter.WORLD_EXTENSION + "(\\.gz)?$", ".sqlite"));
  }

  /**
   * Returns the name of the world file name without the time and without the extension.
   * Example:
   *  "/home/andras/firstworld.bgw.gz" => "firstworld"
   *  "/home/andras/Downloads/bestworld@00005.bgw" => "bestworld"
   */
  public String getWorldName() {
    return getFile().getName().replaceFirst(
        "(@[0-9]*)?\\." + BioFileFilter.WORLD_EXTENSION + "(\\.gz)?$",
        "");
  }

  public File getFile() {
    return file;
  }

  public File getFileForTime(final long time, final Type type) {
    String folderName = getFile().getParent();
    final String baseFilename = baseFilename(getFile().getName(), time);
    final String suffix;
    switch (type) {
      case REGULAR:
        if (Utils.COMPRESS_BACKUPS) {
          suffix = BioFileFilter.WORLD_EXTENSION + ".gz";
        } else {
          suffix = BioFileFilter.WORLD_EXTENSION;
        }
        break;
      case WORLD:
        if (Utils.AUTO_BACKUP_IMAGES_AS_FOLDERS) {
          folderName = folderName + File.separator + "worlds";
          suffix = "png";
        } else {
          suffix = "world.png";
        }
        break;
      case STATS:
        if (Utils.AUTO_BACKUP_IMAGES_AS_FOLDERS) {
          folderName = folderName + File.separator + "stats";
          suffix = "png";
        } else {
          suffix = "stats.png";
        }
        break;
      case JSON:
        suffix = "json";
        break;
      case CLADES:
        if (Utils.AUTO_BACKUP_IMAGES_AS_FOLDERS) {
          folderName = folderName + File.separator + "clades";
          suffix = "png";
        } else {
          suffix = "clades.png";
        }
        break;
      default:
        throw new IllegalArgumentException("Type " + type + " is not supported");
    }

    new File(folderName).mkdirs();

    return new File(folderName, baseFilename + "." + suffix);
  }

  public boolean fileNameContainsTime() {
    return getFile().getName().matches(".*?@[0-9]*\\." + BioFileFilter.WORLD_EXTENSION + "(\\.gz)?$");
  }

  public void appendToCsv(long time, int population, int distinctClades, int distinctCladesWith10Orgs,
      int distinctCladesWith100Orgs, double O2, double CO2, double CO1, double CH4, double detritus, Collection<Organism> organisms) {
    File csvFile = getCsvFile();
    Row row = new Row();
    row.add("time", time);
    row.add("population", population);
    row.add("clades", distinctClades);
    row.add("clades w 10 orgs", distinctCladesWith10Orgs);
    row.add("clades w 100 orgs", distinctCladesWith100Orgs);
    row.add("o2", O2, 2);
    row.add("co2", CO2, 2);
    row.add("co1", CO1, 2);
    row.add("ch4", CH4, 2);
    row.add("detritus", detritus, 2);
    row.add("totalmass", 0, 2);
    row.add("totalenergy", 0, 2);

    Map<String, Integer> organismStats = prepareOrganismStats(organisms, row);
    organismStats.entrySet().stream().forEach(e -> row.add(e.getKey(), e.getValue()));

    new CSV(csvFile).append(row);
  }

  private Map<String, Integer> prepareOrganismStats(Collection<Organism> organisms, Row row) {
    Map<String, Integer> stats = new LinkedHashMap<>(); // using LinkedHashMap to ensure the order of the columns

    double totalMass = 0;
    double totalEnergy = 0;

    for (Organism o : organisms) {
      totalMass += o.getMass();
      totalEnergy += o.getEnergy();

      stats.put("active", stats.getOrDefault("active", 0) + (o.active ? 1 : 0));
      stats.put("alive", stats.getOrDefault("alive", 0) + (o.alive ? 1 : 0));
      stats.put("all frozen", stats.getOrDefault("all frozen", 0) + (o._allfrozen ? 1 : 0));
      stats.put("altruist", stats.getOrDefault("altruist", 0) + (o._altruist ? 1 : 0));
      stats.put("can move", stats.getOrDefault("can move", 0) + (o._canmove == 2 ? 1 : 0));
      stats.put("can react", stats.getOrDefault("can react", 0) + (o._canreact ? 1 : 0));
      stats.put("candodge", stats.getOrDefault("candodge", 0) + (o._candodge ? 1 : 0));
      stats.put("clockwise", stats.getOrDefault("clockwise", 0) + (o._clockwise ? 1 : 0));
      stats.put("crowded", stats.getOrDefault("crowded", 0) + (o._crowded ? 1 : 0));
      stats.put("dodge", stats.getOrDefault("dodge", 0) + (o._dodge ? 1 : 0));
      stats.put("familial", stats.getOrDefault("familial", 0) + (o._familial ? 1 : 0));
      stats.put("generation battle", stats.getOrDefault("generation battle", 0) + (o._generationbattle ? 1 : 0));
      stats.put("has dodged", stats.getOrDefault("has dodged", 0) + (o._hasdodged ? 1 : 0));
      stats.put("has eyes", stats.getOrDefault("has eyes", 0) + (o._haseyes ? 1 : 0));
      stats.put("has good vision", stats.getOrDefault("has good vision", 0) + (o._hasgoodvision ? 1 : 0));
      stats.put("has moved", stats.getOrDefault("has moved", 0) + (o.hasMoved ? 1 : 0));
      stats.put("is auburn", stats.getOrDefault("is auburn", 0) + (o._isauburn ? 1 : 0));
      stats.put("is blond", stats.getOrDefault("is blond", 0) + (o._isblond ? 1 : 0));
      stats.put("is consumer", stats.getOrDefault("is consumer", 0) + (o._isaconsumer ? 1 : 0));
      stats.put("is coral", stats.getOrDefault("is coral", 0) + (o._iscoral ? 1 : 0));
      stats.put("is enhanced", stats.getOrDefault("is enhanced", 0) + (o._isenhanced ? 1 : 0));
      stats.put("is frozen", stats.getOrDefault("is frozen", 0) + (o._isfrozen ? 1 : 0));
      stats.put("is fungus", stats.getOrDefault("is fungus", 0) + (o._isafungus ? 1 : 0));
      stats.put("is gray", stats.getOrDefault("is gray", 0) + (o._isgray ? 1 : 0));
      stats.put("is infectious", stats.getOrDefault("is infectious", 0) + (o._isinfectious ? 1 : 0));
      stats.put("is injured plant", stats.getOrDefault("is injured plant", 0) + (o._isinjuredplant ? 1 : 0));
      stats.put("is injured", stats.getOrDefault("is injured", 0) + (o._isinjured ? 1 : 0));
      stats.put("is killer", stats.getOrDefault("is killer", 0) + (o._isakiller ? 1 : 0));
      stats.put("is lime", stats.getOrDefault("is lime", 0) + (o._islime ? 1 : 0));
      stats.put("is plant", stats.getOrDefault("is plant", 0) + (o._isaplant ? 1 : 0));
      stats.put("is reproductive", stats.getOrDefault("is reproductive", 0) + (o._isreproductive ? 1 : 0));
      stats.put("is silver", stats.getOrDefault("is silver", 0) + (o._issilver ? 1 : 0));
      stats.put("is spike", stats.getOrDefault("is spike", 0) + (o._isspike ? 1 : 0));
      stats.put("modifies leaf", stats.getOrDefault("modifies leaf", 0) + (o._modifiesleaf ? 1 : 0));
      stats.put("modifies lilac", stats.getOrDefault("modifies lilac", 0) + (o._modifieslilac ? 1 : 0));
      stats.put("modifies pink", stats.getOrDefault("modifies pink", 0) + (o._modifiespink ? 1 : 0));
      stats.put("peaceful", stats.getOrDefault("peaceful", 0) + (o._peaceful ? 1 : 0));
      stats.put("sibling battle", stats.getOrDefault("sibling battle", 0) + (o._siblingbattle ? 1 : 0));
      stats.put("social", stats.getOrDefault("social", 0) + (o._social ? 1 : 0));
      stats.put("transfers energy", stats.getOrDefault("transfers energy", 0) + (o._transfersenergy ? 1 : 0));
      stats.put("use extra effects", stats.getOrDefault("use extra effects", 0) + (o._useextraeffects ? 1 : 0));
      stats.put("use frame movement", stats.getOrDefault("use frame movement", 0) + (o._useframemovement ? 1 : 0));
      stats.put("use pretouch effects", stats.getOrDefault("use pretouch effects", 0) + (o._usepretoucheffects ? 1 : 0));
      stats.put("methanotrophs", stats.getOrDefault("methanotrophs", 0) + (o._methanotrophy > 0 ? 1 : 0));
      stats.put("filter feeders", stats.getOrDefault("filter feeders", 0) + (o._filterfeeding > 0 ? 1 : 0));
      stats.put("true plant", stats.getOrDefault("true plant", 0) + (o._photosynthesis > 0 ? 1 : 0));
    }

    row.add("totalmass", totalMass, 2);
    row.add("totalenergy", totalEnergy, 2);

    return stats;
  }

  // Replace any string of format "filename@#####.ext" with "filename@TIME".
  // Files without '@#####' ending also become "filename@TIME".
  private static String baseFilename(String filename, long time) {
    return filename.replaceFirst(
        "(@[0-9]*)?\\." + BioFileFilter.WORLD_EXTENSION + "(\\.gz)?$",
        "@" + formatTime(time));
  }

  private static String formatTime(long time) {
    if (time < 1000000) {
      return String.format("%1$05d", time);
    }

    return Long.toString(time, 10);
  }
}
