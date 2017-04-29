package com.msis.common.crypto;

import com.msis.common.service.ServiceException;

public class Crypto {
	public static String encryptString(String key, String text) throws ServiceException {
		AES aes = new AES(key);
		return aes.encryptString(text);
	}
	
	public static String decryptString(String key, String text) throws ServiceException {
		AES aes = new AES(key);
		return aes.decryptString(text);
	}
}
