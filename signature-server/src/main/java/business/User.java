package business;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
	private String uuid;
	private String email;
	private String password;
	private String token;
	private Signature signature;
	
	public User() { }
	
	public String getUUID() {
		return uuid;
	}
	
	public void setUUID(String id) {
		uuid = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		Pattern rx = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = rx.matcher(email);
        if (!matcher.find())
        	throw new IllegalArgumentException("Invalid email");
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public Signature getSignature() {
		return signature;
	}
	
	public void updateSignature(Signature sig) {
		signature = sig;
	}
	
	public boolean verifySignature(Signature other) {
		if (signature == null)
			return false;
		return signature.match(other);
	}
}
