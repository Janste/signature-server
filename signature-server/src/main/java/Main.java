import static spark.Spark.*;

import org.apache.log4j.BasicConfigurator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import business.Signature;
import business.User;
import utils.Authentication;

public class Main {
    public static void main(String[] args) {
    	BasicConfigurator.configure();
        
        post("/register", (req, res) -> {
        	
        	JsonObject jsonObject = new JsonParser().parse(req.headers("user")).getAsJsonObject();
        	String email = jsonObject.get("user").getAsJsonObject().get("email").getAsString();
        	String password = jsonObject.get("user").getAsJsonObject().get("password").getAsString();
        	
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
        	
        	String uuid = req.params(":uuid");
        	JsonObject jsonObject = new JsonParser().parse(req.headers("user")).getAsJsonObject();
        	String token = jsonObject.get("user").getAsJsonObject().get("token").getAsString();
        	String signature = jsonObject.get("user").getAsJsonObject().get("signature").getAsString();
        	
        	// TODO: Check if token and signature is valid
        	
        	res.status(200);
        	return res;
        });
        
        post("/login", (req, res) -> {
        	
        	JsonObject jsonObject = new JsonParser().parse(req.headers("user")).getAsJsonObject();
        	String email = jsonObject.get("user").getAsJsonObject().get("email").getAsString();
        	String password = jsonObject.get("user").getAsJsonObject().get("password").getAsString();
        	
        	Authentication auth = new Authentication();
        	try {
        		User user = auth.login(email, password);
            	res.status(200);
            	res.body("{\"token\":\"" + user.getToken() + "\",\"uuid\":\"" + user.getUUID() + "\"}");
    			return res.body();
        	} catch (IllegalArgumentException e) {
        		res.status(401);
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
        	JsonObject jsonObject = new JsonParser().parse(req.headers("user")).getAsJsonObject();
        	//TODO check token
        	JsonArray sigObject = new JsonParser().parse(req.headers("signature")).getAsJsonArray();
        	Signature sig = new Signature(sigObject);
        	
        	//TODO load persisted sig and match
        	//
//        	if (!storedSig.match(sig)) {
//        		res.status(401);
//        		res.body("{\"error\": \"Invalid signature\" }"); 
//        	}
//        	else {
//        		res.status(200);
//        	}
        	return res.body();
        });
        
        get("/request/:uuid", (req, res) -> {
        	return "get request uuid";
        });
    }
}