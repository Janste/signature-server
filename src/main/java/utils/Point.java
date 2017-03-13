package utils;

public class Point {
	private double x;
	private double y;
	private double t;
	
	public Point(double x, double y, double time) {
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
	
	public double getTime() {
		return t;
	}
}
