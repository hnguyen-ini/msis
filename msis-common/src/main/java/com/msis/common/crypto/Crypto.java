package com.msis.common.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceStatus;

public class Crypto {
	public static String encryptString(String key, String text) throws ServiceException {
		AES aes = new AES(key);
		return aes.encryptString(text);
	}
	
	public static String decryptString(String key, String text) throws ServiceException {
		AES aes = new AES(key);
		return aes.decryptString(text);
	}
	
	public static String encryptAESKeyByPublicKeyString(String aesKey, String strPublicKey) throws ServiceException {
		try {
			KeyGeneration genKey = new KeyGeneration();
			PublicKey publicKey = genKey.getPublicKeyFromStr64(strPublicKey);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return Base64.encodeBase64String(cipher.doFinal(aesKey.getBytes("UTF-8")));
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.CRYPTO_ERROR, e.getMessage());
		}
	}
	
	public static String decryptAESKeyByPrivateKeyString(String aesKey, String strPrivateKey) throws ServiceException {
		try {
			KeyGeneration genKey = new KeyGeneration();
			PrivateKey privateKey = genKey.getPrivateKeyFromStr64(strPrivateKey);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(cipher.doFinal(Base64.decodeBase64(aesKey)), "UTF-8");
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.CRYPTO_ERROR, e.getMessage());
		}
	}
}
