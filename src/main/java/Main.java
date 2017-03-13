import static spark.Spark.*;

import org.apache.log4j.BasicConfigurator;

public class Main {
    public static void main(String[] args) {
    	BasicConfigurator.configure();
        
        post("/register", (req, res) -> {
        	return "register";
        });
        
        post("/register/:uuid", (req, res) -> {
        	return "register uuid";
        });
        
        post("/login", (req, res) -> {
        	return "login";
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