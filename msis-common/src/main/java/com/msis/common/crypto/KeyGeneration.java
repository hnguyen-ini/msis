package com.msis.common.crypto;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceStatus;

public class KeyGeneration {
	private KeyPairGenerator keyGen;
	private KeyPair pair;
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public KeyGeneration() {}
	
	public KeyGeneration(int keylength) throws ServiceException {
		try {
			this.keyGen = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			this.keyGen.initialize(keylength, random);
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}

	public void createKeys() throws ServiceException {
		try {
			this.pair = this.keyGen.generateKeyPair();
			this.publicKey = pair.getPublic();
			this.privateKey = pair.getPrivate();
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}

	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}

	public PublicKey getPublicKey() {
		return this.publicKey;
	}
	
	public KeyPair getKeyPair() {
		return this.pair;
	}
	
	public byte[] getBytePrivateKey() throws ServiceException {
		try {
			return this.privateKey.getEncoded();
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	public byte[] getBytePublicKey() throws ServiceException {
		try {
			return this.publicKey.getEncoded();
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	public PublicKey getPublicKeyFromStr64(String strPublicKey) throws ServiceException {
		try {
			byte[] byteKeys = Base64.decodeBase64(strPublicKey);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(byteKeys);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			return kf.generatePublic(spec);
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	public PrivateKey getPrivateKeyFromStr64(String strPrivateKey) throws ServiceException {
		try {
			byte[] byteKeys = Base64.decodeBase64(strPrivateKey);
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(byteKeys);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			return kf.generatePrivate(spec);
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	public String getStr64PrivateKey() throws ServiceException {
		try {
			return Base64.encodeBase64String(getBytePrivateKey());
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	public String getStr64PublicKey() throws ServiceException {
		try {
			return Base64.encodeBase64String(getBytePublicKey());
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
}
