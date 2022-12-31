/* Copyright (C) 2006-2010  Joan Queralt Molina
 * Color Mod (C) 2012-2022  MarcoDBAA
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package biogenesis;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class BioXMLParser implements ErrorHandler {
	protected DocumentBuilder builder = null;
	protected Document doc = null;
	
	public BioXMLParser() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		try {
			builder = factory.newDocumentBuilder();
			builder.setErrorHandler(this);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeGeneticCode(PrintStream ps, GeneticCode geneticCode) {
		ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); //$NON-NLS-1$
		ps.println("<!DOCTYPE genetic_code ["); //$NON-NLS-1$
		ps.println("<!ELEMENT genetic_code (gene+)>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code symmetry (1|2|3|4|5|6|7|8) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code mutationrate CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code clonerate CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code activity (0|1|2) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code modifiescream (1|2|3) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code modifiesfallow (1|2|3|4) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code modifiesspore (1|2|3|4|5|6|7|8|9|10|11|12) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code adaptspore (1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|62|63|64|65|66|67|68|69|70) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code modifiesblack (1|2|3|4) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code adaptblack (1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code plague (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code mirror (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code disperse (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code generation (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code sibling (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code altruist (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code familial (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code social (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code peaceful (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code passive (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code clockwise (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code modifiespink (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code modifieslilac (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code modifiessky (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code modifiesleaf (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code selfish (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ELEMENT gene EMPTY>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene length CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene theta CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene color (green|forest|spring|summer|lime|leaf|c4|jade|grass|bark|purple|red|fire|orange|maroon|pink|cream|silver|spike|lilac|gray|violet|olive|sky|blue|ochre|fallow|spore|white|plague|coral|mint|lavender|magenta|rose|cyan|teal|yellow|auburn|indigo|blond|flower|darkgray|gold|dark|eye) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene branch CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene redreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene greenreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene bluereaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene plaguereaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene scourgereaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene whitereaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene grayreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene silverreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene defaultreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene consumerreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene plantreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene magentareaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene pinkreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene coralreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene orangereaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene barkreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene violetreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene virusreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene maroonreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene olivereaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene mintreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene creamreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene spikereaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene fallowreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene lightbluereaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene ochrereaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene skyreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene lilacreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene firereaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene lightbrownreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene greenbrownreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene brownreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene icereaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene brokenreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene sickreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene friendreaction CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("]>"); //$NON-NLS-1$
		ps.println("<genetic_code symmetry=\""+Integer.toString(geneticCode.getSymmetry())+ //$NON-NLS-1$
				"\" mutationrate=\""+Integer.toString(geneticCode.getMutationrate())+ //$NON-NLS-1$
				"\" clonerate=\""+Integer.toString(geneticCode.getClonerate())+ //$NON-NLS-1$
				"\" activity=\""+Integer.toString(geneticCode.getActivity())+ //$NON-NLS-1$
				"\" modifiescream=\""+Integer.toString(geneticCode.getModifiescream())+ //$NON-NLS-1$
				"\" modifiesfallow=\""+Integer.toString(geneticCode.getModifiesfallow())+ //$NON-NLS-1$
				"\" modifiesspore=\""+Integer.toString(geneticCode.getModifiesspore())+ //$NON-NLS-1$
				"\" adaptspore=\""+Integer.toString(geneticCode.getAdaptspore())+ //$NON-NLS-1$
				"\" modifiesblack=\""+Integer.toString(geneticCode.getModifiesblack())+ //$NON-NLS-1$
				"\" adaptblack=\""+Integer.toString(geneticCode.getAdaptblack())+ //$NON-NLS-1$
				"\" mirror=\""+(geneticCode.getMirror()==0?"no":"yes")+ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"\" plague=\""+(geneticCode.getPlague()?"yes":"no")+  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				"\" disperse=\""+(geneticCode.getDisperseChildren()?"yes":"no")+  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				"\" generation=\""+(geneticCode.getGenerationBattle()?"yes":"no")+  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				"\" sibling=\""+(geneticCode.getSiblingBattle()?"yes":"no")+  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				"\" altruist=\""+(geneticCode.getAltruist()?"yes":"no")+  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				"\" familial=\""+(geneticCode.getFamilial()?"yes":"no")+  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				"\" social=\""+(geneticCode.getSocial()?"yes":"no")+  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				"\" peaceful=\""+(geneticCode.getPeaceful()?"yes":"no")+  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				"\" passive=\""+(geneticCode.getPassive()?"yes":"no")+  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				"\" clockwise=\""+(geneticCode.getClockwise()?"yes":"no")+  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				"\" modifiespink=\""+(geneticCode.getModifiespink()?"yes":"no")+  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				"\" modifieslilac=\""+(geneticCode.getModifieslilac()?"yes":"no")+  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				"\" modifiessky=\""+(geneticCode.getModifiessky()?"yes":"no")+  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				"\" modifiesleaf=\""+(geneticCode.getModifiesleaf()?"yes":"no")+  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				"\" selfish=\""+(geneticCode.getSelfish()?"yes":"no")+  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				"\">"); //$NON-NLS-1$
		for (int i=0; i<geneticCode.getNGenes(); i++)
			writeGene(ps,geneticCode.getGene(i));
		ps.println("</genetic_code>"); //$NON-NLS-1$
	}
	
	public static void writeGene(PrintStream ps, Gene gene) {
		ps.println("\t<gene length=\""+Double.toString(gene.getLength())+"\" theta=\""+ //$NON-NLS-1$ //$NON-NLS-2$
				Double.toString(gene.getTheta())+"\" color=\""+ //$NON-NLS-1$
				colorToString(gene.getColor())+"\" branch=\""+ //$NON-NLS-1$
				Integer.toString(gene.getBranch())+"\" redreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getredReaction())+"\" greenreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getgreenReaction())+"\" bluereaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getblueReaction())+"\" plaguereaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getplagueReaction())+"\" scourgereaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getscourgeReaction())+"\" whitereaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getwhiteReaction())+"\" grayreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getgrayReaction())+"\" silverreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getsilverReaction())+"\" defaultreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getdefaultReaction())+"\" consumerreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getconsumerReaction())+"\" plantreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getplantReaction())+"\" magentareaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getmagentaReaction())+"\" pinkreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getpinkReaction())+"\" coralreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getcoralReaction())+"\" orangereaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getorangeReaction())+"\" barkreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getbarkReaction())+"\" violetreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getvioletReaction())+"\" virusreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getvirusReaction())+"\" maroonreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getmaroonReaction())+"\" olivereaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getoliveReaction())+"\" mintreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getmintReaction())+"\" creamreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getcreamReaction())+"\" spikereaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getspikeReaction())+"\" fallowreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getfallowReaction())+"\" lightbluereaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getlightblueReaction())+"\" ochrereaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getochreReaction())+"\" skyreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getskyReaction())+"\" lilacreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getlilacReaction())+"\" firereaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getfireReaction())+"\" lightbrownreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getlightbrownReaction())+"\" greenbrownreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getgreenbrownReaction())+"\" brownreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getbrownReaction())+"\" icereaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.geticeReaction())+"\" brokenreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getbrokenReaction())+"\" sickreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getsickReaction())+"\" friendreaction=\""+ //$NON-NLS-1$
				Integer.toString(gene.getfriendReaction())+"\" />"); //$NON-NLS-1$
	}
	
	public GeneticCode parseGeneticCode(File f) throws SAXException, IOException {
		int symmetry, mirror, mutationrate, clonerate, activity, modifiescream, modifiesfallow, modifiesspore, adaptspore, modifiesblack, adaptblack;
		boolean plague;
		boolean disperse;
		boolean generation;
		boolean sibling;
		boolean altruist;
		boolean familial;
		boolean social;
		boolean peaceful;
		boolean passive;
		boolean clockwise;
		boolean modifiespink;
		boolean modifieslilac;
		boolean modifiessky;
		boolean modifiesleaf;
		boolean selfish;
		List<Gene> genes = new ArrayList<Gene>();
		String s;
		doc = builder.parse(f);
		Element geneticCode = doc.getDocumentElement();
		if (geneticCode.getNodeName().equals("genetic_code")) { //$NON-NLS-1$
			s = geneticCode.getAttribute("symmetry"); //$NON-NLS-1$
			try {
				symmetry = Integer.parseInt(s); //$NON-NLS-1$
			} catch (NumberFormatException e) {
				throw new SAXException("Symmetry has not an allowed value."); //$NON-NLS-1$
			}
			if (symmetry<1 || symmetry>8)
				throw new SAXException("Symmetry has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("mirror"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				mirror = 1;
			else
				if (s.equals("no")) //$NON-NLS-1$
					mirror = 0;
				else
					throw new SAXException("Mirror has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("mutationrate"); //$NON-NLS-1$
			try {
				mutationrate = Integer.parseInt(s); //$NON-NLS-1$
			} catch (NumberFormatException e) {
				throw new SAXException("Mutationrate has not an allowed value."); //$NON-NLS-1$
			}
			if (mutationrate<0 || mutationrate>10000)
				throw new SAXException("Mutationrate has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("clonerate"); //$NON-NLS-1$
			try {
				clonerate = Integer.parseInt(s); //$NON-NLS-1$
			} catch (NumberFormatException e) {
				throw new SAXException("Clonerate has not an allowed value."); //$NON-NLS-1$
			}
			if (clonerate<0 || clonerate>100)
				throw new SAXException("Clonerate has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("activity"); //$NON-NLS-1$
			try {
				activity = Integer.parseInt(s); //$NON-NLS-1$
			} catch (NumberFormatException e) {
				throw new SAXException("Activity has not an allowed value."); //$NON-NLS-1$
			}
			if (activity<0 || activity>2)
				throw new SAXException("Activity has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("modifiescream"); //$NON-NLS-1$
			try {
				modifiescream = Integer.parseInt(s); //$NON-NLS-1$
			} catch (NumberFormatException e) {
				throw new SAXException("Modifiescream has not an allowed value."); //$NON-NLS-1$
			}
			if (modifiescream<1 || modifiescream>3)
				throw new SAXException("Modifiescream has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("modifiesfallow"); //$NON-NLS-1$
			try {
				modifiesfallow = Integer.parseInt(s); //$NON-NLS-1$
			} catch (NumberFormatException e) {
				throw new SAXException("Modifiesfallow has not an allowed value."); //$NON-NLS-1$
			}
			if (modifiesfallow<1 || modifiesfallow>4)
				throw new SAXException("Modifiesfallow has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("modifiesspore"); //$NON-NLS-1$
			try {
				modifiesspore = Integer.parseInt(s); //$NON-NLS-1$
			} catch (NumberFormatException e) {
				throw new SAXException("Modifiesspore has not an allowed value."); //$NON-NLS-1$
			}
			if (modifiesspore<1 || modifiesspore>12)
				throw new SAXException("Modifiesspore has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("adaptspore"); //$NON-NLS-1$
			try {
				adaptspore = Integer.parseInt(s); //$NON-NLS-1$
			} catch (NumberFormatException e) {
				throw new SAXException("Adaptspore has not an allowed value."); //$NON-NLS-1$
			}
			if (adaptspore<1 || adaptspore>70)
				throw new SAXException("Adaptspore has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("modifiesblack"); //$NON-NLS-1$
			try {
				modifiesblack = Integer.parseInt(s); //$NON-NLS-1$
			} catch (NumberFormatException e) {
				throw new SAXException("Modifiesblack has not an allowed value."); //$NON-NLS-1$
			}
			if (modifiesblack<1 || modifiesblack>4)
				throw new SAXException("Modifiesblack has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("adaptblack"); //$NON-NLS-1$
			try {
				adaptblack = Integer.parseInt(s); //$NON-NLS-1$
			} catch (NumberFormatException e) {
				throw new SAXException("Adaptblack has not an allowed value."); //$NON-NLS-1$
			}
			if (adaptblack<1 || adaptblack>24)
				throw new SAXException("Adaptblack has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("plague"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				plague = true;
			else
				if (s.equals("no")) //$NON-NLS-1$
					plague = false;
				else
					throw new SAXException("Plague has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("disperse"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				disperse = true;
			else
				if (s.equals("no")) //$NON-NLS-1$
					disperse = false;
				else
					throw new SAXException("Disperse has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("generation"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				generation = true;
			else
				if (s.equals("no")) //$NON-NLS-1$
					generation = false;
				else
					throw new SAXException("Generation has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("sibling"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				sibling = true;
			else
				if (s.equals("no")) //$NON-NLS-1$
					sibling = false;
				else
					throw new SAXException("Sibling has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("altruist"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				altruist = true;
			else
				if (s.equals("no")) //$NON-NLS-1$
					altruist = false;
				else
					throw new SAXException("Altruist has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("familial"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				familial = true;
			else
				if (s.equals("no")) //$NON-NLS-1$
					familial = false;
				else
					throw new SAXException("Familial has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("social"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				social = true;
			else
				if (s.equals("no")) //$NON-NLS-1$
					social = false;
				else
					throw new SAXException("Social has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("peaceful"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				peaceful = true;
			else
				if (s.equals("no")) //$NON-NLS-1$
					peaceful = false;
				else
					throw new SAXException("Peaceful has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("passive"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				passive = true;
			else
				if (s.equals("no")) //$NON-NLS-1$
					passive = false;
				else
					throw new SAXException("Passive has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("clockwise"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				clockwise = true;
			else
				if (s.equals("no")) //$NON-NLS-1$
					clockwise = false;
				else
					throw new SAXException("Clockwise has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("modifiespink"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				modifiespink = true;
			else
				if (s.equals("no")) //$NON-NLS-1$
					modifiespink = false;
				else
					throw new SAXException("Modifiespink has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("modifieslilac"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				modifieslilac = true;
			else
				if (s.equals("no")) //$NON-NLS-1$
					modifieslilac = false;
				else
					throw new SAXException("Modifieslilac has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("modifiessky"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				modifiessky = true;
			else
				if (s.equals("no")) //$NON-NLS-1$
					modifiessky = false;
				else
					throw new SAXException("Modifiessky has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("modifiesleaf"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				modifiesleaf = true;
			else
				if (s.equals("no")) //$NON-NLS-1$
					modifiesleaf = false;
				else
					throw new SAXException("Modifiesleaf has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("selfish"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				selfish = true;
			else
				if (s.equals("no")) //$NON-NLS-1$
					selfish = false;
				else
					throw new SAXException("Selfish has not an allowed value."); //$NON-NLS-1$
			
			Node gene = geneticCode.getFirstChild();
			gene = getNextElement(gene);
			while (gene != null) {
				genes.add(parseGene((Element)gene));
				gene = getNextElement(gene.getNextSibling());
			}
			return new GeneticCode(genes, symmetry, mirror, mutationrate, clonerate, activity, modifiescream, modifiesfallow, modifiesspore, adaptspore, modifiesblack, adaptblack, plague, disperse, generation, sibling, altruist, familial, social, peaceful, passive, clockwise, modifiespink, modifieslilac, modifiessky, modifiesleaf, selfish);
		}
		throw new SAXException("This file does not contain a genetic_code."); //$NON-NLS-1$
	}
	
	private static Node getNextElement(Node n) {
		while (n != null && n.getNodeType() != Node.ELEMENT_NODE) 
			n = n.getNextSibling();
		return n;
	}
	
	public Gene parseGene(Element gene) throws SAXException {
		double length, theta;
		int branch, redreaction, greenreaction, bluereaction, plaguereaction, scourgereaction, whitereaction, grayreaction, silverreaction, defaultreaction, consumerreaction
		, plantreaction, magentareaction, pinkreaction, coralreaction, orangereaction, barkreaction, violetreaction, virusreaction, maroonreaction, olivereaction, mintreaction
		, creamreaction, spikereaction, fallowreaction, lightbluereaction, ochrereaction, skyreaction, lilacreaction, firereaction, lightbrownreaction, greenbrownreaction
		, brownreaction, icereaction, brokenreaction, sickreaction, friendreaction;
		Color color;
		if (gene.getNodeName().equals("gene")) { //$NON-NLS-1$
			try {
				length = Double.parseDouble(gene.getAttribute("length")); //$NON-NLS-1$
				theta = Double.parseDouble(gene.getAttribute("theta")); //$NON-NLS-1$
				branch = Integer.parseInt(gene.getAttribute("branch")); //$NON-NLS-1$
				redreaction = Integer.parseInt(gene.getAttribute("redreaction")); //$NON-NLS-1$
				greenreaction = Integer.parseInt(gene.getAttribute("greenreaction")); //$NON-NLS-1$
				bluereaction = Integer.parseInt(gene.getAttribute("bluereaction")); //$NON-NLS-1$
				plaguereaction = Integer.parseInt(gene.getAttribute("plaguereaction")); //$NON-NLS-1$
				scourgereaction = Integer.parseInt(gene.getAttribute("scourgereaction")); //$NON-NLS-1$
				whitereaction = Integer.parseInt(gene.getAttribute("whitereaction")); //$NON-NLS-1$
				grayreaction = Integer.parseInt(gene.getAttribute("grayreaction")); //$NON-NLS-1$
				silverreaction = Integer.parseInt(gene.getAttribute("silverreaction")); //$NON-NLS-1$
				defaultreaction = Integer.parseInt(gene.getAttribute("defaultreaction")); //$NON-NLS-1$
				consumerreaction = Integer.parseInt(gene.getAttribute("consumerreaction")); //$NON-NLS-1$
				plantreaction = Integer.parseInt(gene.getAttribute("plantreaction")); //$NON-NLS-1$
				magentareaction = Integer.parseInt(gene.getAttribute("magentareaction")); //$NON-NLS-1$
				pinkreaction = Integer.parseInt(gene.getAttribute("pinkreaction")); //$NON-NLS-1$
				coralreaction = Integer.parseInt(gene.getAttribute("coralreaction")); //$NON-NLS-1$
				orangereaction = Integer.parseInt(gene.getAttribute("orangereaction")); //$NON-NLS-1$
				barkreaction = Integer.parseInt(gene.getAttribute("barkreaction")); //$NON-NLS-1$
				violetreaction = Integer.parseInt(gene.getAttribute("violetreaction")); //$NON-NLS-1$
				virusreaction = Integer.parseInt(gene.getAttribute("virusreaction")); //$NON-NLS-1$
				maroonreaction = Integer.parseInt(gene.getAttribute("maroonreaction")); //$NON-NLS-1$
				olivereaction = Integer.parseInt(gene.getAttribute("olivereaction")); //$NON-NLS-1$
				mintreaction = Integer.parseInt(gene.getAttribute("mintreaction")); //$NON-NLS-1$
				creamreaction = Integer.parseInt(gene.getAttribute("creamreaction")); //$NON-NLS-1$
				spikereaction = Integer.parseInt(gene.getAttribute("spikereaction")); //$NON-NLS-1$
				fallowreaction = Integer.parseInt(gene.getAttribute("fallowreaction")); //$NON-NLS-1$
				lightbluereaction = Integer.parseInt(gene.getAttribute("lightbluereaction")); //$NON-NLS-1$
				ochrereaction = Integer.parseInt(gene.getAttribute("ochrereaction")); //$NON-NLS-1$
				skyreaction = Integer.parseInt(gene.getAttribute("skyreaction")); //$NON-NLS-1$
				lilacreaction = Integer.parseInt(gene.getAttribute("lilacreaction")); //$NON-NLS-1$
				firereaction = Integer.parseInt(gene.getAttribute("firereaction")); //$NON-NLS-1$
				lightbrownreaction = Integer.parseInt(gene.getAttribute("lightbrownreaction")); //$NON-NLS-1$
				greenbrownreaction = Integer.parseInt(gene.getAttribute("greenbrownreaction")); //$NON-NLS-1$
				brownreaction = Integer.parseInt(gene.getAttribute("brownreaction")); //$NON-NLS-1$
				icereaction = Integer.parseInt(gene.getAttribute("icereaction")); //$NON-NLS-1$
				brokenreaction = Integer.parseInt(gene.getAttribute("brokenreaction")); //$NON-NLS-1$
				sickreaction = Integer.parseInt(gene.getAttribute("sickreaction")); //$NON-NLS-1$
				friendreaction = Integer.parseInt(gene.getAttribute("friendreaction")); //$NON-NLS-1$
			} catch (NumberFormatException e) {
				throw new SAXException("Attributes length and theta and reactions do not exist or have not an allowed value."); //$NON-NLS-1$
			}
			try {
				color = stringToColor(gene.getAttribute("color")); //$NON-NLS-1$
			} catch (IllegalArgumentException e) {
				throw new SAXException("Attribute color does not exist or has not an allowed value."); //$NON-NLS-1$
			}
			return new Gene(length,theta,color,branch,redreaction,greenreaction,bluereaction,plaguereaction,scourgereaction,whitereaction,grayreaction,silverreaction
					        ,defaultreaction,consumerreaction,plantreaction,magentareaction,pinkreaction,coralreaction,orangereaction,barkreaction,violetreaction,virusreaction
					        ,maroonreaction,olivereaction,mintreaction,creamreaction,spikereaction,fallowreaction,lightbluereaction,ochrereaction,skyreaction,lilacreaction
					        ,firereaction,lightbrownreaction,greenbrownreaction,brownreaction,icereaction,brokenreaction,sickreaction,friendreaction);
		}
		throw new SAXException("Parse error. "+gene.getNodeName()+" found but gene expected.");  //$NON-NLS-1$//$NON-NLS-2$
	}
	
	private static Color stringToColor(String s) throws IllegalArgumentException {
		if (s.equals("green")) return Color.GREEN; //$NON-NLS-1$
		if (s.equals("forest")) return Utils.ColorFOREST; //$NON-NLS-1$
		if (s.equals("spring")) return Utils.ColorSPRING; //$NON-NLS-1$
		if (s.equals("summer")) return Utils.ColorSUMMER; //$NON-NLS-1$
		if (s.equals("lime")) return Utils.ColorLIME; //$NON-NLS-1$
		if (s.equals("leaf")) return Utils.ColorLEAF; //$NON-NLS-1$
		if (s.equals("c4")) return Utils.ColorC4; //$NON-NLS-1$
		if (s.equals("jade")) return Utils.ColorJADE; //$NON-NLS-1$
		if (s.equals("grass")) return Utils.ColorGRASS; //$NON-NLS-1$
		if (s.equals("bark")) return Utils.ColorBARK; //$NON-NLS-1$
		if (s.equals("purple")) return Utils.ColorPURPLE; //$NON-NLS-1$
		if (s.equals("red")) return Color.RED; //$NON-NLS-1$
		if (s.equals("fire")) return Utils.ColorFIRE; //$NON-NLS-1$
		if (s.equals("orange")) return Color.ORANGE; //$NON-NLS-1$
		if (s.equals("maroon")) return Utils.ColorMAROON; //$NON-NLS-1$
		if (s.equals("pink")) return Color.PINK; //$NON-NLS-1$
		if (s.equals("cream")) return Utils.ColorCREAM; //$NON-NLS-1$
		if (s.equals("silver")) return Color.LIGHT_GRAY; //$NON-NLS-1$
		if (s.equals("spike")) return Utils.ColorSPIKE; //$NON-NLS-1$
		if (s.equals("lilac")) return Utils.ColorLILAC; //$NON-NLS-1$
		if (s.equals("gray")) return Color.GRAY; //$NON-NLS-1$
		if (s.equals("violet")) return Utils.ColorVIOLET; //$NON-NLS-1$
		if (s.equals("olive")) return Utils.ColorOLIVE; //$NON-NLS-1$
		if (s.equals("sky")) return Utils.ColorSKY; //$NON-NLS-1$
		if (s.equals("blue")) return Color.BLUE; //$NON-NLS-1$
		if (s.equals("ochre")) return Utils.ColorOCHRE; //$NON-NLS-1$
		if (s.equals("fallow")) return Utils.ColorFALLOW; //$NON-NLS-1$
		if (s.equals("spore")) return Utils.ColorSPORE; //$NON-NLS-1$
		if (s.equals("white")) return Color.WHITE; //$NON-NLS-1$
		if (s.equals("plague")) return Utils.ColorPLAGUE; //$NON-NLS-1$
		if (s.equals("coral")) return Utils.ColorCORAL; //$NON-NLS-1$
		if (s.equals("mint")) return Utils.ColorMINT; //$NON-NLS-1$
		if (s.equals("lavender")) return Utils.ColorLAVENDER; //$NON-NLS-1$
		if (s.equals("magenta")) return Color.MAGENTA; //$NON-NLS-1$
		if (s.equals("rose")) return Utils.ColorROSE; //$NON-NLS-1$
		if (s.equals("cyan")) return Color.CYAN; //$NON-NLS-1$
		if (s.equals("teal")) return Utils.ColorTEAL; //$NON-NLS-1$
		if (s.equals("yellow")) return Color.YELLOW; //$NON-NLS-1$
		if (s.equals("auburn")) return Utils.ColorAUBURN; //$NON-NLS-1$
		if (s.equals("indigo")) return Utils.ColorINDIGO; //$NON-NLS-1$
		if (s.equals("blond")) return Utils.ColorBLOND; //$NON-NLS-1$
		if (s.equals("flower")) return Utils.ColorFLOWER; //$NON-NLS-1$
		if (s.equals("darkgray")) return Color.DARK_GRAY; //$NON-NLS-1$
		if (s.equals("gold")) return Utils.ColorGOLD; //$NON-NLS-1$
		if (s.equals("dark")) return Utils.ColorDARK; //$NON-NLS-1$
		if (s.equals("eye")) return Utils.ColorEYE; //$NON-NLS-1$
		throw new IllegalArgumentException();
	}
	
	private static String colorToString(Color c) {
		if (c.equals(Color.GREEN)) return "green"; //$NON-NLS-1$
		if (c.equals(Utils.ColorFOREST)) return "forest"; //$NON-NLS-1$
		if (c.equals(Utils.ColorSPRING)) return "spring"; //$NON-NLS-1$
		if (c.equals(Utils.ColorSUMMER)) return "summer"; //$NON-NLS-1$
		if (c.equals(Utils.ColorLIME)) return "lime"; //$NON-NLS-1$
		if (c.equals(Utils.ColorLEAF)) return "leaf"; //$NON-NLS-1$
		if (c.equals(Utils.ColorC4)) return "c4"; //$NON-NLS-1$
		if (c.equals(Utils.ColorJADE)) return "jade"; //$NON-NLS-1$
		if (c.equals(Utils.ColorGRASS)) return "grass"; //$NON-NLS-1$
		if (c.equals(Utils.ColorBARK)) return "bark"; //$NON-NLS-1$
		if (c.equals(Utils.ColorPURPLE)) return "purple"; //$NON-NLS-1$
		if (c.equals(Color.RED)) return "red"; //$NON-NLS-1$
		if (c.equals(Utils.ColorFIRE)) return "fire"; //$NON-NLS-1$
		if (c.equals(Color.ORANGE)) return "orange"; //$NON-NLS-1$
		if (c.equals(Utils.ColorMAROON)) return "maroon"; //$NON-NLS-1$
		if (c.equals(Color.PINK)) return "pink"; //$NON-NLS-1$
		if (c.equals(Utils.ColorCREAM)) return "cream"; //$NON-NLS-1$
		if (c.equals(Color.LIGHT_GRAY)) return "silver"; //$NON-NLS-1$
		if (c.equals(Utils.ColorSPIKE)) return "spike"; //$NON-NLS-1$
		if (c.equals(Utils.ColorLILAC)) return "lilac"; //$NON-NLS-1$
		if (c.equals(Color.GRAY)) return "gray"; //$NON-NLS-1$
		if (c.equals(Utils.ColorVIOLET)) return "violet"; //$NON-NLS-1$
		if (c.equals(Utils.ColorOLIVE)) return "olive"; //$NON-NLS-1$
		if (c.equals(Utils.ColorSKY)) return "sky"; //$NON-NLS-1$
		if (c.equals(Color.BLUE)) return "blue"; //$NON-NLS-1$
		if (c.equals(Utils.ColorOCHRE)) return "ochre"; //$NON-NLS-1$
		if (c.equals(Utils.ColorFALLOW)) return "fallow"; //$NON-NLS-1$
		if (c.equals(Utils.ColorSPORE)) return "spore"; //$NON-NLS-1$
		if (c.equals(Color.WHITE)) return "white"; //$NON-NLS-1$
		if (c.equals(Utils.ColorPLAGUE)) return "plague"; //$NON-NLS-1$
		if (c.equals(Utils.ColorCORAL)) return "coral"; //$NON-NLS-1$
		if (c.equals(Utils.ColorMINT)) return "mint"; //$NON-NLS-1$
		if (c.equals(Utils.ColorLAVENDER)) return "lavender"; //$NON-NLS-1$
		if (c.equals(Color.MAGENTA)) return "magenta"; //$NON-NLS-1$
		if (c.equals(Utils.ColorROSE)) return "rose"; //$NON-NLS-1$
		if (c.equals(Color.CYAN)) return "cyan"; //$NON-NLS-1$
		if (c.equals(Utils.ColorTEAL)) return "teal"; //$NON-NLS-1$
		if (c.equals(Color.YELLOW)) return "yellow"; //$NON-NLS-1$
		if (c.equals(Utils.ColorAUBURN)) return "auburn"; //$NON-NLS-1$
		if (c.equals(Utils.ColorINDIGO)) return "indigo"; //$NON-NLS-1$
		if (c.equals(Utils.ColorBLOND)) return "blond"; //$NON-NLS-1$
		if (c.equals(Utils.ColorFLOWER)) return "flower"; //$NON-NLS-1$
		if (c.equals(Color.DARK_GRAY)) return "darkgray"; //$NON-NLS-1$
		if (c.equals(Utils.ColorGOLD)) return "gold"; //$NON-NLS-1$
		if (c.equals(Utils.ColorDARK)) return "dark"; //$NON-NLS-1$
		if (c.equals(Utils.ColorEYE)) return "eye"; //$NON-NLS-1$
		return ""; //$NON-NLS-1$
	}

	public void warning(SAXParseException arg0) throws SAXException {
		throw new SAXException(arg0.getMessage());
	}

	public void error(SAXParseException arg0) throws SAXException {
		throw new SAXException(arg0.getMessage());
	}

	public void fatalError(SAXParseException arg0) throws SAXException {
		throw new SAXException(arg0.getMessage());
	}
}
