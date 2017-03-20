package utils;

import business.Signature;

public class SignRequest {
	
	private int uuid;
	private String document;
	private Signature signature;
	
	public int getUUID() {
		return uuid;
	}
	public void setUUID(int uuid) {
		this.uuid = uuid;
	}
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
	public Signature getSignature() {
		return signature;
	}
	public void setSignature(Signature signature) {
		this.signature = signature;
	}

}
