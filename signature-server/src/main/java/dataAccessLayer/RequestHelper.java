package dataAccessLayer;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
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
	
	public static boolean checkIfRequestAlreadyExists(Connection connection, SignRequest request) {
		boolean response = false;
		try {
			String sql = "SELECT * FROM REQUEST WHERE document = ?;";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, request.getDocument());
			ResultSet result = ps.executeQuery();
			if (result.next()) {
				response = true;
			}
			result.close();
			ps.close();
		} catch (SQLException e) {
			response = true;
		}
		return response;
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
	
	public static boolean saveSignatureInRequest(Connection connection, SignRequest request) {
		try {
			String sql = "UPDATE REQUEST SET signature = ? WHERE uuid = ? AND document = ?;";
			PreparedStatement ps = connection.prepareStatement(sql);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream objOut = new ObjectOutputStream(baos);
			objOut.writeObject(request.getSignature());
			byte[] bytes = baos.toByteArray();
			ps.setBytes(1, bytes);
			ps.setInt(2, request.getUUID());
			ps.setString(3, request.getDocument());
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
	
	public static boolean checkIfRequestIsSigned(Connection connection, SignRequest request) {
		boolean response = false;
		try {
			String sql = "SELECT * FROM REQUEST WHERE uuid = ? AND document = ? AND signature IS NOT NULL;";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, request.getUUID());
			ps.setString(2, request.getDocument());
			ResultSet result = ps.executeQuery();
			if (result.next()) {
				response = true;
			}
			result.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return response;
	}

}
