package com.msis.core.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msis.common.crypto.AES;
import com.msis.common.service.ServiceException;
import com.msis.core.config.CoreConfig;
import com.msis.core.service.CryptoService;

@Service(value="cryptoService")
public class CryptoServiceImpl implements CryptoService {
	
	@Autowired
	private CoreConfig config;
	
	private AES aesNetwork;
	private AES aesSystem;
	
	@PostConstruct
	public void init() throws ServiceException {
		aesNetwork = new AES(config.publicKey(), config.salt(), config.iv());
		aesSystem = new AES(config.privateKey());
	}
	
	@Override
	public String encryptNetwork(String text) throws ServiceException {
		return aesNetwork.encryptIV(text);
	}

	@Override
	public String decryptNetwork(String text) throws ServiceException {
		return aesNetwork.decryptIV(text);
	}

	@Override
	public String encryptSystem(String text) throws ServiceException {
		return aesSystem.encryptString(text);
	}

	@Override
	public String decryptSystem(String text) throws ServiceException {
		return aesSystem.decryptString(text);
	}

	@Override
	public String encryptUser(String key, String text) throws ServiceException {
		AES aes = new AES(key);
		return aes.encryptString(text);
	}

	@Override
	public String decryptUser(String key, String text) throws ServiceException {
		AES aes = new AES(key);
		return aes.decryptString(text);
	}

}
