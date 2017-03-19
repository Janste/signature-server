package dataAccessLayer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import business.Signature;
import business.User;

public class DatabaseHandler {
	
	private String databaseName;
	private Connection connection;
	private static DatabaseHandler instance = null;
	
	public static DatabaseHandler getInstance() {
		if (instance == null) {
			instance = new DatabaseHandler();
		}
		return instance;
	}
	
	private DatabaseHandler() {
		databaseName = "jdbc:sqlite:signature_server_database.db";
		File setup = new File("signature_server_database.sql");

		if (setup.exists()) {
			try {
				openConnection();

				DatabaseMetaData meta = connection.getMetaData();
				ResultSet rs = meta.getTables(null, null, "user", null);
				if (!rs.next()) {
					Scanner scan = new Scanner(new FileReader(setup));
					scan.useDelimiter("(;(\r)?\n)|(--\n)");
					Statement creationStatement = connection.createStatement();
					while (scan.hasNext()) {
						String line = scan.next();
						if (line.startsWith("/*!") && line.endsWith("*/")) {
							int i = line.indexOf(' ');
							line = line.substring(i + 1, line.length() - " */".length());
						}

						if (line.trim().length() > 0) {
							creationStatement.execute(line);
						}
					}
					creationStatement.close();
					scan.close();
					connection.commit();
				}
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void openConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection(databaseName);
			this.connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public boolean saveUser(User user) {
		return UserHelper.saveUser(connection, user);
	}
	
	public int getUserUUID(String email) {
		return UserHelper.getUserUUID(connection, email);
	}
	
	public User getUserByUUID(String uuid) {
		return UserHelper.getUserByUUID(connection, uuid);
	}
	
	public User getUserByEmail(String email) {
		return UserHelper.getUserByEmail(connection, email);
	}
	
	public User getUserByEmailAndPassword(String email, String password) {
		return UserHelper.getUserByEmailAndPassword(connection, email, password);
	}
	
	public boolean saveSignature(User user) {
		return SignatureHelper.saveSignature(connection, user);
	}
	
	public Signature getSignature(User user) {
		return SignatureHelper.getSignature(connection, user);
	}
	
}
