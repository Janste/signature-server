package dataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import business.User;

public class UserHelper {
	
	public static boolean saveUser(Connection connection, User user) {
		
		try {
			String sql = "INSERT INTO USER VALUES (?, ?, ?);";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.executeUpdate();
			ps.close();
			connection.commit();
		} catch (SQLException e) {
			System.err.println("Email already in use.");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static int getUserUUID(Connection connection, String email) {
		int uuid = -1;
		try {
			String sql = "SELECT uuid FROM USER WHERE email = ?;";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, email);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				uuid = result.getInt("uuid");
			}
			result.close();
			statement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return uuid;
	}
	
	public static User getUserByUUID(Connection connection, String uuid) {
		User user = null;
		try {
			String sql = "SELECT * FROM USER WHERE uuid = ?;";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, uuid);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				user = new User();
				user.setEmail(result.getString("email"));
				user.setPassword(result.getString("password"));
				user.setUUID(uuid);
				
			}
			result.close();
			statement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return user;
	}
	
	public static User getUserByEmail(Connection connection, String email) {
		User user = null;
		try {
			String sql = "SELECT * FROM USER WHERE email = ?;";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, email);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				user = new User();
				user.setEmail(result.getString("email"));
				user.setPassword(result.getString("password"));
				user.setUUID(Integer.toString(result.getInt("uuid")));
			}
			result.close();
			statement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return user;
	}
	
	public static User getUserByEmailAndPassword(Connection connection, String email, String password) {
		User user = null;
		try {
			String sql = "SELECT * FROM USER WHERE email = ?;";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, email);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				String hashedPassword = result.getString("password");
				if (hashedPassword.equals(password)) {
					user = new User();
					user.setEmail(result.getString("email"));
					user.setPassword(result.getString("password"));
					user.setUUID(Integer.toString(result.getInt("uuid")));
				}
			}
			result.close();
			statement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return user;
	}
	
}
