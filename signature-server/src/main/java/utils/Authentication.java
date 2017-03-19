package utils;

import java.io.UnsupportedEncodingException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import business.Signature;
import business.User;
import dataAccessLayer.DatabaseHandler;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Authentication {

	public User register(String email, String password) throws IllegalArgumentException, NoSuchAlgorithmException {
		
		User user = new User();
		user.setEmail(email);
		
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(password.getBytes());
		String hashedPassword = new String(messageDigest.digest());
		user.setPassword(hashedPassword);
		
		if (DatabaseHandler.getInstance().saveUser(user)) {
			user.setUUID(Integer.toString(DatabaseHandler.getInstance().getUserUUID(email)));
			return user;
		} else {
			throw new IllegalArgumentException("Email already in use");
		}
	}
	
	public User login(String email, String password) throws IllegalArgumentException, NoSuchAlgorithmException {
		
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(password.getBytes());
		String hashedPassword = new String(messageDigest.digest());
		
		User user = DatabaseHandler.getInstance().getUserByEmailAndPassword(email, hashedPassword);
		if (user != null) {
			user.setToken(createToken(user.getEmail()));
			return user;
		} else {
			throw new IllegalArgumentException("Invalid credentials");
		}

	}
	
	public User validateToken(String token) {
		return null;
	}
	
	public boolean validateSignature(Signature signature) {
		return false;
	}
	
	private String createToken(String secret) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    String token = JWT.create().withIssuer("auth0").sign(algorithm);
		    return token;
		} catch (UnsupportedEncodingException exception){
			
		} catch (JWTCreationException exception){
			
		}
		return null;
	}
	
}
