package signaturesdk.verification;

import java.util.LinkedList;
import java.util.Vector;

import signaturesdk.beans.PairList;
import signaturesdk.beans.SignWord;


/**
 * Some useful routine to check signatures by different features and techniques
 * @author Luciano Giuseppe
 *
 */
public class Verification {
	
	/**
	 * Calculate the coords distance with DTW 
	 * @param origSign original signature
	 * @param testSign signature to verify
	 * @return words distance
	 */
	public static Vector<Double> coordsDTW(LinkedList<SignWord> origSign, LinkedList<SignWord> testSign) {
		if(origSign.size() == 0 || testSign.size() == 0)
			return null;
		
		Vector<Double> retValues = new Vector<Double>();
		//I can make a check on words in signatures
		int min = Math.min(origSign.size(), testSign.size());
		for(int i = 0; i < min; i++) {
			SignWord original, test;
			original = origSign.get(i);
			test = testSign.get(i);
			DynamicTimeWarping dtw = new DynamicTimeWarping();
			double result = dtw.perform2D(new PairList(original.getX(), original.getY()), 
					new PairList(test.getX(), test.getY()));
			retValues.add(result);
		}
		
		return retValues;
	}
	
	/**
	 * Calculate the velocities distance by DTW 
	 * @param origSign original signature
	 * @param testSign signature to verify
	 * @return word distance
	 */
	public static Vector<Double> velDTW(LinkedList<SignWord> origSign, LinkedList<SignWord> testSign) {
		if(origSign.size() == 0 || testSign.size() == 0)
			return null;
		
		Vector<Double> retValues = new Vector<Double>();
		
		// A check on f1.size==f2.size?
		int min = Math.min(origSign.size(), testSign.size());
		
		for(int i = 0; i < min; i++) {
			SignWord or, test;
			or = origSign.get(i);
			test = testSign.get(i);
			DynamicTimeWarping dtw = new DynamicTimeWarping();
			double result = dtw.perform2D(new PairList(or.getVx(), or.getVy()), 
					new PairList(test.getVx(), test.getVy()));
			retValues.add(result);
			
		}
		
		return retValues;
	}
	
	/**
	 * Compute critical points distance with DTW
	 * @param origSign
	 * @param testSign
	 * @return
	 */
	public static Vector<Double> criticalPointsDTW(LinkedList<SignWord> origSign, LinkedList<SignWord> testSign) {
		if(origSign.size() == 0 || testSign.size() == 0)
			return null;
		
		Vector<Double> retValues = new Vector<Double>();
		
		// A check on f1.size==f2.size?
		int min = Math.min(origSign.size(), testSign.size());
		
		for(int i = 0; i < min; i++) {
			SignWord or, test;
			or = origSign.get(i);
			test = testSign.get(i);
			DynamicTimeWarping dtw = new DynamicTimeWarping();
			double result = dtw.perform2D(or.getCriticalPoints(), test.getCriticalPoints());
			retValues.add(result);
			
		}
		
		return retValues;
	}
	
	/**
	 * Compute internal angles distance with DTW
	 * @param origSign
	 * @param testSign
	 * @return
	 */
	public static Vector<Double> internalAnglesDTW(LinkedList<SignWord> origSign, LinkedList<SignWord> testSign) {
		if(origSign.size() == 0 || testSign.size() == 0)
			return null;
		
		Vector<Double> retValues = new Vector<Double>();
		
		// A check on f1.size==f2.size?
		int min = Math.min(origSign.size(), testSign.size());
		
		for(int i = 0; i < min; i++) {
			SignWord or, test;
			or = origSign.get(i);
			test = testSign.get(i);
			DynamicTimeWarping dtw = new DynamicTimeWarping();
			double result = dtw.perform1D(or.getInternalAngles(), test.getInternalAngles());
			retValues.add(result);
			
		}
		
		return retValues;
	}
	
	/**
	 * Compute external angles distance with DTW
	 * @param origSign
	 * @param testSign
	 * @return
	 */
	public static Vector<Double> externalAnglesDTW(LinkedList<SignWord> origSign, LinkedList<SignWord> testSign) {
		if(origSign.size() == 0 || testSign.size() == 0)
			return null;
		
		Vector<Double> retValues = new Vector<Double>();
		
		// A check on f1.size==f2.size?
		int min = Math.min(origSign.size(), testSign.size());
		
		for(int i = 0; i < min; i++) {
			SignWord or, test;
			or = origSign.get(i);
			test = testSign.get(i);
			DynamicTimeWarping dtw = new DynamicTimeWarping();
			double result = dtw.perform1D(or.getExternalAngles(), test.getExternalAngles());
			retValues.add(result);
			
		}
		
		return retValues;
	}
	
	/**
	 * Compute DTW on the pressure of two signatures
	 * @param origSign
	 * @param testSign
	 * @return
	 */
	public static Vector<Double> pressionDTW(LinkedList<SignWord> origSign, LinkedList<SignWord> testSign) {
		if(origSign.size() == 0 || testSign.size() == 0)
			return null;
		
		Vector<Double> retValues = new Vector<Double>();
		
		// A check on f1.size==f2.size?
		int min = Math.min(origSign.size(), testSign.size());
		
		for(int i = 0; i < min; i++) {
			SignWord or, test;
			or = origSign.get(i);
			test = testSign.get(i);
			DynamicTimeWarping dtw = new DynamicTimeWarping();
			double result = dtw.perform1D(or.getPressure(), test.getPressure());
			retValues.add(result);
			
		}
		
		return retValues;
	}
	
	/**
	 * Extended Regression on signature coords
	 * @param origSign Original signature
	 * @param testSign Test signature
	 * @return Vector of double with values from 0 to 1
	 */
	public static Vector<Double> coordsER2 (LinkedList<SignWord> origSign, LinkedList<SignWord> testSign) {
		if(origSign.size() == 0 || testSign.size() == 0)
			return null;
		
		Vector<Double> retValues = new Vector<Double>();
		
		int min = Math.min(origSign.size(), testSign.size());
		for(int i = 0; i < min; i++) {
			ER2 regression = new ER2();
			int points = Math.min(origSign.get(i).getPointsNum(), testSign.get(i).getPointsNum());
			retValues.add(regression.compute(origSign.get(i), testSign.get(i), points));
		}
		
		return retValues;
	}
	
	/*public static Vector<Double> velER2 (LinkedList<SignWord> signature1, LinkedList<SignWord> signature2) {
		if(signature1.size() == 0 || signature2.size() == 0)
			return null;
		
		Vector<Double> retValues = new Vector<Double>();
		
		int min = Math.min(signature1.size(), signature2.size());
		for(int i = 0; i < min; i++) {
			ER2 regression = new ER2();
			int minPoints = Math.min(signature1.get(i).getVx().size(), signature2.get(i).getVx().size());
			PairList pl1 = new PairList(signature1.get(i).getVx(), signature1.get(i).getVy());
			PairList pl2 = new PairList(signature2.get(i).getVx(), signature2.get(i).getVy());
			retValues.add(regression.compute(pl1, pl2, minPoints));
		}
		
		return retValues;
	}*/
	
	
}
