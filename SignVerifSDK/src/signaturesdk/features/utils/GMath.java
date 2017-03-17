package signaturesdk.features.utils;

import java.util.LinkedList;

/**
 * Geometry Math
 * @author Luciano Giuseppe
 */
public class GMath {
	
	/**
	 * Angle in radians between X axis in P1 and P2
	 * @param p1x
	 * @param p1y
	 * @param p2x
	 * @param p2y
	 * @return angle between 0 and 2 pi
	 */
	public static double angle(double p1x, double p1y, double p2x, double p2y) {
		//traslation of axes
		p2x -= p1x;
		p2y -= p1y;
		
		//compute the angle
		double angle = Math.atan2(p2y,p2x);
		//"convert" negative value to a value between pi and 2 pi 
		if(angle < 0) angle = 6.283185307179586+angle; // 6.283185307179586 = Math.PI*2
		
		return angle;
	}
	
	/**
	 * Angle in degrees
	 * @param p1x
	 * @param p1y
	 * @param p2x
	 * @param p2y
	 * @return
	 */
	public static double angleDegrees(double p1x, double p1y, double p2x, double p2y) {
		return  angle(p1x,p1y,p2x,p2y) * 57.29577951308232; // 57.29577951308232 = 180/Math.PI;
	}
	
	/**
	 * Calc angle in radians between three points using law of cosine
	 * @param cX center point X
	 * @param cY center point Y
	 * @return angle in degree
	 */
	public static double angle(double p1x, double p1y, double cX, double cY, double p3x, double p3y) {
		double pC2 = distance(cX, cY, p3x, p3y);
		double pC3 = distance(cX, cY, p1x, p1y);
		double p23 = distance(p3x, p3y, p1x, p1y);
		
		double t = (pC2*pC2)+(pC3*pC3)-(p23*p23);
		t = div(t, 2*pC2*pC3);
		
		//def of acos: -1 =< t =< 1
		if(t<-1) t=-1;
		else if(t>1) t=1;
		
		return Math.acos(t); 
	}
	
	/**
	 * Angle in degrees between three points using law of cosine
	 * @param p1x
	 * @param p1y
	 * @param cX
	 * @param cY
	 * @param p3x
	 * @param p3y
	 * @return
	 */
	public static double angleDegrees(double p1x, double p1y, double cX, double cY, double p3x, double p3y) {
		return angle(p1x, p1y, cX, cY, p3x, p3y) * 57.29577951308232 ; // 180/Math.PI = 57.29577951308232 
	}
	
	/**
	 * Support division by 0
	 * @param a
	 * @param b
	 * @return
	 */
	public static double div(double a, double b) {
		
		if(b == 0)
			return 0;
		
		return a/b;
	}
	
	/**
	 * Distance between two point
	 * @return segment length
	 */
	public static double distance(double p1x, double p1y, double p2x, double p2y) {
		double dX = p2x-p1x;
		double dY = p2y-p1y;
		
		return Math.hypot(dX, dY);
	}
	
	/**
	 * Compute the len of a polygon
	 * @param x array of x coords
	 * @param y array of y coords
	 * @return 0 if x.len != y.len
	 */
	public static double polyLen(double[] x, double[] y) {
		double len = 0;
		if(x.length != y.length)
			return 0;
		
		for(int i = 1; i < x.length; i++) {
			len += distance(x[i-1], y[i-1], x[i], y[i]);
		}
		
		return len;
	}
	
	/**
	 * Compute the length of all polygon
	 * @param x array of x coords
	 * @param y array of y coords
	 * @return 0 if x.len != y.len
	 */
	public static double polyLen(LinkedList<Double> x, LinkedList<Double> y) {
		double len = 0;
		
		if(x.size() != y.size())
			return 0;
		
		
		for(int i = 1; i < x.size(); i++) {
			len += distance(x.get(i-1), y.get(i-1), x.get(i), y.get(i) );
		}
		
		
		return len;
	}
	

	/**
	 * Compute the length of a part of polygon
	 * @param x X points
	 * @param y Y points
	 * @param firstPoint first point index
	 * @param segmPoints last point index
	 * @return
	 */
	public static double polyLen(LinkedList<Double> x, LinkedList<Double> y, int firstPoint, int segmPoints) {
		double len = 0;
		
		if(x.size() != y.size() || segmPoints > x.size())
			return 0;
		
		for(int i = firstPoint+1; i < segmPoints; i++) {
			len += distance(x.get(i-1), y.get(i-1), x.get(i), y.get(i) );
		}
		
		return len;
	}
}
