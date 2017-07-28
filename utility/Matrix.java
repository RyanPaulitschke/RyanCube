package utility;

import java.util.Arrays;

/**
 * Pseudo-Matrix math library
 * Heavily leverages method overloading to account 
 * for different variable types
 * 
 * @author Ryan Paulitschke
 */
public class Matrix {
	
	/**
	 * Transposes a 2D matrix
	 * @param mtx matrix
	 * @return a transposed matrix
	 */
	public static double[][] transpose(double[][] mtx) {
		int x = mtx.length;
		int y = mtx[0].length;
		double[][] trans = new double[y][x];

			for (int i = 0; i < y; i++) {
				for (int j = 0; j < x; j++) {
					trans[i][j] = mtx[j][i];
				}
			}
			
		return trans;

	}

	/**
	 * Multiplies a matrix by a vector
	 * @param mtx 2D matrix
	 * @param vector 1D matrix
	 * @return 1D matrix
	 */
	public static double[] mtxMulti(double[][] mtx, double[] vector) {

		double[] ans = new double[mtx.length];
		double sum;

		for (int i = 0; i < mtx.length; i++) {
			sum = 0;
			for (int j = 0; j < mtx[0].length; j++) {
				sum += mtx[i][j] * vector[j];
			}
			ans[i] = sum;
		}
		return ans;
	}
	
	/**
	 * Multiplies a vector by a vector
	 * @param vector 1D matrix
	 * @param vector_b 1D matrix
	 * @return 2D matrix
	 */
	public static double[][] mtxMulti(double[] vector, double[] vector_b) {

		double[][] ans = new double[vector.length][vector_b.length];
		
		for (int j = 0; j < vector_b.length; j++) {
			for (int i = 0; i < vector.length; i++) {
				ans[i][j] = vector[i] * vector_b[j];
			}
			
		}
		return ans;
	}

	/**
	 * Multiplies a vector by a matrix
	 * @param vector 1D matrix
	 * @param mtx 2D matrix
	 * @return 1D matrix
	 */
	public static double[] mtxMulti(double[] vector, double[][] mtx) {

		double[] ans;
		double sum;

		if (mtx.length>mtx[0].length && vector.length==1){
			ans = new double[mtx.length];
			for (int j = 0; j < mtx.length; j++) 
				ans[j]= mtx[j][0] * vector[0];
		}else{
			ans = new double[mtx[0].length];
		for (int j = 0; j < mtx[0].length; j++) {
			sum = 0;
			for (int i = 0; i < vector.length; i++) {
				sum += vector[i]*mtx[i][j];
			}
			ans[j] = sum;
		}
	}
	
		return ans;
	}
	
	/**
	 * Multiplies a matrix by a matrix
	 * @param mtx 2D matrix
	 * @param mtx_b 2D matrix
	 * @return 2D matrix
	 */
	public static double[][] mtxMulti(double[][] mtx, double[][] mtx_b) {

		double[][] ans = new double[mtx.length][mtx_b[0].length];
		double sum;

		for (int i = 0; i < mtx.length; i++) {
			for (int j = 0; j < mtx_b[0].length; j++) {
				sum = 0;
				for (int k = 0; k < mtx[0].length; k++) {
					sum += mtx[i][k] * mtx_b[k][j];
				}
				ans[i][j] = sum;
			}

		}
		return ans;
	}

	
	/**
	 * Scalar multiplies a matrix by a matrix
	 * @param mtx 2D matrix
	 * @param mtx_b 2D matrix
	 * @return 2D matrix
	 */
	public static double[][] scalarMulti(double[][] mtx, double[][] mtx_b) {
		double[][] ans = new double[mtx.length][mtx[0].length];

		for (int i = 0; i < mtx.length; i++) {
			for (int j = 0; j < mtx[0].length; j++) {
				ans[i][j] = mtx[i][j] * mtx_b[i][j];
			}
		}

		return ans;

	}
	
	/**
	 * Scalar multiplies 2 vectors
	 * @param vector 1D matrix
	 * @param vector_b 1D matrix
	 * @return 1D matrix
	 */
	public static double[] scalarMulti(double[] vector, double[] vector_b) {
		double[] ans = new double[vector_b.length];

		if (vector.length==1){
			for (int i = 0; i < vector_b.length; i++) {
				ans[i] = vector[0] * vector_b[i];
				}
		}else{
		for (int i = 0; i < vector_b.length; i++) {
			ans[i] = vector[i] * vector_b[i];
			}
		}
		
		return ans;

	}
	
	/**
	 * Scalar multiplies a vector by a single value
	 * @param vector 1D matrix
	 * @param scalar single value
	 * @return 1D matrix
	 */
	public static double[] scalarMulti(double[] vector, double scalar) {
		double[] ans = new double[vector.length];

		for (int i = 0; i < vector.length; i++) {
			ans[i] = vector[i] * scalar;
		}

		return ans;

	}
	
	/**
	 * Scalar multiplies a matrix by a single value
	 * @param mtx 2D matrix
	 * @param scalar single value
	 * @return 2D matrix
	 */
	public static double[][] scalarMulti(double[][] mtx, double scalar) {
		double[][] ans = new double[mtx.length][mtx[0].length];

		for (int i = 0; i < mtx.length; i++) {
			for (int j = 0; j < mtx[0].length; j++) {
				ans[i][j] = mtx[i][j] * scalar;
			}
		}

		return ans;

	}

	/**
	 * Adds a single value to each cell in a matrix
	 * @param mtx 1D matrix
	 * @param value single value
	 * @return 1D matrix
	 */
	public static double[] mtxAdd(double[] mtx, double value) {
		for (int i = 0; i < mtx.length; i++) {
			mtx[i] += value;
		}
		return mtx;
	}
	
	/**
	 * Sum of absolute differences
	 * @param x
	 * @param y
	 * @return
	 */
	public static double mtxSumDiff(double[] x, double[] y){
		double ans=0;
		
		for (int i = 0; i < x.length; i++) {
			ans += Math.abs(x[i]-y[i]);
		}
		
		return ans;
		
	}
	
	/**
	 * Adds 2 vectors together
	 * @param vector 1D matrix
	 * @param vector_b 1D matrix
	 * @return 1D matrix
	 */
	public static double[] mtxAdd(double[] vector, double[] vector_b) {
		for (int i = 0; i < vector.length; i++) {
			vector[i] += vector_b[i];
		}
		return vector;
	}

	/**
	 * Result of adding 2 vectors together
	 * @param vector 1D matrix
	 * @param vector_b 1D matrix
	 * @return 1D matrix
	 */
	public static double[] mtxAddT(double[] vector, double[] vector_b) {
		double[] nvector = new double[vector.length];
		for (int i = 0; i < vector.length; i++) {
			nvector[i] = vector[i] + vector_b[i];
		}
		return nvector;
	}
	
	/**
	 * Adds a single value to each cell in a matrix
	 * @param mtx 2D matrix
	 * @param value single value
	 * @return 2D matrix
	 */
	public static double[][] mtxAdd(double[][] mtx, double value) {
		for (int i = 0; i < mtx.length; i++) {
			for (int j = 0; j < mtx[0].length; j++) {
				mtx[i][j] += value;
			}
		}
		return mtx;
	}
	
	/**
	 * Adds 2 matrices together
	 * @param mtx 2D matrix
	 * @param mtx_b 2D matrix
	 * @return 2D matrix
	 */
	public static double[][] mtxAdd(double[][] mtx, double[][] mtx_b) {
		for (int i = 0; i < mtx.length; i++) {
			for (int j = 0; j < mtx[0].length; j++) {
				mtx[i][j] += mtx_b[i][j];
			}
		}
		return mtx;
	}
	
	/**
	 * Result of Adding 2 matrices together
	 * @param mtx 2D matrix
	 * @param mtx_b 2D matrix
	 * @return 2D matrix
	 */
	public static double[][] mtxAddT(double[][] mtx, double[][] mtx_b) {
		double[][] nmtx = new double[mtx.length][mtx[0].length];
		
		for (int i = 0; i < mtx.length; i++) {
			for (int j = 0; j < mtx[0].length; j++) {
				nmtx[i][j] = mtx[i][j] + mtx_b[i][j];
			}
		}
		return nmtx;
	}
	
}
