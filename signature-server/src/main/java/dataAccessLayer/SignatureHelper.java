package dataAccessLayer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import business.Signature;
import business.User;

public class SignatureHelper {

	public static boolean saveSignature(Connection connection, User user) {

		try {
			String sql = "INSERT INTO SIGNATURE VALUES (?, ?, ?);";
			PreparedStatement ps = connection.prepareStatement(sql);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream objOut = new ObjectOutputStream(baos);
			objOut.writeObject(user.getSignature());
			byte[] bytes = baos.toByteArray();
			ps.setBytes(2, bytes);
			ps.setString(3, user.getUUID());
			ps.executeUpdate();
			ps.close();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public static Signature getSignature(Connection connection, User user) {
		Signature sig = null;
		byte[] b = null;
		try {
			String sql = "SELECT * FROM SIGNATURE WHERE uuid = ?;";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, user.getUUID());
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				b = result.getBytes("signature");
				ByteArrayInputStream bin = new ByteArrayInputStream(b);
				ObjectInputStream objIn = new ObjectInputStream(bin);
				sig = (Signature)objIn.readObject();
			}
			result.close();
			statement.close();
		} catch (SQLException | IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return sig;
	}

}
