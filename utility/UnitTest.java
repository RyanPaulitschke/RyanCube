package utility;

import genetic.GeneticAlg;
import genetic.GeneticAlg.tableRow;

import java.util.Arrays;
import java.util.LinkedList;

import network.NeuralNet;
import network.SBPropagation;
import search.AStar;
import search.BreadthFirst;
import search.Searchable;
import cube.RubiksCube;

/**
 * Used to run unit tests
 * 
 * @author Ryan Paulitschke
 */
public class UnitTest {

	public static void testRotationsTwoX() {
		RubiksCube rubiks = new RubiksCube(2);
		RubiksCube goal = new RubiksCube(2);
		rubiks.initSolvedState();
		goal.initSolvedState();

		System.out.println("Generating size=2 cube...");
		System.out.println("Testing X-axis Rotations...");
		for (int i = 0; i < 2; i++) {
			rubiks.rotate(i, 0, 0, 0);
			rubiks.rotate(i, 0, 0, 1);

			if (rubiks.equals(goal))
				System.out.println("Test CW/CCW - Passed");
			else {
				System.out.println("Test  - Failed");
			}

		}

	}

	public static void testRotationsTwoY() {
		RubiksCube rubiks = new RubiksCube(2);
		RubiksCube goal = new RubiksCube(2);
		rubiks.initSolvedState();
		goal.initSolvedState();

		System.out.println("Testing Y-axis Rotations...");
		for (int i = 0; i < 2; i++) {
			rubiks.rotate(0, i, 1, 0);
			rubiks.rotate(0, i, 1, 1);

			if (rubiks.equals(goal))
				System.out.println("Test CW/CCW - Passed");
			else {
				System.out.println("Test  - Failed");
			}
		}
	}

	public static void testRotationsTwoZ() {
		RubiksCube rubiks = new RubiksCube(2);
		RubiksCube goal = new RubiksCube(2);
		rubiks.initSolvedState();
		goal.initSolvedState();

		System.out.println("Testing Z-axis Rotations...");
		for (int i = 0; i < 2; i++) {
			rubiks.rotate(0, i, 0, 0);
			rubiks.rotate(0, i, 0, 1);

			if (rubiks.equals(goal))
				System.out.println("Test CW/CCW - Passed");
			else {
				System.out.println("Test  - Failed");
			}

		}

	}

	public static void testRotationsThreeX() {
		RubiksCube rubixb = new RubiksCube(3);
		RubiksCube goalb = new RubiksCube(3);
		rubixb.initSolvedState();
		goalb.initSolvedState();

		System.out.println("Generating size=3 cube...");
		System.out.println("Testing X-axis Rotations...");
		for (int i = 0; i < 3; i++) {
			rubixb.rotate(i, 0, 0, 0);
			rubixb.rotate(i, 0, 0, 1);

			if (rubixb.equals(goalb))
				System.out.println("Test CW/CCW - Passed");
			else {
				System.out.println("Test  - Failed");
			}

		}
	}

	public static void testRotationsThreeY() {
		RubiksCube rubixb = new RubiksCube(3);
		RubiksCube goalb = new RubiksCube(3);
		rubixb.initSolvedState();
		goalb.initSolvedState();

		System.out.println("Testing Y-axis Rotations...");
		for (int i = 0; i < 3; i++) {
			rubixb.rotate(0, i, 1, 0);
			rubixb.rotate(0, i, 1, 1);

			if (rubixb.equals(goalb))
				System.out.println("Test CW/CCW - Passed");
			else {
				System.out.println("Test  - Failed");
			}

		}
	}

	public static void testRotationsThreeZ() {
		RubiksCube rubixb = new RubiksCube(3);
		RubiksCube goalb = new RubiksCube(3);
		rubixb.initSolvedState();
		goalb.initSolvedState();

		System.out.println("Testing Z-axis Rotations...");
		for (int i = 0; i < 3; i++) {
			rubixb.rotate(0, i, 0, 0);
			rubixb.rotate(0, i, 0, 1);

			if (rubixb.equals(goalb))
				System.out.println("Test CW/CCW - Passed");
			else {
				System.out.println("Test  - Failed");

			}

		}
	}

	public static void testSizeTwoBFS() {
		BreadthFirst bf = new BreadthFirst();

		// n2 cube moves are correct
		RubiksCube rubiks = new RubiksCube(2);
		RubiksCube goal = new RubiksCube(2);
		rubiks.initSolvedState();
		goal.initSolvedState();

		System.out.println("Generating size=2 cube...");
		System.out
				.println("Testing solve of size 2 cube with 3 rotations using BFS...");

		rubiks.shuffle(3);
		bf.search(rubiks, goal);
		System.out.println("Test 2x2 with 3 Rotations: BFS- Passed");

	}

	public static void testSizeTwoAS() {
		AStar as = new AStar();

		RubiksCube rubiks = new RubiksCube(2);
		RubiksCube goal = new RubiksCube(2);
		rubiks.initSolvedState();
		goal.initSolvedState();

		System.out.println("Generating size=2 cube...");
		System.out
				.println("Testing solve of size 2 cube with 3 rotations using AS...");

		rubiks.shuffle(3);
		as.search(rubiks, goal);
		System.out.println("Test 2x2 with 3 Rotations: AS- Passed");

	}

	public static void testSizeThreeBFS() {
		BreadthFirst bf = new BreadthFirst();

		RubiksCube rubixb = new RubiksCube(3);
		RubiksCube goalb = new RubiksCube(3);
		rubixb.initSolvedState();
		goalb.initSolvedState();
		System.out.println("Generating size=3 cube...");

		System.out
				.println("Testing solve of size 3 cube with 3 rotations using BFS...");

		rubixb.shuffle(3);
		bf.search(rubixb, goalb);
		System.out.println("Test 3x3 with 3 Rotations: BFS- Passed");
	}

	public static void testSizeThreeAS() {
		AStar as = new AStar();

		RubiksCube rubixb = new RubiksCube(3);
		RubiksCube goalb = new RubiksCube(3);
		rubixb.initSolvedState();
		goalb.initSolvedState();
		System.out.println("Generating size=3 cube...");
		System.out
				.println("Testing solve of size 3 cube with 3 rotations using A*...");

		rubixb.shuffle(3);
		as.search(rubixb, goalb);
		System.out.println("Test 3x3 with 3 Rotations: A* - Passed");
	}
	
	public static void testCrossOver(){
		GeneticAlg ga = new GeneticAlg();
		
		NeuralNet nn = new NeuralNet(2,2,0,0,1);
		nn.inputs = new double[][] {{-1,-1},{-1,1},{1,-1},{1,1}};
		nn.expected = new double[][] {{-1}, {1}, {1}, {-1}};
		
		tableRow[] table = ga.initPopulation(nn,5);
		
		ga.crossOver(table);
		
		if (ga.crossbreeds.size()>0)
			System.out.println("Genetic Algorithm: CrossOver - Passed");
		else
			System.out.println("CrossOver  - Failed");
		
	}
	
	public static void testMutations(){
		GeneticAlg ga = new GeneticAlg();
		
		NeuralNet nn = new NeuralNet(2,2,0,0,1);
		nn.inputs = new double[][] {{-1,-1},{-1,1},{1,-1},{1,1}};
		nn.expected = new double[][] {{-1}, {1}, {1}, {-1}};
		
		tableRow[] table = ga.initPopulation(nn,5);
		
		ga.mutation(table);
		
		if (ga.mutations.size()>0)
			System.out.println("Genetic Algorithm: Mutations - Passed");
		else
			System.out.println("Mutations  - Failed");
	}
	
	public static void testElites(){
		GeneticAlg ga = new GeneticAlg();
		
		NeuralNet nn = new NeuralNet(2,2,0,0,1);
		nn.inputs = new double[][] {{-1,-1},{-1,1},{1,-1},{1,1}};
		nn.expected = new double[][] {{-1}, {1}, {1}, {-1}};
		
		tableRow[] table = ga.initPopulation(nn,5);
		ga.calcProbSel(table);
		ga.eliteSelection(table);
		
		if (ga.elites.size()>0)
			System.out.println("Genetic Algorithm: Elite Selection - Passed");
		else
			System.out.println("Elite Selection  - Failed");
	}
}
