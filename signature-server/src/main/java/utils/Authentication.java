package utils;

import java.io.UnsupportedEncodingException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import business.Signature;
import business.User;
import dataAccessLayer.DatabaseHandler;

public class Authentication {

	public User register(String email, String password) throws IllegalArgumentException {
		
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		
		if (DatabaseHandler.getInstance().saveUser(user)) {
			user.setUUID(Integer.toString(DatabaseHandler.getInstance().getUserUUID(email)));
			return user;
		} else {
			throw new IllegalArgumentException("Email already in use");
		}
	}
	
	public User login(String email, String password) throws IllegalArgumentException {
		User user = DatabaseHandler.getInstance().getUserByEmailAndPassword(email, password);
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
