package signaturesdk.beans;

import java.io.Serializable;

import javax.crypto.SealedObject;

/**
 * Use this class to save encrypted signature, see signaturesdk.storage.Disk.saveEncrypted method
 * @author Luciano Giuseppe
 *
 */
public class SignatureEncrypt implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private SealedObject encryptSignature;
	private int originalObjectHash;
	private String Nome, Cognome, CF;
	
	public SignatureEncrypt(SealedObject encryptSignature,
			int objHash, String nome, String cognome, String cF) {
		super();
		this.encryptSignature = encryptSignature;
		this.originalObjectHash = objHash;
		Nome = nome;
		Cognome = cognome;
		CF = cF;
	}
	
	public SealedObject getEncryptSignature() {
		return encryptSignature;
	}
	public int getSignHash() {
		return originalObjectHash;
	}
	public String getNome() {
		return Nome;
	}
	public String getCognome() {
		return Cognome;
	}
	public String getCF() {
		return CF;
	}
}
