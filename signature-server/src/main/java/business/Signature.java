package business;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import signaturesdk.beans.AcquisitionSignWord;
import signaturesdk.beans.SignWord;
import signaturesdk.features.Normalize;
import signaturesdk.features.Sample;
import signaturesdk.verification.Verification;
import utils.Point;

public class Signature {
	private List<List<Point>> sigData;
	
	public Signature(JsonArray data) {
		sigData = new ArrayList<>();
		for (JsonElement word : data) {
			List<Point> wordList = new ArrayList<>();
			JsonArray wordArray = word.getAsJsonArray();
			for (JsonElement point : wordArray) {
				JsonObject pointObj = point.getAsJsonObject();
				double x = pointObj.get("x").getAsDouble();
				double y = pointObj.get("y").getAsDouble();
				long t = pointObj.get("time").getAsLong();
				wordList.add(new Point(x, y, t));
			}
			sigData.add(wordList);
		}
	}
	
	public boolean match(Signature other) {
		LinkedList<SignWord> s1 = this.getVerifiableSignature();
		LinkedList<SignWord> s2 = other.getVerifiableSignature();
		
		Sample s = new Sample();
		s.sample(s1, s2);
		Vector<Double> ret = Verification.coordsER2(s.getSignature1(), s.getSignature2());
		for (double v: ret) {
			if (v < 0.85)
				return false;
		}
		return true;
	}
	
	public LinkedList<SignWord> getVerifiableSignature() {
		LinkedList<AcquisitionSignWord> sig = new LinkedList<>();
		for (List<Point> w : sigData) {
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
	
	// Test method
	public void print() {
		for (List<Point> lp : sigData) {
			for (Point p : lp) {
				System.out.println(p.getX() + " " + p.getY() + " " + p.getTime());
			}
		}
	}
}
