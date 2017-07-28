package genetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import utility.Matrix;
import display.Gui;
import network.NeuralNet;
import network.SBPImpl;

/**
 * Genetic Algorithm
 * 
 * @author Ryan Paulitschke
 *
 */
public class GeneticAlg {
	//Initial Population Size
	public int pop=100;
	
	// Percentage splits, must total 1
	public double amt_mutation = 0.4;
	public double amt_crossover = 0.4;
	public double amt_elite = 0.2;
	
	// List of Mutated & crossbread Genomes
	public ArrayList<Genome> mutations;
	public ArrayList<Genome> crossbreeds;
	public ArrayList<Genome> elites;

	public void GA(NeuralNet nnet, double[][] training_data, int max_generations) {
		if (max_generations < 1) {
			System.out.println(max_generations
							+ " is an invalid parameter, generations must be positive.\nNumber of generations was set to 10.");
			max_generations = 10;
		}

		//Run timer
		long start = System.currentTimeMillis();
		
		// Init Pop
		tableRow[] table = initPopulation(nnet, pop); //5 minimum
		// Calc Raw Fit
		calcRawFitness(nnet, table);
		// Calc Fit
		calcFitnessRank(table);
		// Check if best genome is good enough
		tableRow s_elite = eliteSelectionSingle(table);
		//Don't calculate diversity on first run-through (no elites to compare too)
		// calc prob selection
		calcProbSel(table);
		// do elite selection
		eliteSelection(table);
		// mutate
		mutation(table);
		// crossover
		crossOver(table);	
		
		for(int i=1;i<max_generations;i++){
			// Combine elites, mutations, and crossbreeds
			table = initNextPop();
			// Calc Raw Fit
			calcRawFitness(nnet, table);
			// Calc Fit
			calcFitnessRank(table);
			// Check if best genome is good enough
			s_elite = eliteSelectionSingle(table);

			if (s_elite.raw_fitness < 0.01) {
				System.out.println("Genome found with: " + s_elite.raw_fitness
						* 100 + "% error");
				
				exportGenome(nnet,s_elite);
				System.out.println("Genome was found in Generation: "+i+" from a pool of "+table.length+" genomes"
						+ "\nUse classify to test");
				break;
			}

			// calc Raw Div
			calcRawDiversity(table);
			// calc Div
			calcDiversityRank(table);
			// calc combined rank
			calcCombinedRank(table);
			// calc prob selection
			calcProbSel(table);
			// do elite selection
			eliteSelection(table);
			// mutate
			mutation(table);
			// crossover
			crossOver(table);
			
				}
		
		System.out.println("Total execution time: "+((double)(System.currentTimeMillis() - start)) * 0.001+"s\n");
		if (s_elite.raw_fitness >= 0.01) {
			//display(table);
			System.out.println("GA Failed to train within a <1% Error accuracy\nMost effective genome was imported to the network\nClassify can be used to test effectiveness");
			calcFitnessRank(table);
			exportGenome(nnet,table[0]);
			
		}
	}

	
	/**
	 * Calculates the avg fitness value of a table
	 * @param table
	 * @return avg fitness
	 */
	public double avgFitness(tableRow[] table){
		double subtotal=0;
		
		for (tableRow t:table){
			subtotal+=t.raw_fitness;
		}
		
		return subtotal/table.length;
		
	}
	
	
	/**
	 * A row of a table incharge of keeping track of genome GA properties
	 */
	public class tableRow {
		public Genome genome;
		public double raw_fitness;
		public int fitness; // rank
		public double probability_selection;

		public double raw_diversity;
		public int diversity; // rank

		public double combined_rank;
	}

	/**
	 * Creates the initial population/table
	 * @param nn neural network
	 * @param amt size of population
	 * @return population table
	 */
	public tableRow[] initPopulation(NeuralNet nn, int amt) {
		tableRow[] population = new tableRow[amt];

		System.out.println("Population of " + population.length+" Genomes was created\nGenetic algorithm is running...");

		for (int h = 0; h < amt; h++) {

			ArrayList<Gene> lg = new ArrayList<Gene>();

			NeuralNet nnet = new NeuralNet(nn.inlsize, nn.hiddenlsize, nn.hlsb,
					nn.hlsc, nn.outlsize);
			nnet.inputs = nn.inputs;
			nnet.expected = nn.expected;

			// Store Weights in genes
			for (int k = 0; k < nnet.weights.length; k++) {
				int y = nnet.weights[k].length;
				int x = nnet.weights[k][0].length;

				for (int i = 0; i < y; i++) {
					for (int j = 0; j < x; j++) {
						Gene g = new Gene(nnet.weights[k][i][j]);
						lg.add(g);
					}
				}
			}

			// Store Bias Weights in genes
			for (int k = 0; k < nnet.bweights.length; k++) {
				int y = nnet.bweights[k].length;

				for (int i = 0; i < y; i++) {
						Gene g = new Gene(nnet.bweights[k][i]);
						lg.add(g);
				}
			}
			
			// store genomes in table
			population[h] = new tableRow();
			population[h].genome = new DoubleGenome(lg);

		}
		return population;
	}

	/**
	 * Creates the next generation of the population
	 * @return table
	 */
	public tableRow[] initNextPop(){
		ArrayList<Genome> new_table = new ArrayList<Genome>();
		
		new_table.addAll(elites);
		new_table.addAll(mutations);
		new_table.addAll(crossbreeds);
		
		tableRow[] pop = new tableRow[new_table.size()];
		
		for (int h = 0; h < new_table.size(); h++) {
			pop[h] = new tableRow();
			pop[h].genome = new_table.get(h);
		}
		
		return pop;
	}
	
	/**
	 * Displays the table
	 * @param t
	 */
	public void display(tableRow[] t){
		for(int i=0;i<t.length;i++)
		System.out.println("rFit:"+t[i].raw_fitness+" Fit#"+t[i].fitness+" rDiv:"+t[i].raw_diversity+" Div#"+t[i].diversity+" C#"+t[i].combined_rank);
	}
	
	/**
	 * Handles the CrossOver of genomes
	 * @param table
	 */
	public void crossOver(tableRow[] table) {

		int num_mutations = (int) Math.floor((table.length * amt_crossover)*0.5);

		crossbreeds = new ArrayList<Genome>();

		Random rand = new Random();

		ArrayList<Genome> cb;
		
		for (int i = 0; i < num_mutations; i++) {
			int index = rand.nextInt(table.length);
			int index_b = rand.nextInt(table.length);
			
			cb = table[index].genome.crossOver(table[index_b].genome);
			crossbreeds.add(cb.get(0));
			crossbreeds.add(cb.get(1));
		}
	}

	/**
	 * Handles the mutation of genomes
	 * @param table
	 */
	public void mutation(tableRow[] table) {
		int num_mutations = (int) Math.floor(table.length * amt_mutation);

		mutations = new ArrayList<Genome>();

		Random rand = new Random();

		for (int i = 0; i < num_mutations; i++) {
			int index = rand.nextInt(table.length);

			mutations.add(table[index].genome.mutate());

		}
	}

	/**
	 * Selects a single elite genome based on probability selection
	 * @param table
	 * @return a single genome
	 */
	public tableRow eliteSelectionSingle(tableRow[] table) {

		Random rand = new Random();
		double choice = rand.nextDouble();

		double sub_total = 0;
		for (int i = 0; i < table.length; i++) {
			sub_total += table[i].probability_selection;
			if (choice < sub_total)
				return table[i];
		}

		return table[0];
	}

	/**
	 * Selects the top amt_elite % based on probability selection
	 * elite genomes are stored in "elites"
	 * @param table
	 */
	public void eliteSelection(tableRow[] table) {
		elites = new ArrayList<Genome>();

		ArrayList<tableRow> tlist = new ArrayList<tableRow>(
				Arrays.asList(table));

		int num_elites = (int) Math.floor(table.length * amt_elite);
		
		if (num_elites==0 && table.length>0)
		num_elites=1;

		Random rand = new Random();

		// Pick "top" % of combined rank
		while (num_elites > 0) {
			double choice = rand.nextDouble();

			tlist.remove(pickElite(tlist, choice));
			calcProbSel(tlist);

			num_elites--;
		}

	}

	/**
	 * Picks a single elite based on choice value
	 * @param tlist
	 * @param choice
	 * @return a single elite
	 */
	public tableRow pickElite(ArrayList<tableRow> tlist, double choice) {
		double sub_total = 0;
		for (int i = 0; i < tlist.size(); i++) {
			sub_total += tlist.get(i).probability_selection;
			if (choice < sub_total) {
				elites.add(tlist.get(i).genome);
				return tlist.get(i);
			}
		}
		return null;
	}


	/**
	 * Used to compare by raw fitness
	 */
	Comparator<tableRow> by_fitness = new Comparator<tableRow>() {
		@Override
		public int compare(tableRow i, tableRow j) {
			return ((Double) i.raw_fitness).compareTo(j.raw_fitness);
		}
	};

	/**
	 * Calculates the raw fitness for a table
	 * @param table
	 */
	public void calcFitnessRank(tableRow[] table) {
		Arrays.sort(table, by_fitness);

		for (int i = 0; i < table.length; i++) {
			table[i].fitness = i;
		}
	}

	/**
	 * Used to compare by diversity score
	 */
	Comparator<tableRow> by_diversity = new Comparator<tableRow>() {
		@Override
		public int compare(tableRow i, tableRow j) {
			return ((Double) j.raw_diversity).compareTo(i.raw_diversity);
		}
	};
	
	/**
	 * Calculates the diversity rank for a table
	 * @param table
	 */
	public void calcDiversityRank(tableRow[] table) {
		Arrays.sort(table, by_diversity);

		for (int i = 0; i < table.length; i++) {
			table[i].diversity = i;
		}
	}
	
	/**
	 * Used to compare by combined rank
	 */
	Comparator<tableRow> by_combined = new Comparator<tableRow>() {
		@Override
		public int compare(tableRow i, tableRow j) {
			return ((Double) i.combined_rank).compareTo(j.combined_rank);
		}
	};
	
	/**
	 * Calculates the combined rank for a table
	 * @param table
	 */
	public void calcCombinedRank(tableRow[] table) {

		for (int i = 0; i < table.length; i++) {
			table[i].combined_rank = table[i].fitness+table[i].diversity;
		}
		
		Arrays.sort(table, by_combined);
		
	}
	
	/**
	 * Calculates the probability selection for a table(Array)
	 * @param table
	 */
	public void calcProbSel(tableRow[] table) {

		table[0].probability_selection = 0.667;

		for (int i = 1; i < table.length; i++) {
			table[i].probability_selection = 0.667 * (1 - table[i - 1].probability_selection);
		}

	}

	/**
	 * Calculates the probability selection for a table(ArrayList)
	 * @param table
	 */
	public void calcProbSel(ArrayList<tableRow> table) {

		table.get(0).probability_selection = 0.667;

		for (int i = 1; i < table.size(); i++) {
			table.get(i).probability_selection = 0.667 * (1 - table.get(i - 1).probability_selection);
		}

	}

	/**
	 * Calculates the raw diversity value for a given table
	 * @param table to hold the diversity scores
	 * @return table
	 */
	public tableRow[] calcRawDiversity(tableRow[] table) {
		for (int h = 0; h < table.length; h++) {
			double div = 0;

			for (int i = 0; i < elites.size(); i++) {
				for (int j = 0; j < table[h].genome.getGenes().size(); j++) {
					div += Math.abs(elites.get(i).getGenes().get(j).value
							- table[h].genome.getGenes().get(j).value);
				}
			}

			table[h].raw_diversity = div;
		}

		return table;
	}

	/**
	 * Calculates the raw fitness values for a given table
	 * @param nnet network used to calc fitness
	 * @param table to hold the fitness scores
	 * @return table
	 */
	public tableRow[] calcRawFitness(NeuralNet nnet, tableRow[] table) {
		// Convert Genome into network
		for (int h = 0; h < table.length; h++) {
			int count = 0;

			// Retrieve Weights from genes
			for (int k = 0; k < nnet.weights.length; k++) {
				int y = nnet.weights[k].length;
				int x = nnet.weights[k][0].length;

				for (int i = 0; i < y; i++) {
					for (int j = 0; j < x; j++) {
						nnet.weights[k][i][j] = table[h].genome.getGenes().get(
								count).value;
						count++;
					}
				}
			}
			
			// Retrieve Bias Weights from genes
			for (int k = 0; k < nnet.bweights.length; k++) {
				int y = nnet.bweights[k].length;

				for (int i = 0; i < y; i++) {
					nnet.bweights[k][i]=table[h].genome.getGenes().get(
							count).value;
					
					count++;
				}
			}

			table[h].raw_fitness = fitnessFunc(nnet);


		}

		return table;
	}
	
	/**
	 * Loads the genome contained in the given table row into the given network
	 * @param nnet neural network
	 * @param table table containing genome
	 */
	public void exportGenome(NeuralNet nnet, tableRow table){
		int count = 0;

		// Retrieve Weights from genes
		for (int k = 0; k < nnet.weights.length; k++) {
			int y = nnet.weights[k].length;
			int x = nnet.weights[k][0].length;

			for (int i = 0; i < y; i++) {
				for (int j = 0; j < x; j++) {
					nnet.weights[k][i][j] = table.genome.getGenes().get(
							count).value;
					count++;
				}
			}
		}
		
		// Retrieve Bias Weights from genes
		for (int k = 0; k < nnet.bweights.length; k++) {
			int y = nnet.bweights[k].length;

			for (int i = 0; i < y; i++) {
				nnet.bweights[k][i]=table.genome.getGenes().get(
						count).value;
				
				count++;
			}
		}
	}

	
	/**
	 * error = absolute ( actual value - expected value)
	 * * @return error (1.0 = 100% error rating)
	 */
	public double fitnessFunc(SBPImpl nnet){
		NeuralNet nn = (NeuralNet)nnet;
		double error = 0;
		
		for (int i = 0; i < nn.inputs.length; i++) {
			nn.feedForward(nn.inputs,i);
			error+= Matrix.mtxSumDiff(nn.layersout[nn.layersout.length-1], nn.expected[i]);
		}
		return (error/(nn.expected.length));
	}
	
}
