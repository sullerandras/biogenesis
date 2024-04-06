package biogenesis;

import java.awt.Color;

import javax.swing.JComboBox;


public class ColorComboBox extends JComboBox {
	private static final long serialVersionUID = Utils.VERSION;
	private static final String[] colorValues = {Messages.getString("T_GREEN"),Messages.getString("T_FOREST"),  //$NON-NLS-1$//$NON-NLS-2$
		Messages.getString("T_IVY"),Messages.getString("T_SPRING"),Messages.getString("T_SUMMER"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_LIME"),Messages.getString("T_LEAF"),Messages.getString("T_C4"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_JADE"),Messages.getString("T_GRASS"),Messages.getString("T_BARK"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_PURPLE"),Messages.getString("T_PLANKTON"),Messages.getString("T_RED"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_FIRE"),Messages.getString("T_ORANGE"),Messages.getString("T_MAROON"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_CRIMSON"),Messages.getString("T_PINK"),Messages.getString("T_CREAM"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_SILVER"),Messages.getString("T_SPIKE"),Messages.getString("T_LILAC"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_GRAY"),Messages.getString("T_VIOLET"),Messages.getString("T_OLIVE"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_SKY"),Messages.getString("T_BLUE"),Messages.getString("T_OCHRE"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_FALLOW"),Messages.getString("T_SPORE"),Messages.getString("T_WHITE"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_PLAGUE"),Messages.getString("T_CORAL"),Messages.getString("T_MINT"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_LAVENDER"),Messages.getString("T_MAGENTA"),Messages.getString("T_ROSE"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_CYAN"),Messages.getString("T_TEAL"),Messages.getString("T_DRIFT"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
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
		case 2: return Utils.ColorIVY;
		case 3: return Utils.ColorSPRING;
		case 4: return Utils.ColorSUMMER;
		case 5: return Utils.ColorLIME;
		case 6: return Utils.ColorLEAF;
		case 7: return Utils.ColorC4;
		case 8: return Utils.ColorJADE;
		case 9: return Utils.ColorGRASS;
		case 10: return Utils.ColorBARK;
		case 11: return Utils.ColorPURPLE;
		case 12: return Utils.ColorPLANKTON;
		case 13: return Color.RED;
		case 14: return Utils.ColorFIRE;
		case 15: return Color.ORANGE;
		case 16: return Utils.ColorMAROON;
		case 17: return Utils.ColorCRIMSON;
		case 18: return Color.PINK;
		case 19: return Utils.ColorCREAM;
		case 20: return Color.LIGHT_GRAY;
		case 21: return Utils.ColorSPIKE;
		case 22: return Utils.ColorLILAC;
		case 23: return Color.GRAY;
		case 24: return Utils.ColorVIOLET;
		case 25: return Utils.ColorOLIVE;
		case 26: return Utils.ColorSKY;
		case 27: return Color.BLUE;
		case 28: return Utils.ColorOCHRE;
		case 29: return Utils.ColorFALLOW;
		case 30: return Utils.ColorSPORE;
		case 31: return Color.WHITE;
		case 32: return Utils.ColorPLAGUE;
		case 33: return Utils.ColorCORAL;
		case 34: return Utils.ColorMINT;
		case 35: return Utils.ColorLAVENDER;
		case 36: return Color.MAGENTA;
		case 37: return Utils.ColorROSE;
		case 38: return Color.CYAN;
		case 39: return Utils.ColorTEAL;
		case 40: return Utils.ColorDRIFT;
		case 41: return Utils.ColorSPIN;
		case 42: return Color.YELLOW;
		case 43: return Utils.ColorAUBURN;
		case 44: return Utils.ColorINDIGO;
		case 45: return Utils.ColorBLOND;
		case 46: return Utils.ColorFLOWER;
		case 47: return Color.DARK_GRAY;
		case 48: return Utils.ColorGOLD;
		case 49: return Utils.ColorDARK;
		case 50: return Utils.ColorEYE;
		case 51: return Utils.ColorWINTER;
		case 52: return Utils.ColorOLDBARK;
		case 53: return Utils.ColorDARKJADE;
		case 54: return Utils.ColorDARKGREEN;
		case 55: return Utils.ColorDARKFIRE;
		case 56: return Utils.ColorDARKLILAC;
		case 57: return Utils.ColorDEEPSKY;
		case 58: return Utils.ColorDARKOLIVE;
		case 59: return Utils.ColorSPIKEPOINT;
		case 60: return Utils.ColorFRUIT;
		case 61: return Utils.ColorVISION;
		case 62: return Utils.ColorICE;
		case 63: return Utils.ColorLIGHT_BLUE;
		case 64: return Utils.ColorLIGHTBROWN;
		case 65: return Utils.ColorGREENBROWN;
		case 66: return Utils.ColorBROKEN;
		case 67: return Utils.ColorDEADBARK;
		default: return Utils.ColorBROWN;
		}
	}
	
	public void setSelectedColor(Color c) {
		if (c.equals(Color.GREEN)) setSelectedIndex(0);
		if (c.equals(Utils.ColorFOREST)) setSelectedIndex(1);
		if (c.equals(Utils.ColorIVY)) setSelectedIndex(2);
		if (c.equals(Utils.ColorSPRING)) setSelectedIndex(3);
		if (c.equals(Utils.ColorSUMMER)) setSelectedIndex(4);
		if (c.equals(Utils.ColorLIME)) setSelectedIndex(5);
		if (c.equals(Utils.ColorLEAF)) setSelectedIndex(6);
		if (c.equals(Utils.ColorC4)) setSelectedIndex(7);
		if (c.equals(Utils.ColorJADE)) setSelectedIndex(8);
		if (c.equals(Utils.ColorGRASS)) setSelectedIndex(9);
		if (c.equals(Utils.ColorBARK)) setSelectedIndex(10);
		if (c.equals(Utils.ColorPURPLE)) setSelectedIndex(11);
		if (c.equals(Utils.ColorPLANKTON)) setSelectedIndex(12);
		if (c.equals(Color.RED)) setSelectedIndex(13);
		if (c.equals(Utils.ColorFIRE)) setSelectedIndex(14);
		if (c.equals(Color.ORANGE)) setSelectedIndex(15);
		if (c.equals(Utils.ColorMAROON)) setSelectedIndex(16);
		if (c.equals(Utils.ColorCRIMSON)) setSelectedIndex(17);
		if (c.equals(Color.PINK)) setSelectedIndex(18);
		if (c.equals(Utils.ColorCREAM)) setSelectedIndex(19);
		if (c.equals(Color.LIGHT_GRAY)) setSelectedIndex(20);
		if (c.equals(Utils.ColorSPIKE)) setSelectedIndex(21);
		if (c.equals(Utils.ColorLILAC)) setSelectedIndex(22);
		if (c.equals(Color.GRAY)) setSelectedIndex(23);
		if (c.equals(Utils.ColorVIOLET)) setSelectedIndex(24);
		if (c.equals(Utils.ColorOLIVE)) setSelectedIndex(25);
		if (c.equals(Utils.ColorSKY)) setSelectedIndex(26);
		if (c.equals(Color.BLUE)) setSelectedIndex(27);
		if (c.equals(Utils.ColorOCHRE)) setSelectedIndex(28);
		if (c.equals(Utils.ColorFALLOW)) setSelectedIndex(29);
		if (c.equals(Utils.ColorSPORE)) setSelectedIndex(30);
		if (c.equals(Color.WHITE)) setSelectedIndex(31);
		if (c.equals(Utils.ColorPLAGUE)) setSelectedIndex(32);
		if (c.equals(Utils.ColorCORAL)) setSelectedIndex(33);
		if (c.equals(Utils.ColorMINT)) setSelectedIndex(34);
		if (c.equals(Utils.ColorLAVENDER)) setSelectedIndex(35);
		if (c.equals(Color.MAGENTA)) setSelectedIndex(36);
		if (c.equals(Utils.ColorROSE)) setSelectedIndex(37);
		if (c.equals(Color.CYAN)) setSelectedIndex(38);
		if (c.equals(Utils.ColorTEAL)) setSelectedIndex(39);
		if (c.equals(Utils.ColorDRIFT)) setSelectedIndex(40);
		if (c.equals(Utils.ColorSPIN)) setSelectedIndex(41);
		if (c.equals(Color.YELLOW)) setSelectedIndex(42);
		if (c.equals(Utils.ColorAUBURN)) setSelectedIndex(43);
		if (c.equals(Utils.ColorINDIGO)) setSelectedIndex(44);
		if (c.equals(Utils.ColorBLOND)) setSelectedIndex(45);
		if (c.equals(Utils.ColorFLOWER)) setSelectedIndex(46);
		if (c.equals(Color.DARK_GRAY)) setSelectedIndex(47);
		if (c.equals(Utils.ColorGOLD)) setSelectedIndex(48);
		if (c.equals(Utils.ColorDARK)) setSelectedIndex(49);
		if (c.equals(Utils.ColorEYE)) setSelectedIndex(50);
		if (c.equals(Utils.ColorWINTER)) setSelectedIndex(51);
		if (c.equals(Utils.ColorOLDBARK)) setSelectedIndex(52);
		if (c.equals(Utils.ColorDARKJADE)) setSelectedIndex(53);
		if (c.equals(Utils.ColorDARKGREEN)) setSelectedIndex(54);
		if (c.equals(Utils.ColorDARKFIRE)) setSelectedIndex(55);
		if (c.equals(Utils.ColorDARKLILAC)) setSelectedIndex(56);
		if (c.equals(Utils.ColorDEEPSKY)) setSelectedIndex(57);
		if (c.equals(Utils.ColorDARKOLIVE)) setSelectedIndex(58);
		if (c.equals(Utils.ColorSPIKEPOINT)) setSelectedIndex(59);
		if (c.equals(Utils.ColorFRUIT)) setSelectedIndex(60);
		if (c.equals(Utils.ColorVISION)) setSelectedIndex(61);
		if (c.equals(Utils.ColorICE)) setSelectedIndex(62);
		if (c.equals(Utils.ColorLIGHT_BLUE)) setSelectedIndex(63);
		if (c.equals(Utils.ColorLIGHTBROWN)) setSelectedIndex(64);
		if (c.equals(Utils.ColorGREENBROWN)) setSelectedIndex(65);
		if (c.equals(Utils.ColorBROKEN)) setSelectedIndex(66);
		if (c.equals(Utils.ColorDEADBARK)) setSelectedIndex(67);
	}
}
