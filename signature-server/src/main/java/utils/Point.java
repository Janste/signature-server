package utils;

import java.io.Serializable;

public class Point implements Serializable {
	private static final long serialVersionUID = -1818344562883838209L;
	private double x;
	private double y;
	private long t;
	
	public Point(double x, double y, long time) {
		this.x = x;
		this.y = y;
		this.t = time;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public long getTime() {
		return t;
	}
}
