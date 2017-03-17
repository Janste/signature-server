package business;

import java.util.LinkedList;
import java.util.Vector;

import signaturesdk.beans.AcquisitionSignWord;
import signaturesdk.beans.SignWord;
import signaturesdk.features.Normalize;
import signaturesdk.features.Sample;
import signaturesdk.verification.Verification;
import utils.Point;

public class Signature {
	private Point[][] data;
	
	public Signature() {
		//TODO take JSON and convert to data
	}
	
	public boolean match(Signature other) {
		LinkedList<SignWord> s1 = this.getVerifiableSignature();
		LinkedList<SignWord> s2 = other.getVerifiableSignature();
		
		Sample s = new Sample();
		s.sample(s1, s2);
		Vector<Double> ret = Verification.coordsER2(s.getSignature1(), s.getSignature2());
		for (double v: ret) {
			//TODO use the value for each of the words to decide if it's close enough
		}
		return false;
	}
	
	public LinkedList<SignWord> getVerifiableSignature() {
		LinkedList<AcquisitionSignWord> sig = new LinkedList<>();
		for (Point[] w : data) {
			LinkedList<Double> x = new LinkedList<>();
			LinkedList<Double> y = new LinkedList<>();
			LinkedList<Long> time = new LinkedList<>();
			for (Point p : w) {
				x.add(p.getX());
				y.add(p.getY());
				time.add(p.getTime());
			}
			AcquisitionSignWord word = new AcquisitionSignWord(x, y, new LinkedList<Double>(), time);
			sig.add(word);
		}
		return (new Normalize(sig)).size();
	}
}
