package network;

import java.util.Arrays;
import java.util.Random;

import cube.RubiksCube;
import utility.Matrix;

/**
 * (Stochastic & Batch) Back Propagation
 * 
 * @author Ryan Paulitschke
 */
public class SBPropagation {

	public int epochs = 5;
	public int iters = 500;

	/**
	 * Stochastic Back Propagation
	 * 
	 * @param nnet
	 *            NeutralNetwork
	 * @param training_data
	 *            data to train on
	 */
	public void SBP(SBPImpl nnet, double[][] training_data) {
		int epoch = 0;
		boolean success = false;
		double error = 0;
		double lowest_error = 100000;

		while (epoch < epochs) {
			nnet.initWeights();
			int iterations = 0;
			while (iterations < iters) {

				Random rand = new Random();
				int num = rand.nextInt(training_data.length);

				nnet.feedForward(training_data, num);

				calcDeltas(nnet,num);
				calcWeightUpdates(nnet,num);

				updateWeights(nnet);

				iterations++;
			}

			error = calcError(nnet);
			System.out.println("#" + epoch + ": " + error * 100 + "% Error");

			if (error < lowest_error) {
				lowest_error = error;
				nnet.saveNN(true);
			}

			if (error < 0.001) {
				success = true;
				break;
			}

			epoch++;
		}

		if (success)
			System.out.println("SUCCESS! \n# of Epochs: " + (epoch + 1));
		else
			System.out.println("FAILURE, please train again");

		nnet.quickLoad();
		System.out
				.println("\nBest error rate: "
						+ lowest_error
						* 100
						+ "% Error\nEdge weights from the most effective epoch have been loaded and can be saved.\n");

	}
	
	/**
	 * Updates neural network weight values
	 */
	public void updateWeights(SBPImpl nnet){
		NeuralNet nn = (NeuralNet)nnet;
		for (int i=0;i<nn.weights.length;i++){
			if (nn.prevdsweights[i]==null)
				Matrix.mtxAdd(Matrix.mtxAdd(nn.weights[i],Matrix.scalarMulti(nn.weights[i],nn.weightdecay)),Matrix.scalarMulti(nn.dsweights[i],nn.learningrate));
			else
				Matrix.mtxAdd(Matrix.mtxAdd(Matrix.mtxAdd(nn.weights[i],Matrix.scalarMulti(nn.weights[i],nn.weightdecay)),Matrix.scalarMulti(nn.dsweights[i],nn.learningrate)),Matrix.scalarMulti(nn.prevdsweights[i],nn.momentum));
				
			//store previous weight change for momentum
			nn.prevdsweights[i]=nn.dsweights[i];
			
			Matrix.mtxAdd(nn.bweights[i],Matrix.scalarMulti(nn.dsweights[i][0],nn.learningrate*nn.momentum));
		}
	}
	
	
	//INDEX OF CURRENT LINE BEING TRAINED
	public double[][] calcDeltas(SBPImpl nnet,int index){
		NeuralNet nn = (NeuralNet)nnet;
		int len = nn.layersin.length-1;
		
		nn.deltas = new double[len+1][];
		nn.deltas[0] = Matrix.scalarMulti(nn.applySigmoidPrime(nn.layersin[len]),Matrix.mtxAddT(Matrix.scalarMulti(nn.layersout[len],-1), nn.expected[index]));
		
		for (int i=1;i<=len;i++){ 
			nn.deltas[i] = Matrix.scalarMulti(nn.applySigmoidPrime(nn.layersin[len-i]),Matrix.mtxMulti(nn.deltas[i-1], Matrix.transpose(nn.weights[len+1-i]))); 
		}
	
		
		
		return nn.deltas;
	}
	
	//INDEX OF CURRENT LINE BEING TRAINED
	public double[][][] calcWeightUpdates(SBPImpl nnet,int index){
		NeuralNet nn = (NeuralNet)nnet;
		int len = nn.weights.length-1;
			
		nn.dsweights = new double[nn.weights.length][][];
		
		for (int i=0;i<len;i++){
			nn.dsweights[len-i] = Matrix.mtxMulti(nn.layersout[len-(i+1)], nn.deltas[i]);
		}
		
		nn.dsweights[0] = Matrix.mtxMulti(nn.inputs[index],nn.deltas[nn.deltas.length-1]);
		
		if (nn.prevdsweights==null){
			nn.prevdsweights = new double[nn.weights.length][][];
			for (int i=0;i<nn.weights.length;i++){
				nn.prevdsweights[i]=nn.dsweights[i];
			}
		}
			
		return nn.dsweights;
	}
	
	/**
	 * error = absolute ( actual value - expected value)
	 * * @return error (1.0 = 100% error rating)
	 */
	public double calcError(SBPImpl nnet){
		NeuralNet nn = (NeuralNet)nnet;
		double error = 0;
		
		for (int i = 0; i < nn.inputs.length; i++) {
			nn.feedForward(nn.inputs,i);
			error+= Matrix.mtxSumDiff(nn.layersout[nn.layersout.length-1], nn.expected[i]);
			}
		return (error/(nn.expected.length));
	}

}
