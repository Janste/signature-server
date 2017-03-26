import static spark.Spark.get;
import static spark.Spark.post;

import java.util.List;

import org.apache.log4j.BasicConfigurator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import business.Signature;
import business.User;
import dataAccessLayer.DatabaseHandler;
import utils.Authentication;
import utils.SignRequest;

public class Main {
    public static void main(String[] args) {
    	BasicConfigurator.configure();
    	DatabaseHandler.getInstance();
        
        post("/register", (req, res) -> {
        	
        	JsonObject jsonObject = new JsonParser().parse(req.body()).getAsJsonObject();
        	String email = jsonObject.get("user").getAsJsonObject().get("email").getAsString();
        	String password = jsonObject.get("user").getAsJsonObject().get("password").getAsString();
        	
        	Authentication auth = new Authentication();
        	try {
        		User user = auth.register(email, password);
            	res.status(200);
            	res.body("{\"token\":\"" + user.getToken() + "\",\"uuid\":\"" + user.getUUID() + "\"}");
    			return res.body();
        	} catch (IllegalArgumentException e) {
        		res.status(422);
        		return e.getMessage();
        	}
        	
        });
        
        post("/register/:uuid", (req, res) -> {
        	String uuid = req.params(":uuid");
        	String token = req.headers("token");
        	JsonObject jsonObject = new JsonParser().parse(req.body()).getAsJsonObject();
        	JsonArray signature = jsonObject.get("signature").getAsJsonArray();
        	
        	if (signature.size() < 1) {
        		res.body("{\"error\": \"Invalid signature\"}");
        		res.status(422);
        		return res.body();
        	}
        	
        	Authentication auth = new Authentication();
        	User user = auth.validateToken(token);
        	
        	if (user != null && uuid.equals(user.getUUID())) {
        		Signature sig = new Signature(signature);
            	user.updateSignature(sig);
            	DatabaseHandler.getInstance().saveSignature(user);
            	res.status(200);
            	return "";
        	} else {
        		res.status(401);
        		res.body("{\"error\": \"Invalid credentials\" }");
        		return res.body();
        	}
        	
        	
        });
        
        post("/login", (req, res) -> {
        	
        	JsonObject jsonObject = new JsonParser().parse(req.body()).getAsJsonObject();
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
        	String token = req.headers("token");
        	JsonObject jsonObject = new JsonParser().parse(req.body()).getAsJsonObject();
        	String document = jsonObject.get("user").getAsJsonObject().get("document").getAsString();
        	
        	Authentication auth = new Authentication();
        	User user = auth.validateToken(token);
        	
        	if (user != null) {
        		SignRequest signRequest = new SignRequest();
        		signRequest.setUUID(Integer.parseInt(user.getUUID()));
        		signRequest.setDocument(document);
        		if (DatabaseHandler.getInstance().checkIfRequestAlreadyExists(signRequest)) {
        			res.status(400);
        			res.body("{\"error\": \"Document already exists\" }");
            		return res.body();
        		} else {
        			DatabaseHandler.getInstance().saveNewRequest(signRequest);
                	res.status(200);
                	return "";
        		}
        	} else {
        		res.status(401);
        		res.body("{\"error\": \"Invalid credentials\" }");
        		return res.body();
        	}
        	
        });
        
        get("/request", (req, res) -> {
        	
        	String token = req.headers("token");
        	Authentication auth = new Authentication();
        	User user = auth.validateToken(token);

        	if (user != null) {
        		
        		List<SignRequest> requests = DatabaseHandler.getInstance().checkForRequests(user);
        		StringBuilder sb = new StringBuilder();
        		sb.append("{\"requests\":[");
            	for (int i = 0; i < requests.size(); i++) {
            		sb.append("\"id\":\"" + requests.get(i).getDocument() + "\"");
            		if (!(i + 1 == requests.size())) {
            			sb.append(",");
            		}
				}
            	
            	sb.append("]}");
            	res.status(200);
            	res.body(sb.toString());
            	return res.body();
        	} else {
        		res.status(401);
        		res.body("{\"error\": \"Invalid credentials\" }");
        		return res.body();
        	}
        	
        });
        
        post("/request/:id", (req, res) -> {
        	String token = req.headers("token");
        	JsonObject jsonObject = new JsonParser().parse(req.body()).getAsJsonObject();
        	String document = req.params(":id");
        	
        	Authentication auth = new Authentication();
        	User user = auth.validateToken(token);
        	
        	if (user != null) {
        		JsonArray sigObject = jsonObject.get("signature").getAsJsonArray();
            	Signature sig = new Signature(sigObject);
            	Signature storedSig = DatabaseHandler.getInstance().getSignature(user);

            	if (storedSig == null) {
            		res.status(401);
            		res.body("{\"error\": \"Invalid signature\" }"); 
            	} else if (!storedSig.match(sig)) {
            		res.status(401);
            		res.body("{\"error\": \"Invalid signature\" }"); 
            	}
            	else {
            		SignRequest request = new SignRequest();
            		request.setUUID(Integer.parseInt(user.getUUID()));
            		request.setDocument(document);
            		DatabaseHandler.getInstance().saveSignatureInRequest(request);
            		res.status(200);
            		res.body("");
            	}
        	} else {
        		res.status(401);
        		res.body("{\"error\": \"Invalid credentials\" }");
        	}
        	
        	return res.body();
        });
        
        get("/request/:id", (req, res) -> {
        	String token = req.headers("token");
        	String document = req.params(":id");
        	
        	Authentication auth = new Authentication();
        	User user = auth.validateToken(token);
        	
        	if (user != null) {
        		SignRequest request = new SignRequest();
        		request.setUUID(Integer.parseInt(user.getUUID()));
        		request.setDocument(document);
        		
        		if (DatabaseHandler.getInstance().checkIfRequestIsSigned(request)) {
        			Signature sig = DatabaseHandler.getInstance().getSignature(user);
        			res.status(200);
        			res.body(sig.getAsJsonString());
        		} else {
        			res.status(204);
        			res.body("{\"info\": \"Not signed\" }");
        		}
        	} else {
        		res.status(401);
        		res.body("{\"error\": \"Invalid credentials\" }");
        	}
        	
        	return res.body();
        });
    }
}