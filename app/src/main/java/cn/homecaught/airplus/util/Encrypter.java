package cn.homecaught.airplus.util;

import android.util.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Encrypter {

	private static final String KEY = "123456";

	/**
	 * des����
	 * @param strMessage
	 * @return
	 */
	public static String encryptByDes(String strMessage){
		try{
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS7Padding");
			IvParameterSpec zeroIv = new IvParameterSpec(new byte[cipher.getBlockSize()]);
			SecretKeySpec key = new SecretKeySpec(KEY.getBytes(),"DES");
			
			cipher.init(Cipher.ENCRYPT_MODE, key,zeroIv);
			byte[] byteMi = cipher.doFinal(strMessage.getBytes("UTF8"));
			
			String strMi = Base64.encodeToString(byteMi, Base64.DEFAULT);
			
			return strMi;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/***
	 * des����
	 * @param strMi
	 * @return
	 */
	public static String decryptByDes(String strMi) {
		try {
			byte[] byteMi = Base64.decode(strMi, Base64.DEFAULT);
			
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS7Padding");
			IvParameterSpec zeroIv = new IvParameterSpec(new byte[cipher.getBlockSize()]);
			SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), "DES");
			
			cipher.init(Cipher.DECRYPT_MODE, key,zeroIv);
			byte[] byteMing = cipher.doFinal(byteMi);
			
			String strMing = new String(byteMing, "UTF8");
			return strMing;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * md5����
	 * @param strPassword
	 * @return
	 */
	public static String encryptByMD5(String strPassword){
		String strPasswordMD5 = "";
		try{
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte buf[] = digest.digest(strPassword.getBytes());
			String stmp = null;
			for (int n = 0; n < buf.length; n++) {
				stmp = Integer.toHexString(buf[n] & 0xff);
				strPasswordMD5 = stmp.length() == 1 ? 
						(strPasswordMD5 + "0" + stmp) : (strPasswordMD5 + stmp);
			}
		}catch(Exception e){
			
		}
		return strPasswordMD5;
	}
	
	/***
	 * sha��1����
	 * @param strMessage
	 * @return
	 */
	public static String encryptBySHA1(String strMessage){
		String strEncrypt = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			byte buf[] = digest.digest(strMessage.getBytes());
			String stmp = null;
			for (int n = 0; n < buf.length; n++) {
				stmp = Integer.toHexString(buf[n] & 0xff);
				strEncrypt = stmp.length() == 1 ? (strEncrypt + "0" + stmp) : (strEncrypt + stmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strEncrypt;
	}
}
