package utils;

import java.io.UnsupportedEncodingException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import business.Signature;
import business.User;

public class Authentication {

	public User register(String email, String password) throws IllegalArgumentException {
		
		// TODO: Add user to DB.
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setUUID("1");
		return user;
	}
	
	public User login(String email, String password) throws IllegalArgumentException {
		
		// TODO: Fetch user from DB.
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setUUID("1");
		user.setToken(createToken(user.getEmail()));
		return user;
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
