package com.msis.core.service;

import com.msis.common.service.ServiceException;

public interface CryptoService {
	String encryptNetwork(String text) throws ServiceException;
	String decryptNetwork(String text) throws ServiceException;
	
	String encryptSystem(String text) throws ServiceException;
	String decryptSystem(String text) throws ServiceException;
	
	String encryptUser(String key, String text) throws ServiceException;
	String decryptUser(String key, String text) throws ServiceException;
}
