package signaturesdk.beans;

import java.io.Serializable;
import java.util.LinkedList;

import signaturesdk.beans.AcquisitionSignWord;

/**
 * Contains the original words of signature
 * @author Luciano Giuseppe
 *
 */
public class SignatureData implements Serializable {

	private static final long serialVersionUID = 1L;

	private LinkedList<AcquisitionSignWord> signature;
   
    public SignatureData(LinkedList<AcquisitionSignWord> signature) {
        this.signature = signature;
    }
    
    
    public LinkedList<AcquisitionSignWord> getSignature() {
		return signature;
	}
	

	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignatureData that = (SignatureData) o;
        if (signature != null ? !signature.equals(that.signature) : that.signature != null) return false;
        
        return true;
    }

    public int hashCode() {
        int result;
        result = (signature != null ? signature.hashCode() : 0);
        return result;
    }
}
