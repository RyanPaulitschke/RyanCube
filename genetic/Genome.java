package genetic;

import java.util.ArrayList;

/**
 * Genome Parent
 * 
 * @author Ryan Paulitschke
 */
public interface Genome {
	public ArrayList<Gene> getGenes();

	public Genome mutate();

	public ArrayList<Genome> crossOver(Genome genome);
}
