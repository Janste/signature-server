package signaturesdk.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.LinkedList;

import signaturesdk.beans.SignWord;
import signaturesdk.features.utils.GMath;



/**
 * Generate directional hash value from a signature
 * @auth Giuseppe Luciano  May 2013
 *
 */
public class Directional {
	
	/**
	 * SALT phrase
	 */
	final static private String SALT = "mypersonalphrase";
	
	/**
	 * How many SHA iterations 
	 */
	final static private int DIGEST_ITERATION = 1;
	
	/*
	 * Use more chars to make difficult a bruteforce attack
	 */
	final static private class MOVEMENTS {
		static String NEW_WORD = "0";
		static String UP = "1";
		static String DOWN = "2";
		static String LEFT = "3";
		static String RIGHT = "4";
	}
	
	final static private class SEGM_LEN {
		static String SHORT = "s";
		@SuppressWarnings("unused")
		static String UGUAL = "u";
		static String LONG = "l";
	}
	
	final static private class POINT_POS {
		static String MIN = "a";
		static String SAME = "w";
		static String HIGH = "d";
	}
	
	/**
	 * Used to statore computation on an axis
	 */
	private class AxisStruct {
		public AxisStruct(double firstPoint) {
			this.segmFirstPoint = firstPoint;
			this.lastMove = MOVEMENTS.NEW_WORD;
			this.hash = "";
		}
		
		public double segmFirstPoint;
		public int segmsCounter = 0; //segments counter
		public String lastMove;
		public String hash;
		public double lastSegmLen;
	}
	
	private LinkedList<SignWord> signature;
	
	public Directional(LinkedList<SignWord> signature) {
		this.signature = signature;
	}
	
	
	/**
	 * Print a byte array as a String
	 * @param buffer
	 */
	private String byteToHex(final byte[] buffer)
	{
	    Formatter formatter = new Formatter();
	    for (byte b : buffer)
	    {
	        formatter.format("%02x", b);
	    }
	    String result = formatter.toString();
	    formatter.close();
	    return result;
	}
	
	/**
	 * Compute the directional hash of a signature
	 * @param signature The signature to compute the hash
	 * @return String with hash value
	 */
	public String compute() {
		
		String hash = "";
		for(SignWord s : signature) {
			hash += compute(s) + MOVEMENTS.NEW_WORD;
		}
		
		return sha(hash);
		//return hash;
	}
	
	
	/**
	 * Hash the input string with SHA 256 
	 * @param str String to hash
	 * @return Returns the SHA hash as a string.
	 */
	private String sha(String str) {
		byte[] out;
		/*
		 * I use SHA to conceal signature directional hash
		 * More info: https://www.owasp.org/index.php/Hashing_Java
		 */
		try {
			 MessageDigest md = MessageDigest.getInstance("SHA-256");
			 md.reset();
			 md.update(SALT.getBytes());
			 out = str.getBytes();
			 for(int i = 0; i < DIGEST_ITERATION; i++) {
				 out = md.digest(out); //from javadoc: The digest is reset after this call is made. 
			 }
			 
			 //format byte for string
			 str = byteToHex(out);
			} catch (NoSuchAlgorithmException e) {
				//Hasn't Java SHA-256?! It returns directly the directional hash value.
			}
		
		return str;
	}
	
	
		
	/**
	 * Calculate directional hash of a SignWord by X-Y coords
	 * @param sw
	 */
	private String compute(SignWord sw) {
		AxisStruct X = null, Y = null;
		
		LinkedList<Double> x = sw.getX();
		LinkedList<Double> y = sw.getY();
		
		if(x.size() < 10) return "";
		
		String yHash = "", xHash = "";
		int segmFirstPoint = 0;
		
		//compute a range range 
		double range = GMath.polyLen(x, y,0, sw.getPartsSize().getFirst() ) / 10;
		
		int segmsCount = 0;
		double lastX = 0, lastY = 0;
		for(Integer segmPointsNum: sw.getPartsSize()) {
			int points = segmPointsNum;
			
			//verify first point position
			if(segmsCount != 0) {
				double t = x.get(segmFirstPoint);
				if(Math.abs(t-lastX) <= range) {
					xHash += POINT_POS.SAME;
				} else if(t < lastX) {
					xHash += POINT_POS.MIN;
				} else {
					xHash += POINT_POS.HIGH;
				}
				
				t = y.get(segmFirstPoint);
				if(Math.abs(t-lastY) <= range) {
					yHash += POINT_POS.SAME;
				} else if(t < lastY) {
					yHash += POINT_POS.MIN;
				} else {
					yHash += POINT_POS.HIGH;
				}
			}
			
			int i = segmFirstPoint + 4;
			X = new AxisStruct(x.get(i-1));
			Y = new AxisStruct(y.get(i-1));
			for (; i < points-3; i++) { //to remove some "noise" at start and at end
	
				/*
				 * Acting on Y
				 */
				
				//Y direction?
				if(y.get(i) >= y.get(i-1)) { 
					workOnDirection(Y, MOVEMENTS.UP, y.get(i));
				} else { 
					workOnDirection(Y, MOVEMENTS.DOWN, y.get(i));
				}
				
				/*
				 * Acting on X
				 */
				
				//X direction?
				if(x.get(i) >= x.get(i-1)) { 
					workOnDirection(X, MOVEMENTS.RIGHT, x.get(i));
				} else {
					workOnDirection(X, MOVEMENTS.LEFT, x.get(i));
				}
			}
			
			//length info for the last segment
			i--;
			yHash += Y.hash; //+ lastStep(Y, y.get(i)); //commented because make algo more sensitive 
			xHash += X.hash; //+ lastStep(X, x.get(i));
			
			segmFirstPoint = points;
			
			//store segment last point
			lastX = x.get(points-1);
			lastY = y.get(points-1);
			segmsCount++;
			
		}
		
		//return the final hash string
		return yHash + xHash;
	}
	
	/**
	 * Extract info on X/Y direction
	 * @param axis
	 * @param MOVEMENT
	 * @param point
	 */
	private void workOnDirection(AxisStruct axis, String MOVEMENT, double point) {
		
		// direction changed (comp by String ref)
		if(axis.lastMove != MOVEMENT) {
			//I'm starting an other segment
			
			//insert info length on last segment before...
			axis.hash += segmLengthInfo(axis, point);
			
			//say that other segment starts
			axis.hash += MOVEMENT;
		}
		
		axis.lastMove = MOVEMENT; //save last movement
	}

	/**
	 * Return the info about current segment length regard the the previous
	 * @param axis
	 * @param lastPoint
	 * @return
	 */
	private String segmLengthInfo(AxisStruct axis, double lastPoint) {
		String hash = "";
		
		/* 
		 * first to start another segment
		 * I take last segment length
		 */
		double currentSegmLen = Math.abs(axis.segmFirstPoint - lastPoint);
		
		if(axis.segmsCounter >= 2) { 
			//Insert info on segmnet len into hash
			hash += fuzzyLen(axis.lastSegmLen, currentSegmLen);
		}
		axis.segmsCounter++;
		
		// save infos for the new segment
		axis.lastSegmLen = currentSegmLen;
		axis.segmFirstPoint = lastPoint;
		
		return hash;
	}
	
	/**
	 * Return a string according to current segment length passed is short, ugual or long to 
	 * previus segment len
	 * @param prevLen
	 * @param currentLen
	 * @return SHORT, LONG or UGUAL string 
	 */
	private String fuzzyLen(double prevLen, double currentLen) {
					
		double t = currentLen/prevLen;		
		 if(t > 1.5)
			return SEGM_LEN.LONG;
		else
			return SEGM_LEN.SHORT;
	}
	
	/*
	 * Set information about last segment length 
	 * Is it real useful?
	 */
	@SuppressWarnings("unused")
	private String lastStep(AxisStruct axis, double lastPoint) {
		if(axis.segmsCounter >= 2) {
			double currentSegmLen = Math.abs(axis.segmFirstPoint - lastPoint);
			return fuzzyLen(axis.lastSegmLen, currentSegmLen);
		}
		return "";
	}
	
}
