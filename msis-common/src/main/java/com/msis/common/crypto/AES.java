package com.msis.common.crypto;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.msis.common.parser.StringHexParser;
import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceStatus;

public class AES {
	private static String encryptionAlgorithm =  "AES";    
    private String encryptionKey;
    private byte[] salt;
    private byte[] iv;
    private SecretKeySpec secretKey;
    
    public AES(String encryptKey) {
    	this.encryptionKey = encryptKey;
    }
    
    public AES(String key, String salt, String iv) throws ServiceException{
    	this.encryptionKey = key;
    	this.salt = StringHexParser.hexStringToByteArray(salt);
    	this.iv = StringHexParser.hexStringToByteArray(iv);
    	secretKey = (SecretKeySpec) generateKeyFromPassword(key, this.salt);
    }
    
    private SecretKey generateKeyFromPassword(String password, byte[] saltBytes) throws ServiceException {
    	try {
	        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), saltBytes, 234, 128);
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	        SecretKey secretKey = keyFactory.generateSecret(keySpec);
	        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    	} catch (Exception e) {
    		throw new ServiceException(ServiceStatus.CRYPTO_ERROR, "AES: Generation Key Error, " + e.getMessage());
    	}
    }
    
    private Cipher getCipherIV(int mode) throws ServiceException {
    	try {
    		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
    		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    	    cipher.init(mode, secretKey, ivParameterSpec);
    	    return cipher;
    	} catch (Exception e) {
    		throw new ServiceException(ServiceStatus.CRYPTO_ERROR, "AES: Initial Cipher Failed " + e.getMessage());
    	}
    }
    
    public String decryptIV(String text) throws ServiceException {
    	try {
    		if (text == null || text.isEmpty())
    			return text;
    		byte[] plainBytes = getCipherIV(Cipher.DECRYPT_MODE).doFinal(Base64.decodeBase64(text));
            return new String(plainBytes);
    	} catch (Exception e) {
    		throw new ServiceException(ServiceStatus.CRYPTO_ERROR, "AES: Decryption Failed " + e.getMessage());
    	}
    }
    
    public String encryptIV(String text) throws ServiceException {
    	try {
    		if (text == null || text.isEmpty())
    			return text;
    		byte[] plainBytes = getCipherIV(Cipher.ENCRYPT_MODE).doFinal(text.getBytes());
            return Base64.encodeBase64String(plainBytes);
    	} catch (Exception e) {
    		throw new ServiceException(ServiceStatus.CRYPTO_ERROR, "AES: Decryption Failed " + e.getMessage());
    	}
    }

    private Cipher getCipher(int cipherMode) throws ServiceException {
        try {
	    	SecretKeySpec keySpecification = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), encryptionAlgorithm);
	        Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
	        cipher.init(cipherMode, keySpecification);
	        return cipher;
        } catch (Exception e) {
        	throw new ServiceException(ServiceStatus.CRYPTO_ERROR, "AES: Initial Cipher Failed " + e.getMessage());
        }
    }
    
    public String encryptString(String plainText) throws ServiceException {
        try {
        	if (plainText == null || plainText.isEmpty())
        		return plainText;
	    	Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
	        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
	        return Base64.encodeBase64String(encryptedBytes);
        } catch (Exception e) {
        	throw new ServiceException(ServiceStatus.CRYPTO_ERROR, "AES: Encrypting String Failed " + e.getMessage());
        }
    }
    
    public String decryptString(String encrypted) throws ServiceException {
    	try {
    		if (encrypted == null || encrypted.isEmpty())
        		return encrypted;
	        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
	        byte[] plainBytes = cipher.doFinal(Base64.decodeBase64(encrypted));
	        return new String(plainBytes);
    	} catch (Exception e) {
    		throw new ServiceException(ServiceStatus.CRYPTO_ERROR, "AES: Decrypting String Failed " + e.getMessage());
    	}
    }

    public byte[] encryptBinary(byte[] data) throws ServiceException {
        try {
	    	Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
	        return cipher.doFinal(data);
        } catch (Exception e) {
        	throw new ServiceException(ServiceStatus.CRYPTO_ERROR, "AES: Encrypting Binary Failed " + e.getMessage());
        }
    }

    public byte[] decryptBinary(byte[] encryptedData) throws ServiceException {
    	try {
	        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);        
	        return cipher.doFinal(encryptedData);
    	} catch (Exception e) {
    		throw new ServiceException(ServiceStatus.CRYPTO_ERROR, "AES: Decrypting Binary Failed " + e.getMessage());
    	}
    }    

	public String encrypBinaryToString64(byte[] data) throws ServiceException {
		try {
			return Base64.encodeBase64String(encryptBinary(data));
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.CRYPTO_ERROR, "AES: Encrypting Binary to String64 Failed " + e.getMessage());
		}
	}
	
	public byte[] decrypString64ToBinary(String data) throws ServiceException {
		try {
			return decryptBinary(Base64.decodeBase64(data));
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.CRYPTO_ERROR, "AES: Decrypting String64 to Binary Failed " + e.getMessage());
		}
	}
}
