package biogenesis;

public enum GeneColor {
  NOCOLOR(""),
  GREEN("green"),
  FOREST("forest"),
  SPRING("spring"),
  SUMMER("summer"),
  LIME("lime"),
  LEAF("leaf"),
  C4("c4"),
  JADE("jade"),
  GRASS("grass"),
  BARK("bark"),
  PURPLE("purple"),
  PLANKTON("plankton"),
  RED("red"),
  FIRE("fire"),
  ORANGE("orange"),
  MAROON("maroon"),
  PINK("pink"),
  CREAM("cream"),
  SILVER("silver"),
  SPIKE("spike"),
  LILAC("lilac"),
  GRAY("gray"),
  VIOLET("violet"),
  OLIVE("olive"),
  SKY("sky"),
  BLUE("blue"),
  OCHRE("ochre"),
  FALLOW("fallow"),
  SPORE("spore"),
  WHITE("white"),
  PLAGUE("plague"),
  CORAL("coral"),
  MINT("mint"),
  LAVENDER("lavender"),
  MAGENTA("magenta"),
  ROSE("rose"),
  CYAN("cyan"),
  TEAL("teal"),
  YELLOW("yellow"),
  AUBURN("auburn"),
  INDIGO("indigo"),
  BLOND("blond"),
  FLOWER("flower"),
  DARKGRAY("darkgray"),
  GOLD("gold"),
  DARK("dark"),
  EYE("eye"),
  WINTER(""),
  OLDBARK(""),
  DARKJADE(""),
  DARKGREEN(""),
  DARKFIRE(""),
  DARKLILAC(""),
  DEEPSKY(""),
  DARKOLIVE(""),
  SPIKEPOINT(""),
  FRUIT(""),
  VISION(""),
  ICE(""),
  LIGHT_BLUE(""),
  LIGHTBROWN(""),
  GREENBROWN(""),
  BROKEN(""),
  DEADBARK(""),
  BROWN("");

  private final String name;

  private GeneColor(String name) {
    this.name = name;
  }

  public static GeneColor getColor(int color) {
    return GeneColor.values()[color + 1];
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }
}
