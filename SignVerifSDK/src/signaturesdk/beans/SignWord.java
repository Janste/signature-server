package signaturesdk.beans;

import java.io.Serializable;
import java.util.LinkedList;

import signaturesdk.features.utils.Copier;



/**
 * A signature word with the features extracted / to extract
 * @author Luciano Giuseppe
 *
 */
public class SignWord implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	
	private LinkedList<Double> x;
	private LinkedList<Double> y;
	private LinkedList<Integer> partsSize; //pos where words are merged 
	private LinkedList<Double> vx;
	private LinkedList<Double> vy;
	private PairList criticalPoints;
	private LinkedList<Double> internalAngles;
	private LinkedList<Double> externalAngles;
	private LinkedList<Long> t; //time
	private LinkedList<Double> pressure;
	
	private double minX, maxX, meanX;
	private double minY, maxY, meanY;

	public SignWord() {
		this.x = new LinkedList<Double>();
		this.y = new LinkedList<Double>();
		this.t = new LinkedList<Long>();
		this.pressure = new LinkedList<Double>();
		this.initLinkedLists();
	}
	
	/**
	 * Construct a SignWord given X, Y, Time, and unionPoints
	 * @param x
	 * @param y
	 * @param t
	 * @param partsSize a list of size of every word parts
	 * @param clone clone the lists or take objects reference
	 */
	public SignWord(LinkedList<Double> x, LinkedList<Double> y,
			LinkedList<Long> time, LinkedList<Double> pressure, LinkedList<Integer> partsSize, boolean clone) {
		if(clone) {
			this.x = Copier.doubleList(x);
			this.y = Copier.doubleList(y);
			this.t = Copier.longList(time);
			this.partsSize = Copier.intList(partsSize);
			this.pressure = Copier.doubleList(pressure);
		} else {
			this.x = x;
			this.y = y;
			this.t = time;
			this.partsSize = partsSize;
			this.pressure = pressure;
		}
		this.initLinkedLists();
	}
	
	/**
	 * Import lists from AcquisitionSignWord(just copy: x, y, time) and set unionPoints
	 * @param word AcquisitionSignWord to convert into SignWord
	 * @param unionPoints
	 * @param clone say to clone lists' elements or pass only the objects reference
	 */
	public SignWord(AcquisitionSignWord word, LinkedList<Integer> unionPoints, boolean clone) {
		if(clone) {
			this.x = Copier.doubleList(word.getX());
			this.y = Copier.doubleList(word.getY());
			this.t =  Copier.longList(word.getTime());
			this.partsSize = Copier.intList(unionPoints);
			this.pressure = Copier.doubleList(word.getPressure());
		} else {
			this.x = word.getX();
			this.y = word.getY();
			this.t = word.getTime();
			this.partsSize = unionPoints;
			this.pressure = word.getPressure();
		}
		this.initLinkedLists();
	}
	
	private void initLinkedLists() {
		this.vx = new LinkedList<Double>();
		this.vy = new LinkedList<Double>();
		this.criticalPoints = new PairList();
		this.internalAngles = new LinkedList<Double>();
		this.externalAngles = new LinkedList<Double>();
	}
	
	@Override
	public SignWord clone() {
		SignWord sw = new SignWord(this.x,this.y, this.t, this.pressure, this.partsSize, true);
		
		//copy velocity
		sw.setVx(Copier.doubleList(this.vx));
		sw.setVy(Copier.doubleList(this.vy));
		
		//copy critical points
		PairList pl = new PairList();
		pl.setValues1(Copier.doubleList(this.criticalPoints.getValues1()));
		pl.setValues2(Copier.doubleList(this.criticalPoints.getValues2()));
		sw.setCriticalPointers(pl);
		
		//copy angles
		sw.setInternalAngles(Copier.doubleList(this.internalAngles));
		sw.setExternalAngles(Copier.doubleList(this.externalAngles));
		
		return sw;
	}

	public LinkedList<Double> getX() {
		return x;
	}
	
	public LinkedList<Double> getY() {
		return y;
	}
	
	public void setX(LinkedList<Double> x) {
		this.x = x;
	}
	
	public void setY(LinkedList<Double> y) {
		this.y = y;
	}
	
	public LinkedList<Long> getTime() {
		return t;
	}
	
	public void setTime(LinkedList<Long> t) {
		this.t = t;
	}
	
	public void setPressure(LinkedList<Double> pressure) {
		this.pressure = pressure;
	}
	
	public LinkedList<Double> getPressure() {
		return this.pressure;
	}

	public double getMinX() {
		return minX;
	}

	public double getMaxX() {
		return maxX;
	}

	public double getMeanX() {
		return meanX;
	}

	public double getMinY() {
		return minY;
	}

	public double getMaxY() {
		return maxY;
	}

	public double getMeanY() {
		return meanY;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public void setMeanX(double meanX) {
		this.meanX = meanX;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	public void setMeanY(double meanY) {
		this.meanY = meanY;
	}

	public LinkedList<Double> getVx() {
		return this.vx;
	}

	public LinkedList<Double> getVy() {
		return this.vy;
	}

	public void setVx(LinkedList<Double> vx) {
		this.vx = vx;
	}

	public void setVy(LinkedList<Double> vy) {
		this.vy = vy;
	}

	public LinkedList<Double> getInternalAngles() {
		return internalAngles;
	}

	public void setInternalAngles(LinkedList<Double> internalAngles) {
		this.internalAngles = internalAngles;
	}

	public LinkedList<Double> getExternalAngles() {
		return externalAngles;
	}

	public void setExternalAngles(LinkedList<Double> externalAngles) {
		this.externalAngles = externalAngles;
	}

	public int getPointsNum() {
		return this.x.size();
	}

	public PairList getCriticalPoints() {
		return criticalPoints;
	}

	public void setCriticalPointers(PairList criticalPointers) {
		this.criticalPoints = criticalPointers;
	}

	/**
	 * Return a list with number of points foreach part of word (for merged words)
	 * @return
	 */
	public LinkedList<Integer> getPartsSize() {
		return partsSize;
	}

	public void setPartsSize(LinkedList<Integer> unionPoints) {
		this.partsSize = unionPoints;
	}

}
