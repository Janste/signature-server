package dataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import business.User;
import utils.SignRequest;

public class RequestHelper {
	
	public static boolean saveNewRequest(Connection connection, SignRequest request) {
		try {
			String sql = "INSERT INTO REQUEST VALUES (?, ?, ?, ?);";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(2, request.getUUID());
			ps.setString(3, request.getDocument());
			ps.setNull(4, java.sql.Types.BLOB);
			ps.executeUpdate();
			ps.close();
			connection.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static List<SignRequest> checkForRequests(Connection connection, User user) {
		
		List<SignRequest> requests = new ArrayList<>();
		try {
			String sql = "SELECT * FROM REQUEST WHERE uuid = ? AND signature IS NULL;";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, user.getUUID());
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				SignRequest req = new SignRequest();
				req.setUUID(result.getInt("uuid"));
				req.setDocument(result.getString("document"));
				requests.add(req);
			}
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requests;
	}

}
