package biogenesis;

import java.awt.Color;

import javax.swing.JComboBox;


public class ColorComboBox extends JComboBox {
	private static final long serialVersionUID = Utils.VERSION;
	private static final String[] colorValues = {Messages.getString("T_GREEN"),Messages.getString("T_FOREST"),  //$NON-NLS-1$//$NON-NLS-2$
		Messages.getString("T_SPRING"),Messages.getString("T_SUMMER"),Messages.getString("T_LIME"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_LEAF"),Messages.getString("T_C4"),Messages.getString("T_JADE"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_GRASS"),Messages.getString("T_BARK"),Messages.getString("T_PURPLE"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_PLANKTON"),Messages.getString("T_RED"),Messages.getString("T_FIRE"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_ORANGE"),Messages.getString("T_MAROON"),Messages.getString("T_PINK"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_CREAM"),Messages.getString("T_SILVER"),Messages.getString("T_SPIKE"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_LILAC"),Messages.getString("T_GRAY"),Messages.getString("T_VIOLET"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_OLIVE"),Messages.getString("T_SKY"),Messages.getString("T_BLUE"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_OCHRE"),Messages.getString("T_FALLOW"),Messages.getString("T_SPORE"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_WHITE"),Messages.getString("T_PLAGUE"),Messages.getString("T_CORAL"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_MINT"),Messages.getString("T_LAVENDER"),Messages.getString("T_MAGENTA"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_ROSE"),Messages.getString("T_CYAN"),Messages.getString("T_TEAL"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_SPIN"),Messages.getString("T_YELLOW"),Messages.getString("T_AUBURN"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_INDIGO"),Messages.getString("T_BLOND"),Messages.getString("T_FLOWER"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_DARKGRAY"),Messages.getString("T_GOLD"),Messages.getString("T_DARK"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_EYE")};  //$NON-NLS-1$
	
	public ColorComboBox(Color c) {
		super(colorValues);
		setSelectedColor(c);
	}
	
	public Color getSelectedColor() {
		switch (getSelectedIndex()) {
		case 0: return Color.GREEN;
		case 1: return Utils.ColorFOREST;
		case 2: return Utils.ColorSPRING;
		case 3: return Utils.ColorSUMMER;
		case 4: return Utils.ColorLIME;
		case 5: return Utils.ColorLEAF;
		case 6: return Utils.ColorC4;
		case 7: return Utils.ColorJADE;
		case 8: return Utils.ColorGRASS;
		case 9: return Utils.ColorBARK;
		case 10: return Utils.ColorPURPLE;
		case 11: return Utils.ColorPLANKTON;
		case 12: return Color.RED;
		case 13: return Utils.ColorFIRE;
		case 14: return Color.ORANGE;
		case 15: return Utils.ColorMAROON;
		case 16: return Color.PINK;
		case 17: return Utils.ColorCREAM;
		case 18: return Color.LIGHT_GRAY;
		case 19: return Utils.ColorSPIKE;
		case 20: return Utils.ColorLILAC;
		case 21: return Color.GRAY;
		case 22: return Utils.ColorVIOLET;
		case 23: return Utils.ColorOLIVE;
		case 24: return Utils.ColorSKY;
		case 25: return Color.BLUE;
		case 26: return Utils.ColorOCHRE;
		case 27: return Utils.ColorFALLOW;
		case 28: return Utils.ColorSPORE;
		case 29: return Color.WHITE;
		case 30: return Utils.ColorPLAGUE;
		case 31: return Utils.ColorCORAL;
		case 32: return Utils.ColorMINT;
		case 33: return Utils.ColorLAVENDER;
		case 34: return Color.MAGENTA;
		case 35: return Utils.ColorROSE;
		case 36: return Color.CYAN;
		case 37: return Utils.ColorTEAL;
		case 38: return Utils.ColorSPIN;
		case 39: return Color.YELLOW;
		case 40: return Utils.ColorAUBURN;
		case 41: return Utils.ColorINDIGO;
		case 42: return Utils.ColorBLOND;
		case 43: return Utils.ColorFLOWER;
		case 44: return Color.DARK_GRAY;
		case 45: return Utils.ColorGOLD;
		case 46: return Utils.ColorDARK;
		case 47: return Utils.ColorEYE;
		case 48: return Utils.ColorWINTER;
		case 49: return Utils.ColorOLDBARK;
		case 50: return Utils.ColorDARKJADE;
		case 51: return Utils.ColorDARKGREEN;
		case 52: return Utils.ColorDARKFIRE;
		case 53: return Utils.ColorDARKLILAC;
		case 54: return Utils.ColorDEEPSKY;
		case 55: return Utils.ColorDARKOLIVE;
		case 56: return Utils.ColorSPIKEPOINT;
		case 57: return Utils.ColorFRUIT;
		case 58: return Utils.ColorVISION;
		case 59: return Utils.ColorICE;
		case 60: return Utils.ColorLIGHT_BLUE;
		case 61: return Utils.ColorLIGHTBROWN;
		case 62: return Utils.ColorGREENBROWN;
		case 63: return Utils.ColorBROKEN;
		case 64: return Utils.ColorDEADBARK;
		default: return Utils.ColorBROWN;
		}
	}
	
	public void setSelectedColor(Color c) {
		if (c.equals(Color.GREEN)) setSelectedIndex(0);
		if (c.equals(Utils.ColorFOREST)) setSelectedIndex(1);
		if (c.equals(Utils.ColorSPRING)) setSelectedIndex(2);
		if (c.equals(Utils.ColorSUMMER)) setSelectedIndex(3);
		if (c.equals(Utils.ColorLIME)) setSelectedIndex(4);
		if (c.equals(Utils.ColorLEAF)) setSelectedIndex(5);
		if (c.equals(Utils.ColorC4)) setSelectedIndex(6);
		if (c.equals(Utils.ColorJADE)) setSelectedIndex(7);
		if (c.equals(Utils.ColorGRASS)) setSelectedIndex(8);
		if (c.equals(Utils.ColorBARK)) setSelectedIndex(9);
		if (c.equals(Utils.ColorPURPLE)) setSelectedIndex(10);
		if (c.equals(Utils.ColorPLANKTON)) setSelectedIndex(11);
		if (c.equals(Color.RED)) setSelectedIndex(12);
		if (c.equals(Utils.ColorFIRE)) setSelectedIndex(13);
		if (c.equals(Color.ORANGE)) setSelectedIndex(14);
		if (c.equals(Utils.ColorMAROON)) setSelectedIndex(15);
		if (c.equals(Color.PINK)) setSelectedIndex(16);
		if (c.equals(Utils.ColorCREAM)) setSelectedIndex(17);
		if (c.equals(Color.LIGHT_GRAY)) setSelectedIndex(18);
		if (c.equals(Utils.ColorSPIKE)) setSelectedIndex(19);
		if (c.equals(Utils.ColorLILAC)) setSelectedIndex(20);
		if (c.equals(Color.GRAY)) setSelectedIndex(21);
		if (c.equals(Utils.ColorVIOLET)) setSelectedIndex(22);
		if (c.equals(Utils.ColorOLIVE)) setSelectedIndex(23);
		if (c.equals(Utils.ColorSKY)) setSelectedIndex(24);
		if (c.equals(Color.BLUE)) setSelectedIndex(25);
		if (c.equals(Utils.ColorOCHRE)) setSelectedIndex(26);
		if (c.equals(Utils.ColorFALLOW)) setSelectedIndex(27);
		if (c.equals(Utils.ColorSPORE)) setSelectedIndex(28);
		if (c.equals(Color.WHITE)) setSelectedIndex(29);
		if (c.equals(Utils.ColorPLAGUE)) setSelectedIndex(30);
		if (c.equals(Utils.ColorCORAL)) setSelectedIndex(31);
		if (c.equals(Utils.ColorMINT)) setSelectedIndex(32);
		if (c.equals(Utils.ColorLAVENDER)) setSelectedIndex(33);
		if (c.equals(Color.MAGENTA)) setSelectedIndex(34);
		if (c.equals(Utils.ColorROSE)) setSelectedIndex(35);
		if (c.equals(Color.CYAN)) setSelectedIndex(36);
		if (c.equals(Utils.ColorTEAL)) setSelectedIndex(37);
		if (c.equals(Utils.ColorSPIN)) setSelectedIndex(38);
		if (c.equals(Color.YELLOW)) setSelectedIndex(39);
		if (c.equals(Utils.ColorAUBURN)) setSelectedIndex(40);
		if (c.equals(Utils.ColorINDIGO)) setSelectedIndex(41);
		if (c.equals(Utils.ColorBLOND)) setSelectedIndex(42);
		if (c.equals(Utils.ColorFLOWER)) setSelectedIndex(43);
		if (c.equals(Color.DARK_GRAY)) setSelectedIndex(44);
		if (c.equals(Utils.ColorGOLD)) setSelectedIndex(45);
		if (c.equals(Utils.ColorDARK)) setSelectedIndex(46);
		if (c.equals(Utils.ColorEYE)) setSelectedIndex(47);
		if (c.equals(Utils.ColorWINTER)) setSelectedIndex(48);
		if (c.equals(Utils.ColorOLDBARK)) setSelectedIndex(49);
		if (c.equals(Utils.ColorDARKJADE)) setSelectedIndex(50);
		if (c.equals(Utils.ColorDARKGREEN)) setSelectedIndex(51);
		if (c.equals(Utils.ColorDARKFIRE)) setSelectedIndex(52);
		if (c.equals(Utils.ColorDARKLILAC)) setSelectedIndex(53);
		if (c.equals(Utils.ColorDEEPSKY)) setSelectedIndex(54);
		if (c.equals(Utils.ColorDARKOLIVE)) setSelectedIndex(55);
		if (c.equals(Utils.ColorSPIKEPOINT)) setSelectedIndex(56);
		if (c.equals(Utils.ColorFRUIT)) setSelectedIndex(57);
		if (c.equals(Utils.ColorVISION)) setSelectedIndex(58);
		if (c.equals(Utils.ColorICE)) setSelectedIndex(59);
		if (c.equals(Utils.ColorLIGHT_BLUE)) setSelectedIndex(60);
		if (c.equals(Utils.ColorLIGHTBROWN)) setSelectedIndex(61);
		if (c.equals(Utils.ColorGREENBROWN)) setSelectedIndex(62);
		if (c.equals(Utils.ColorBROKEN)) setSelectedIndex(63);
		if (c.equals(Utils.ColorDEADBARK)) setSelectedIndex(64);
	}
}
