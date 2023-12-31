package biogenesis.clade_analyzer;

import java.awt.Color;

/**
 * Contains the name of the clade, as per Docsy's naming scheme:
 * https://docs.google.com/document/d/1sa728yOAqV-ZREyM_zxKj7Nek4g2WnesrpbWdnt5YRU/edit?usp=sharing
 */
public class CladeName {
  private final boolean isVirus;
  private final boolean isConsumer;
  private final boolean isPlant;
  private final boolean isOther;
  private final int symmetry;
  private final boolean mirror;
  private final String primaryColors;
  private final String secondaryColors;
  private final String tertiaryColors;

  private Color cachedColor;

  public CladeName(boolean isVirus, boolean isConsumer, boolean isPlant, boolean isOther, int symmetry, boolean mirror,
      ColorCounter primaryColors, ColorCounter secondaryColors, ColorCounter tertiaryColors) {
        this.isVirus = isVirus;
        this.isConsumer = isConsumer;
        this.isPlant = isPlant;
        this.isOther = isOther;
        this.symmetry = symmetry;
        this.mirror = mirror;
        this.primaryColors = primaryColors.toString();
        this.secondaryColors = secondaryColors.toString();
        this.tertiaryColors = tertiaryColors.toString();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (isVirus) {
      sb.append("V");
    }
    if (isConsumer) {
      sb.append("C");
    }
    if (isPlant) {
      sb.append("P");
    }
    if (isOther) {
      sb.append("O");
    }

    // symmetry
    sb.append(symmetry);
    if (mirror) {
      sb.append("M");
    }

    if (primaryColors.length() > 0) {
      // separator
      sb.append(" - ");

      // primary colors
      sb.append(primaryColors);
    }

    if (secondaryColors.length() > 0) {
      // separator
      sb.append(" - ");

      // secondary colors
      sb.append(secondaryColors);
    }

    if (tertiaryColors.length() > 0) {
      // separator
      sb.append(" - ");

      // tertiary colors
      sb.append(tertiaryColors);
    }

    return sb.toString();
  }

  public boolean isVirus() {
    return isVirus;
  }

  public boolean isConsumer() {
    return isConsumer;
  }

  public boolean isPlant() {
    return isPlant;
  }

  public boolean isOther() {
    return isOther;
  }

  public static final Color VIRUS_COLOR = new Color(0.75f, 0.75f, 0.75f);
  public static final Color CONSUMER_COLOR = new Color(1.0f, 0.25f, 0.25f);
  public static final Color PLANT_COLOR = new Color(0.25f, 1.0f, 0.25f);
  public static final Color OTHER_COLOR = new Color(0.25f, 0.25f, 1.0f);

  public Color getColor() {
    if (cachedColor != null) {
      return cachedColor;
    }

    float r = 1.0f;
    float g = 1.0f;
    float b = 1.0f;

    if (isVirus) {
      r *= VIRUS_COLOR.getRed() / 255.0f;
      g *= VIRUS_COLOR.getGreen() / 255.0f;
      b *= VIRUS_COLOR.getBlue() / 255.0f;
    }
    if (isConsumer) {
      r *= CONSUMER_COLOR.getRed() / 255.0f;
      g *= CONSUMER_COLOR.getGreen() / 255.0f;
      b *= CONSUMER_COLOR.getBlue() / 255.0f;
    }
    if (isPlant) {
      r *= PLANT_COLOR.getRed() / 255.0f;
      g *= PLANT_COLOR.getGreen() / 255.0f;
      b *= PLANT_COLOR.getBlue() / 255.0f;
    }
    if (isOther) {
      r *= OTHER_COLOR.getRed() / 255.0f;
      g *= OTHER_COLOR.getGreen() / 255.0f;
      b *= OTHER_COLOR.getBlue() / 255.0f;
    }

    cachedColor = new Color(r, g, b, 1.0f);

    return cachedColor;
  }
}
