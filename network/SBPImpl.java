package network;

/**
 * Back Propagation Interface
 * 
 * @author Ryan Paulitschke
 */
public interface SBPImpl {
	public void initWeights();
	public void feedForward(double[][] input, int index);
	public void saveNN(Boolean quicksave);
	public void quickLoad();
}
