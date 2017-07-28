package network;

import java.util.Arrays;

import utility.Matrix;

/**
 * Classifier
 * 
 * @author Ryan Paulitschke
 */
public class Classifier {

	/**
	 * Classifies the network input data at the given index
	 */
	public void classify(NeuralNet nn,int index){
		if (index<nn.expected.length){
		nn.feedForward(nn.inputs,index);
		System.out.println("Calculated "+Arrays.toString(nn.layersout[nn.layersout.length-1])+" : "+Arrays.toString(nn.expected[index])+" Actual");
		System.out.println(Matrix.mtxSumDiff(nn.layersout[nn.layersout.length-1], nn.expected[index]) * 100 + "% Error\n");
		}else{
			System.out.println("Number must be between [0, "+(nn.expected.length-1)+"] for this dataset");
		}
	}
}
