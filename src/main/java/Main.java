import static spark.Spark.*;

import org.apache.log4j.BasicConfigurator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import business.User;
import utils.Authentication;

public class Main {
    public static void main(String[] args) {
    	BasicConfigurator.configure();
        
        post("/register", (req, res) -> {
        	
        	JsonObject jsonObject = new JsonParser().parse(req.headers("user")).getAsJsonObject();
        	String email = jsonObject.get("user").getAsJsonObject().get("email").getAsString();
        	String password = jsonObject.get("user").getAsJsonObject().get("email").getAsString();
        	
        	Authentication auth = new Authentication();
        	try {
        		User user = auth.register(email, password);
            	res.status(200);
            	res.body("{\"uuid\":\"" + user.getUUID() + "\"}");
    			return res.body();
        	} catch (IllegalArgumentException e) {
        		res.status(422);
        		return e.getMessage();
        	}
        	
        });
        
        post("/register/:uuid", (req, res) -> {
        	return "register uuid";
        });
        
        post("/login", (req, res) -> {
        	
        	JsonObject jsonObject = new JsonParser().parse(req.headers("user")).getAsJsonObject();
        	String email = jsonObject.get("user").getAsJsonObject().get("email").getAsString();
        	String password = jsonObject.get("user").getAsJsonObject().get("email").getAsString();
        	
        	Authentication auth = new Authentication();
        	try {
        		User user = auth.login(email, password);
            	res.status(200);
            	res.body("{\"token\":\"" + user.getToken() + "\",\"uuid\":\"" + user.getUUID() + "\"}");
    			return res.body();
        	} catch (IllegalArgumentException e) {
        		res.status(422);
        		return e.getMessage();
        	}
        	
        });
        
        post("/request", (req, res) -> {
        	return "post request";
        });
        
        get("/request", (req, res) -> {
        	return "get request";
        });
        
        post("/request/:uuid", (req, res) -> {
        	return "post request uuid";
        });
        
        get("/request/:uuid", (req, res) -> {
        	return "get request uuid";
        });
        
    }
}