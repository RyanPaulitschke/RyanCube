package genetic;

import java.util.ArrayList;
import java.util.Random;

import cube.RubiksCube;

/**
 * Genome Child used for genomes with double parameter genes
 * 
 * @author Ryan Paulitschke
 */
public class DoubleGenome implements Genome {

	public ArrayList<Gene> genes;

	public DoubleGenome(ArrayList<Gene> g) {
		this.genes = g;
	}

	public ArrayList<Gene> getGenes() {
		return this.genes;
	}

	/**
	 * Mutates the current genome
	 */
	public Genome mutate() {
		Random rng = new Random();
		ArrayList<Gene> mutation = new ArrayList<Gene>();

		for (int i = 0; i < genes.size(); i++) {
			if (rng.nextInt(4) == 0)
				mutation.add(new Gene(genes.get(i).value)); // No modifications
			else {
				mutation.add(new Gene(genes.get(i).value
						+ (rng.nextDouble() - 0.5) * 0.1));
			}

		}

		return new DoubleGenome(mutation);

	}

	/**
	 * Performs crossOver between 2 genomes and returns the resulting 2 genomes
	 */
	public ArrayList<Genome> crossOver(Genome genome) {
		Random rng = new Random();
		ArrayList<Gene> child = new ArrayList<Gene>();
		ArrayList<Gene> child_b = new ArrayList<Gene>();

		DoubleGenome g = (DoubleGenome) genome;

		for (int i = 0; i < genes.size(); i++) {

			if (rng.nextInt(2) == 0) {
				child.add(new Gene(genes.get(i).value));
				child_b.add(new Gene(g.genes.get(i).value));
			} else {
				child_b.add(new Gene(genes.get(i).value));
				child.add(new Gene(g.genes.get(i).value));
			}
		}

		ArrayList<Genome> children = new ArrayList<Genome>();
		children.add(new DoubleGenome(child));
		children.add(new DoubleGenome(child_b));

		return children;
	}

}
