package display;

import genetic.Gene;
import genetic.GeneticAlg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import network.NeuralNet;
import search.AStar;
import search.BreadthFirst;
import search.Searchable;
import display.Gfx;
import display.Window;
import cube.Controller;
import cube.RubiksCube;

/**
 * Incharge of initializing and running the RubixCube project.
 * @author Ryan Paulitschke
 */
public class Gui {
	public static RubiksCube rubiks, srubix; 
	public static Controller controller;
	
	public static void main(String[] args) throws InterruptedException, IOException {
		Gfx.preload();
				
		Window window = new Window();
		window.initCubeSize();

		javax.swing.SwingUtilities.invokeLater(window);

		// Sleep until a cube size has been selected
		while (!Window.finished) {
			Thread.sleep(10);
		}
		// create initial cube
		rubiks = new RubiksCube(window.CUBESIZE);
		rubiks.initSolvedState();
		// create "Solved" Cube
		srubix = new RubiksCube(window.CUBESIZE);
		srubix.initSolvedState();

		// Create user cube controls
		controller = window.createController(rubiks, window);
		window.frame.addKeyListener(controller);
		System.out.println("Max Memory: "
				+ java.lang.Runtime.getRuntime().maxMemory() / 1000000 + "MB");

		window.drawCube(rubiks);
		controller.refresh();


	}

}
