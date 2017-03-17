/**
 * Project: Signature Verification
 * @author Ajay R, Keshav Kumar HK and Sachin Sudheendra
 * @autor Luciano Giuseppe 03-2013
 */

package signaturesdk.verification;

import java.util.LinkedList;

import signaturesdk.beans.PairList;


/**
 * DTW with Sakoe-Chiba band
 * 
 */
public class DynamicTimeWarping {

	private double[][] DTWMatrix;
	//private int[][] wrapPath; 
	private int numRows, numCols;
	private int T, costSCB; // Sakoe-Chiba band attribs.
	private int numOptimalDistance;

	public DynamicTimeWarping() {
		// the default value
		numOptimalDistance = Integer.MAX_VALUE;
	}

	/**
	 * Perform the DTW on two dimensions vectors
	 * 
	 * @param signatureOriginal
	 *            original Signature
	 * @param signatureTest
	 *            signature to test
	 * @return the distance between vectors
	 */
	public double perform2D(PairList signatureOriginal, PairList signatureTest) {
		double[][] distanceMatrix;
		// original signature
		final LinkedList<Double> xOriginalSign = signatureOriginal.getValues1();
		final LinkedList<Double> yOriginalSign = signatureOriginal.getValues2();
		this.numRows = xOriginalSign.size();
		// signature to test
		final LinkedList<Double> xTestSign = signatureTest.getValues1();
		final LinkedList<Double> yTestSign = signatureTest.getValues2();
		this.numCols = xTestSign.size();

		// for warping paths having a local slope within the bounds 1/2 and 2
		if ((this.numCols >= 2 * this.numRows)
				|| (this.numRows >= 2 * this.numCols)) {
			return Double.POSITIVE_INFINITY;
		}

		// compute the distance matrix
		distanceMatrix = new double[this.numRows][this.numCols];
		for (int i = 0; i < this.numRows; i++) {
			for (int j = 0; j < this.numCols; j++) {

				// compute the euclidean distance
				double deltaX = xOriginalSign.get(i) - xTestSign.get(j);
				double deltaY = yOriginalSign.get(i) - yTestSign.get(j);
				distanceMatrix[i][j] = Math.sqrt((deltaX * deltaX)
						+ (deltaY * deltaY));
			}
		}

		return computeDTWMatrix(distanceMatrix);
	}
	
	/**
	 * Compute the DTW on vectors of one dimension
	 * 
	 * @param original
	 *            original sequence
	 * @param test
	 *            sequence to test
	 * @return distance between two sequence
	 */
	public double perform1D(LinkedList<Double> original, LinkedList<Double> test) {
		double[][] distanceMatrix;

		// original signature
		this.numRows = original.size();
		// signature to test
		this.numCols = test.size();

		// for warping paths having a local slope within the bounds 1/2 and 2
		if ((this.numCols >= 2 * this.numRows)
				|| (this.numRows >= 2 * this.numCols)) {
			return Double.POSITIVE_INFINITY;
		}

		// compute the distance matrix
		distanceMatrix = new double[this.numRows][this.numCols];
		for (int i = 0; i < this.numRows; i++) {
			for (int j = 0; j < this.numCols; j++) {

				// compute the euclidean distance
				distanceMatrix[i][j] = Math.abs(original.get(i) - test.get(j));
			}
		}

		return this.computeDTWMatrix(distanceMatrix);
	}
	
	private double computeDTWMatrix(double[][] distanceMatrix) {
		this.DTWMatrix = new double[this.numRows][this.numCols];
		this.DTWMatrix[0][0] = distanceMatrix[0][0];

		// fill the first row
		for (int i = 1; i < this.numRows; i++)
			this.DTWMatrix[i][0] = distanceMatrix[i][0]
					+ this.DTWMatrix[i - 1][0];

		// initialize the first column
		for (int i = 1; i < numCols; i++)
			this.DTWMatrix[0][i] = distanceMatrix[0][i]
					+ this.DTWMatrix[0][i - 1];

		// fill the others
		for (int i = 1; i < numRows; i++)
			for (int j = 1; j < numCols; j++)
				this.DTWMatrix[i][j] = distanceMatrix[i][j]
						+ Math.min(this.DTWMatrix[i - 1][j], //insetion
								Math.min(
								this.DTWMatrix[i - 1][j - 1], // match
								this.DTWMatrix[i][j - 1]) //deletion
							);

		return computePath();

		/*
		 * Inaccurate 
		 * return this.DTWMatrix[numRows-1][numCols-1];
		 */
	}

	/**
	 * Compute the warp path length and the warp path cost with Sakoe-Chiba band
	 * 
	 * @return warp path cost
	 */
	public double computePath() {
		// wrapPath = new int[4000][2]; //save the warp path

		// for Sakoe-Chiba band
		this.T = (int) this.numRows / 5;
		this.costSCB = (this.numCols - T) / (this.numRows - T);

		numOptimalDistance = 0;
		int i = this.numRows - 1;
		int j = this.numCols - 1;
		double pathCost = DTWMatrix[i][j];

		double minimumValue;
		while ((i > 0) || (j > 0)) {
			if (i == 0) {
				j--;
			} else if (j == 0) {
				i--;
			} else {
				// here you can change the step size
				minimumValue = this.min(i, j);
				if (minimumValue == DTWMatrix[i - 1][j])
					i--;
				else if (minimumValue == DTWMatrix[i][j - 1])
					j--;
				else {
					i--;
					j--;
				}
			} // end else

			pathCost += DTWMatrix[i][j];
			/*
			 * wrapPath[numOptimalDistance][0] = i;
			 * wrapPath[numOptimalDistance][1] = j;
			 */
			numOptimalDistance++;
		}

		return pathCost;
	}

	/**
	 * Get the min according to Sakoe-Chiba Band
	 * 
	 * @author Luciano Giuseppe
	 * @param i
	 *            index
	 * @param j
	 *            index
	 * @return the min value
	 */
	private double min(int i, int j) {
		boolean first = true, third = true; // second=true; -> [i-i][j-1] can't
											// be check
		int minBand, maxBand;

		// check Sakoe-Chiba band on [i-1][j]
		minBand = costSCB * (i - 1 - T);
		if (minBand < 0)
			minBand = 0; // can be < 0
		maxBand = costSCB * (i - 1 + T);
		if (maxBand > this.numCols - 1)
			maxBand = this.numCols - 1;
		if (j < minBand || j > maxBand)
			first = false;

		// check on [i][j-1]
		minBand = costSCB * (i - T);
		if (minBand < 0)
			minBand = 0; // can be < 0
		maxBand = costSCB * (i + T);
		if (maxBand > this.numCols - 1)
			maxBand = this.numCols - 1;
		if ((j - 1) < minBand || (j - 1) > maxBand)
			third = false;

		// now take the min value
		if (first && third) {
			return Math.min(DTWMatrix[i - 1][j],
					Math.min(DTWMatrix[i - 1][j - 1], DTWMatrix[i][j - 1]));
		} else if (first == false) {
			return Math.min(DTWMatrix[i - 1][j - 1], DTWMatrix[i][j - 1]);
		} else { // third == false
			return Math.min(DTWMatrix[i - 1][j - 1], DTWMatrix[i - 1][j]);
		}

	}

	/**
	 * Get the warp path length
	 * 
	 * @return Integer.MAX_VALUE if DTW hasn't success
	 */
	public double getPathLength() {
		return this.numOptimalDistance;
	}

}
