/**
 * Project: Signature Verification
 * @author Ajay R, Keshav Kumar HK and Sachin Sudheendra
 * @author Luciano Giuseppe March 2013
 */

package signaturesdk.features;

import java.util.LinkedList;

import signaturesdk.beans.AcquisitionSignWord;
import signaturesdk.beans.SignWord;


/**
 * Normalize (size or coordinates) and rotate a signature
 * @author Luciano Giuseppe
 *
 */
public class Normalize {
		
	private double minX, maxX, meanX;
	private double minY, maxY, meanY;
	private LinkedList<AcquisitionSignWord> words;
	
	/**
	 * Constructor
	 * @param signature the signature to normalize
	 */
	public Normalize(LinkedList<AcquisitionSignWord> signature) {
		this.words = signature;
	}

	/**
	 * Normalize coords and apply rotation to every words in signature.
	 * Also merge continuous words
	 * @param sign
	 * @return
	 */
	public LinkedList<SignWord> coords() {
		LinkedList<SignWord> normWords = merger(words);
		
		for(SignWord w : normWords) {
			coords(w);
		}
		
		return normWords;
	}


	/**
	 * Normalize the signature coordinates and apply rotation
	 */
	private void coords(SignWord signatureWord) {
		LinkedList<Double> yList = signatureWord.getY();
		LinkedList<Double> xList = signatureWord.getX();
		
		importMMM(signatureWord);
		
		applyRotation(yList, xList);
		
		double newX, newY, newMinX = 0, newMinY = 0, newMaxX = 0, newMaxY = 0, sumX= 0, sumY=0;
		double deltaX = Math.abs(maxX - minX);
		double deltaY = Math.abs(maxY - minY);
		for (int i = 0; i < yList.size(); i++) {
			
			newX = ((xList.get(i) - minX) / deltaX);
			xList.set(i, newX);
			
			
			newY = ((yList.get(i) - minY) / deltaY);
			yList.set(i, newY);
			
			//to compute values to set into words
			sumX += newX;
			sumY += newY;
			if(i == 0) {
				newMaxX = newMinX = newX;
				newMaxY = newMinY = newY;
			} else {
				if(newX < newMinX) newMinX = newX;
				if(newX > newMaxX) newMaxX = newX;
				
				if(newY < newMinY) newMinY = newY;
				if(newY > newMaxY) newMaxY = newY;
			}
		}
		
		minX = newMinX;
		maxX = newMaxX;
		minY = newMinY;
		maxY = newMaxY;
		meanX = sumX/yList.size();
		meanY = sumY/yList.size();
		
		//set new Min, Max and Mean values in the SignWord
		setWordMMM(signatureWord);
		
		return;
	}

	/**
	 * Normalize signature size and apply rotation to every words.
	 * Also merge continuous words
	 * 
	 * @author Luciano Giuseppe
	 * @return A List of SignWord normalized and rotated
	 */
	public LinkedList<SignWord> size() {
		LinkedList<SignWord> normWords =  merger(words);
		
		for (SignWord w : normWords) {
			size(w);
		}
		return normWords;
	}

	/**
	 * Normalize the signature size and apply rotation
	 * 
	 * @author Luciano Giuseppe
	 * @return A Word normalized
	 */
	private void size(SignWord signatureWord) {
		
		//compute min, max and mean X & Y of current word
		importMMM(signatureWord);
		
		LinkedList<Double> yList = signatureWord.getY();
		LinkedList<Double> xList = signatureWord.getX();

//		applyRotation(yList, xList);		
		
		//apply size normalization
		double newX, newY, sumX=0, sumY=0;
		double deltaX = Math.abs(maxX - minX);
		double deltaY = Math.abs(maxY - minY);
		for (int i = 0; i < xList.size(); i++) {
			
			//apply size normalization
			newX = (xList.get(i) - meanX) / deltaX;
			xList.set(i, newX);
			newY = (yList.get(i) - meanY) / deltaY;
			yList.set(i, newY);

			//calc. the next min, max, mean
			sumX += newX;
			sumY += newY;
			if(i == 0) {
				maxX = minX = newX;
				maxY = minY = newY;
			} else {
				if(newX < minX) minX = newX;
				if(newX > maxX) maxX = newX;
				
				if(newY < minY) minY = newY;
				if(newY > maxY) maxY = newY;
			}
		}
		meanX = sumX/yList.size();
		meanY = sumY/yList.size();
		
		//set new Min, Max and Mean values in the SignWord
		setWordMMM(signatureWord);
		
		return;
	}


	/**
	 * Apply a rotation to word according to its barycenter
	 * TODO: test!!
	 * @param yList
	 * @param xList
	 */
	private void applyRotation(LinkedList<Double> yList,
			LinkedList<Double> xList) {
		
		//calc values for rotation
		double ang = Math.atan2(meanY, meanX);// (meanX, meanY) is word barycenter
		
		//Show the barycenter point and angle
		//System.out.println("X: "+ meanX + ", Y: " +meanY +", angle: " + Math.toDegrees(ang));
		
		double cosA = Math.cos(ang);
		double sinA = Math.sin(ang);
		double sumX = 0, sumY = 0, size =  xList.size();
				
		double newX, newY, x, y;
		for (int i = 0; i < size; i++) {
			x = xList.get(i);
			y = yList.get(i);
			
			//apply rotation matrix
			newX = x*cosA - y*sinA;
			newY = x*cosA + y*sinA;
			
			xList.set(i, newX);
			yList.set(i, newY);
			
			//calc values for optimization
			sumX += newX;
			sumY += newY;
			if(i == 0) {
				maxX = minX = newX;
				maxY = minY = newY;
			} else {
				if(newX < minX) minX = newX;
				if(newX > maxX) maxX = newX;
				
				if(newY < minY) minY = newY;
				if(newY > maxY) maxY = newY;
			}
		}
		meanX = sumX/size;
		meanY = sumY/size;
		return;
	}
	
	/**
	 * Merger signature words
	 * @param words
	 * @return 
	 */
	private LinkedList<SignWord> merger(LinkedList<AcquisitionSignWord> words) {
		LinkedList<SignWord> mergedWords = new LinkedList<SignWord>();
		LinkedList<Integer> unionPoints = new LinkedList<Integer>();
		int i, wNumb, uP, wSize;
		
		//loop on input words
		uP = 0;
		wNumb = words.size();
		i = 0;
		SignWord merged = null; //no words merged
		while(i < wNumb) {
					
			if( ((i+1) < wNumb) && canJoin(words.get(i), words.get(i+1)) ) {

				//save the point of merging
				wSize = words.get(i).getX().size();
				uP += wSize; //points
				unionPoints.add(uP);
				
				//merge words into new SignWord
				if(merged == null) {
					merged = new SignWord(words.get(i), null, false);
				} 
				merged.getX().addAll(words.get(i+1).getX());
				merged.getY().addAll(words.get(i+1).getY());
				merged.getTime().addAll(words.get(i+1).getTime());
				merged.getPressure().addAll(words.get(i+1).getPressure());

			} else {
	
				//save the last word merged, if it was done, or the last in list
				wSize = words.get(i).getX().size();
				uP += wSize; //points
				unionPoints.add(uP);
				if(merged == null)
					mergedWords.add(new SignWord(words.get(i), unionPoints, false));
				else {
					mergedWords.add(merged);
					merged.setPartsSize(unionPoints);
				}
				
				//for next iteration
				merged = null; //start an other word merging
				unionPoints = new LinkedList<Integer>();
				uP = 0;
			}
			i++;
		}
			
		words.clear();
		return mergedWords;
	}


	/**
	 * Import min, max and mean values
	 * 
	 * @param word
	 */
	private void importMMM(SignWord word) {		
		LinkedList<Double> x = word.getX(), y = word.getY();
		
		maxX = minX = x.getFirst();
		maxY = minY = y.getFirst();
		double sumX=0, sumY=0, size = x.size(), t;
		for(int i = 1; i < size; i++) {
			t = x.get(i);
			if(t < minX) minX = t;
			if(t > maxX) maxX = t;
			sumX += t;
			
			t = y.get(i);
			if(t < minY) minY = t;
			if(t > maxY) maxY = t;
			sumY += t;
			
		}
		meanX = sumX / size;
		meanY = sumY / size;
		
		//setWordMMM(word);
		
	}
	
	/**
	 * Set signature word: Min, Max and Mean of X and Y axis
	 * @param word
	 */
	private void setWordMMM(SignWord word) {
		word.setMaxX(maxX);
		word.setMinX(minX);
		
		word.setMaxY(maxY);
		word.setMinY(minY);
		
		word.setMeanX(meanX);
		word.setMeanY(meanY);
	}
	
	/**
	 * Can I join two words?
	 * @param a first word
	 * @param b second word
	 * @return true if can join the signature
	 * TODO: edit this to make join words according your params.
	 */
	private boolean canJoin(AcquisitionSignWord a, AcquisitionSignWord b) {
		int minDist = 10;
		
		if( min(b.getX())  <= (max(a.getX())+ minDist))
			return true;
		
		return false;
	}
	
	public double min(LinkedList<Double> data) {
		double minTemp = data.getFirst();
		for (Double aDouble : data) {
			if (aDouble < minTemp)
				minTemp = aDouble;
		}
		return minTemp;
	}

	public double max(LinkedList<Double> data) {
		double maxTemp = data.getFirst();
		for (Double aDouble : data) {
			if (aDouble > maxTemp)
				maxTemp = aDouble;
		}
		return maxTemp;
	}

	/**
	 * Calculate the mean of a list of values
	 * 
	 * @return mean
	 */
	public static double mean(LinkedList<Double> data) {
		double mean = 0;
		for (Double aDouble : data) {
			mean += aDouble;
		}
		return mean / data.size();
	}
}
