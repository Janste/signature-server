package signaturesdk.beans;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Have a pair of values: x-y, Vx-Vy, etc..
 * @author peppe
 *
 */
public class PairList implements Serializable {

	private static final long serialVersionUID = 1L;
	private LinkedList<Double> values1;
	private LinkedList<Double> values2;
	private int num; //for other uses
	
	public PairList() {
		values1 = new LinkedList<Double>();
		values2 = new LinkedList<Double>();
	}
	
	/**
	 * A list of pair as (X,Y) or (Vx,Vy)
	 * @param values1 Values as X or Vx
	 * @param values2 Value as Y or Vy
	 */
	public PairList(LinkedList<Double> values1, LinkedList<Double> values2) {
		this.values1 = values1;
		this.values2 = values2;
	}
	
	public PairList(LinkedList<Double> values1, LinkedList<Double> values2, int num) {
		this(values1, values2);
		this.num = num;
	}

	/**
	 * Values as X or Vx
	 */
	public LinkedList<Double> getValues1() {
		return values1;
	}
	
	/**
	 * Values as Y or Yx
	 */
	public LinkedList<Double> getValues2() {
		return values2;
	}
	
	/**
	 * Values as X or Vx
	 */
	public void setValues1(LinkedList<Double> values1) {
		this.values1 = values1;
	}
	
	/**
	 * Values as Y or Yx
	 */
	public void setValues2(LinkedList<Double> values2) {
		this.values2 = values2;
	}

	/**
	 * For ER2
	 * @return
	 */
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	public int size() {
		return this.values1.size();
	}
	
}
