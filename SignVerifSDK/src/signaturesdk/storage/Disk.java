package signaturesdk.storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import signaturesdk.beans.SignatureData;
import signaturesdk.beans.SignatureEncrypt;


/**
 * Save and load a signature from hard disk
 * @author Luciano Giuseppe 03-2013
 *
 */
public class Disk {
	/**
	 * The extesion of file to save or to open
	 */
	public final static String FILE_EXTENSION = ".sign";
	
	private final static byte SALT [] = {3, 7, 105,5, 1, 8 ,9 ,15,  122};
 	//Init Vector of 16byte 
 	private final static byte[] mInitVec = {124, 58,45,68, 35, 58 ,69 ,45, 37, 5, 68, 98, 29, 35, 10, 32};
	
 	/**
 	 * Save an object to disk
 	 * @param signature
 	 * @param fileName used as fileName
 	 * @return null if all right otherwise a string with error
 	 */
 	public static String save(Object signature, String fileName) {
 		//save the file
		ObjectOutputStream  oos = null;
		try {
			if(!fileName.endsWith(Disk.FILE_EXTENSION)) fileName = fileName + Disk.FILE_EXTENSION; 
			
			oos = new ObjectOutputStream(new FileOutputStream(fileName));
			oos.writeObject(signature);
			oos.flush();
		} catch (FileNotFoundException e) {
			return e.getMessage();
		} catch (IOException e) {
			return e.getMessage();
		} finally {
			try {
				if(oos !=  null)
					oos.close();
				else
					return "Error in closing";
			} catch (IOException e) {
				return e.getMessage();
			}
		}
		
		return null;
 	}
 	
	/** 
	 * Save a SignatureData to a disk in encrypted form
	 * @param fileName used as fileName
	 * @param signature
	 * @param hash
	 * @return true if everything is gone alright
	 */
	public static boolean saveEncrypted(String fileName, SignatureData signature, String hash) {		
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e1) {
			System.out.println("Error: " + e1.getMessage());
			return false;
		} catch (NoSuchPaddingException e1) {
			System.out.println("Error: " + e1.getMessage());
			return false;
		}
		
    	SecretKeyFactory secKeyFactory = null;
		try {
			secKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		} catch (NoSuchAlgorithmException e1) {
			System.out.println("Error: " + e1.getMessage());
			return false;
		}
		
    	KeySpec keySpec = new PBEKeySpec(hash.toCharArray(), SALT, 10, 128);
    	SecretKey key = null;
		try {
			key = secKeyFactory.generateSecret(keySpec);
		} catch (InvalidKeySpecException e) {
			System.out.println("Error: " + e.getMessage());
			return false;
		}
    	
		SecretKey AESKey = new SecretKeySpec(key.getEncoded(), "AES");
		try {
			cipher.init(Cipher.ENCRYPT_MODE, AESKey, new IvParameterSpec(mInitVec));
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println("Error: " + e.getMessage());
			return false;
		} catch (InvalidKeyException e) {
			System.out.println("Error: " + e.getMessage());
			return false;
		}
		
		SignatureEncrypt signEncr = null;
		try {
			signEncr = new SignatureEncrypt(new SealedObject(signature, cipher), signature.hashCode(), "asa", "asas", "Asa");
		} catch (IllegalBlockSizeException e) {
			System.out.println("Error: " + e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
			return false;
		}
		
		//save the file
		ObjectOutputStream  oos = null;
		try {
			if(!fileName.endsWith(Disk.FILE_EXTENSION)) fileName = fileName + Disk.FILE_EXTENSION;
			
			oos = new ObjectOutputStream(new FileOutputStream(fileName));
			oos.writeObject(signEncr);
			oos.flush();
		} catch (FileNotFoundException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			try {
				if(oos !=  null)
					oos.close();
				else
					return false;
			} catch (IOException e) {}
		}
		return true;
		
		
		//WARNING: "return save(se, "CF");" gives error when open a file
	}
	
	/**
	 * Open a file and load datas
	 * @param CF Used as filename without extension
	 * @return the object read, or a string with error
	 */
	public static Object open(String CF) {
		ObjectInputStream ois = null;
		try {
			if(!CF.endsWith(Disk.FILE_EXTENSION)) CF = CF + Disk.FILE_EXTENSION;
			
			ois = new ObjectInputStream(new FileInputStream(CF));
		} catch (FileNotFoundException e2) {
			return "Error: " + e2.getMessage();
		} catch (IOException e2) {
			return "Error: " + e2.getMessage();
		}
		
		Object retValue = null;
		try {
			retValue = ois.readObject();
		} catch (IOException e) {
			return "Error: " + e.getMessage();
		} catch (ClassNotFoundException e) {
			 return "Error: " + e.getMessage();
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
		
		return retValue;
	}
	
	/**
	 * Try to load an encrypted signature by his hash value
	 * @param CF used as filename without extension
	 * @param hash is the password
	 * @return the signature decrypted or a null value if hash value doesn't decrypt the file or there's an error
	 */
	public static SignatureData openEncrypted(String CF, String hash) {
				
		SignatureEncrypt signEncr = null;
		try {
			if(!CF.endsWith(Disk.FILE_EXTENSION)) CF = CF + Disk.FILE_EXTENSION;
			
			signEncr = (SignatureEncrypt) open(CF);
		} catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
			return null;
		}
		
		
		SecretKeyFactory secKeyFactory = null;
		try {
			secKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		} catch (NoSuchAlgorithmException e2) {
			System.out.println("Error: " + e2.getMessage());
			return null;
		}
		
		KeySpec  keySpec = new PBEKeySpec(hash.toCharArray(), SALT, 10, 128);
		SecretKey key = null;
		try {
			key = secKeyFactory.generateSecret(keySpec);
		} catch (InvalidKeySpecException e) {
			System.out.println("Error: " + e.getMessage());
			return null;
		}
		
		SecretKey AESKey = new SecretKeySpec(key.getEncoded(), "AES");
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e2) {
			System.out.println("Error: " + e2.getMessage());
			return null;
		} catch (NoSuchPaddingException e2) {
			System.out.println("Error: " + e2.getMessage());
			return null;
		}
		
		try {
			cipher.init(Cipher.DECRYPT_MODE, AESKey, new IvParameterSpec(mInitVec));
		} catch (InvalidAlgorithmParameterException e1) {
			System.out.println("Error: " + e1.getMessage());
			return null;
		} catch (InvalidKeyException e) {
			System.out.println("Error: " + e.getMessage());
			return null;
		}
		
		SignatureData signData = null;
		try {
			signData = (SignatureData)signEncr.getEncryptSignature().getObject(cipher);
		} catch (ClassNotFoundException e) {
			System.out.println("Error: " + e.getMessage());
		} catch(BadPaddingException e) {
			System.out.println("Error: " + e.getMessage());
		} catch(IllegalBlockSizeException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
		
		return signData;
	}
}
