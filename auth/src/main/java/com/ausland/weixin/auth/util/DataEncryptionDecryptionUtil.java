package com.ausland.weixin.auth.util;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.ArrayUtils;

public class DataEncryptionDecryptionUtil {
	public static SecretKey getSecretEncryptionKey() throws Exception {
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(256); // The AES key size in number of bits
		SecretKey secKey = generator.generateKey();
		System.out.println("Algorithms: " + generator.getAlgorithm());
		return secKey;
	}
	
	public static byte[] encryptText(String plainText, byte[] iv, String key) throws Exception {
		Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		
		byte[] keyArray = Base64.getDecoder().decode(hexToBase64(key));

		SecretKeySpec keySepc = new SecretKeySpec(keyArray, "AES");
		
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		aesCipher.init(Cipher.ENCRYPT_MODE, keySepc, ivSpec);
		byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
		return byteCipherText;
	}
	
	public static String decryptText(byte[] iv, byte[] byteCipherText, String key) throws Exception {
		Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] keyArray = Base64.getDecoder().decode(hexToBase64(key));

		SecretKeySpec keySepc = new SecretKeySpec(keyArray, "AES");
		
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		aesCipher.init(Cipher.DECRYPT_MODE, keySepc, ivSpec);
		byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
		return new String(bytePlainText);
	}

	public static String bytesToHex(byte[] hash) {
		return DatatypeConverter.printHexBinary(hash);
	}
	
	public static String bytesToBase64(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}
	
	public static String hexToBase64(String hex) {
		return bytesToBase64(DatatypeConverter.parseHexBinary(hex));
	}
	
	public static byte[] createIV(String msg) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5"); 
		byte[] hash = md.digest(msg.getBytes("UTF-8"));		

		return hash;
	}
	
	public static String finalEncryptedData(byte[] iv, byte[] key) {
		byte[] finalArray = ArrayUtils.addAll(iv, key);
				
		return bytesToBase64(finalArray);
	}
	public static String decryption(String encryptedData, String key) throws Exception {
		byte[] bytes = Base64.getDecoder().decode(encryptedData);
		byte[] iv = Arrays.copyOfRange(bytes, 0, 16);
		byte[] data = Arrays.copyOfRange(bytes, 16, bytes.length);
		return decryptText(iv, data, key);
	}
	public static String encryption(String data, String key, String randomStr) throws Exception {
		byte[] iv = createIV(randomStr);
		byte[] cipher = encryptText(data, iv, key);
		return finalEncryptedData(iv, cipher);
	}
}
