package utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import business.Signature;
import business.User;
import dataAccessLayer.DatabaseHandler;

public class Authentication {
	
	private static String secret = "qwertyasdfgh1234567890";

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
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").acceptExpiresAt(86400).build();
		    DecodedJWT jwt = verifier.verify(token);
		    
		    String email = jwt.getClaim("user").asString();
		    User user = DatabaseHandler.getInstance().getUserByEmail(email);
		    return user;

		} catch (UnsupportedEncodingException exception) {
			return null;
		} catch (JWTVerificationException exception) {
			return null;
		} catch (Exception exception) {
			return null;
		}
	}
	
	public boolean validateSignature(Signature signature) {
		return false;
	}
	
	private String createToken(String username) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    String token = JWT.create().withClaim("user", username).withIssuer("auth0").sign(algorithm);
		    return token;
		} catch (UnsupportedEncodingException exception) {
			
		} catch (JWTCreationException exception){
			
		}
		return null;
	}
	
}
