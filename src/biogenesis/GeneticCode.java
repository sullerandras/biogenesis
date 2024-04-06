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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * This class implements a full organism's genetic code. A genetic code is
 * composed by a number of genes, a symmetry, optional mirroring, optional children
 * dispersing, maximum life age, and energy needed to reproduce it.
 * 
 * Genes are represented with segments that form the organism's body and are
 * drawn one behind the other. This basic set is repeated symmetry times in one
 * of two possible ways:
 * 
 * If mirroring is not applied, each repetition is rotated so they are distributed
 * in a uniform way around the central (starting) point.
 * 
 * When mirrored, segments are calculated as follows:
 * First, third, fifth and septh repetitions are applied a rotation of
 * 0, 180, 90, 270 degrees respectively.
 * 
 * Second repetition is like the first one with opposite x coordinate.
 * Fourth repetition is like the third one with opposite x coordinate.
 * Sixth repetition is like the fifth one with opposite y coordinate.
 * Eigth repetition is like the septh one with opposite y coordinate.
 */
public class GeneticCode implements Cloneable, Serializable {
	/**
	 * The version of this class
	 */
	private static final long serialVersionUID = Utils.FILE_VERSION;
	/**
	 * Array with the genes. Every gene is represented by symmetry
	 * segments when drawing the organism.
	 */
	@Expose
	protected Gene[] _genes;
	/**
	 * The symmetry used when drawing the organism. Possible values are
	 * 1 - 8. 
	 */
	@Expose
	protected int _symmetry;
	/**
	 * Mirroring indicates if symmetric segments are drawn in the same
	 * way than the original, only changing their angle, or if they are
	 * drawn like in a mirror. 0 = not mirrored, 1 = mirrored
	 */
	@Expose
	protected int _mirror;
	/**
	 * The mutationrate of this organism. Possible values are
	 * min mutationrate - max mutationrate. 
	 */
	protected int _mutationrate;
	/**
	 * The clonerate of this organism. Possible values are
	 * min clonerate - max clonerate. 
	 */
	protected int _clonerate;
	/**
	 * The home X % coordinate of this organism. Possible values are
	 * -1 (birth coordinates will be used) or 0 - 100 
	 */
	protected double _homeX;
	/**
	 * The home Y % coordinate of this organism. Possible values are
	 * -1 (birth coordinates will be used) or 0 - 100 
	 */
	protected double _homeY;
	/**
	 * The seasons of the summer segments. Possible values are
	 * 0 - 2. 
	 */
	protected int _activity;
	/**
	 * If the number of genes, the symmetry or the mirroring changed, the clade has to be updated.
	 */
	protected int _updateClade;
	/**
	 * Version of the plague. Possible values are
	 * true and false.
	 */
	protected boolean _plague;
	/**
	 * Indicates if children will be placed moving towards the same
	 * direction than the father or to a different direction to move
	 * away.
	 */
	protected boolean _disperseChildren;
	/**
	 * Indicates if parents attack children and children attack parents.
	 */
	protected boolean _generationBattle;
	/**
	 * Indicates if siblings battle each other.
	 */
	protected boolean _siblingBattle;
	/**
	 * Indicates if organism will heal other altruist organisms or remove their infections.
	 * Altruists will not attack these segments.
	 */
	protected boolean _altruist;
	/**
	 * Indicates if organism will heal related organisms or remove their infections.
	 */
	protected boolean _familial;
	/**
	 * Indicates if organisms with rose segments are social towards other organisms with rose segments,
	 * if both rose segments have the same length or theta value.
	 */
	protected boolean _social;
	/**
	 * Indicates if organisms with rose segments are peaceful towards other organisms with rose segments.
	 */
	protected boolean _peaceful;
	/**
	 * Indicates if organisms with teal segments will move around the world.
	 * Passive organisms only move, when they are touched.
	 */
	protected boolean _passive;
	/**
	 * Indicates if passive organisms with cyan segments will rotate clockwise or counter-clockwise
	 */
	protected boolean _clockwise;
	/**
	 * Modifies the function of pink
	 */
	protected boolean _modifiespink;
	/**
	 * Modifies the function of enhanced cream 
	 */
	protected int _modifiescream;
	/**
	 * Modifies the function of spore
	 */
	protected int _modifiesspore;
	/**
	 * Modifies the function of spore further
	 */
	protected int _adaptspore;
	/**
	 * Modifies the function of dark/black
	 */
	protected int _modifiesblack;
	/**
	 * Modifies the function of dark/black further
	 */
	protected int _adaptblack;
	/**
	 * Modifies the function of lilac
	 */
	protected boolean _modifieslilac;
	/**
	 * Modifies the function of fallow
	 */
	protected int _modifiesfallow;
	/**
	 * Modifies the function of sky
	 */
	protected boolean _modifiessky;
	/**
	 * Modifies the function of leaf
	 */
	protected boolean _modifiesleaf;
	/**
	 * Indicates if organisms will always retain half of their energy if they reproduce.
	 */
	protected boolean _selfish;
	/**
	 * Generation number
	 */
	protected int _generation;
	/**
	 * Identification number of this organisms clade.
	 */
	@Expose
	protected String _cladeID;
	// Getters
	/**
	 * Returns the symmetry applied to organisms with this genetic code
	 * 
	 * @return  a value of 1 - 8.
	 */
	public int getSymmetry() {
		return _symmetry;
	}
	/**
	 * Returns if mirroring is applied to organisms with this genetic code
	 * 
	 * @return  0 if no mirroring is applied, 1 if mirroring is applied.
	 */
	public int getMirror() {
		return _mirror;
	}
	/**
	 * Returns the mutationrate applied to organisms with this genetic code
	 * 
	 * @return  a value of min mutationrate - max mutationrate.
	 */
	public int getMutationrate() {
		return _mutationrate;
	}
	/**
	 * Returns the clonerate applied to organisms with this genetic code
	 * 
	 * @return  a value of min clonerate - max clonerate.
	 */
	public int getClonerate() {
		return _clonerate;
	}
	/**
	 * Returns the home X % coordinate applied to organisms with this genetic code
	 * 
	 * @return  a value of -1 or 0 - 100
	 */
	public double getHomeX() {
		return _homeX;
	}
	/**
	 * Returns the home Y % coordinate applied to organisms with this genetic code
	 * 
	 * @return  a value of -1 or 0 - 100
	 */
	public double getHomeY() {
		return _homeY;
	}
	/**
	 * Returns the season activity applied to organisms with this genetic code
	 * 
	 * @return  a value of 0 - 2.
	 */
	public int getActivity() {
		return _activity;
	}
	/**
	 * Returns if the clade needs to be updated
	 * 
	 * @return  true or false.
	 */
	public int getUpdateClade() {
		return _updateClade;
	}
	/**
	 * Returns the plague version applied to organisms with this genetic code
	 * 
	 * @return  true or false.
	 */
	public boolean getPlague() {
		return _plague;
	}
	/**
	 * Returns if organisms with this genetic code will disperse their children or not.
	 * 
	 * @return  true if the organism disperses its children, false otherwise.
	 */
	public boolean getDisperseChildren() {
		return _disperseChildren;
	}
	/**
	 * Returns if organisms with this genetic code will attack their parents and children.
	 * 
	 * @return  true if the organism attacks its parent and children, false otherwise.
	 */
	public boolean getGenerationBattle() {
		return _generationBattle;
	}
	/**
	 * Returns if organisms with this genetic code will attack their siblings.
	 * 
	 * @return  true if the organism attacks its siblings, false otherwise.
	 */
	public boolean getSiblingBattle() {
		return _siblingBattle;
	}
	/**
	 * Returns if organisms with this genetic code will be altruistic or not.
	 * 
	 * @return  true if the organism is altruistic, false otherwise.
	 */
	public boolean getAltruist() {
		return _altruist;
	}
	/**
	 * Returns if organisms with this genetic code will be familial or not.
	 * 
	 * @return  true if the organism is familial, false otherwise.
	 */
	public boolean getFamilial() {
		return _familial;
	}
	/**
	 * Returns if organisms with this genetic code will be social or not.
	 * 
	 * @return  true if the organism is social, false otherwise.
	 */
	public boolean getSocial() {
		return _social;
	}
	/**
	 * Returns if organisms with this genetic code will be peaceful or not.
	 * 
	 * @return  true if the organism is peaceful, false otherwise.
	 */
	public boolean getPeaceful() {
		return _peaceful;
	}
	/**
	 * Returns if organisms with this genetic code will be passive or not.
	 * 
	 * @return  true if the organism is passive, false otherwise.
	 */
	public boolean getPassive() {
		return _passive;
	}
	/**
	 * Returns if organisms with this genetic code will turn clockwise or not.
	 * 
	 * @return  true if the organism turns clockwise, false otherwise.
	 */
	public boolean getClockwise() {
		return _clockwise;
	}
	/**
	 * Returns if the function of pink is modified or not.
	 * 
	 * @return  true if the function of pink is modified, false otherwise.
	 */
	public boolean getModifiespink() {
		return _modifiespink;
	}
	/**
	 * Returns the specialization of enhanced parasites
	 * 
	 * @return  a value of 1 - 3.
	 */
	public int getModifiescream() {
		return _modifiescream;
	}
	/**
	 * Returns the specialization of spores
	 * 
	 * @return  a value of 1 - 12.
	 */
	public int getModifiesspore() {
		return _modifiesspore;
	}
	/**
	 * Returns the further specialization of spores
	 * 
	 * @return  a value of 1 - 70.
	 */
	public int getAdaptspore() {
		return _adaptspore;
	}
	/**
	 * Returns the version of black
	 * 
	 * @return  a value of 1 - 6.
	 */
	public int getModifiesblack() {
		return _modifiesblack;
	}
	/**
	 * Returns the further specialization of black
	 * 
	 * @return  a value of 1 - 25.
	 */
	public int getAdaptblack() {
		return _adaptblack;
	}
	/**
	 * Returns if the function of lilac is modified or not.
	 * 
	 * @return  true if the function of lilac is modified, false otherwise.
	 */
	public boolean getModifieslilac() {
		return _modifieslilac;
	}
	/**
	 * Returns the specialization of fallow
	 * 
	 * @return  a value of 1 - 4.
	 */
	public int getModifiesfallow() {
		return _modifiesfallow;
	}
	/**
	 * Returns if the function of sky is modified or not.
	 * 
	 * @return  true if the function of sky is modified, false otherwise.
	 */
	public boolean getModifiessky() {
		return _modifiessky;
	}
	/**
	 * Returns if the function of leaf is modified or not.
	 * 
	 * @return  true if the function of leaf is modified, false otherwise.
	 */
	public boolean getModifiesleaf() {
		return _modifiesleaf;
	}
	/**
	 * Returns if organisms with this genetic code will always retain half of their energy if they reproduce or not.
	 * 
	 * @return  true if the organism retains half of its energy, false otherwise.
	 */
	public boolean getSelfish() {
		return _selfish;
	}
	/**
	 * Returns the generation number of this organism.
	 * 
	 * @return  The generation number of this organism.
	 */
	public int getGeneration() {
		return _generation;
	}
	/**
	 * Returns the identification number of this organisms clade.
	 * 
	 * @return  The identification number of this organisms clade.
	 */
	public String getcladeID() {
		return _cladeID;
	}
	/**
	 * Return a reference to a gene.
	 * 
	 * @param i  The index of the gene in the genetic code.
	 * @return  A reference to the gene
	 */
	public Gene getGene(int i) {
		return _genes[i];
	}
	/**
	 * Return the number of genes of this code
	 * 
	 * @return  The number of genes
	 */
	public int getNGenes() {
		return _genes.length;
	}
	/**
	 * Gives mirror a random value (0 or 1)
	 */
	private void randomMirror() {
		_mirror = Utils.random.nextInt(2);
	}
	/**
	 * Gives symmetry a random value (1 - 8)
	 */
	private void randomSymmetry() {
		_symmetry = Utils.random.nextInt(8)+1;
	}
	/**
	 * Gives mutationrate a random value (min mutationrate - max mutationrate)
	 */
	private void randomMutationrate() {
		_mutationrate = Utils.random.nextInt(Utils.MAX_MUTATION_RATE - Utils.MIN_MUTATION_RATE + 1) + Utils.MIN_MUTATION_RATE;
	}
	/**
	 * Gives mutationrate a random value (old mutationrate - max mutationrate)
	 */
	private void increaseMutationrate() {
		if (_mutationrate < Utils.MAX_MUTATION_RATE) {
			_mutationrate = Utils.random.nextInt(Utils.MAX_MUTATION_RATE - _mutationrate + 1) + _mutationrate;
		}
	}
	/**
	 * Gives mutationrate a random value (min mutationrate - old mutationrate)
	 */
	private void decreaseMutationrate() {
		if (_mutationrate > Utils.MIN_MUTATION_RATE) {
			_mutationrate = Utils.random.nextInt(_mutationrate - Utils.MIN_MUTATION_RATE + 1) + Utils.MIN_MUTATION_RATE;
		}
	}
	/**
	 * Gives clonerate a random value (min clonerate - max clonerate)
	 */
	private void randomClonerate() {
		_clonerate = Utils.random.nextInt(Utils.MAX_CLONE_RATE - Utils.MIN_CLONE_RATE + 1) + Utils.MIN_CLONE_RATE;
	}
	/**
	 * Gives clonerate a random value (old clonerate - max clonerate)
	 */
	private void increaseClonerate() {
		if (_clonerate < Utils.MAX_CLONE_RATE) {
			_clonerate = Utils.random.nextInt(Utils.MAX_CLONE_RATE - _clonerate + 1) + _clonerate;
		}
	}
	/**
	 * Gives clonerate a random value (min clonerate - old clonerate)
	 */
	private void decreaseClonerate() {
		if (_clonerate > Utils.MIN_CLONE_RATE) {
			_clonerate = Utils.random.nextInt(_clonerate - Utils.MIN_CLONE_RATE + 1) + Utils.MIN_CLONE_RATE;
		}
	}
	/**
	 * Gives home X % coordinate a random value (-1 or 0 - 100)
	 */
	private void randomHomeX() {
		if (Utils.random.nextBoolean()) {
			_homeX = -1;
		} else {
			_homeX = Utils.random.nextDouble() * 100.0;
		}
	}
	/**
	 * Gives home Y % coordinate a random value (-1 or 0 - 100)
	 */
	private void randomHomeY() {
		if (Utils.random.nextBoolean()) {
			_homeY = -1;
		} else {
			_homeY = Utils.random.nextDouble() * 100.0;
		}
	}
	/**
	 * Increases home X % coordinate by a maximum of 1%
	 */
	private void increaseHomeX() {
		_homeX = _homeX + Utils.random.nextDouble();
	}
	/**
	 * Increases home Y % coordinate by a maximum of 1%
	 */
	private void increaseHomeY() {
		_homeY = _homeY + Utils.random.nextDouble();
	}
	/**
	 * Decreases home X % coordinate by a maximum of 1%
	 */
	private void decreaseHomeX() {
		_homeX = _homeX - Utils.random.nextDouble();
	}
	/**
	 * Increases home X % coordinate by a maximum of 1%
	 */
	private void decreaseHomeY() {
		_homeY = _homeY - Utils.random.nextDouble();
	}
	/**
	 * Gives activity a random value (0 - 2)
	 */
	private void randomActivity() {
		_activity = Utils.random.nextInt(3);
	}
	/**
	 * Create a random genes array making sure that there will be more or equal than
	 * 1 and less or equal than the initial complexity value.
	 * It needs symmetry to have a valid value. 
	 */
	private void randomGenes() {
		int nSegments = (1 + Utils.random.nextInt(Utils.INITIAL_COMPLEXITY)) * _symmetry; // 1 - Initial complexity value
		if (nSegments % _symmetry != 0)
		    nSegments += (_symmetry - (nSegments % _symmetry));
		int nGenes = nSegments / _symmetry;
		_genes = new Gene[nGenes];
		for (int i=0; i<nGenes; i++) {
			_genes[i] = new Gene();
			_genes[i].randomize();
			if (Utils.random.nextBoolean()) {
				_genes[i].setBranch(-1);
			} else {
				_genes[i].setBranch(Utils.random.nextInt(i+1));
			}
		}
	}
	/**
	 * Gives plague version a random value (true or false)
	 */
	private void randomPlague() {
		_plague = Utils.random.nextBoolean();
	}
	/**
	 * Decide randomly if organisms with this genetic code will try to
	 * disperse their children or not.
	 */
	private void randomDisperseChildren() {
		_disperseChildren =  Utils.random.nextBoolean();
	}
	/**
	 * Decide randomly if organisms with this genetic code will
	 * attack their parents and children or not.
	 */
	private void randomGenerationBattle() {
		_generationBattle =  Utils.random.nextBoolean();
	}
	/**
	 * Decide randomly if organisms with this genetic code will
	 * attack their siblings or not.
	 */
	private void randomSiblingBattle() {
		_siblingBattle =  Utils.random.nextBoolean();
	}
	/**
	 * Decide randomly if organisms with this genetic code will be
	 * altruistic or not.
	 */
	private void randomAltruist() {
		_altruist =  Utils.random.nextBoolean();
	}
	/**
	 * Decide randomly if organisms with this genetic code will be
	 * familial or not.
	 */
	private void randomFamilial() {
		_familial =  Utils.random.nextBoolean();
	}
	/**
	 * Decide randomly if organisms with this genetic code will be
	 * social or not.
	 */
	private void randomSocial() {
		_social =  Utils.random.nextBoolean();
	}
	/**
	 * Decide randomly if organisms with this genetic code will be
	 * peaceful or not.
	 */
	private void randomPeaceful() {
		_peaceful =  Utils.random.nextBoolean();
	}
	/**
	 * Decide randomly if organisms with this genetic code will be
	 * passive or not.
	 */
	private void randomPassive() {
		_passive =  Utils.random.nextBoolean();
	}
	/**
	 * Decide randomly if organisms with this genetic code will turn
	 * clockwise or not.
	 */
	private void randomClockwise() {
		_clockwise =  Utils.random.nextBoolean();
	}
	/**
	 * Decide randomly if the function of pink is modified or not.
	 */
	private void randomModifiespink() {
		_modifiespink =  Utils.random.nextBoolean();
	}
	/**
	 * Gives modifiescream a random value (1 - 3)
	 */
	private void randomModifiescream() {
		_modifiescream = Utils.random.nextInt(3)+1;
	}
	/**
	 * Gives modifiesspore a random value (1 - 12)
	 */
	private void randomModifiesspore() {
		_modifiesspore = Utils.random.nextInt(12)+1;
	}
	/**
	 * Gives adaptspore a random value (1 - 70)
	 */
	private void randomAdaptspore() {
		_adaptspore = Utils.random.nextInt(70)+1;
	}
	/**
	 * Gives modifiesblack a random value (1 - 6)
	 */
	private void randomModifiesblack() {
		_modifiesblack = Utils.random.nextInt(6)+1;
	}
	/**
	 * Gives adaptblack a random value (1 - 25)
	 */
	private void randomAdaptblack() {
		_adaptblack = Utils.random.nextInt(25)+1;
	}
	/**
	 * Decide randomly if the function of lilac is modified or not.
	 */
	private void randomModifieslilac() {
		_modifieslilac =  Utils.random.nextBoolean();
	}
	/**
	 * Gives modifiesfallow a random value (1 - 4)
	 */
	private void randomModifiesfallow() {
		_modifiesfallow = Utils.random.nextInt(4)+1;
	}
	/**
	 * Decide randomly if the function of sky is modified or not.
	 */
	private void randomModifiessky() {
		_modifiessky =  Utils.random.nextBoolean();
	}
	/**
	 * Decide randomly if the function of leaf is modified or not.
	 */
	private void randomModifiesleaf() {
		_modifiesleaf =  Utils.random.nextBoolean();
	}
	/**
	 * Decide randomly if organisms with this genetic code will always retain half of their energy if they reproduce or not.
	 */
	private void randomSelfish() {
		_selfish =  Utils.random.nextBoolean();
	}
	/**
	 * Creates a new random genetic code.
	 */
	public GeneticCode() {
		randomMirror(); 
		randomSymmetry();
		randomGenes();
		randomMutationrate();
		randomClonerate();
		randomHomeX();
		randomHomeY();
		randomActivity();
		randomModifiescream();
		randomModifiesfallow();
		randomModifiesspore();
		randomAdaptspore();
		randomModifiesblack();
		randomAdaptblack();
		randomPlague();
		randomDisperseChildren();
		randomGenerationBattle();
		randomSiblingBattle();
		randomAltruist();
		randomFamilial();
		randomSocial();
		randomPeaceful();
		randomPassive();
		randomClockwise();
		randomModifiespink();
		randomModifieslilac();
		randomModifiessky();
		randomModifiesleaf();
		randomSelfish();
	}	
	/**
	 * Creates a genetic code given its content.
	 * No check about the validity of the information is done.
	 * 
	 * @param genes  A list containing the genes
	 * @param symmetry  The symmetry that an organism with this genetic code will have.
	 * @param mirror  0 if the organism won't be mirrored, 1 if it will.
	 * @param mutationrate  The mutationrate that an organism with this genetic code will have.
	 * @param clonerate  The clonerate that an organism with this genetic code will have.
	 * @param homeX  The X % coordinate that an organism with this genetic code will have.
	 * @param homeY  The Y % coordinate that an organism with this genetic code will have.
	 * @param activity  The summer activity that an organism with this genetic code will have.
	 * @param modifiescream The specialization of an enhanced parasite.
	 * @param modifiesfallow The specialization of fallow. 
	 * @param modifiesspore The specialization of spores.
	 * @param adaptspore The further specialization of spores.
	 * @param modifiesblack The version of black.
	 * @param adaptblack The further specialization of black.
	 * @param plague  The plague version that an organism with this genetic code will have.
	 * @param disperseChildren  true if the organism will disperse its children.
	 * @param generationBattle  true if the organism attacks its parent and children.
	 * @param siblingBattle  true if the organism attacks its siblings.
	 * @param altruist  true if the organism is an altruist.
	 * @param familial  true if the organism is familial.
	 * @param social  true if the organism is social.
	 * @param peaceful  true if the organism is peaceful.
	 * @param passive  true if the organism is passive.
	 * @param clockwise  true if the organism turns clockwise.
	 * @param modifiespink  true if the function of pink is modified.
	 * @param modifieslilac  true if the function of lilac is modified.
	 * @param modifiessky  true if the function of sky is modified.
	 * @param modifiesleaf  true if the function of leaf is modified.
	 * @param selfish  true if the organism retains half of its energy if it reproduces.
	 */
	public GeneticCode(List<Gene> genes, int symmetry, int mirror, int mutationrate, int clonerate, double homeX, double homeY, int activity, int modifiescream,
		int modifiesfallow, int modifiesspore, int adaptspore, int modifiesblack, int adaptblack, boolean plague, boolean disperseChildren, boolean generationBattle,
		boolean siblingBattle, boolean altruist, boolean familial, boolean social, boolean peaceful, boolean passive, boolean clockwise, boolean modifiespink,
		boolean modifieslilac, boolean modifiessky, boolean modifiesleaf, boolean selfish) {
		int nGenes = genes.size();
		_genes = new Gene[nGenes];
		genes.toArray(_genes);
		_mirror = mirror;
		_symmetry = symmetry;
		_mutationrate = mutationrate;
		_clonerate = clonerate;
		_homeX = homeX;
		_homeY = homeY;
		_activity = activity;
		_modifiescream = modifiescream;
		_modifiesfallow = modifiesfallow;
		_modifiesspore = modifiesspore;
		_adaptspore = adaptspore;
		_modifiesblack = modifiesblack;
		_adaptblack = adaptblack;
		_plague = plague;
		_disperseChildren = disperseChildren;
		_generationBattle = generationBattle;
		_siblingBattle = siblingBattle;
		_altruist = altruist;
		_familial = familial;
		_social = social;
		_peaceful = peaceful;
		_passive = passive;
		_clockwise = clockwise;
		_modifiespink = modifiespink;
		_modifieslilac = modifieslilac;
		_modifiessky = modifiessky;
		_modifiesleaf = modifiesleaf;
		_selfish = selfish;
	}
	/**
	 * Creates a new genetic code based on the father genetic code but
	 * applying random mutations to it.
	 * 
	 * @param parentCode  The genetic code that this code will be based on.
	 */
	public GeneticCode(GeneticCode parentCode) {
		int i,j;
		int addedGene = -1;
		int removedGene = -1;
		int clonedGene = 0;
		int nGenes;
		boolean randomLength;
		boolean randomTheta;
		boolean randomBranch;
		boolean repairBranch = false;
		boolean randomredReaction;
		boolean randomgreenReaction;
		boolean randomblueReaction;
		boolean randomplagueReaction;
		boolean randomscourgeReaction;
		boolean randomwhiteReaction;
		boolean randomgrayReaction;
		boolean randomsilverReaction;
		boolean randomdefaultReaction;
		boolean randomconsumerReaction;
		boolean randomplantReaction;
		boolean randommagentaReaction;
		boolean randompinkReaction;
		boolean randomcoralReaction;
		boolean randomorangeReaction;
		boolean randombarkReaction;
		boolean randomvioletReaction;
		boolean randomvirusReaction;
		boolean randommaroonReaction;
		boolean randomcrimsonReaction;
		boolean randomoliveReaction;
		boolean randommintReaction;
		boolean randomcreamReaction;
		boolean randomspikeReaction;
		boolean randomfallowReaction;
		boolean randomlightblueReaction;
		boolean randomochreReaction;
		boolean randomskyReaction;
		boolean randomlilacReaction;
		boolean randomfireReaction;
		boolean randomlightbrownReaction;
		boolean randomgreenbrownReaction;
		boolean randombrownReaction;
		boolean randomiceReaction;
		boolean randombrokenReaction;
		boolean randomsickReaction;
		boolean randomfriendReaction;
		boolean randomColor;
		
		_mutationrate = parentCode.getMutationrate();
		if (Utils.randomMutation()) {
			// mutate mutation rate
			if (Utils.random.nextInt(10) < 1) {
				// large change
				if (Utils.random.nextBoolean()) {
                	// increase
					increaseMutationrate();
				} else {
					// decrease
					decreaseMutationrate();
				}
			} else {
				// small change
                if (Utils.random.nextBoolean()) {
                	// increase
                	_mutationrate = _mutationrate + Utils.random.nextInt(2)+1;
				} else {
					// decrease
					_mutationrate = _mutationrate - Utils.random.nextInt(2)-1;
				}
			}
		}
		// check if organism is inside allowed mutation rates
		if (_mutationrate > Utils.MAX_MUTATION_RATE) {
			_mutationrate = Utils.MAX_MUTATION_RATE;
		} else {
			if (_mutationrate < Utils.MIN_MUTATION_RATE) {
				_mutationrate = Utils.MIN_MUTATION_RATE;
			}
		}
		_clonerate = parentCode.getClonerate();
		if (Utils.randomMutation()) {
			// mutate clone rate
			if (Utils.random.nextInt(10) < 1) {
				// large change
				if (Utils.random.nextBoolean()) {
                	// increase
					increaseClonerate();
				} else {
					// decrease
					decreaseClonerate();
				}
			} else {
				// small change
                if (Utils.random.nextBoolean()) {
                	// increase
                	_clonerate = _clonerate + 1;
				} else {
					// decrease
					_clonerate = _clonerate - 1;
				}
			}
		}
		// check if organism is inside allowed clone rates
		if (_clonerate > Utils.MAX_CLONE_RATE) {
			_clonerate = Utils.MAX_CLONE_RATE;
		} else {
			if (_clonerate < Utils.MIN_CLONE_RATE) {
				_clonerate = Utils.MIN_CLONE_RATE;
			}
		}
		_homeX = parentCode.getHomeX();
		if (Utils.random.nextInt(1000) < _mutationrate) {
			// mutate homeX
			if (Utils.random.nextInt(10) < 1) {
				// large random change
				randomHomeX();
			} else {
				if (_homeX >= 0) {
					if (Utils.random.nextBoolean()) {
	                	// increase
						increaseHomeX();
					} else {
						// decrease
						decreaseHomeX();
					}
				}
			}
		}
		// check if organism is inside allowed homeX % coordinates
		if (_homeX != -1) {
			if (_homeX > 100) {
				_homeX = 100;
			} else {
				if (_homeX < 0) {
					_homeX = 0;
				}
			}
		}
		_homeY = parentCode.getHomeY();
		if (Utils.random.nextInt(1000) < _mutationrate) {
			// mutate homeY
			if (Utils.random.nextInt(10) < 1) {
				// large random change
				randomHomeY();
			} else {
				if (_homeY >= 0) {
					if (Utils.random.nextBoolean()) {
	                	// increase
						increaseHomeY();
					} else {
						// decrease
						decreaseHomeY();
					}
				}
			}
		}
		// check if organism is inside allowed homeY % coordinates
		if (_homeY != -1) {
			if (_homeY > 100) {
				_homeY = 100;
			} else {
				if (_homeY < 0) {
					_homeY = 0;
				}
			}
		}
		if (Utils.random.nextInt(10000) < _mutationrate) {
			// change symmetry
			randomSymmetry();
			// keep mirror
			_mirror = parentCode.getMirror();
			// keep number of segments
			nGenes = parentCode.getNGenes();
			if (_symmetry != parentCode.getSymmetry()) {
				_updateClade = -1;
			}
		} else {
			// keep symmetry
			_symmetry = parentCode.getSymmetry();
			if (Utils.random.nextInt(10000) < _mutationrate) {
				// change mirror
				randomMirror();
				// keep number of segments
				nGenes = parentCode.getNGenes();
				if (_mirror != parentCode.getMirror()) {
					_updateClade = -1;
				}
			} else {
				// keep mirror
				_mirror = parentCode.getMirror();
				if (Utils.random.nextInt(10000) < _mutationrate) {
					// change number of segments
					if (Utils.random.nextBoolean()) {
						// increase segments
						if (parentCode.getNGenes() >= 100) {
							nGenes = parentCode.getNGenes();
						} else {
							nGenes = parentCode.getNGenes() + 1;
							addedGene = Utils.random.nextInt(nGenes);
							_updateClade = -1;
						}
					} else {
					    // decrease segments
						if (parentCode.getNGenes() <= 1) {
							nGenes = parentCode.getNGenes();
						} else {
							nGenes = parentCode.getNGenes() - 1;
							removedGene = Utils.random.nextInt(parentCode.getNGenes());
							_updateClade = -1;
							repairBranch = true;
						}
					}
				} else {
				    // keep number of segments
					nGenes = parentCode.getNGenes();
				}
			}
		}
		// Create genes
		_genes = new Gene[nGenes];
		for (i=0,j=0; i<nGenes; i++,j++) {
			if (removedGene == j) {
				i--;
				continue;
			}
			if (addedGene == i) {
				if (Utils.random.nextInt(100) < _clonerate) {
					// Clone the gene after or before itself, and always randomize rotation
					if (i == 0) {
						clonedGene = -1;
					} else {
						if (i<nGenes-1) {
							if (Utils.random.nextBoolean()) {
								clonedGene = -1;
							} else {
								clonedGene = 1;
								j--;
							}
						} else {
							clonedGene = 1;
							j--;
						}
					}
				} else {
					// Create a new gene
					_genes[i] = new Gene();
					_genes[i].randomize();
					if (Utils.random.nextBoolean()) {
						_genes[i].setBranch(-1);
					} else {
						_genes[i].setBranch(Utils.random.nextInt(i+1));
					}
					j--;
					continue;
				}
			}
			randomLength = randomTheta = randomBranch = randomredReaction = randomgreenReaction = randomblueReaction = randomplagueReaction = randomscourgeReaction
			= randomwhiteReaction = randomgrayReaction = randomsilverReaction = randomdefaultReaction = randomconsumerReaction = randomplantReaction = randommagentaReaction
			= randompinkReaction = randomcoralReaction = randomorangeReaction = randombarkReaction = randomvioletReaction = randomvirusReaction = randommaroonReaction
			= randomcrimsonReaction = randomoliveReaction = randommintReaction = randomcreamReaction = randomspikeReaction = randomfallowReaction = randomlightblueReaction
			= randomochreReaction = randomskyReaction = randomlilacReaction = randomfireReaction = randomlightbrownReaction = randomgreenbrownReaction = randombrownReaction
			= randomiceReaction = randombrokenReaction = randomsickReaction = randomfriendReaction = randomColor = false;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomLength = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomTheta = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomBranch = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomredReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomgreenReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomblueReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomplagueReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomscourgeReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomwhiteReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomgrayReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomsilverReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomdefaultReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomconsumerReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomplantReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randommagentaReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randompinkReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomcoralReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomorangeReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randombarkReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomvioletReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomvirusReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randommaroonReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomcrimsonReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomoliveReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randommintReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomcreamReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomspikeReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomfallowReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomlightblueReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomochreReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomskyReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomlilacReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomfireReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomlightbrownReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomgreenbrownReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randombrownReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomiceReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randombrokenReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomsickReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomfriendReaction = true;
			if (Utils.random.nextInt(10000) < _mutationrate)
				randomColor = true;
			if (randomLength || randomTheta || randomBranch || repairBranch || randomredReaction || randomgreenReaction || randomblueReaction || randomplagueReaction
				|| randomscourgeReaction || randomwhiteReaction || randomgrayReaction || randomsilverReaction || randomdefaultReaction || randomconsumerReaction
				|| randomplantReaction || randommagentaReaction || randompinkReaction || randomcoralReaction || randomorangeReaction || randombarkReaction
				|| randomvioletReaction || randomvirusReaction || randommaroonReaction || randomcrimsonReaction || randomoliveReaction || randommintReaction
				|| randomcreamReaction || randomspikeReaction || randomfallowReaction || randomlightblueReaction || randomochreReaction || randomskyReaction
				|| randomlilacReaction || randomfireReaction || randomlightbrownReaction || randomgreenbrownReaction || randombrownReaction || randomiceReaction
				|| randombrokenReaction || randomsickReaction || randomfriendReaction || randomColor || clonedGene != 0) {
				_genes[i] = new Gene();
				if (clonedGene != 0) {
					if ((Utils.random.nextBoolean()) || (randomLength)) {
						_genes[i].randomizeLength();
					} else {
						_genes[i].setLength(parentCode.getGene(j).getLength());
					}
					_genes[i].randomizeTheta();
					if ((Utils.random.nextInt(4) < 1) || (randomBranch)) {
						if (Utils.random.nextBoolean()) {
							_genes[i].setBranch(-1);
						} else {
							_genes[i].setBranch(Utils.random.nextInt(i+1));
						}
					} else {
						_genes[i].setBranch(parentCode.getGene(j).getBranch());
						if (clonedGene == 1) {
							if (_genes[i].getBranch() == -1) {
								if (Utils.random.nextBoolean()) {
									_genes[i].setBranch(i-1);
								} else {
									if (Utils.random.nextBoolean()) {
										_genes[i].setBranch(i);
									}
								}
							} else {
								if (Utils.random.nextBoolean()) {
									if (Utils.random.nextBoolean()) {
										_genes[i].setBranch(-1);
									} else {
										_genes[i].setBranch(i);
									}
								}
							}
						}
					}
				} else {
					if (randomLength)
						_genes[i].randomizeLength();
					else
						_genes[i].setLength(parentCode.getGene(j).getLength());
					if (randomTheta)
						_genes[i].randomizeTheta();
					else
						_genes[i].setTheta(parentCode.getGene(j).getTheta());
					if (randomBranch) {
						if (Utils.random.nextBoolean()) {
							_genes[i].setBranch(-1);
						} else {
							_genes[i].setBranch(Utils.random.nextInt(i+1));
						}
					} else {
						_genes[i].setBranch(parentCode.getGene(j).getBranch());
						if ((repairBranch) && (_genes[i].getBranch() > i)) {
							_genes[i].setBranch(i);
						}
					}
				}
				if (randomredReaction)
					_genes[i].randomizeredReaction();
				else
					_genes[i].setredReaction(parentCode.getGene(j).getredReaction());
				if (randomgreenReaction)
					_genes[i].randomizegreenReaction();
				else
					_genes[i].setgreenReaction(parentCode.getGene(j).getgreenReaction());
				if (randomblueReaction)
					_genes[i].randomizeblueReaction();
				else
					_genes[i].setblueReaction(parentCode.getGene(j).getblueReaction());
				if (randomplagueReaction)
					_genes[i].randomizeplagueReaction();
				else
					_genes[i].setplagueReaction(parentCode.getGene(j).getplagueReaction());
				if (randomscourgeReaction)
					_genes[i].randomizescourgeReaction();
				else
					_genes[i].setscourgeReaction(parentCode.getGene(j).getscourgeReaction());
				if (randomwhiteReaction)
					_genes[i].randomizewhiteReaction();
				else
					_genes[i].setwhiteReaction(parentCode.getGene(j).getwhiteReaction());
				if (randomgrayReaction)
					_genes[i].randomizegrayReaction();
				else
					_genes[i].setgrayReaction(parentCode.getGene(j).getgrayReaction());
				if (randomsilverReaction)
					_genes[i].randomizesilverReaction();
				else
					_genes[i].setsilverReaction(parentCode.getGene(j).getsilverReaction());
				if (randomdefaultReaction)
					_genes[i].randomizedefaultReaction();
				else
					_genes[i].setdefaultReaction(parentCode.getGene(j).getdefaultReaction());
				if (randomconsumerReaction)
					_genes[i].randomizeconsumerReaction();
				else
					_genes[i].setconsumerReaction(parentCode.getGene(j).getconsumerReaction());
				if (randomplantReaction)
					_genes[i].randomizeplantReaction();
				else
					_genes[i].setplantReaction(parentCode.getGene(j).getplantReaction());
				if (randommagentaReaction)
					_genes[i].randomizemagentaReaction();
				else
					_genes[i].setmagentaReaction(parentCode.getGene(j).getmagentaReaction());
				if (randompinkReaction)
					_genes[i].randomizepinkReaction();
				else
					_genes[i].setpinkReaction(parentCode.getGene(j).getpinkReaction());
				if (randomcoralReaction)
					_genes[i].randomizecoralReaction();
				else
					_genes[i].setcoralReaction(parentCode.getGene(j).getcoralReaction());
				if (randomorangeReaction)
					_genes[i].randomizeorangeReaction();
				else
					_genes[i].setorangeReaction(parentCode.getGene(j).getorangeReaction());
				if (randombarkReaction)
					_genes[i].randomizebarkReaction();
				else
					_genes[i].setbarkReaction(parentCode.getGene(j).getbarkReaction());
				if (randomvioletReaction)
					_genes[i].randomizevioletReaction();
				else
					_genes[i].setvioletReaction(parentCode.getGene(j).getvioletReaction());
				if (randomvirusReaction)
					_genes[i].randomizevirusReaction();
				else
					_genes[i].setvirusReaction(parentCode.getGene(j).getvirusReaction());
				if (randommaroonReaction)
					_genes[i].randomizemaroonReaction();
				else
					_genes[i].setmaroonReaction(parentCode.getGene(j).getmaroonReaction());
				if (randomcrimsonReaction)
					_genes[i].randomizecrimsonReaction();
				else
					_genes[i].setcrimsonReaction(parentCode.getGene(j).getcrimsonReaction());
				if (randomoliveReaction)
					_genes[i].randomizeoliveReaction();
				else
					_genes[i].setoliveReaction(parentCode.getGene(j).getoliveReaction());
				if (randommintReaction)
					_genes[i].randomizemintReaction();
				else
					_genes[i].setmintReaction(parentCode.getGene(j).getmintReaction());
				if (randomcreamReaction)
					_genes[i].randomizecreamReaction();
				else
					_genes[i].setcreamReaction(parentCode.getGene(j).getcreamReaction());
				if (randomspikeReaction)
					_genes[i].randomizespikeReaction();
				else
					_genes[i].setspikeReaction(parentCode.getGene(j).getspikeReaction());
				if (randomfallowReaction)
					_genes[i].randomizefallowReaction();
				else
					_genes[i].setfallowReaction(parentCode.getGene(j).getfallowReaction());
				if (randomlightblueReaction)
					_genes[i].randomizelightblueReaction();
				else
					_genes[i].setlightblueReaction(parentCode.getGene(j).getlightblueReaction());
				if (randomochreReaction)
					_genes[i].randomizeochreReaction();
				else
					_genes[i].setochreReaction(parentCode.getGene(j).getochreReaction());
				if (randomskyReaction)
					_genes[i].randomizeskyReaction();
				else
					_genes[i].setskyReaction(parentCode.getGene(j).getskyReaction());
				if (randomlilacReaction)
					_genes[i].randomizelilacReaction();
				else
					_genes[i].setlilacReaction(parentCode.getGene(j).getlilacReaction());
				if (randomfireReaction)
					_genes[i].randomizefireReaction();
				else
					_genes[i].setfireReaction(parentCode.getGene(j).getfireReaction());
				if (randomlightbrownReaction)
					_genes[i].randomizelightbrownReaction();
				else
					_genes[i].setlightbrownReaction(parentCode.getGene(j).getlightbrownReaction());
				if (randomgreenbrownReaction)
					_genes[i].randomizegreenbrownReaction();
				else
					_genes[i].setgreenbrownReaction(parentCode.getGene(j).getgreenbrownReaction());
				if (randombrownReaction)
					_genes[i].randomizebrownReaction();
				else
					_genes[i].setbrownReaction(parentCode.getGene(j).getbrownReaction());
				if (randomiceReaction)
					_genes[i].randomizeiceReaction();
				else
					_genes[i].seticeReaction(parentCode.getGene(j).geticeReaction());
				if (randombrokenReaction)
					_genes[i].randomizebrokenReaction();
				else
					_genes[i].setbrokenReaction(parentCode.getGene(j).getbrokenReaction());
				if (randomsickReaction)
					_genes[i].randomizesickReaction();
				else
					_genes[i].setsickReaction(parentCode.getGene(j).getsickReaction());
				if (randomfriendReaction)
					_genes[i].randomizefriendReaction();
				else
					_genes[i].setfriendReaction(parentCode.getGene(j).getfriendReaction());
				if (randomColor) {
					_genes[i].randomizeColor();
					if ((!getGene(i).getColor().equals(parentCode.getGene(j).getColor())) && (clonedGene == 0)) {
						if (_updateClade <= 0) {
							_updateClade = 1;
						} else {
							_updateClade++;
						}
					}
				} else {
					_genes[i].setColor(parentCode.getGene(j).getColor());
				}
				if (clonedGene != 0) {
					if (clonedGene == -1) {
						j--;
					}
					clonedGene = 0;
				}
			} else {
				_genes[i] = parentCode.getGene(j);
			}			
		}
        
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomActivity();
		else
			_activity = parentCode.getActivity();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomModifiescream();
		else
			_modifiescream = parentCode.getModifiescream();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomModifiesfallow();
		else
			_modifiesfallow = parentCode.getModifiesfallow();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomModifiesspore();
		else
			_modifiesspore = parentCode.getModifiesspore();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomAdaptspore();
		else
			_adaptspore = parentCode.getAdaptspore();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomModifiesblack();
		else
			_modifiesblack = parentCode.getModifiesblack();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomAdaptblack();
		else
			_adaptblack = parentCode.getAdaptblack();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomPlague();
		else
			_plague = parentCode.getPlague();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomDisperseChildren();
		else
			_disperseChildren = parentCode.getDisperseChildren();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomGenerationBattle();
		else
			_generationBattle = parentCode.getGenerationBattle();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomSiblingBattle();
		else
			_siblingBattle = parentCode.getSiblingBattle();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomAltruist();
		else
			_altruist = parentCode.getAltruist();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomFamilial();
		else
			_familial = parentCode.getFamilial();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomSocial();
		else
			_social = parentCode.getSocial();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomPeaceful();
		else
			_peaceful = parentCode.getPeaceful();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomPassive();
		else
			_passive = parentCode.getPassive();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomClockwise();
		else
			_clockwise = parentCode.getClockwise();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomModifiespink();
		else
			_modifiespink = parentCode.getModifiespink();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomModifieslilac();
		else
			_modifieslilac = parentCode.getModifieslilac();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomModifiessky();
		else
			_modifiessky = parentCode.getModifiessky();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomModifiesleaf();
		else
			_modifiesleaf = parentCode.getModifiesleaf();
		if (Utils.random.nextInt(10000) < _mutationrate)
			randomSelfish();
		else
			_selfish = parentCode.getSelfish();
		// Add 1 to the generation number
		_generation = parentCode.getGeneration() + 1;
		// Add the clade identification
		_cladeID = parentCode.getcladeID();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		GeneticCode newCode = null;
		try {
			newCode = (GeneticCode) super.clone();
			newCode._genes = new Gene[_genes.length];
			for (int i=0; i<_genes.length; i++)
				newCode._genes[i] = (Gene) _genes[i].clone();
		} catch (CloneNotSupportedException e) {// We should never reach this
		}
		return newCode;
	}
	
	/**
	 * Draws a representation of this genetic code. This representation
	 * is equivalent to draw an adult organism with this genetic code and
	 * no rotation.
	 * 
	 * @param g  The place where the representation is drawn.
	 * @param width  The width of the available space. 
	 * @param height  The height of the available space.
	 */
	public void draw(Graphics g, int width, int height) {
		int[][] x0 = new int[_symmetry][_genes.length];
		int[][] y0 = new int[_symmetry][_genes.length];
		int[][] x1 = new int[_symmetry][_genes.length];
		int[][] y1 = new int[_symmetry][_genes.length];
		int maxX = 0;
		int minX = 0;
		int maxY = 0;
		int minY = 0;
		double scale = 1.0;
		Vector2D v = new Vector2D();
		Graphics2D g2 = (Graphics2D) g;

		for (int i=0; i<_symmetry; i++) {
			for (int j=0; j<_genes.length; j++) {
				v.setModulus(_genes[j].getLength());
				if (j==0) {
					x0[i][j]=y0[i][j]=0;
					if (_mirror == 0 || i%2==0)
						v.setTheta(_genes[j].getTheta()+i*2*Math.PI/_symmetry);
					else {
						v.setTheta(_genes[j].getTheta()+(i-1)*2*Math.PI/_symmetry);
						v.invertX();
					}
				} else {
					if (_genes[j].getBranch() == -1) {
						x0[i][j] = x1[i][j-1];
						y0[i][j] = y1[i][j-1];
						if (_mirror == 0 || i%2==0)
							v.addDegree(_genes[j].getTheta());
						else
							v.addDegree(-_genes[j].getTheta());
					} else {
					if (_genes[j].getBranch() == 0) {
						x0[i][j]=y0[i][j]=0;
						if (_mirror == 0 || i%2==0)
							v.setTheta(_genes[j].getTheta()+i*2*Math.PI/_symmetry);
						else {
							v.setTheta(_genes[j].getTheta()+(i-1)*2*Math.PI/_symmetry);
							v.invertX();
						}
					} else {
						x0[i][j] = x1[i][_genes[j].getBranch() - 1];
						y0[i][j] = y1[i][_genes[j].getBranch() - 1];
						if (_mirror == 0 || i%2==0)
							v.addDegree(_genes[j].getTheta());
						else
							v.addDegree(-_genes[j].getTheta());
					    }
					}
				}
				
				x1[i][j] = (int) Math.round(v.getX() + x0[i][j]);
				y1[i][j] = (int) Math.round(v.getY() + y0[i][j]);
				
				maxX = Math.max(maxX, Math.max(x0[i][j], x1[i][j]));
				maxY = Math.max(maxY, Math.max(y0[i][j], y1[i][j]));
				minX = Math.min(minX, Math.min(x0[i][j], x1[i][j]));
				minY = Math.min(minY, Math.min(y0[i][j], y1[i][j]));
			}
		}
		
		g2.translate(width/2, height/2);
		if (maxX-minX > width)
			scale = (double)width/(double)(maxX-minX);
		if (maxY-minY > height)
			scale = Math.min(scale, (double)height/(double)(maxY-minY));
		g2.scale(scale, scale);
		
		for (int i=0; i<_symmetry; i++) {
			for (int j=0; j<_genes.length; j++) {
				x0[i][j]+=(-minX-maxX)/2;
				x1[i][j]+=(-minX-maxX)/2;
				y0[i][j]+=(-minY-maxY)/2;
				y1[i][j]+=(-minY-maxY)/2;
				g2.setColor(_genes[j].getColor());
				g2.drawLine(x0[i][j],y0[i][j],x1[i][j],y1[i][j]);
			}
		}
	}
}

