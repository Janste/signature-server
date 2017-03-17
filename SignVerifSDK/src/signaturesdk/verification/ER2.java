/**
 * Project: Signature Verification
 * @author Ajay R, Keshav Kumar HK and Sachin Sudheendra
 * @author Luciano Giuseppe 2012
 */

package signaturesdk.verification;


import java.util.LinkedList;

import signaturesdk.beans.PairList;
import signaturesdk.beans.SignWord;


public class ER2 {

    private double meanXOriginal;
    private double meanYOriginal;
    private double meanXTest;
    private double meanYTest;

    /**
     * Compute the Extended Regression of two words of two signatures
     * @param sign1 SignWord
     * @param sign2 SignWord
     * @param points number to compare
     * @return
     */
    public double compute(SignWord sign1,SignWord sign2, int n) {
    	//import the means
    	this.meanXOriginal = sign1.getMeanX();
    	this.meanYOriginal = sign1.getMeanY();
    	
    	this.meanXTest = sign2.getMeanX();
    	this.meanYTest = sign2.getMeanY();
    	
    	return evaluateFormulae(sign1.getX(), sign1.getY(), sign2.getX(), sign2.getY(), n);
    }
    
    /**
     * Compute the Extended Regression of two Pair of values
     * @param sourceData PairList
     * @param testData PairList
     * @param points number to compare
     * @return 	likeness from 0 to 1
     */
    public double compute(PairList sourceData, PairList testData, int n) {
        calculateMean(sourceData.getValues1(), sourceData.getValues2(), testData.getValues1(), testData.getValues2());
        
        return evaluateFormulae(sourceData.getValues1(), sourceData.getValues2(), testData.getValues1(), testData.getValues2(), n);
    }

    /**
     * Calculate the mean of sequence
     * @param xOriginal
     * @param yOriginal
     * @param nr
     * @param xTest
     * @param yTest
     * @param nt
     */
    private void calculateMean(LinkedList<Double> xOriginal, LinkedList<Double> yOriginal, LinkedList<Double> xTest, LinkedList<Double> yTest) {
        double sumx = 0;
        double sumy = 0;
        int nt, nr = xOriginal.size();
        for (int i = 0; i < nr; i++) {
            sumx += xOriginal.get(i);
            sumy += yOriginal.get(i);
        }
        meanXOriginal = sumx / nr;
        meanYOriginal = sumy / nr;
        
        sumx = 0;
        sumy = 0;
        nt = xTest.size();
        for (int i = 0; i < nt; i++) {
            sumx += xTest.get(i);
            sumy += yTest.get(i);
        }
        meanXTest = sumx / nt;
        meanYTest = sumy / nt;
    }

    private double evaluateFormulae(LinkedList<Double> xr, LinkedList<Double> yr, LinkedList<Double> xt, LinkedList<Double> yt, int n) {
        double nume = calculateNumerator(xr, yr, xt, yt, n);
        double deno = calculateDenominator(xr, yr, xt, yt, n);
        
        return (nume / deno);
    }

    private double calculateNumerator(LinkedList<Double> xOr, LinkedList<Double> yOr, LinkedList<Double> xT, LinkedList<Double> yT, int n) {
        double dim1 = 0;
        for (int i = 0; i < n; i++) {
            dim1 += (xOr.get(i) - meanXOriginal) * (xT.get(i) - meanXTest);
        }
        
        double dim2 = 0;
        for (int i = 0; i < n; i++) {
            dim2 += (yOr.get(i) - meanYOriginal) * (yT.get(i) - meanYTest);
        }       
        
        double finalNumeSum = dim1 + dim2; //make the sum of the two dimension
        return finalNumeSum * finalNumeSum; // make the square
    }

    private double calculateDenominator(LinkedList<Double> xr, LinkedList<Double> yr, LinkedList<Double> xt, LinkedList<Double> yt, int n) {
        double t;
        
        //for original signature	
    	double xsum1 = 0;
        for (int i = 0; i < n; i++) {
            t = (xr.get(i) - meanXOriginal);
        	xsum1 += (t*t);
        }
        double ysum1 = 0;
        for (int i = 0; i < n; i++) {
        	t = (yr.get(i) - meanYOriginal);
            ysum1 += (t*t);
        }
        double sumSign1 = xsum1 + ysum1;
       
        //for signature to test
        double xsum2 = 0;
        for (int i = 0; i < n; i++) {
            t = (xt.get(i) - meanXTest);
        	xsum2 += (t*t);
        }
        double ysum2 = 0;
        for (int i = 0; i < n; i++) {
        	t = (yt.get(i) - meanYTest);
            ysum2 += (t*t);
        }
        double sumSign2 = xsum2 + ysum2;
        
        return sumSign1 * sumSign2; //multiplies the two dimensions
    }
}
