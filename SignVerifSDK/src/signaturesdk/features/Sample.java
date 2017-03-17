package signaturesdk.features;

import java.util.LinkedList;

import signaturesdk.beans.PairList;
import signaturesdk.beans.SignWord;
import signaturesdk.features.utils.GMath;


/**
 * Class useful to sample signatures by a raw signature. It was designed to use
 * with ER2 and to compute directional hash, so it doesn't care about critical
 * points
 * 
 * @author Luciano Giuseppe May 2013
 */
public class Sample {

	public static class Method {
		public static final int STANDARD = 1;
		public static final int AVERAGE = 2;
		public static final int FORCE = 3;
	}

	private LinkedList<SignWord> signature1, signature2;

	public Sample() {
		signature1 = new LinkedList<SignWord>();
		signature2 = new LinkedList<SignWord>();
	}

	/**
	 * Sample a signature
	 * 
	 * @param signature
	 *            The signature to sample
	 * @param samplingMethod
	 *            one of NORMAL, AVERAGE or DOUBLE
	 * @param considerWordPart take care of part size or sample as one word
	 * @return The signature sampled
	 */
	public LinkedList<SignWord> sample(LinkedList<SignWord> signature,
			int samplingMethod, boolean considerWordPart) {
		LinkedList<Integer> parts = new LinkedList<Integer>();
		this.signature1.clear();
		
		for (SignWord wordToSample : signature) {

			//sample by union points

			//prepare
			int firstPoint = 0;
			LinkedList<Double> outX = new LinkedList<Double>();
			LinkedList<Double> outY = new LinkedList<Double>();
			LinkedList<Integer> unionPoints = new LinkedList<Integer>();
			
			//sample considering the word parts or not
			if(considerWordPart) {
				parts = wordToSample.getPartsSize();
			} else {
				parts = new LinkedList<Integer>();
				parts.add(wordToSample.getX().size());
			}
			
			//for each parts
			for(Integer partSize: parts) {
			
				// how many points I have to get sampling
				int pointsToSample = partSize - firstPoint; 
				pointsToSample = pointsToSample * samplingMethod;
				if (samplingMethod == Sample.Method.STANDARD)
					pointsToSample = pointsToSample + 5; // From tests: sampling
															// needs just few points more
				else if (samplingMethod == Sample.Method.AVERAGE) {
					pointsToSample = pointsToSample + (wordToSample.getPointsNum() / 2);
				} else {
					pointsToSample = pointsToSample * 2;
				}
				
				//part length
				double lenWord = GMath.polyLen(wordToSample.getX(), wordToSample.getY(), firstPoint, partSize);
				if(lenWord == 0) 
					continue;
				
				//sample!!
				PairList campionamentoSign = sampleWord(wordToSample, lenWord, pointsToSample, firstPoint, partSize);
				outX.addAll(campionamentoSign.getValues1());
				outY.addAll(campionamentoSign.getValues2());
				unionPoints.add(outX.size()); //save part size: number of points
				
				//for the next iteration
				firstPoint = partSize;
			}
			
			//save the word sampled
			signature1.add(new SignWord(outX, outY, null,null, unionPoints, false));
		}

		return this.signature1;
	}

	/**
	 * Sample two signatures
	 * 
	 * @param signature1
	 * @param signature2
	 */
	public void sample(LinkedList<SignWord> signature1,
			LinkedList<SignWord> signature2) {
		
		this.signature1.clear();
		this.signature2.clear();
		
		PairList campionamentoSign;
		double lenWord;

		int size = Math.min(signature1.size(), signature2.size());
		for (int wordsCounter = 0; wordsCounter < size; wordsCounter++) {
			SignWord w1 = signature1.get(wordsCounter);
			SignWord w2 = signature2.get(wordsCounter);
				
			/*
			 * Start sampling
			 */
				
			// how many points I have to sample
			int pointsToSample = Math.max(w1.getPointsNum(), w2.getPointsNum());

			// sample signature1
			lenWord = GMath.polyLen(w1.getX(), w1.getY());
			campionamentoSign = sampleWord(w1, lenWord, pointsToSample, 0, w1.getPointsNum());
			// save the computed points
			this.signature1.add(new SignWord(campionamentoSign.getValues1(), campionamentoSign.getValues2(), null, null, null, false) );
			
			// sample signature2
			lenWord = GMath.polyLen(w2.getX(), w2.getY());
			campionamentoSign = sampleWord(w2, lenWord, pointsToSample, 0, w2.getPointsNum());
			this.signature2.add(new SignWord(campionamentoSign.getValues1(), campionamentoSign.getValues2(), null, null, null, false) );
		
		}
	}

	/**
	 * Sample a (part) SignWord according to how many points are need
	 * 
	 * @param word
	 *            word to sample
	 * @param wordLen
	 *            length of the signWord
	 * @param totalPoints
	 * 			 Number of points to compute	
	 * @param firstPoint
	 *            (an index) where sample starts
	 * @param segmPoints
	 * 			  Points of the segment to sample
	 * 
	 * @return X and Y points computed
	 */
	private PairList sampleWord(SignWord word, double wordLen, int totalPoints, int firstPoint, int segmPoints) {
		
		LinkedList<Double> x = word.getX();
		LinkedList<Double> y = word.getY();

		if (x.size() == 0 || y.size() == 0 || (x.size() != y.size()) )
			return null;

		PairList retValue = new PairList();
		LinkedList<Double> outX = retValue.getValues1();
		LinkedList<Double> outY = retValue.getValues2();
		double segmLen = wordLen / (totalPoints - 1); // totalPoints - 1: with 3 points
														// we have 2 segment
		
		// insert the first point
		double oldX = x.get(firstPoint);
		double oldY = y.get(firstPoint);
		outX.add(oldX);
		outY.add(oldY);

		double currX = 0, currY = 0, dist = 0;
		// double computedLen = 0;
		double angle = 0, xComputed, yComputed;
		int c = firstPoint + 1; // points counter

		// for every points in a Word
		while (c < segmPoints) {
			boolean loop = true;
			double oldLen = 0, currLen = 0;

			// get a point at distance 'segmLen' from a point
			while (loop && c < segmPoints) {
				currX = x.get(c);
				currY = y.get(c);

				// find the segment where can be the point at distance segmLen
				dist = GMath.distance(oldX, oldY, currX, currY);
				currLen += dist;
				if (currLen >= segmLen) {
					loop = false; // I have found a possible segment:
									// (oldX,oldY)-(currX,currY)
					continue;
				}

				// computedLen += dist;

				oldLen = currLen;
				oldX = currX;
				oldY = currY;
				c++; // get the next point in original Word
			}

			// get the polar coordinates of point found
			double phi = segmLen - oldLen;
			angle = GMath.angle(oldX, oldY, currX, currY);

			// computedLen += phi;

			// get the Cartesian coordinates of the point found
			xComputed = oldX + (phi * Math.cos(angle));
			yComputed = oldY + (phi * Math.sin(angle));

			// to calculate the next point
			oldX = xComputed;
			oldY = yComputed;

			outX.add(xComputed);
			outY.add(yComputed);
		}

		// System.out.println("Len: "+computedLen + " - points:"+totalPoints);

		/*
		 * Is there a point more? Why? the division is not accurate, example in one dimension:
		 * I need 4 points and the word length is 1:
		 * segmLen is 1/3=0.333, so the algo compute 5 points: (first point) 0 - 0.33 - 0.66 - 0.99 - 1.0 (last point)
		 * So I remove the last point: 1.0
		 */
		if (outX.size() == (totalPoints + 1)) {
			outX.removeLast();
			outY.removeLast();
		}

		/*
		 * Set the last point as in original word
		 */
		outX.set(totalPoints - 1, x.get(segmPoints-1));
		outY.set(totalPoints - 1, y.get(segmPoints-1));

		return retValue;
	}

	/**
	 * Great common divisor (Euclidean algo)
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private int GCD(int a, int b) {
		int r;
		r = a % b;
		while (r != 0) {
			a = b;
			b = r;
			r = a % b;
		}
		return b;
	}

	/**
	 * Least common multiple
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private int lcm(int a, int b) {
		return ((a * b) / GCD(a, b));
	}

	/**
	 * Get the signature sampled
	 */
	public LinkedList<SignWord> getSignature() {
		return this.signature1;
	}
	
	/**
	 * Get the first signature sampled
	 */
	public LinkedList<SignWord> getSignature1() {
		return this.signature1;
	}

	/**
	 * Get the second signature sampled
	 */
	public LinkedList<SignWord> getSignature2() {
		return this.signature2;
	}

}
