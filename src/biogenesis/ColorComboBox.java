package biogenesis;

import java.awt.Color;

import javax.swing.JComboBox;


public class ColorComboBox extends JComboBox {
	private static final long serialVersionUID = Utils.VERSION;
	private static final String[] colorValues = {Messages.getString("T_GREEN"),Messages.getString("T_FOREST"),  //$NON-NLS-1$//$NON-NLS-2$
		Messages.getString("T_SPRING"),Messages.getString("T_SUMMER"),Messages.getString("T_LIME"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_LEAF"),Messages.getString("T_C4"),Messages.getString("T_JADE"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_GRASS"),Messages.getString("T_BARK"),Messages.getString("T_PURPLE"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_RED"),Messages.getString("T_FIRE"),Messages.getString("T_ORANGE"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_MAROON"),Messages.getString("T_PINK"),Messages.getString("T_CREAM"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_SILVER"),Messages.getString("T_SPIKE"),Messages.getString("T_LILAC"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_GRAY"),Messages.getString("T_VIOLET"),Messages.getString("T_OLIVE"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_SKY"),Messages.getString("T_BLUE"),Messages.getString("T_OCHRE"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_FALLOW"),Messages.getString("T_SPORE"),Messages.getString("T_WHITE"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_PLAGUE"),Messages.getString("T_CORAL"),Messages.getString("T_MINT"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_LAVENDER"),Messages.getString("T_MAGENTA"),Messages.getString("T_ROSE"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_CYAN"),Messages.getString("T_TEAL"),Messages.getString("T_YELLOW"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_AUBURN"),Messages.getString("T_INDIGO"),Messages.getString("T_BLOND"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_FLOWER"),Messages.getString("T_DARKGRAY"),Messages.getString("T_GOLD"),  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_DARK"),Messages.getString("T_EYE")};  //$NON-NLS-1$//$NON-NLS-2$ 
	
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
		case 11: return Color.RED;
		case 12: return Utils.ColorFIRE;
		case 13: return Color.ORANGE;
		case 14: return Utils.ColorMAROON;
		case 15: return Color.PINK;
		case 16: return Utils.ColorCREAM;
		case 17: return Color.LIGHT_GRAY;
		case 18: return Utils.ColorSPIKE;
		case 19: return Utils.ColorLILAC;
		case 20: return Color.GRAY;
		case 21: return Utils.ColorVIOLET;
		case 22: return Utils.ColorOLIVE;
		case 23: return Utils.ColorSKY;
		case 24: return Color.BLUE;
		case 25: return Utils.ColorOCHRE;
		case 26: return Utils.ColorFALLOW;
		case 27: return Utils.ColorSPORE;
		case 28: return Color.WHITE;
		case 29: return Utils.ColorPLAGUE;
		case 30: return Utils.ColorCORAL;
		case 31: return Utils.ColorMINT;
		case 32: return Utils.ColorLAVENDER;
		case 33: return Color.MAGENTA;
		case 34: return Utils.ColorROSE;
		case 35: return Color.CYAN;
		case 36: return Utils.ColorTEAL;
		case 37: return Color.YELLOW;
		case 38: return Utils.ColorAUBURN;
		case 39: return Utils.ColorINDIGO;
		case 40: return Utils.ColorBLOND;
		case 41: return Utils.ColorFLOWER;
		case 42: return Color.DARK_GRAY;
		case 43: return Utils.ColorGOLD;
		case 44: return Utils.ColorDARK;
		case 45: return Utils.ColorEYE;
		case 46: return Utils.ColorWINTER;
		case 47: return Utils.ColorOLDBARK;
		case 48: return Utils.ColorDARKJADE;
		case 49: return Utils.ColorDARKGREEN;
		case 50: return Utils.ColorDARKFIRE;
		case 51: return Utils.ColorDARKLILAC;
		case 52: return Utils.ColorDEEPSKY;
		case 53: return Utils.ColorDARKOLIVE;
		case 54: return Utils.ColorSPIKEPOINT;
		case 55: return Utils.ColorFRUIT;
		case 56: return Utils.ColorVISION;
		case 57: return Utils.ColorICE;
		case 58: return Utils.ColorLIGHT_BLUE;
		case 59: return Utils.ColorLIGHTBROWN;
		case 60: return Utils.ColorGREENBROWN;
		case 61: return Utils.ColorBROKEN;
		case 62: return Utils.ColorDEADBARK;
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
		if (c.equals(Color.RED)) setSelectedIndex(11);
		if (c.equals(Utils.ColorFIRE)) setSelectedIndex(12);
		if (c.equals(Color.ORANGE)) setSelectedIndex(13);
		if (c.equals(Utils.ColorMAROON)) setSelectedIndex(14);
		if (c.equals(Color.PINK)) setSelectedIndex(15);
		if (c.equals(Utils.ColorCREAM)) setSelectedIndex(16);
		if (c.equals(Color.LIGHT_GRAY)) setSelectedIndex(17);
		if (c.equals(Utils.ColorSPIKE)) setSelectedIndex(18);
		if (c.equals(Utils.ColorLILAC)) setSelectedIndex(19);
		if (c.equals(Color.GRAY)) setSelectedIndex(20);
		if (c.equals(Utils.ColorVIOLET)) setSelectedIndex(21);
		if (c.equals(Utils.ColorOLIVE)) setSelectedIndex(22);
		if (c.equals(Utils.ColorSKY)) setSelectedIndex(23);
		if (c.equals(Color.BLUE)) setSelectedIndex(24);
		if (c.equals(Utils.ColorOCHRE)) setSelectedIndex(25);
		if (c.equals(Utils.ColorFALLOW)) setSelectedIndex(26);
		if (c.equals(Utils.ColorSPORE)) setSelectedIndex(27);
		if (c.equals(Color.WHITE)) setSelectedIndex(28);
		if (c.equals(Utils.ColorPLAGUE)) setSelectedIndex(29);
		if (c.equals(Utils.ColorCORAL)) setSelectedIndex(30);
		if (c.equals(Utils.ColorMINT)) setSelectedIndex(31);
		if (c.equals(Utils.ColorLAVENDER)) setSelectedIndex(32);
		if (c.equals(Color.MAGENTA)) setSelectedIndex(33);
		if (c.equals(Utils.ColorROSE)) setSelectedIndex(34);
		if (c.equals(Color.CYAN)) setSelectedIndex(35);
		if (c.equals(Utils.ColorTEAL)) setSelectedIndex(36);
		if (c.equals(Color.YELLOW)) setSelectedIndex(37);
		if (c.equals(Utils.ColorAUBURN)) setSelectedIndex(38);
		if (c.equals(Utils.ColorINDIGO)) setSelectedIndex(39);
		if (c.equals(Utils.ColorBLOND)) setSelectedIndex(40);
		if (c.equals(Utils.ColorFLOWER)) setSelectedIndex(41);
		if (c.equals(Color.DARK_GRAY)) setSelectedIndex(42);
		if (c.equals(Utils.ColorGOLD)) setSelectedIndex(43);
		if (c.equals(Utils.ColorDARK)) setSelectedIndex(44);
		if (c.equals(Utils.ColorEYE)) setSelectedIndex(45);
		if (c.equals(Utils.ColorWINTER)) setSelectedIndex(46);
		if (c.equals(Utils.ColorOLDBARK)) setSelectedIndex(47);
		if (c.equals(Utils.ColorDARKJADE)) setSelectedIndex(48);
		if (c.equals(Utils.ColorDARKGREEN)) setSelectedIndex(49);
		if (c.equals(Utils.ColorDARKFIRE)) setSelectedIndex(50);
		if (c.equals(Utils.ColorDARKLILAC)) setSelectedIndex(51);
		if (c.equals(Utils.ColorDEEPSKY)) setSelectedIndex(52);
		if (c.equals(Utils.ColorDARKOLIVE)) setSelectedIndex(53);
		if (c.equals(Utils.ColorSPIKEPOINT)) setSelectedIndex(54);
		if (c.equals(Utils.ColorFRUIT)) setSelectedIndex(55);
		if (c.equals(Utils.ColorVISION)) setSelectedIndex(56);
		if (c.equals(Utils.ColorICE)) setSelectedIndex(57);
		if (c.equals(Utils.ColorLIGHT_BLUE)) setSelectedIndex(58);
		if (c.equals(Utils.ColorLIGHTBROWN)) setSelectedIndex(59);
		if (c.equals(Utils.ColorGREENBROWN)) setSelectedIndex(60);
		if (c.equals(Utils.ColorBROKEN)) setSelectedIndex(61);
		if (c.equals(Utils.ColorDEADBARK)) setSelectedIndex(62);
	}
}
