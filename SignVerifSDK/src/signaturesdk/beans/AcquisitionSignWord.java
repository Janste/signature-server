package signaturesdk.beans;

import java.io.Serializable;
import java.util.LinkedList;

import signaturesdk.features.utils.Copier;


/**
 * A word of the acquisition device
 * @author Luciano Giuseppe
 *
 */
public class AcquisitionSignWord implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	
	private LinkedList<Double> x;
	private LinkedList<Double> y;
	private LinkedList<Double> pressure;
	private LinkedList<Long> t; //time
	
	private double minX, maxX, meanX;
	private double minY, maxY, meanY;
	
	public AcquisitionSignWord() {
		this.x = new LinkedList<Double>();
		this.y = new LinkedList<Double>();
		this.pressure = new LinkedList<Double>();
		this.t = new LinkedList<Long>();
	}
	
	public AcquisitionSignWord(LinkedList<Double> x, LinkedList<Double> y,
			LinkedList<Double> pressure, LinkedList<Long> time) {
		this.x = x;
		this.y = y;
		this.pressure = pressure;
		this.t = time;
	}
	
	
	@Override
	public AcquisitionSignWord clone() {
		return new AcquisitionSignWord(Copier.doubleList(x), Copier.doubleList(y), 
				Copier.doubleList(pressure), Copier.longList(t));
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMeanX() {
		return meanX;
	}

	public void setMeanX(double meanX) {
		this.meanX = meanX;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMeanY() {
		return meanY;
	}

	public void setMeanY(double meanY) {
		this.meanY = meanY;
	}

	public LinkedList<Double> getX() {
		return x;
	}

	public LinkedList<Double> getY() {
		return y;
	}

	public LinkedList<Double> getPressure() {
		return this.pressure;
	}
	
	public LinkedList<Long> getTime() {
		return t;
	}

	public int getPointsNum() {
		return x.size();
	}
	
	

}
