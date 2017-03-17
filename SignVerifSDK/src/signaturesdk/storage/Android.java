//TODO: an-comment the code to use with android (in Eclipse select all and Ctrl+7)
//
//package signaturesdk.storage;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//
//import android.content.Context;
//import android.content.ContextWrapper;
//import android.os.Environment;
//
///**
// * Save signature to internal or external storage
// * @author Luciano Giuseppe 05/2013
// *
// */
//public class Android {
//	public final static String FILE_EXTENSION = ".sign";
//	
//	/**
//	 * Save the signature object to the external storage
//	 * @param signature Object contains the signature
//	 * @param fileName
//	 * @return null if operation is ok, otherwise return a string with error description
//	 */
//	public static String saveToSd(Object signature, String fileName) {
//
//		String state = Environment.getExternalStorageState();
//	    if (!Environment.MEDIA_MOUNTED.equals(state)) {
//	       return "App can't write on SD";
//	    }
//
//	    
//		
//		ObjectOutputStream  oos = null;
//		try {
//			if(!fileName.endsWith(Android.FILE_EXTENSION)) 
//				fileName = fileName + Android.FILE_EXTENSION; 
//			
//			File file = new File(Environment.getExternalStorageDirectory(), fileName);
//			oos = new ObjectOutputStream(new FileOutputStream(file));
//			oos.writeObject(signature);
//			oos.flush();
//		} catch (FileNotFoundException e) {
//			return e.getMessage();
//		} catch (IOException e) {
//			return e.getMessage();
//		} finally {
//			try {
//				if(oos !=  null)
//					oos.close();
//				else
//					return "Error in closing";
//			} catch (IOException e) {
//				return e.getMessage();
//			}
//		}
//		
//		return null;
//	}
//	
//	
//	/**
//	 * Save the signature to internal storage
//	 * @param signature Object contains the signature
//	 * @param fileName
//	 * @param context App Context
//	 * @return null if operation is ok, otherwise return a string with error description
//	 */
//	public static String saveToInternal(Object signature, String fileName, Context context) {
//		
// 
//		ObjectOutputStream  oos = null;
//		try {
//			if(!fileName.endsWith(Android.FILE_EXTENSION)) 
//				fileName = fileName + Android.FILE_EXTENSION; 
//			
//			
//			File file = new File(context.getFilesDir(), fileName); 
//			oos = new ObjectOutputStream(new FileOutputStream(file));
//			oos.writeObject(signature);
//			oos.flush();
//		} catch (FileNotFoundException e) {
//			return e.getMessage();
//		} catch (IOException e) {
//			return e.getMessage();
//		} finally {
//			try {
//				if(oos !=  null)
//					oos.close();
//				else
//					return "Error in closing";
//			} catch (IOException e) {
//				return e.getMessage();
//			}
//		}
//		
//		return null;
//	}
//}
