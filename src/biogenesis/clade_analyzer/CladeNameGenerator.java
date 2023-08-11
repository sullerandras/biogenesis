package biogenesis.clade_analyzer;

import biogenesis.Gene;
import biogenesis.GeneColor;
import biogenesis.GeneticCode;
import biogenesis.Organism;

public class CladeNameGenerator {
  public static String generateName(GeneticCode geneticCode) {
    boolean isVirus = false;
    boolean isConsumer = false;
    boolean isPlant = false;
    boolean isOther = false;

    ColorCounter primaryColors = new ColorCounter();
    ColorCounter secondaryColors = new ColorCounter();
    ColorCounter tertiaryColors = new ColorCounter();

    for (int i = 0; i < geneticCode.getNGenes(); i++) {
      Gene gene = geneticCode.getGene(i);
      GeneColor type = Organism.getGeneColor(gene.getColor());
      switch (type) {
        case NOCOLOR:
          break;
        case GREEN:
          isPlant = true;
          primaryColors.add(type);
          break;
        case FOREST:
          isPlant = true;
          primaryColors.add(type);
          break;
        case SPRING:
          isPlant = true;
          primaryColors.add(type);
          break;
        case SUMMER:
          isPlant = true;
          primaryColors.add(type);
          break;
        case LIME:
          isPlant = true;
          primaryColors.add(type);
          break;
        case LEAF:
          isPlant = true;
          primaryColors.add(type);
          break;
        case C4:
          isPlant = true;
          primaryColors.add(type);
          break;
        case JADE:
          isPlant = true;
          primaryColors.add(type);
          break;
        case GRASS:
          isPlant = true;
          primaryColors.add(type);
          break;
        case BARK:
          break;
        case PURPLE:
          isPlant = true;
          primaryColors.add(type);
          break;
        case RED:
          isConsumer = true;
          primaryColors.add(type);
          break;
        case FIRE:
          isConsumer = true;
          primaryColors.add(type);
          break;
        case ORANGE:
          isConsumer = true;
          primaryColors.add(type);
          break;
        case MAROON:
          isConsumer = true;
          primaryColors.add(type);
          break;
        case PINK:
          isConsumer = true;
          primaryColors.add(type);
          break;
        case CREAM:
          isConsumer = true;
          primaryColors.add(type);
          break;
        case SILVER:
          break;
        case SPIKE:
          break;
        case LILAC:
          secondaryColors.add(type);
          break;
        case GRAY:
          secondaryColors.add(type);
          break;
        case VIOLET:
          secondaryColors.add(type);
          break;
        case OLIVE:
          secondaryColors.add(type);
          break;
        case SKY:
          secondaryColors.add(type);
          break;
        case BLUE:
          secondaryColors.add(type);
          break;
        case OCHRE:
          secondaryColors.add(type);
          break;
        case FALLOW:
          secondaryColors.add(type);
          break;
        case SPORE:
          break;
        case WHITE:
          isVirus = true;
          primaryColors.add(type);
          break;
        case PLAGUE:
          secondaryColors.add(type);
          break;
        case CORAL:
          primaryColors.add(type);
          break;
        case MINT:
          tertiaryColors.add(type);
          break;
        case LAVENDER:
          tertiaryColors.add(type);
          break;
        case MAGENTA:
          tertiaryColors.add(type);
          break;
        case ROSE:
          break;
        case CYAN:
          secondaryColors.add(type);
          break;
        case TEAL:
          secondaryColors.add(type);
          break;
        case YELLOW:
          tertiaryColors.add(type);
          break;
        case AUBURN:
          tertiaryColors.add(type);
          break;
        case INDIGO:
          tertiaryColors.add(type);
          break;
        case BLOND:
          tertiaryColors.add(type);
          break;
        case FLOWER:
          tertiaryColors.add(type);
          break;
        case DARKGRAY:
          tertiaryColors.add(type);
          break;
        case GOLD:
          tertiaryColors.add(type);
          break;
        case DARK:
          break;
        case EYE:
          secondaryColors.add(type);
          break;
        case WINTER:
          break;
        case OLDBARK:
          break;
        case DARKJADE:
          break;
        case DARKGREEN:
          break;
        case DARKFIRE:
          break;
        case DARKLILAC:
          break;
        case DEEPSKY:
          break;
        case DARKOLIVE:
          break;
        case SPIKEPOINT:
          break;
        case FRUIT:
          break;
        case VISION:
          break;
        case ICE:
          break;
        case LIGHT_BLUE:
          break;
        case LIGHTBROWN:
          break;
        case GREENBROWN:
          break;
        case BROKEN:
          break;
        case DEADBARK:
          break;
        case BROWN:
          break;
      }
    }

    if (!isVirus && !isConsumer && !isPlant) {
      isOther = true;
    }

    StringBuilder sb = new StringBuilder();

    // type
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
    sb.append(geneticCode.getSymmetry());
    if (geneticCode.getMirror() == 1) {
      sb.append("M");
    }

    if (primaryColors.size() > 0) {
      // separator
      sb.append(" - ");

      // primary colors
      sb.append(primaryColors);
    }

    if (secondaryColors.size() > 0) {
      // separator
      sb.append(" - ");

      // secondary colors
      sb.append(secondaryColors);
    }

    if (tertiaryColors.size() > 0) {
      // separator
      sb.append(" - ");

      // tertiary colors
      sb.append(tertiaryColors);
    }

    return sb.toString();
  }
}
