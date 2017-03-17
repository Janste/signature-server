package signaturesdk.features;

import java.util.LinkedList;

import signaturesdk.beans.PairList;
import signaturesdk.beans.SignWord;
import signaturesdk.features.utils.GMath;




/**
 * Extract from a signature this features: velocity, critical points,
 * internal and external angles
 * @author Luciano Giuseppe 2012
 *
 */
public class Extract {
	/**
	 * Calculate the velocities from every word and extract critical points
	 * @return critical points number
	 * @throws Exception there's a delta time zero
	 */
	public static int calculateVelocities(LinkedList<SignWord> inputSign) throws Exception {
		int criticalPoints = 0;
		for (SignWord sw : inputSign) {
			criticalPoints += calculateVelocities(sw);
		}

		return criticalPoints;
	}

	/**
	 * Calculate velocities from SignWord and extract critical points
	 * 
	 * @param sw
	 * @return critical points number
	 * @throws Exception 
	 */
	public static int calculateVelocities(SignWord sw) throws Exception {
		LinkedList<Long> t = sw.getTime();
		LinkedList<Double> x = sw.getX();
		LinkedList<Double> y = sw.getY();
		int points = x.size();
		LinkedList<Double> vx = sw.getVx();
		LinkedList<Double> vy = sw.getVy();
		
		//velocity and critical points already calc
		if(vx.size() != 0)
			return sw.getCriticalPoints().getValues1().size();
		
		// critical points coords
		LinkedList<Double> criticalX = new LinkedList<Double>();
		LinkedList<Double> criticalY = new LinkedList<Double>();
		
		//insert the first point of word as critical
		criticalX.add(x.get(0));
		criticalY.add(y.get(0));

		double  dt, dXt, dYt, oldVx=0, oldVy=0;
		for (int i = 1; i < points; i++) {
			
			if ((dt = t.get(i) - t.get(i - 1)) == 0) {  
				throw new Exception("Delta time between two points is zero: acquisition origin is not precise");
			}
			dt /= 1000000;
			
			dXt = ( (x.get(i) - x.get(i - 1)) * 1000) / dt;
			vx.add(dXt);
			dYt = ( (y.get(i) - y.get(i - 1)) * 1000) / dt;
			vy.add(dYt);
			
			//check critical points
			if(i != 1 && reversalVel(dXt, dYt, oldVx, oldVy)) {
					criticalX.add(x.get(i));
					criticalY.add(y.get(i));
				}
			
			//for the next iteration
			oldVx = dXt;
			oldVy = dYt;
		}
		
		//insert the last point of word as critical
		criticalX.add(x.get(points-1));
		criticalY.add(y.get(points-1));
				
		sw.setCriticalPointers(new PairList(criticalX, criticalY));
		return criticalX.size();
	}
	
	/**
	 * Check reversal of velocity
	 * @param vx
	 * @param vy
	 * @param oldVx
	 * @param oldVy
	 * @return true if there was a reversal
	 */
	private static boolean reversalVel(double vx, double vy, double oldVx, double oldVy) {
		boolean retVal = false;
		if((oldVx == 0 && vx != 0) || (oldVx != 0 && vx == 0))
			retVal = true;
		if((oldVy == 0 && vy != 0) || (oldVy != 0 && vy == 0))
			retVal = true;
		
		if((oldVx > 0 && vx <= 0) || (oldVx <= 0 && vx > 0))
			retVal = true;
		if((oldVy > 0 && vy <= 0) || (oldVy <= 0 && vy > 0))
			retVal = true;
		
		return retVal;
	}
	
	/**
	 * Extract internal and external angles in a signature
	 * @param sign
	 */
	public static void angles(LinkedList<SignWord> sign) {
		for(SignWord sw : sign) {
			angles(sw);
		}
	}
	
	/**
	 * Extract internal angle between 2 points, and external angles between 4 points
	 * @param word
	 */
	public static void angles(SignWord word) {
		LinkedList<Double> x = word.getX();
		LinkedList<Double> y = word.getY();
		
		LinkedList<Double> inAngles = word.getInternalAngles();
		LinkedList<Double> extAngles = word.getExternalAngles();
		
		//already calc
		if((inAngles.size() != 0) || (extAngles.size() != 0))
			return;
		
		double angle;
		for(int i = 1; i < x.size(); i++) {
			angle = GMath.angle(x.get(i-1), y.get(i-1), x.get(i), y.get(i));
			inAngles.add(angle);
			
			//every 5 points:0 to 4
			if((i%4)==0) {
				angle = 360 - GMath.angle(x.get(i-4), y.get(i-4), x.get(i-2), y.get(i-2), x.get(i), y.get(i));
				extAngles.add(angle);
			}
		}
	}
	

	/**
	 * Return the number of points in a signature
	 * 
	 * @param sign
	 * @return
	 */
	public static int pointsNumber(LinkedList<SignWord> sign) {
		int n = 0;
		for (SignWord sw : sign) {
			n += sw.getX().size();
		}
		return n;
	}

	/**
	 * Return the number of points in a word of signature
	 * 
	 * @param signWord
	 * @return
	 */
	public static int pointsNumber(SignWord signWord) {
		return signWord.getX().size();
	}
	
	/**
	 * Calculate the total signature time
	 * @param sign
	 * @return
	 */
	public static long totalTime(LinkedList<SignWord> sign) {
		//totalTime = last point time - first point time
		return sign.getLast().getTime().getLast() - sign.getFirst().getTime().getFirst();
	}
	
	/**
	 * Calculate the time of a word
	 * @param sign
	 * @return
	 */
	public static long totalTime(SignWord sign) {
		return sign.getTime().getLast() - sign.getTime().getFirst();
	}

}
