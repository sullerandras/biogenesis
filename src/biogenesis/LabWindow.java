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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.xml.sax.SAXException;


public class LabWindow extends JDialog implements ActionListener, ChangeListener {
	private static final long serialVersionUID = Utils.FILE_VERSION;
	protected MainWindow mainWindow;
	protected List<Gene> genesList;
	protected JButton cancelButton;
	protected JButton okButton;
	protected JPanel genesPanel;
	protected JPanel drawPanel;
	protected JScrollPane genesScroll;
	protected JLabel energyLabel;
	protected JLabel lifeLabel;
	protected JLabel segmentsLabel;
	protected JTextField mutationLabel;
	protected JTextField cloneLabel;
	protected JTextField homeXLabel;
	protected JTextField homeYLabel;
	protected JTextField base1XLabel;
	protected JTextField base1YLabel;
	protected JTextField base2XLabel;
	protected JTextField base2YLabel;
	protected JComboBox symmetryCombo;
	protected JComboBox mirrorCombo;
	protected JComboBox activityCombo;
	protected JComboBox modifiescreamCombo;
	protected JComboBox modifiesfallowCombo;
	protected JComboBox modifiessporeCombo;
	protected JComboBox adaptsporeCombo;
	protected JComboBox modifiesblackCombo;
	protected JComboBox adaptblackCombo;
	protected JComboBox plagueCombo;
	protected JComboBox disperseCombo;
	protected JComboBox generationCombo;
	protected JComboBox siblingCombo;
	protected JComboBox altruistCombo;
	protected JComboBox familialCombo;
	protected JComboBox socialCombo;
	protected JComboBox peacefulCombo;
	protected JComboBox passiveCombo;
	protected JComboBox clockwiseCombo;
	protected JComboBox modifiespinkCombo;
	protected JComboBox modifieslilacCombo;
	protected JComboBox modifiesskyCombo;
	protected JComboBox modifiesleafCombo;
	protected JComboBox selfishCombo;

	protected int symmetry=2;
	protected int energy=40;
	protected int life=Utils.MAX_AGE;
	protected int mirror=0;
	protected int mutationrate= (Utils.MIN_MUTATION_RATE + Utils.MAX_MUTATION_RATE) / 2;
	protected int clonerate= (Utils.MIN_CLONE_RATE + Utils.MAX_CLONE_RATE) / 2;
	protected double homeX= -1;
	protected double homeY= -1;
	protected double base1X= -1;
	protected double base1Y= -1;
	protected double base2X= -1;
	protected double base2Y= -1;
	protected int activity=2;
	protected int modifiescream=2;
	protected int modifiesfallow=2;
	protected int modifiesspore=4;
	protected int adaptspore=4;
	protected int modifiesblack=1;
	protected int adaptblack=1;
	protected boolean plague = false;
	protected boolean disperseChildren = false;
	protected boolean generationBattle = false;
	protected boolean siblingBattle = false;
	protected boolean altruist = false;
	protected boolean familial = false;
	protected boolean social = false;
	protected boolean peaceful = false;
	protected boolean passive = false;
	protected boolean clockwise = false;
	protected boolean modifiespink = false;
	protected boolean modifieslilac = false;
	protected boolean modifiessky = false;
	protected boolean modifiesleaf = false;
	protected boolean selfish = false;

	public LabWindow(MainWindow v, GeneticCode gc) {
		super(v, Messages.getString("T_GENETIC_LABORATORY")); //$NON-NLS-1$
		init(v, gc);
	}

	public LabWindow(MainWindow v) {
		super(v, Messages.getString("T_GENETIC_LABORATORY")); //$NON-NLS-1$
		init(v, v.getVisibleWorld().clippedGeneticCode);
	}

	private void init(MainWindow v, GeneticCode gc) {
		mainWindow = v;
		genesList = new ArrayList<Gene>();
		if (gc != null)
			importGeneticCode(gc);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setComponents();
		pack();
		WindowManager.registerWindow(this, getWidth(), getHeight(), 0, 0);
		//setResizable(false);
		cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	dispose();
            }
            });
		okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	if (genesList.size() > 0)
            		mainWindow.getVisibleWorld().setClippedGeneticCode(new GeneticCode(genesList, symmetry, mirror, mutationrate, clonerate, homeX, homeY, base1X, base1Y,
            		base2X, base2Y, activity, modifiescream, modifiesfallow, modifiesspore, adaptspore, modifiesblack, adaptblack, plague, disperseChildren, generationBattle,
            		siblingBattle, altruist, familial, social, peaceful, passive, clockwise, modifiespink, modifieslilac, modifiessky, modifiesleaf, selfish));
            	else
            		mainWindow.getVisibleWorld().removeClippedGeneticCode();
            	dispose();
            }
            });

		addWindowListener(new AppFocusWindowAdapter());
		setVisible(true);
	}

	/* Initialize the variables of this dialog using a previously existing genetic code */
	private void importGeneticCode(GeneticCode g) {
		mutationrate = g.getMutationrate();
		clonerate = g.getClonerate();
		homeX = g.getHomeX();
		homeY = g.getHomeY();
		base1X = g.getBase1X();
		base1Y = g.getBase1Y();
		base2X = g.getBase2X();
		base2Y = g.getBase2Y();
		activity = g.getActivity();
		modifiescream = g.getModifiescream();
		modifiesfallow = g.getModifiesfallow();
		modifiesspore = g.getModifiesspore();
		adaptspore = g.getAdaptspore();
		modifiesblack = g.getModifiesblack();
		adaptblack = g.getAdaptblack();
		plague = g.getPlague();
		disperseChildren = g.getDisperseChildren();
		generationBattle = g.getGenerationBattle();
		siblingBattle = g.getSiblingBattle();
		altruist = g.getAltruist();
		familial = g.getFamilial();
		social = g.getSocial();
		peaceful = g.getPeaceful();
		passive = g.getPassive();
		clockwise = g.getClockwise();
		modifiespink = g.getModifiespink();
		modifieslilac = g.getModifieslilac();
		modifiessky = g.getModifiessky();
		modifiesleaf = g.getModifiesleaf();
		selfish = g.getSelfish();
		mirror = g.getMirror();
		symmetry = g.getSymmetry();
		for (int i=0; i<g.getNGenes(); i++)
			genesList.add((Gene)g.getGene(i).clone());
		energy = 40 + 3 * symmetry * genesList.size();
		life = Utils.MAX_AGE + (int) ((genesList.size() * symmetry)/Utils.AGE_DIVISOR);
	}

	/* Initialize all the components of the dialog */
	protected void setComponents() {
		getContentPane().setLayout(new BorderLayout());
		JPanel generalPanel = new JPanel();
		generalPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		generalPanel.add(new JLabel(Messages.getString("T_SEGMENTS"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		segmentsLabel = new JLabel(Integer.toString(genesList.size() * symmetry));
		generalPanel.add(segmentsLabel, gridBagConstraints);
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		generalPanel.add(new JLabel(Messages.getString("T_ENERGY_TO_REPRODUCE"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		energyLabel = new JLabel(Integer.toString(energy));
		generalPanel.add(energyLabel, gridBagConstraints);
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		generalPanel.add(new JLabel(Messages.getString("T_LIFE_EXPECTANCY"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 0;
		lifeLabel = new JLabel(Integer.toString(life));
		generalPanel.add(lifeLabel, gridBagConstraints);
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 0;
		generalPanel.add(new JLabel(Messages.getString("T_MUTATION_PERCENTAGE"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 0;
		mutationLabel = new JTextField(Integer.toString(mutationrate),4);
		mutationLabel.setText(Integer.toString(mutationrate));
		mutationLabel.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	int i;
				try {
					i = Integer.parseInt(mutationLabel.getText());
					if (i >= Utils.MIN_MUTATION_RATE && i <= Utils.MAX_MUTATION_RATE) mutationrate = i;
				} catch (NumberFormatException ex) {
					// Keep old value if there is a problem
				}
            }
        });
		generalPanel.add(mutationLabel, gridBagConstraints);
		gridBagConstraints.gridx = 8;
		gridBagConstraints.gridy = 0;
		generalPanel.add(new JLabel(Messages.getString("T_CLONE_PERCENTAGE"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 9;
		gridBagConstraints.gridy = 0;
		cloneLabel = new JTextField(Integer.toString(clonerate),4);
		cloneLabel.setText(Integer.toString(clonerate));
		cloneLabel.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	int i;
				try {
					i = Integer.parseInt(cloneLabel.getText());
					if (i >= Utils.MIN_CLONE_RATE && i <= Utils.MAX_CLONE_RATE) clonerate = i;
				} catch (NumberFormatException ex) {
					// Keep old value if there is a problem
				}
            }
        });
		generalPanel.add(cloneLabel, gridBagConstraints);
		gridBagConstraints.gridx = 10;
		gridBagConstraints.gridy = 0;
		generalPanel.add(new JLabel(Messages.getString("T_HOME_X_PERCENTAGE"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 11;
		gridBagConstraints.gridy = 0;
		homeXLabel = new JTextField(Double.toString(homeX));
		homeXLabel.setText(Double.toString(homeX));
		homeXLabel.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	double d;
				try {
					d = Double.parseDouble(homeXLabel.getText());
					if ((d >= 0 && d <= 100) || (d == -1)) homeX = d;
				} catch (NumberFormatException ex) {
					// Keep old value if there is a problem
				}
            }
        });
		generalPanel.add(homeXLabel, gridBagConstraints);
		gridBagConstraints.gridx = 12;
		gridBagConstraints.gridy = 0;
		generalPanel.add(new JLabel(Messages.getString("T_HOME_Y_PERCENTAGE"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 13;
		gridBagConstraints.gridy = 0;
		homeYLabel = new JTextField(Double.toString(homeY));
		homeYLabel.setText(Double.toString(homeY));
		homeYLabel.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	double d;
				try {
					d = Double.parseDouble(homeYLabel.getText());
					if ((d >= 0 && d <= 100) || (d == -1)) homeY = d;
				} catch (NumberFormatException ex) {
					// Keep old value if there is a problem
				}
            }
        });
		generalPanel.add(homeYLabel, gridBagConstraints);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		generalPanel.add(new JLabel(Messages.getString("T_SYMMETRY"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		String[] symmetryValues = {"1","2","3","4","5","6","7","8"};  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		symmetryCombo = new JComboBox(symmetryValues);
		symmetryCombo.setSelectedItem(Integer.toString(symmetry));
		symmetryCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					symmetry = Integer.parseInt((String)symmetryCombo.getSelectedItem());
					energy = 40 + 3 * symmetry * genesList.size();
					energyLabel.setText(Integer.toString(energy));
					life = Utils.MAX_AGE + (int) ((genesList.size() * symmetry)/Utils.AGE_DIVISOR);
					lifeLabel.setText(Integer.toString(life));
					segmentsLabel.setText(Integer.toString(genesList.size() * symmetry));
					drawPanel.repaint();
				}
			}
		});
		generalPanel.add(symmetryCombo, gridBagConstraints);
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		generalPanel.add(new JLabel(Messages.getString("T_MIRROR"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		String[] noyesValues = {Messages.getString("T_NO"), Messages.getString("T_YES")}; //$NON-NLS-1$ //$NON-NLS-2$
		mirrorCombo = new JComboBox(noyesValues);
		mirrorCombo.setSelectedIndex(mirror);
		mirrorCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					mirror = mirrorCombo.getSelectedIndex();
					drawPanel.repaint();
				}
			}
		});
		generalPanel.add(mirrorCombo, gridBagConstraints);
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 1;
		generalPanel.add(new JLabel(Messages.getString("T_DISPERSE_CHILDREN"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 1;
		disperseCombo = new JComboBox(noyesValues);
		disperseCombo.setSelectedIndex(disperseChildren==false?0:1);
		disperseCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED)
					disperseChildren = disperseCombo.getSelectedIndex()==0? false: true;
			}
		});
		generalPanel.add(disperseCombo, gridBagConstraints);
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 1;
		generalPanel.add(new JLabel(Messages.getString("T_FAMILIAL"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 1;
		familialCombo = new JComboBox(noyesValues);
		familialCombo.setSelectedIndex(familial==false?0:1);
		familialCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED)
					familial = familialCombo.getSelectedIndex()==0? false: true;
			}
		});
		generalPanel.add(familialCombo, gridBagConstraints);
		gridBagConstraints.gridx = 8;
		gridBagConstraints.gridy = 1;
		generalPanel.add(new JLabel(Messages.getString("T_ALTRUIST"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 9;
		gridBagConstraints.gridy = 1;
		altruistCombo = new JComboBox(noyesValues);
		altruistCombo.setSelectedIndex(altruist==false?0:1);
		altruistCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED)
					altruist = altruistCombo.getSelectedIndex()==0? false: true;
			}
		});
		generalPanel.add(altruistCombo, gridBagConstraints);
		gridBagConstraints.gridx = 10;
		gridBagConstraints.gridy = 1;
		generalPanel.add(new JLabel(Messages.getString("T_MIMICALL"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 11;
		gridBagConstraints.gridy = 1;
		String[] modifiesblackValues = {"1","2","3","4","5","6"};  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		modifiesblackCombo = new JComboBox(modifiesblackValues);
		modifiesblackCombo.setSelectedItem(Integer.toString(modifiesblack));
		modifiesblackCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					modifiesblack = Integer.parseInt((String)modifiesblackCombo.getSelectedItem());
				}
			}
		});
		generalPanel.add(modifiesblackCombo, gridBagConstraints);
		gridBagConstraints.gridx = 12;
		gridBagConstraints.gridy = 1;
		generalPanel.add(new JLabel(Messages.getString("T_BASE1X_PERCENTAGE"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 13;
		gridBagConstraints.gridy = 1;
		base1XLabel = new JTextField(Double.toString(base1X));
		base1XLabel.setText(Double.toString(base1X));
		base1XLabel.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	double d;
				try {
					d = Double.parseDouble(base1XLabel.getText());
					if ((d >= 0 && d <= 100) || (d == -1)) base1X = d;
				} catch (NumberFormatException ex) {
					// Keep old value if there is a problem
				}
            }
        });
		generalPanel.add(base1XLabel, gridBagConstraints);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		generalPanel.add(new JLabel(Messages.getString("T_ACTIVITY"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		String[] activityValues = {"0","1","2"};  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		activityCombo = new JComboBox(activityValues);
		activityCombo.setSelectedItem(Integer.toString(activity));
		activityCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					activity = Integer.parseInt((String)activityCombo.getSelectedItem());
				}
			}
		});
		generalPanel.add(activityCombo, gridBagConstraints);
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		generalPanel.add(new JLabel(Messages.getString("T_GENERATION_BATTLE"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 2;
		generationCombo = new JComboBox(noyesValues);
		generationCombo.setSelectedIndex(generationBattle==false?0:1);
		generationCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED)
					generationBattle = generationCombo.getSelectedIndex()==0? false: true;
			}
		});
		generalPanel.add(generationCombo, gridBagConstraints);
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 2;
		generalPanel.add(new JLabel(Messages.getString("T_SIBLING_BATTLE"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 2;
		siblingCombo = new JComboBox(noyesValues);
		siblingCombo.setSelectedIndex(siblingBattle==false?0:1);
		siblingCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED)
					siblingBattle = siblingCombo.getSelectedIndex()==0? false: true;
			}
		});
		generalPanel.add(siblingCombo, gridBagConstraints);
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 2;
		generalPanel.add(new JLabel(Messages.getString("T_SOCIAL"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 2;
		socialCombo = new JComboBox(noyesValues);
		socialCombo.setSelectedIndex(social==false?0:1);
		socialCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED)
					social = socialCombo.getSelectedIndex()==0? false: true;
			}
		});
		generalPanel.add(socialCombo, gridBagConstraints);
		gridBagConstraints.gridx = 8;
		gridBagConstraints.gridy = 2;
		generalPanel.add(new JLabel(Messages.getString("T_PEACEFUL"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 9;
		gridBagConstraints.gridy = 2;
		peacefulCombo = new JComboBox(noyesValues);
		peacefulCombo.setSelectedIndex(peaceful==false?0:1);
		peacefulCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED)
					peaceful = peacefulCombo.getSelectedIndex()==0? false: true;
			}
		});
		generalPanel.add(peacefulCombo, gridBagConstraints);
		gridBagConstraints.gridx = 10;
		gridBagConstraints.gridy = 2;
		generalPanel.add(new JLabel(Messages.getString("T_ADAPTBLACK"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 11;
		gridBagConstraints.gridy = 2;
		String[] adaptblackValues = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25"};  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		adaptblackCombo = new JComboBox(adaptblackValues);
		adaptblackCombo.setSelectedItem(Integer.toString(adaptblack));
		adaptblackCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					adaptblack = Integer.parseInt((String)adaptblackCombo.getSelectedItem());
				}
			}
		});
		generalPanel.add(adaptblackCombo, gridBagConstraints);
		gridBagConstraints.gridx = 12;
		gridBagConstraints.gridy = 2;
		generalPanel.add(new JLabel(Messages.getString("T_BASE1Y_PERCENTAGE"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 13;
		gridBagConstraints.gridy = 2;
		base1YLabel = new JTextField(Double.toString(base1Y));
		base1YLabel.setText(Double.toString(base1Y));
		base1YLabel.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	double d;
				try {
					d = Double.parseDouble(base1YLabel.getText());
					if ((d >= 0 && d <= 100) || (d == -1)) base1Y = d;
				} catch (NumberFormatException ex) {
					// Keep old value if there is a problem
				}
            }
        });
		generalPanel.add(base1YLabel, gridBagConstraints);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		generalPanel.add(new JLabel(Messages.getString("T_MODIFIESCREAM"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		String[] modifiescreamValues = {"1","2","3"};  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		modifiescreamCombo = new JComboBox(modifiescreamValues);
		modifiescreamCombo.setSelectedItem(Integer.toString(modifiescream));
		modifiescreamCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					modifiescream = Integer.parseInt((String)modifiescreamCombo.getSelectedItem());
				}
			}
		});
		generalPanel.add(modifiescreamCombo, gridBagConstraints);
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		generalPanel.add(new JLabel(Messages.getString("T_MODIFIESSPORE"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 3;
		String[] modifiessporeValues = {"1","2","3","4","5","6","7","8","9","10","11","12"};  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		modifiessporeCombo = new JComboBox(modifiessporeValues);
		modifiessporeCombo.setSelectedItem(Integer.toString(modifiesspore));
		modifiessporeCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					modifiesspore = Integer.parseInt((String)modifiessporeCombo.getSelectedItem());
				}
			}
		});
		generalPanel.add(modifiessporeCombo, gridBagConstraints);
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 3;
		generalPanel.add(new JLabel(Messages.getString("T_SELFISH"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 3;
		selfishCombo = new JComboBox(noyesValues);
		selfishCombo.setSelectedIndex(selfish==false?0:1);
		selfishCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED)
					selfish = selfishCombo.getSelectedIndex()==0? false: true;
			}
		});
		generalPanel.add(selfishCombo, gridBagConstraints);
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 3;
		generalPanel.add(new JLabel(Messages.getString("T_PASSIVE"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 3;
		passiveCombo = new JComboBox(noyesValues);
		passiveCombo.setSelectedIndex(passive==false?0:1);
		passiveCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED)
					passive = passiveCombo.getSelectedIndex()==0? false: true;
			}
		});
		generalPanel.add(passiveCombo, gridBagConstraints);
		gridBagConstraints.gridx = 8;
		gridBagConstraints.gridy = 3;
		generalPanel.add(new JLabel(Messages.getString("T_CLOCKWISE"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 9;
		gridBagConstraints.gridy = 3;
		clockwiseCombo = new JComboBox(noyesValues);
		clockwiseCombo.setSelectedIndex(clockwise==false?0:1);
		clockwiseCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED)
					clockwise = clockwiseCombo.getSelectedIndex()==0? false: true;
			}
		});
		generalPanel.add(clockwiseCombo, gridBagConstraints);
		gridBagConstraints.gridx = 10;
		gridBagConstraints.gridy = 3;
		generalPanel.add(new JLabel(Messages.getString("T_MODIFIESLEAF"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 11;
		gridBagConstraints.gridy = 3;
		modifiesleafCombo = new JComboBox(noyesValues);
		modifiesleafCombo.setSelectedIndex(modifiesleaf==false?0:1);
		modifiesleafCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED)
					modifiesleaf = modifiesleafCombo.getSelectedIndex()==0? false: true;
			}
		});
		generalPanel.add(modifiesleafCombo, gridBagConstraints);
		gridBagConstraints.gridx = 12;
		gridBagConstraints.gridy = 3;
		generalPanel.add(new JLabel(Messages.getString("T_BASE2X_PERCENTAGE"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 13;
		gridBagConstraints.gridy = 3;
		base2XLabel = new JTextField(Double.toString(base2X));
		base2XLabel.setText(Double.toString(base2X));
		base2XLabel.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	double d;
				try {
					d = Double.parseDouble(base2XLabel.getText());
					if ((d >= 0 && d <= 100) || (d == -1)) base2X = d;
				} catch (NumberFormatException ex) {
					// Keep old value if there is a problem
				}
            }
        });
		generalPanel.add(base2XLabel, gridBagConstraints);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		generalPanel.add(new JLabel(Messages.getString("T_MODIFIESFALLOW"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		String[] modifiesfallowValues = {"1","2","3","4"};  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		modifiesfallowCombo = new JComboBox(modifiesfallowValues);
		modifiesfallowCombo.setSelectedItem(Integer.toString(modifiesfallow));
		modifiesfallowCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					modifiesfallow = Integer.parseInt((String)modifiesfallowCombo.getSelectedItem());
				}
			}
		});
		generalPanel.add(modifiesfallowCombo, gridBagConstraints);
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 4;
		generalPanel.add(new JLabel(Messages.getString("T_ADAPTSPORE"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 4;
		String[] adaptsporeValues = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70"};  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		adaptsporeCombo = new JComboBox(adaptsporeValues);
		adaptsporeCombo.setSelectedItem(Integer.toString(adaptspore));
		adaptsporeCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					adaptspore = Integer.parseInt((String)adaptsporeCombo.getSelectedItem());
				}
			}
		});
		generalPanel.add(adaptsporeCombo, gridBagConstraints);
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 4;
		generalPanel.add(new JLabel(Messages.getString("T_MODIFIESPINK"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 4;
		modifiespinkCombo = new JComboBox(noyesValues);
		modifiespinkCombo.setSelectedIndex(modifiespink==false?0:1);
		modifiespinkCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED)
					modifiespink = modifiespinkCombo.getSelectedIndex()==0? false: true;
			}
		});
		generalPanel.add(modifiespinkCombo, gridBagConstraints);
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 4;
		generalPanel.add(new JLabel(Messages.getString("T_MODIFIESLILAC"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 4;
		modifieslilacCombo = new JComboBox(noyesValues);
		modifieslilacCombo.setSelectedIndex(modifieslilac==false?0:1);
		modifieslilacCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED)
					modifieslilac = modifieslilacCombo.getSelectedIndex()==0? false: true;
			}
		});
		generalPanel.add(modifieslilacCombo, gridBagConstraints);
		gridBagConstraints.gridx = 8;
		gridBagConstraints.gridy = 4;
		generalPanel.add(new JLabel(Messages.getString("T_MODIFIESPLAGUE"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 9;
		gridBagConstraints.gridy = 4;
		plagueCombo = new JComboBox(noyesValues);
		plagueCombo.setSelectedIndex(plague==false?0:1);
		plagueCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED)
					plague = plagueCombo.getSelectedIndex()==0? false: true;
			}
		});
		generalPanel.add(plagueCombo, gridBagConstraints);
		gridBagConstraints.gridx = 10;
		gridBagConstraints.gridy = 4;
		generalPanel.add(new JLabel(Messages.getString("T_MODIFIESSKY"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 11;
		gridBagConstraints.gridy = 4;
		modifiesskyCombo = new JComboBox(noyesValues);
		modifiesskyCombo.setSelectedIndex(modifiessky==false?0:1);
		modifiesskyCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED)
					modifiessky = modifiesskyCombo.getSelectedIndex()==0? false: true;
			}
		});
		generalPanel.add(modifiesskyCombo, gridBagConstraints);
		gridBagConstraints.gridx = 12;
		gridBagConstraints.gridy = 4;
		generalPanel.add(new JLabel(Messages.getString("T_BASE2Y_PERCENTAGE"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 13;
		gridBagConstraints.gridy = 4;
		base2YLabel = new JTextField(Double.toString(base2Y));
		base2YLabel.setText(Double.toString(base2Y));
		base2YLabel.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	double d;
				try {
					d = Double.parseDouble(base2YLabel.getText());
					if ((d >= 0 && d <= 100) || (d == -1)) base2Y = d;
				} catch (NumberFormatException ex) {
					// Keep old value if there is a problem
				}
            }
        });
		generalPanel.add(base2YLabel, gridBagConstraints);

		getContentPane().add(generalPanel,BorderLayout.NORTH);
		genesPanel = new JPanel();
		genesScroll = new JScrollPane(genesPanel);
		genesScroll.setPreferredSize(new Dimension(500,300));
		refreshGenesPanel();
		getContentPane().add(genesScroll,BorderLayout.WEST);

		drawPanel = new JPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			public void paintComponent (Graphics g) {
				super.paintComponent(g);
		        draw(g);
		    }
		};
		drawPanel.setPreferredSize(new Dimension(200,200));
		drawPanel.setBackground(Color.BLACK);
		getContentPane().add(drawPanel,BorderLayout.CENTER);

		JPanel buttonsPanel = new JPanel();
		okButton = new JButton(Messages.getString("T_COPY_TO_CLIPBOARD")); //$NON-NLS-1$

		buttonsPanel.add(okButton);
		cancelButton = new JButton(Messages.getString("T_CANCEL")); //$NON-NLS-1$
		buttonsPanel.add(cancelButton);
		JButton clearButton = new JButton(Messages.getString("T_CLEAR")); //$NON-NLS-1$
		clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	genesList.clear();
            	refreshGenesPanel();
                symmetry=2;
            	energy=40;
            	life=Utils.MAX_AGE;
            	mirror=0;
            	mutationrate= (Utils.MIN_MUTATION_RATE + Utils.MAX_MUTATION_RATE) / 2;
            	clonerate= (Utils.MIN_CLONE_RATE + Utils.MAX_CLONE_RATE) / 2;
            	homeX= -1;
            	homeY= -1;
            	base1X= -1;
            	base1Y= -1;
            	base2X= -1;
            	base2Y= -1;
            	activity=2;
            	modifiescream=2;
            	modifiesfallow=2;
            	modifiesspore=4;
            	adaptspore=4;
            	modifiesblack=1;
            	adaptblack=1;
            	plague = false;
            	disperseChildren = false;
            	generationBattle = false;
            	siblingBattle = false;
            	altruist = false;
            	familial = false;
            	social = false;
            	peaceful = false;
            	passive = false;
            	clockwise = false;
            	modifiespink = false;
            	modifieslilac = false;
            	modifiessky = false;
            	modifiesleaf = false;
            	selfish = false;
            	mutationLabel.setText(Integer.toString(mutationrate));
            	cloneLabel.setText(Integer.toString(clonerate));
            	homeXLabel.setText(Double.toString(homeX));
            	homeYLabel.setText(Double.toString(homeY));
            	base1XLabel.setText(Double.toString(base1X));
            	base1YLabel.setText(Double.toString(base1Y));
            	base2XLabel.setText(Double.toString(base2X));
            	base2YLabel.setText(Double.toString(base2Y));
				symmetryCombo.setSelectedItem(Integer.toString(symmetry));
				mirrorCombo.setSelectedIndex(mirror);
				disperseCombo.setSelectedIndex(disperseChildren==false?0:1);
				selfishCombo.setSelectedIndex(selfish==false?0:1);
				familialCombo.setSelectedIndex(familial==false?0:1);
				altruistCombo.setSelectedIndex(altruist==false?0:1);
				activityCombo.setSelectedItem(Integer.toString(activity));
				generationCombo.setSelectedIndex(generationBattle==false?0:1);
				siblingCombo.setSelectedIndex(siblingBattle==false?0:1);
				socialCombo.setSelectedIndex(social==false?0:1);
				peacefulCombo.setSelectedIndex(peaceful==false?0:1);
				modifiescreamCombo.setSelectedItem(Integer.toString(modifiescream));
				modifiessporeCombo.setSelectedItem(Integer.toString(modifiesspore));
				modifiesblackCombo.setSelectedItem(Integer.toString(modifiesblack));
				modifiespinkCombo.setSelectedIndex(modifiespink==false?0:1);
				modifieslilacCombo.setSelectedIndex(modifieslilac==false?0:1);
				plagueCombo.setSelectedIndex(plague==false?0:1);
				modifiesskyCombo.setSelectedIndex(modifiessky==false?0:1);
				modifiesleafCombo.setSelectedIndex(modifiesleaf==false?0:1);
				modifiesfallowCombo.setSelectedItem(Integer.toString(modifiesfallow));
				adaptsporeCombo.setSelectedItem(Integer.toString(adaptspore));
				adaptblackCombo.setSelectedItem(Integer.toString(adaptblack));
				passiveCombo.setSelectedIndex(passive==false?0:1);
				clockwiseCombo.setSelectedIndex(clockwise==false?0:1);
            }
            });
		buttonsPanel.add(clearButton);
		JButton importButton = new JButton(Messages.getString("T_IMPORT")); //$NON_NLS-1$
		importButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GeneticCode g;
	    		try {
	    			JFileChooser chooser = mainWindow.getGeneticCodeChooser();
				chooser=mainWindow.setUpdateUI(chooser);
	    			chooser.setFileFilter(new BioFileFilter(BioFileFilter.GENETIC_CODE_EXTENSION));
	    			int returnVal = chooser.showOpenDialog(LabWindow.this);
	    			if (returnVal == JFileChooser.APPROVE_OPTION) {
	    				try {
	    					// Read XML code from file
	    					BioXMLParser parser = new BioXMLParser();
							g = parser.parseGeneticCode(chooser.getSelectedFile());
							genesList.clear();
							importGeneticCode(g);
							refreshGenesPanel();
							mutationLabel.setText(Integer.toString(mutationrate));
							cloneLabel.setText(Integer.toString(clonerate));
							homeXLabel.setText(Double.toString(homeX));
			            	homeYLabel.setText(Double.toString(homeY));
			            	base1XLabel.setText(Double.toString(base1X));
			            	base1YLabel.setText(Double.toString(base1Y));
			            	base2XLabel.setText(Double.toString(base2X));
			            	base2YLabel.setText(Double.toString(base2Y));
							symmetryCombo.setSelectedItem(Integer.toString(symmetry));
							mirrorCombo.setSelectedIndex(mirror);
							disperseCombo.setSelectedIndex(disperseChildren==false?0:1);
							selfishCombo.setSelectedIndex(selfish==false?0:1);
							familialCombo.setSelectedIndex(familial==false?0:1);
							altruistCombo.setSelectedIndex(altruist==false?0:1);
							activityCombo.setSelectedItem(Integer.toString(activity));
							generationCombo.setSelectedIndex(generationBattle==false?0:1);
							siblingCombo.setSelectedIndex(siblingBattle==false?0:1);
							socialCombo.setSelectedIndex(social==false?0:1);
							peacefulCombo.setSelectedIndex(peaceful==false?0:1);
							modifiescreamCombo.setSelectedItem(Integer.toString(modifiescream));
							modifiessporeCombo.setSelectedItem(Integer.toString(modifiesspore));
							modifiesblackCombo.setSelectedItem(Integer.toString(modifiesblack));
							modifiespinkCombo.setSelectedIndex(modifiespink==false?0:1);
							modifieslilacCombo.setSelectedIndex(modifieslilac==false?0:1);
							plagueCombo.setSelectedIndex(plague==false?0:1);
							modifiesskyCombo.setSelectedIndex(modifiessky==false?0:1);
							modifiesleafCombo.setSelectedIndex(modifiesleaf==false?0:1);
							modifiesfallowCombo.setSelectedItem(Integer.toString(modifiesfallow));
							adaptsporeCombo.setSelectedItem(Integer.toString(adaptspore));
							adaptblackCombo.setSelectedItem(Integer.toString(adaptblack));
							passiveCombo.setSelectedIndex(passive==false?0:1);
							clockwiseCombo.setSelectedIndex(clockwise==false?0:1);
	    				} catch (SAXException ex) {
	    					ex.printStackTrace();
	    					JOptionPane.showMessageDialog(null,Messages.getString("T_WRONG_FILE_VERSION"),Messages.getString("T_READ_ERROR"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
	    				} catch (IOException ex) {
	    					ex.printStackTrace();
	    					JOptionPane.showMessageDialog(null,Messages.getString("T_CANT_READ_FILE"),Messages.getString("T_READ_ERROR"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
						}
	    			}
	    		} catch (SecurityException ex) {
	    			ex.printStackTrace();
	    			JOptionPane.showMessageDialog(null,Messages.getString("T_PERMISSION_DENIED"),Messages.getString("T_PERMISSION_DENIED"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
	    		}
			}
		});
		buttonsPanel.add(importButton);
		JButton exportButton = new JButton(Messages.getString("T_EXPORT")); //$NON_NLS-1$
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (genesList.size() > 0) {
					GeneticCode exportcode = new GeneticCode(genesList, symmetry, mirror, mutationrate, clonerate, homeX, homeY, base1X, base1Y, base2X, base2Y, activity,
							modifiescream, modifiesfallow, modifiesspore, adaptspore, modifiesblack, adaptblack, plague, disperseChildren, generationBattle, siblingBattle,
							altruist, familial, social, peaceful, passive, clockwise, modifiespink, modifieslilac, modifiessky, modifiesleaf, selfish);
					mainWindow.saveObjectAs(LabWindow.this, exportcode);
				}
			}
		});
		buttonsPanel.add(exportButton);
		getContentPane().add(buttonsPanel,BorderLayout.SOUTH);

		getRootPane().setDefaultButton(okButton);
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().startsWith("c")) {
			int modifiedGene = Integer.parseInt(evt.getActionCommand().substring(1));
			ColorComboBox colorCombo = (ColorComboBox) evt.getSource();
			genesList.get(modifiedGene).setColor(colorCombo.getSelectedColor());
			drawPanel.repaint();
		}
		// Add a new gene after the last one
		if (evt.getActionCommand().equals("add")) { //$NON-NLS-1$
			Gene gene = new Gene(2.0,0.0,Color.GREEN,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
			genesList.add(gene);
			refreshGenesPanel();
		}
		// Deletes an existing gene
		if (evt.getActionCommand().startsWith("d")) { //$NON-NLS-1$
			int deletedGene = Integer.parseInt(evt.getActionCommand().substring(1));
			genesList.remove(deletedGene);
			refreshGenesPanel();
		}
		// Insert a new gene before the selected position
		if (evt.getActionCommand().startsWith("i")) { //$NON-NLS-1$
			Gene gene = new Gene(2.0,0.0,Color.GREEN,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
			int insertPosition = Integer.parseInt(evt.getActionCommand().substring(1));
			genesList.add(insertPosition, gene);
			refreshGenesPanel();
		}
		// Clone a gene and add it before the selected position
		if (evt.getActionCommand().startsWith("r")) { //$NON-NLS-1$
			Gene gene = new Gene(2.0,0.0,Color.GREEN,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
			int clonedGene = Integer.parseInt(evt.getActionCommand().substring(1));
			gene = genesList.get(clonedGene);
			genesList.add(clonedGene, (Gene)gene.clone());
			refreshGenesPanel();
		}
	}

	protected void refreshGenesPanel() {
		genesPanel.removeAll();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		genesPanel.setLayout(gridbag);
		Iterator<Gene> it;
		int i;
		Gene gene;
		constraints.gridx = 1;
		constraints.gridy = 0;
		genesPanel.add(new JLabel(Messages.getString("T_GENE")), constraints); //$NON-NLS-1$
		constraints.gridx = 2;
		genesPanel.add(new JLabel(Messages.getString("T_LENGTH")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 3;
		genesPanel.add(new JLabel(Messages.getString("T_ROTATION")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 4;
		genesPanel.add(new JLabel(Messages.getString("T_COLOR2")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 5;
		genesPanel.add(new JLabel(Messages.getString("T_BRANCH")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 9;
		genesPanel.add(new JLabel(Messages.getString("T_GREEN")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 10;
		genesPanel.add(new JLabel(Messages.getString("T_BARK")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 11;
		genesPanel.add(new JLabel(Messages.getString("T_RED")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 12;
		genesPanel.add(new JLabel(Messages.getString("T_FIRE")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 13;
		genesPanel.add(new JLabel(Messages.getString("T_ORANGE")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 14;
		genesPanel.add(new JLabel(Messages.getString("T_MAROON")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 15;
		genesPanel.add(new JLabel(Messages.getString("T_CRIMSON")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 16;
		genesPanel.add(new JLabel(Messages.getString("T_PINK")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 17;
		genesPanel.add(new JLabel(Messages.getString("T_CREAM")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 18;
		genesPanel.add(new JLabel(Messages.getString("T_SILVER")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 19;
		genesPanel.add(new JLabel(Messages.getString("T_SPIKE")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 20;
		genesPanel.add(new JLabel(Messages.getString("T_LILAC")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 21;
		genesPanel.add(new JLabel(Messages.getString("T_GRAY")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 22;
		genesPanel.add(new JLabel(Messages.getString("T_VIOLET")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 23;
		genesPanel.add(new JLabel(Messages.getString("T_OLIVE")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 24;
		genesPanel.add(new JLabel(Messages.getString("T_SKY")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 25;
		genesPanel.add(new JLabel(Messages.getString("T_BLUE")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 26;
		genesPanel.add(new JLabel(Messages.getString("T_OCHRE")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 27;
		genesPanel.add(new JLabel(Messages.getString("T_FALLOW")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 28;
		genesPanel.add(new JLabel(Messages.getString("T_WHITE")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 29;
		genesPanel.add(new JLabel(Messages.getString("T_VIRUS")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 30;
		genesPanel.add(new JLabel(Messages.getString("T_PLAGUE")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 31;
		genesPanel.add(new JLabel(Messages.getString("T_SCOURGE")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 32;
		genesPanel.add(new JLabel(Messages.getString("T_CORAL")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 33;
		genesPanel.add(new JLabel(Messages.getString("T_MINT")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 34;
		genesPanel.add(new JLabel(Messages.getString("T_MAGENTA")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 35;
		genesPanel.add(new JLabel(Messages.getString("T_DEFAULT")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 36;
		genesPanel.add(new JLabel(Messages.getString("T_CONSUMER")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 37;
		genesPanel.add(new JLabel(Messages.getString("T_PLANT")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 38;
		genesPanel.add(new JLabel(Messages.getString("T_ICE")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 39;
		genesPanel.add(new JLabel(Messages.getString("T_LBL")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 40;
		genesPanel.add(new JLabel(Messages.getString("T_LBR")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 41;
		genesPanel.add(new JLabel(Messages.getString("T_GBR")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 42;
		genesPanel.add(new JLabel(Messages.getString("T_BROKEN")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 43;
		genesPanel.add(new JLabel(Messages.getString("T_BRO")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 44;
		genesPanel.add(new JLabel(Messages.getString("T_SICK")+" "), constraints); //$NON-NLS-1$
		constraints.gridx = 45;
		genesPanel.add(new JLabel(Messages.getString("T_FRIEND")+" "), constraints); //$NON-NLS-1$
		for (it = genesList.iterator(), i=0; it.hasNext(); i++) {
			gene = it.next();
			constraints.gridx = 1;
			constraints.gridy = i+1;
			JLabel label = new JLabel(i+": "); //$NON-NLS-1$
			gridbag.setConstraints(label,constraints);
			genesPanel.add(label);

			constraints.gridx = 2;
			LengthSpinner lengthSpinner = new LengthSpinner(gene);
			lengthSpinner.addChangeListener(this);
			genesPanel.add(lengthSpinner, constraints);

			constraints.gridx = 3;
			ThetaSpinner thetaSpinner = new ThetaSpinner(gene);
			thetaSpinner.addChangeListener(this);
			genesPanel.add(thetaSpinner, constraints);

			constraints.gridx = 4;
			ColorComboBox colorCombo = new ColorComboBox(gene.getColor());
			colorCombo.addActionListener(this);
			colorCombo.setActionCommand("c"+i);
			genesPanel.add(colorCombo, constraints);

			constraints.gridx = 5;
			BranchSpinner branchSpinner = new BranchSpinner(gene);
			branchSpinner.addChangeListener(this);
			genesPanel.add(branchSpinner, constraints);

			constraints.gridx = 6;
			JButton insertButton = new JButton(Messages.getString("T_INSERT")); //$NON-NLS-1$
			insertButton.setActionCommand("i"+i); //$NON-NLS-1$
			gridbag.setConstraints(insertButton,constraints);
			genesPanel.add(insertButton);
			insertButton.addActionListener(this);

			constraints.gridx = 7;
			JButton cloneButton = new JButton(Messages.getString("T_CLONE")); //$NON-NLS-1$
			cloneButton.setActionCommand("r"+i); //$NON-NLS-1$
			gridbag.setConstraints(cloneButton,constraints);
			genesPanel.add(cloneButton);
			cloneButton.addActionListener(this);

			constraints.gridx = 8;
			JButton deleteButton = new JButton(Messages.getString("T_DELETE")); //$NON-NLS-1$
			deleteButton.setActionCommand("d"+i); //$NON-NLS-1$
			gridbag.setConstraints(deleteButton,constraints);
			genesPanel.add(deleteButton);
			deleteButton.addActionListener(this);

			constraints.gridx = 9;
			ReactionSpinner1 reactionSpinner1 = new ReactionSpinner1(gene);
			reactionSpinner1.addChangeListener(this);
			genesPanel.add(reactionSpinner1, constraints);

			constraints.gridx = 10;
			ReactionSpinner2 reactionSpinner2 = new ReactionSpinner2(gene);
			reactionSpinner2.addChangeListener(this);
			genesPanel.add(reactionSpinner2, constraints);

			constraints.gridx = 11;
			ReactionSpinner3 reactionSpinner3 = new ReactionSpinner3(gene);
			reactionSpinner3.addChangeListener(this);
			genesPanel.add(reactionSpinner3, constraints);

			constraints.gridx = 12;
			ReactionSpinner4 reactionSpinner4 = new ReactionSpinner4(gene);
			reactionSpinner4.addChangeListener(this);
			genesPanel.add(reactionSpinner4, constraints);

			constraints.gridx = 13;
			ReactionSpinner5 reactionSpinner5 = new ReactionSpinner5(gene);
			reactionSpinner5.addChangeListener(this);
			genesPanel.add(reactionSpinner5, constraints);

			constraints.gridx = 14;
			ReactionSpinner6 reactionSpinner6 = new ReactionSpinner6(gene);
			reactionSpinner6.addChangeListener(this);
			genesPanel.add(reactionSpinner6, constraints);

			constraints.gridx = 15;
			ReactionSpinner7 reactionSpinner7 = new ReactionSpinner7(gene);
			reactionSpinner7.addChangeListener(this);
			genesPanel.add(reactionSpinner7, constraints);

			constraints.gridx = 16;
			ReactionSpinner8 reactionSpinner8 = new ReactionSpinner8(gene);
			reactionSpinner8.addChangeListener(this);
			genesPanel.add(reactionSpinner8, constraints);

			constraints.gridx = 17;
			ReactionSpinner9 reactionSpinner9 = new ReactionSpinner9(gene);
			reactionSpinner9.addChangeListener(this);
			genesPanel.add(reactionSpinner9, constraints);

			constraints.gridx = 18;
			ReactionSpinner10 reactionSpinner10 = new ReactionSpinner10(gene);
			reactionSpinner10.addChangeListener(this);
			genesPanel.add(reactionSpinner10, constraints);

			constraints.gridx = 19;
			ReactionSpinner11 reactionSpinner11 = new ReactionSpinner11(gene);
			reactionSpinner11.addChangeListener(this);
			genesPanel.add(reactionSpinner11, constraints);

			constraints.gridx = 20;
			ReactionSpinner12 reactionSpinner12 = new ReactionSpinner12(gene);
			reactionSpinner12.addChangeListener(this);
			genesPanel.add(reactionSpinner12, constraints);

			constraints.gridx = 21;
			ReactionSpinner13 reactionSpinner13 = new ReactionSpinner13(gene);
			reactionSpinner13.addChangeListener(this);
			genesPanel.add(reactionSpinner13, constraints);

			constraints.gridx = 22;
			ReactionSpinner14 reactionSpinner14 = new ReactionSpinner14(gene);
			reactionSpinner14.addChangeListener(this);
			genesPanel.add(reactionSpinner14, constraints);

			constraints.gridx = 23;
			ReactionSpinner15 reactionSpinner15 = new ReactionSpinner15(gene);
			reactionSpinner15.addChangeListener(this);
			genesPanel.add(reactionSpinner15, constraints);

			constraints.gridx = 24;
			ReactionSpinner16 reactionSpinner16 = new ReactionSpinner16(gene);
			reactionSpinner16.addChangeListener(this);
			genesPanel.add(reactionSpinner16, constraints);

			constraints.gridx = 25;
			ReactionSpinner17 reactionSpinner17 = new ReactionSpinner17(gene);
			reactionSpinner17.addChangeListener(this);
			genesPanel.add(reactionSpinner17, constraints);

			constraints.gridx = 26;
			ReactionSpinner18 reactionSpinner18 = new ReactionSpinner18(gene);
			reactionSpinner18.addChangeListener(this);
			genesPanel.add(reactionSpinner18, constraints);

			constraints.gridx = 27;
			ReactionSpinner19 reactionSpinner19 = new ReactionSpinner19(gene);
			reactionSpinner19.addChangeListener(this);
			genesPanel.add(reactionSpinner19, constraints);

			constraints.gridx = 28;
			ReactionSpinner20 reactionSpinner20 = new ReactionSpinner20(gene);
			reactionSpinner20.addChangeListener(this);
			genesPanel.add(reactionSpinner20, constraints);

			constraints.gridx = 29;
			ReactionSpinner21 reactionSpinner21 = new ReactionSpinner21(gene);
			reactionSpinner21.addChangeListener(this);
			genesPanel.add(reactionSpinner21, constraints);

			constraints.gridx = 30;
			ReactionSpinner22 reactionSpinner22 = new ReactionSpinner22(gene);
			reactionSpinner22.addChangeListener(this);
			genesPanel.add(reactionSpinner22, constraints);

			constraints.gridx = 31;
			ReactionSpinner23 reactionSpinner23 = new ReactionSpinner23(gene);
			reactionSpinner23.addChangeListener(this);
			genesPanel.add(reactionSpinner23, constraints);

			constraints.gridx = 32;
			ReactionSpinner24 reactionSpinner24 = new ReactionSpinner24(gene);
			reactionSpinner24.addChangeListener(this);
			genesPanel.add(reactionSpinner24, constraints);

			constraints.gridx = 33;
			ReactionSpinner25 reactionSpinner25 = new ReactionSpinner25(gene);
			reactionSpinner25.addChangeListener(this);
			genesPanel.add(reactionSpinner25, constraints);

			constraints.gridx = 34;
			ReactionSpinner26 reactionSpinner26 = new ReactionSpinner26(gene);
			reactionSpinner26.addChangeListener(this);
			genesPanel.add(reactionSpinner26, constraints);

			constraints.gridx = 35;
			ReactionSpinner27 reactionSpinner27 = new ReactionSpinner27(gene);
			reactionSpinner27.addChangeListener(this);
			genesPanel.add(reactionSpinner27, constraints);

			constraints.gridx = 36;
			ReactionSpinner28 reactionSpinner28 = new ReactionSpinner28(gene);
			reactionSpinner28.addChangeListener(this);
			genesPanel.add(reactionSpinner28, constraints);

			constraints.gridx = 37;
			ReactionSpinner29 reactionSpinner29 = new ReactionSpinner29(gene);
			reactionSpinner29.addChangeListener(this);
			genesPanel.add(reactionSpinner29, constraints);

			constraints.gridx = 38;
			ReactionSpinner30 reactionSpinner30 = new ReactionSpinner30(gene);
			reactionSpinner30.addChangeListener(this);
			genesPanel.add(reactionSpinner30, constraints);

			constraints.gridx = 39;
			ReactionSpinner31 reactionSpinner31 = new ReactionSpinner31(gene);
			reactionSpinner31.addChangeListener(this);
			genesPanel.add(reactionSpinner31, constraints);

			constraints.gridx = 40;
			ReactionSpinner32 reactionSpinner32 = new ReactionSpinner32(gene);
			reactionSpinner32.addChangeListener(this);
			genesPanel.add(reactionSpinner32, constraints);

			constraints.gridx = 41;
			ReactionSpinner33 reactionSpinner33 = new ReactionSpinner33(gene);
			reactionSpinner33.addChangeListener(this);
			genesPanel.add(reactionSpinner33, constraints);

			constraints.gridx = 42;
			ReactionSpinner34 reactionSpinner34 = new ReactionSpinner34(gene);
			reactionSpinner34.addChangeListener(this);
			genesPanel.add(reactionSpinner34, constraints);

			constraints.gridx = 43;
			ReactionSpinner35 reactionSpinner35 = new ReactionSpinner35(gene);
			reactionSpinner35.addChangeListener(this);
			genesPanel.add(reactionSpinner35, constraints);

			constraints.gridx = 44;
			ReactionSpinner36 reactionSpinner36 = new ReactionSpinner36(gene);
			reactionSpinner36.addChangeListener(this);
			genesPanel.add(reactionSpinner36, constraints);
			
			constraints.gridx = 45;
			ReactionSpinner37 reactionSpinner37 = new ReactionSpinner37(gene);
			reactionSpinner37.addChangeListener(this);
			genesPanel.add(reactionSpinner37, constraints);
		}
		constraints.gridx = 1;
		constraints.gridwidth = 2;
		constraints.gridy = i+1;
		JButton addButton = new JButton(Messages.getString("T_ADD")); //$NON-NLS-1$
		addButton.setActionCommand("add"); //$NON-NLS-1$
		gridbag.setConstraints(addButton,constraints);
		genesPanel.add(addButton);
		addButton.addActionListener(this);

		energy = 40 + 3 * symmetry * genesList.size();
		energyLabel.setText(Integer.toString(energy));
		life = Utils.MAX_AGE + (int) ((genesList.size() * symmetry)/Utils.AGE_DIVISOR);
		lifeLabel.setText(Integer.toString(life));
		segmentsLabel.setText(Integer.toString(genesList.size() * symmetry));

		validate();
		repaint();
	}

	protected void draw(Graphics g) {
		GeneticCode code = new GeneticCode(genesList, symmetry, mirror, mutationrate, clonerate, homeX, homeY, base1X, base1Y, base2X, base2Y, activity, modifiescream, modifiesfallow, modifiesspore, adaptspore, modifiesblack, adaptblack, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
		code.draw(g, drawPanel.getSize().width, drawPanel.getSize().height);
	}

	@Override
	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource() instanceof LengthSpinner) {
			LengthSpinner spinner = (LengthSpinner) evt.getSource();
			spinner.getGene().setLength(spinner.getLength());
		}
		if (evt.getSource() instanceof ThetaSpinner) {
			ThetaSpinner spinner = (ThetaSpinner) evt.getSource();
			spinner.getGene().setTheta(spinner.getTheta());
		}
		if (evt.getSource() instanceof BranchSpinner) {
			BranchSpinner spinner = (BranchSpinner) evt.getSource();
			spinner.getGene().setBranch(spinner.getBranch());
		}
		if (evt.getSource() instanceof ReactionSpinner1) {
			ReactionSpinner1 spinner = (ReactionSpinner1) evt.getSource();
			spinner.getGene().setgreenReaction(spinner.getgreenReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner2) {
			ReactionSpinner2 spinner = (ReactionSpinner2) evt.getSource();
			spinner.getGene().setbarkReaction(spinner.getbarkReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner3) {
			ReactionSpinner3 spinner = (ReactionSpinner3) evt.getSource();
			spinner.getGene().setredReaction(spinner.getredReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner4) {
			ReactionSpinner4 spinner = (ReactionSpinner4) evt.getSource();
			spinner.getGene().setfireReaction(spinner.getfireReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner5) {
			ReactionSpinner5 spinner = (ReactionSpinner5) evt.getSource();
			spinner.getGene().setorangeReaction(spinner.getorangeReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner6) {
			ReactionSpinner6 spinner = (ReactionSpinner6) evt.getSource();
			spinner.getGene().setmaroonReaction(spinner.getmaroonReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner7) {
			ReactionSpinner7 spinner = (ReactionSpinner7) evt.getSource();
			spinner.getGene().setcrimsonReaction(spinner.getcrimsonReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner8) {
			ReactionSpinner8 spinner = (ReactionSpinner8) evt.getSource();
			spinner.getGene().setpinkReaction(spinner.getpinkReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner9) {
			ReactionSpinner9 spinner = (ReactionSpinner9) evt.getSource();
			spinner.getGene().setcreamReaction(spinner.getcreamReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner10) {
			ReactionSpinner10 spinner = (ReactionSpinner10) evt.getSource();
			spinner.getGene().setsilverReaction(spinner.getsilverReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner11) {
			ReactionSpinner11 spinner = (ReactionSpinner11) evt.getSource();
			spinner.getGene().setspikeReaction(spinner.getspikeReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner12) {
			ReactionSpinner12 spinner = (ReactionSpinner12) evt.getSource();
			spinner.getGene().setlilacReaction(spinner.getlilacReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner13) {
			ReactionSpinner13 spinner = (ReactionSpinner13) evt.getSource();
			spinner.getGene().setgrayReaction(spinner.getgrayReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner14) {
			ReactionSpinner14 spinner = (ReactionSpinner14) evt.getSource();
			spinner.getGene().setvioletReaction(spinner.getvioletReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner15) {
			ReactionSpinner15 spinner = (ReactionSpinner15) evt.getSource();
			spinner.getGene().setoliveReaction(spinner.getoliveReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner16) {
			ReactionSpinner16 spinner = (ReactionSpinner16) evt.getSource();
			spinner.getGene().setskyReaction(spinner.getskyReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner17) {
			ReactionSpinner17 spinner = (ReactionSpinner17) evt.getSource();
			spinner.getGene().setblueReaction(spinner.getblueReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner18) {
			ReactionSpinner18 spinner = (ReactionSpinner18) evt.getSource();
			spinner.getGene().setochreReaction(spinner.getochreReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner19) {
			ReactionSpinner19 spinner = (ReactionSpinner19) evt.getSource();
			spinner.getGene().setfallowReaction(spinner.getfallowReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner20) {
			ReactionSpinner20 spinner = (ReactionSpinner20) evt.getSource();
			spinner.getGene().setwhiteReaction(spinner.getwhiteReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner21) {
			ReactionSpinner21 spinner = (ReactionSpinner21) evt.getSource();
			spinner.getGene().setvirusReaction(spinner.getvirusReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner22) {
			ReactionSpinner22 spinner = (ReactionSpinner22) evt.getSource();
			spinner.getGene().setplagueReaction(spinner.getplagueReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner23) {
			ReactionSpinner23 spinner = (ReactionSpinner23) evt.getSource();
			spinner.getGene().setscourgeReaction(spinner.getscourgeReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner24) {
			ReactionSpinner24 spinner = (ReactionSpinner24) evt.getSource();
			spinner.getGene().setcoralReaction(spinner.getcoralReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner25) {
			ReactionSpinner25 spinner = (ReactionSpinner25) evt.getSource();
			spinner.getGene().setmintReaction(spinner.getmintReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner26) {
			ReactionSpinner26 spinner = (ReactionSpinner26) evt.getSource();
			spinner.getGene().setmagentaReaction(spinner.getmagentaReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner27) {
			ReactionSpinner27 spinner = (ReactionSpinner27) evt.getSource();
			spinner.getGene().setdefaultReaction(spinner.getdefaultReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner28) {
			ReactionSpinner28 spinner = (ReactionSpinner28) evt.getSource();
			spinner.getGene().setconsumerReaction(spinner.getconsumerReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner29) {
			ReactionSpinner29 spinner = (ReactionSpinner29) evt.getSource();
			spinner.getGene().setplantReaction(spinner.getplantReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner30) {
			ReactionSpinner30 spinner = (ReactionSpinner30) evt.getSource();
			spinner.getGene().seticeReaction(spinner.geticeReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner31) {
			ReactionSpinner31 spinner = (ReactionSpinner31) evt.getSource();
			spinner.getGene().setlightblueReaction(spinner.getlightblueReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner32) {
			ReactionSpinner32 spinner = (ReactionSpinner32) evt.getSource();
			spinner.getGene().setlightbrownReaction(spinner.getlightbrownReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner33) {
			ReactionSpinner33 spinner = (ReactionSpinner33) evt.getSource();
			spinner.getGene().setgreenbrownReaction(spinner.getgreenbrownReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner34) {
			ReactionSpinner34 spinner = (ReactionSpinner34) evt.getSource();
			spinner.getGene().setbrokenReaction(spinner.getbrokenReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner35) {
			ReactionSpinner35 spinner = (ReactionSpinner35) evt.getSource();
			spinner.getGene().setbrownReaction(spinner.getbrownReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner36) {
			ReactionSpinner36 spinner = (ReactionSpinner36) evt.getSource();
			spinner.getGene().setsickReaction(spinner.getsickReaction());
		}
		if (evt.getSource() instanceof ReactionSpinner37) {
			ReactionSpinner37 spinner = (ReactionSpinner37) evt.getSource();
			spinner.getGene().setfriendReaction(spinner.getfriendReaction());
		}
		drawPanel.repaint();
	}
}

class LengthSpinner extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public LengthSpinner(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getLength(), 2.0, 18.0, 0.1));
		setEditor(new JSpinner.NumberEditor(this, "#0.0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public double getLength() {
		return ((SpinnerNumberModel)getModel()).getNumber().doubleValue();
	}
}

class ThetaSpinnerModel extends SpinnerNumberModel {
	private static final long serialVersionUID = Utils.VERSION;
	int firstValue, lastValue;

	public ThetaSpinnerModel(int value, int minimum, int maximum, int stepSize) {
		super(value, minimum, maximum, stepSize);
		firstValue = minimum;
		lastValue = maximum;
	}

	@Override
	public Object getNextValue() {
		Object value = super.getNextValue();
		if (value == null)
			value = firstValue;
		return value;
	}

	@Override
	public Object getPreviousValue() {
		Object value = super.getPreviousValue();
		if (value == null)
			value = lastValue;
		return value;
	}
}

class ThetaSpinner extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ThetaSpinner(Gene gene) {
		super();
		_gene = gene;
		int initialValue = (int)Math.round(_gene.getTheta()*180.0/Math.PI);
		while (initialValue < 0) initialValue+=360;
		while (initialValue > 359) initialValue-=360;
		setModel(new ThetaSpinnerModel(initialValue, 0, 359, 1));
		setEditor(new JSpinner.NumberEditor(this, "##0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public double getTheta() {
		return ((SpinnerNumberModel)getModel()).getNumber().doubleValue()*Math.PI/180.0;
	}
}

class BranchSpinner extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public BranchSpinner(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getBranch(), -1, 999, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getBranch() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner1 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner1(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getgreenReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getgreenReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner2 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner2(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getbarkReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getbarkReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner3 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner3(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getredReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getredReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner4 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner4(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getfireReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getfireReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner5 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner5(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getorangeReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getorangeReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner6 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner6(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getmaroonReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getmaroonReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner7 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner7(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getcrimsonReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getcrimsonReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner8 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner8(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getpinkReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getpinkReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner9 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner9(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getcreamReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getcreamReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner10 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner10(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getsilverReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getsilverReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner11 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner11(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getspikeReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getspikeReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner12 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner12(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getlilacReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getlilacReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner13 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner13(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getgrayReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getgrayReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner14 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner14(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getvioletReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getvioletReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner15 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner15(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getoliveReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getoliveReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner16 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner16(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getskyReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getskyReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner17 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner17(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getblueReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getblueReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner18 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner18(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getochreReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getochreReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner19 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner19(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getfallowReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getfallowReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner20 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner20(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getwhiteReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getwhiteReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner21 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner21(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getvirusReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getvirusReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner22 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner22(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getplagueReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getplagueReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner23 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner23(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getscourgeReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getscourgeReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner24 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner24(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getcoralReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getcoralReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner25 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner25(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getmintReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getmintReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner26 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner26(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getmagentaReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getmagentaReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner27 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner27(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getdefaultReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getdefaultReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner28 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner28(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getconsumerReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getconsumerReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner29 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner29(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getplantReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getplantReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner30 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner30(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.geticeReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int geticeReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner31 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner31(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getlightblueReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getlightblueReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner32 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner32(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getlightbrownReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getlightbrownReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner33 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner33(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getgreenbrownReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getgreenbrownReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner34 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner34(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getbrokenReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getbrokenReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner35 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner35(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getbrownReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getbrownReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner36 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner36(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getsickReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getsickReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}

class ReactionSpinner37 extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ReactionSpinner37(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getfriendReaction(), 0, 5, 1));
		setEditor(new JSpinner.NumberEditor(this, "#0"));
	}

	public Gene getGene() {
		return _gene;
	}

	public void setGene(Gene gene) {
		_gene = gene;
	}

	public int getfriendReaction() {
		return ((SpinnerNumberModel)getModel()).getNumber().intValue();
	}
}


