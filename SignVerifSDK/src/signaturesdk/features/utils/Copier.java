package signaturesdk.features.utils;

import java.util.LinkedList;

import signaturesdk.beans.AcquisitionSignWord;
import signaturesdk.beans.SignWord;


/**
 * Own personal amanuensis
 * It creates a new LinkedList which elements has different reference from the originals
 */
public class Copier {

	public static LinkedList<Double> doubleList(LinkedList<Double> toClone) {
		if(toClone == null) 
			return null;
		
		LinkedList<Double> retValue = new LinkedList<Double>();
		 for(Double el : toClone) {
			 retValue.add(el.doubleValue());
		 }
		return retValue;
	}
	
	public static LinkedList<Long> longList(LinkedList<Long> toClone) {
		if(toClone == null) 
			return null;
		
		LinkedList<Long> retValue = new LinkedList<Long>();
		 for(Long el : toClone) {
			 retValue.add(el.longValue());
		 }
		return retValue;
	}
	
	public static LinkedList<Integer> intList(LinkedList<Integer> toClone) {
		if(toClone == null) 
			return null;
		
		LinkedList<Integer> retValue = new LinkedList<Integer>();
		 for(Integer el : toClone) {
			 retValue.add(el.intValue());
		 }
		return retValue;
	}
	
	public static LinkedList<AcquisitionSignWord> acquisitionSignature(LinkedList<AcquisitionSignWord> toClone) {
		if(toClone == null) 
			return null;
		
		LinkedList<AcquisitionSignWord> retValue = new LinkedList<AcquisitionSignWord>();
		for(AcquisitionSignWord el : toClone) {
			retValue.add(el.clone());
		}
		return retValue;
	}
	
	public static LinkedList<SignWord> signature(LinkedList<SignWord> toClone) {
		if(toClone == null) 
			return null;
		
		LinkedList<SignWord> retValue = new LinkedList<SignWord>();
		for(SignWord el : toClone) {
			retValue.add(el.clone());
		}
		return retValue;
	}

}
