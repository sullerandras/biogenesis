package biogenesis.test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import biogenesis.BioFile;
import biogenesis.Utils;

public class BioFileTest {
  @Test
  public void testGetFileForTime_When_AUTO_BACKUP_IMAGES_AS_FOLDERS_isFalse() {
    BioFile bioFile = new BioFile(new File("/home/andras/firstworld.bgw.gz"));

    Utils.AUTO_BACKUP_IMAGES_AS_FOLDERS = false;

    assertEquals(new File("/home/andras/firstworld@01000.bgw"), bioFile.getFileForTime(1000, BioFile.Type.REGULAR));
    assertEquals(new File("/home/andras/firstworld@01000.json"), bioFile.getFileForTime(1000, BioFile.Type.JSON));
    assertEquals(new File("/home/andras/firstworld@01000.world.png"), bioFile.getFileForTime(1000, BioFile.Type.WORLD));
    assertEquals(new File("/home/andras/firstworld@01000.clades.png"), bioFile.getFileForTime(1000, BioFile.Type.CLADES));
    assertEquals(new File("/home/andras/firstworld@01000.stats.png"), bioFile.getFileForTime(1000, BioFile.Type.STATS));
  }

  @Test
  public void testGetFileForTime_When_AUTO_BACKUP_IMAGES_AS_FOLDERS_isTrue() {
    BioFile bioFile = new BioFile(new File("/home/andras/firstworld.bgw.gz"));

    Utils.AUTO_BACKUP_IMAGES_AS_FOLDERS = true;

    assertEquals(new File("/home/andras/firstworld@01000.bgw"), bioFile.getFileForTime(1000, BioFile.Type.REGULAR));
    assertEquals(new File("/home/andras/firstworld@01000.json"), bioFile.getFileForTime(1000, BioFile.Type.JSON));
    assertEquals(new File("/home/andras/worlds/firstworld@01000.png"), bioFile.getFileForTime(1000, BioFile.Type.WORLD));
    assertEquals(new File("/home/andras/clades/firstworld@01000.png"), bioFile.getFileForTime(1000, BioFile.Type.CLADES));
    assertEquals(new File("/home/andras/stats/firstworld@01000.png"), bioFile.getFileForTime(1000, BioFile.Type.STATS));
  }
}
