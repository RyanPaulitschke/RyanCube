package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import cube.RubiksCube;
import search.Searchable;
import utility.Matrix;

/**
 * Neural Network
 * 
 * @author Ryan Paulitschke
 */
public class NeuralNet implements SBPImpl {
	
	public int inlsize, hiddenlsize, hlsb, hlsc, outlsize;
	public double bias, learningrate, momentum, weightdecay;
	public double[][] layersin, layersout, deltas, bweights, inputs, expected;
	public double[][][] weights, dsweights,prevdsweights;
	
	public NeuralNet(int inputlayersize, int hiddenlayersize, int hlsb, int hlsc, int outputlayersize) {
		this.inlsize = inputlayersize;
		this.hiddenlsize = hiddenlayersize;
		this.hlsb = hlsb;
		this.hlsc = hlsc;
		this.outlsize = outputlayersize;
		this.bias = 2.0; //Usually more effective than 1.0
		this.learningrate = 0.1;
		this.momentum = 0.01;
		this.weightdecay = 0.0;
		initWeights();
	}
	

	/**
	 * Initializes or resets network weight values
	 */
	public void initWeights() {
		

		int numlayers;
		int[] sizes;
	
		if (hlsb==0){
			numlayers=2;
			hlsc=0;
			sizes = new int[] {inlsize, hiddenlsize, outlsize};
		}
		else if (hlsc==0){
			numlayers = 3;
			sizes = new int[] {inlsize, hiddenlsize, hlsb, outlsize};
		}
		else{
			numlayers = 4;
			sizes = new int[] {inlsize, hiddenlsize, hlsb, hlsc, outlsize};
		}
		
		bweights = new double[numlayers][];
		
		//Initialize bias weights
		for (int i=0;i<numlayers;i++){
			double[] w = new double[sizes[i+1]];
			for (int j = 0; j < sizes[i+1]; j++) {
				w[j] = ThreadLocalRandom.current().nextDouble(-1, 1);
			}
			bweights[i] = w;
		}
		
		weights = new double[numlayers][][];
		
		int numedges = inlsize * hiddenlsize + hiddenlsize * outlsize;
		double sqrd = Math.sqrt(numedges);//numedges * numedges;
		
		//Initialize edge weights
		for (int k=0;k<numlayers;k++){
			// Edges connecting the layers
			weights[k] = new double[sizes[k]][sizes[k+1]];
			for (int i = 0; i < sizes[k]; i++) {
				for (int j = 0; j < sizes[k+1]; j++) {
					weights[k][i][j] = ThreadLocalRandom.current().nextDouble(-1 / sqrd, 1 / sqrd);
				}
			}
		}
	}
	
	
	/**
	 * Network Feed Forward
	 */
	public void feedForward(double[][] input, int index) {
		this.inputs = input;

		layersin = new double[weights.length][];
		layersout = new double[weights.length][];
		
		layersin[0] = Matrix.mtxMulti(inputs[index], weights[0]);
		Matrix.mtxAdd(layersin[0], Matrix.scalarMulti(bweights[0],bias));
		layersout[0] = applySigmoid(layersin[0]);

		for(int i=1;i<weights.length;i++){
			layersin[i] = Matrix.mtxMulti(layersout[i-1], weights[i]);
			Matrix.mtxAdd(layersin[i], Matrix.scalarMulti(bweights[i],bias));
			layersout[i] = applySigmoid(layersin[i]);
		}
	}

	/**
	 * Activation function
	 * @param netj
	 * @return value
	 */
	public double sigmoid(double netj) {
		double a =1.716;
		double b =0.667;

		return a * Math.tanh(b * netj);
	}

	/**
	 * Derivative of the activation function
	 * @param input
	 * @return value
	 */
	public double sigmoidPrime(double input) {
		double a = 1.716;
		double b = 0.667;

		return a * b / Math.pow(Math.cosh(b * input), 2);
	}

	/**
	 * Applies the derivative of the activation function to a vector
	 * @param vector 1D array
	 * @return 1D array
	 */
	public double[] applySigmoidPrime(double[] vector) {
		double[] mtxNew = new double[vector.length];

		for (int i = 0; i < vector.length; i++) {
			mtxNew[i] = sigmoidPrime(vector[i]);
		}
		return mtxNew;
	}

	/**
	 * Applies the derivative of the activation function to a matrix
	 * @param mtx 2D matrix
	 * @return 2D matrix
	 */
	public double[][] applySigmoidPrime(double[][] mtx) {
		double[][] mtxNew = new double[mtx.length][mtx[0].length];

		for (int i = 0; i < mtx.length; i++) {
			for (int j = 0; j < mtx[0].length; j++) {
				mtxNew[i][j] = sigmoidPrime(mtx[i][j]);
			}
		}
		return mtxNew;
	}
	
	
	/**
	 * Applies the activation function to a vector
	 * @param vector 1D array
	 * @return 1D array
	 */
	public double[] applySigmoid(double[] vector) {
		double[] mtxNew = new double[vector.length];

		for (int i = 0; i < vector.length; i++) {
			mtxNew[i] = sigmoid(vector[i]);
		}
		return mtxNew;
	}

	/**
	 * Applies the activation function to a matrix
	 * @param mtx 2D matrix
	 * @return 2D matrix
	 */
	public double[][] applySigmoid(double[][] mtx) {
		double[][] mtxNew = new double[mtx.length][mtx[0].length];

		for (int i = 0; i < mtx.length; i++) {
			for (int j = 0; j < mtx[0].length; j++) {
				mtxNew[i][j] = sigmoid(mtx[i][j]);
			}
		}
		return mtxNew;
	}
	
	/**
	 * Saves a neural network
	 */
	public void saveNN(Boolean quicksave) {
		
		if (quicksave){
			try {
				save(new BufferedWriter(new FileWriter("quicksave.txt")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else{
			JFrame frame = new JFrame();

			JFileChooser chooser = new JFileChooser();

			if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
				File selected = chooser.getSelectedFile();
				System.out.println("Saved data to: " + selected.getAbsolutePath());
				try (BufferedWriter writer = Files.newBufferedWriter(Paths
						.get(selected.getAbsolutePath()))) {
						
					save(writer);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Save cancelled");
			}

		}



	}

	
	/**
	 * File writing for network saving
	 * @param writer
	 * @throws IOException
	 */
	public void save(BufferedWriter writer) throws IOException{
		//Save sizes & rates 
		writer.write(""+inlsize+"\n"+hiddenlsize+"\n"+hlsb+"\n"+hlsc+"\n"+outlsize+"\n");
		writer.write(""+bias+"\n"+learningrate+"\n"+momentum+"\n"+weightdecay+"\n");
		
		//Save Weights
		int num = weights.length;
		writer.write(num+"\n");
		for (int k = 0; k < num; k++) {
			int y = weights[k].length;
			int x = weights[k][0].length;
			writer.write(x+"\n"+y+"\n");
			
			for (int i = 0; i < y; i++) {
				for (int j = 0; j < x; j++) {
					writer.write(weights[k][i][j]+",");
				}
			}
			writer.write("\n");
		}
		
		//Save bias weights
		num = bweights.length;
		writer.write(num+"\n");
			for (int i = 0; i < num; i++) {
				writer.write(bweights[i].length+"\n");
				
				for (int j=0;j<bweights[i].length;j++){
					writer.write(bweights[i][j]+",");
				}
				writer.write("\n");
			}
			writer.close();
	}
	
	
	/**
	 * Loads previously quick saved network.
	 */
	public void quickLoad(){
		
		
		try (BufferedReader reader = new BufferedReader(new FileReader("quicksave.txt"))) {
				
			inlsize = Integer.parseInt(reader.readLine());
			hiddenlsize = Integer.parseInt(reader.readLine());
			hlsb = Integer.parseInt(reader.readLine());
			hlsc = Integer.parseInt(reader.readLine());
			outlsize = Integer.parseInt(reader.readLine());
			bias = Double.parseDouble(reader.readLine());
			learningrate = Double.parseDouble(reader.readLine());
			momentum = Double.parseDouble(reader.readLine());
			weightdecay = Double.parseDouble(reader.readLine());
			
			
			//Load Weights 1
			int num = Integer.parseInt(reader.readLine());
			weights = new double[num][][];
			
			for (int h=0;h<num;h++){
			int x = Integer.parseInt(reader.readLine());
			int y = Integer.parseInt(reader.readLine());
			
			double[][] new_array = new double[y][x];
			
			String arr = reader.readLine();
			arr = arr.replaceAll("\\s+","");
			String[] cols = arr.split(",");

			int k=0;
			
			for (int i=0;i<y;i++){
				for (int j=0;j<x;j++){
					new_array[i][j] = Double.parseDouble(cols[k]);
					k++;
				}
			}
			
			weights[h] = new_array;
			System.out.println("Weight "+h+": "+Arrays.deepToString(weights[h])+"...LOADED");
			}

			//Load Bias Weights
			int x = Integer.parseInt(reader.readLine());
			bweights = new double[x][];
			
			for (int j=0;j<x;j++){
			int y = Integer.parseInt(reader.readLine());
			double[] bweight = new double[y];	
			
			String arr = reader.readLine();
			arr = arr.replaceAll("\\s+","");
			String[] vector = arr.split(",");
			int k=0;
			
			for (int i=0;i<y;i++){
				bweight[i] = Double.parseDouble(vector[k]);
				k++;
			}
			
			bweights[j] = bweight;
			}
			
	
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	
	
	
	/**
	 * Loads a neural network
	 */
	public void loadNN() {
		JFrame frame = new JFrame();
		JFileChooser chooser = new JFileChooser();
	
		if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
			File selected = chooser.getSelectedFile();
			System.out.println("Loaded file: " + selected.getAbsolutePath());
			try (BufferedReader reader = Files.newBufferedReader(Paths
					.get(selected.getAbsolutePath()))) {
					
				prevdsweights=null;
				inlsize = Integer.parseInt(reader.readLine());
				hiddenlsize = Integer.parseInt(reader.readLine());
				hlsb = Integer.parseInt(reader.readLine());
				hlsc = Integer.parseInt(reader.readLine());
				outlsize = Integer.parseInt(reader.readLine());
				System.out.println("Network dimensions...LOADED");
				bias = Double.parseDouble(reader.readLine());
				learningrate = Double.parseDouble(reader.readLine());
				momentum = Double.parseDouble(reader.readLine());
				weightdecay = Double.parseDouble(reader.readLine());
				System.out.println("Bias+weight & learning rate...LOADED");
				
				
				//Load Weights 1
				int num = Integer.parseInt(reader.readLine());
				weights = new double[num][][];
				
				for (int h=0;h<num;h++){
				int x = Integer.parseInt(reader.readLine());
				int y = Integer.parseInt(reader.readLine());
				
				double[][] new_array = new double[y][x];
				
				String arr = reader.readLine();
				arr = arr.replaceAll("\\s+","");
				String[] cols = arr.split(",");

				int k=0;
				
				for (int i=0;i<y;i++){
					for (int j=0;j<x;j++){
						new_array[i][j] = Double.parseDouble(cols[k]);
						k++;
					}
				}
				
				weights[h] = new_array;
				System.out.println("Weight "+h+": "+Arrays.deepToString(weights[h])+"...LOADED");
				}
				
				
				//Load Bias Weights
				int x = Integer.parseInt(reader.readLine());
				bweights = new double[x][];
				
				for (int j=0;j<x;j++){
				int y = Integer.parseInt(reader.readLine());
				double[] bweight = new double[y];	
				
				String arr = reader.readLine();
				arr = arr.replaceAll("\\s+","");
				String[] vector = arr.split(",");
				int k=0;
				
				for (int i=0;i<y;i++){
					bweight[i] = Double.parseDouble(vector[k]);
					k++;
				}
				
				bweights[j] = bweight;
				System.out.println("Bias Weights "+j+": "+Arrays.toString(bweights[j])+"...LOADED");
				}
				
		
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Loading cancelled");
		}

	}
		/**
		 * Loads a Training Data
		 */
		public void importData() {
			JFrame frame = new JFrame();
			JFileChooser chooser = new JFileChooser();
		
			if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
				File selected = chooser.getSelectedFile();
				System.out.println("Loaded file: " + selected.getAbsolutePath());
				try (BufferedReader reader = Files.newBufferedReader(Paths
						.get(selected.getAbsolutePath()))) {
						
					String str;

					List<String> new_inputs = new ArrayList<String>();
					List<String> new_expected = new ArrayList<String>();
					
					while((str = reader.readLine()) != null){
						String[] arr = str.split("\\|");
						new_inputs.add(arr[0]);
						new_expected.add(arr[1]);
					}
					
					String[] str_in = new_inputs.toArray(new String[0]);
					String[] str_exp = new_expected.toArray(new String[0]);
					
					inputs = new double[str_in.length][];
					expected = new double[str_exp.length][];
					
					
					for(int i=0;i<str_in.length;i++){
					String[] parts = str_in[i].split(",");
					double[] temp_inputs = new double[parts.length];
					
					int ind=0;
					for(String var: parts){
						temp_inputs[ind]= Double.parseDouble(var);
						ind++;
					}
					inputs[i]=temp_inputs;
					
					String[] partsb = str_exp[i].split(",");
					double[] temp_expected = new double[partsb.length];
					
					ind=0;
					for(String var: partsb){
						temp_expected[ind]= Double.parseDouble(var);
						ind++;
					}
					expected[i]=temp_expected;
					
					}
					System.out.println("\nSuccessfully Imported:\nInputs :"+Arrays.deepToString(inputs));
					System.out.println("Expected: "+Arrays.deepToString(expected));
					
			
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Loading cancelled");
			}

			System.out.println("Length of inputs detected: "+inputs[0].length+"\nLength of outputs: "+expected[0].length);

	}


}
