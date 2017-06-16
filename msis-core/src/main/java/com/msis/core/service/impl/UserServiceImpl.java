package com.msis.core.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.msis.common.crypto.AES;
import com.msis.common.crypto.Crypto;
import com.msis.common.crypto.KeyGeneration;
import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceStatus;
import com.msis.common.utils.ListUtils;
import com.msis.common.utils.PasswordUtils;
import com.msis.core.cache.CacheService;
import com.msis.core.config.CoreConfig;
import com.msis.core.model.Mail;
import com.msis.core.model.Session;
import com.msis.core.model.User;
import com.msis.core.repository.UserRepository;
import com.msis.core.service.CryptoService;
import com.msis.core.service.MailService;
import com.msis.core.service.UserService;

/**
 * 
 * @author hien.nguyen
 *
 */
@Service(value="userService")
public class UserServiceImpl implements UserService{
	static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CoreConfig coreConfig;
	@Autowired
	private MailService mailService;
	@Autowired 
	private CacheService cacheService;
	@Autowired
	private CryptoService cryptoService;
	
	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public void delete(User user) {
		userRepository.delete(user);
	}
	
	@Override
	public void deleteByEmail(String email) throws ServiceException {
		User user = findByEmail(email);
		if (user == null)
			throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found user by id " + email);
		delete(user);
	}
	
	@Override
	public void deleteAll() {
		userRepository.deleteAll();		
	}

	@Override
	public User findById(String id) {
		return userRepository.findById(id);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public User findByToken(String token) {
		return userRepository.findByToken(token);
	}

	@Override
	public List<User> findAll() {
		Iterable iterable = userRepository.findAll();
		return ListUtils.toList(iterable);
	}
	
	@Override
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public List<User> findByLastName(String lastName) {
		return userRepository.findByLastName(lastName);
	}

	@Override
	public Page<User> findByLastName(String lastName, Pageable pageable) {
		return userRepository.findByLastName(lastName, pageable);
	}

	@Override
	public List<User> findByFirstName(String firstName) {
		return userRepository.findByFirstName(firstName);
	}
	
	@Override
	public Page<User> findByFirstName(String firstName, Pageable pageable) {
		return userRepository.findByFirstName(firstName, pageable);
	}

	@Override
	public User regUser(User user) throws ServiceException {
		try {
			if (!okUser(user))
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Invaid user information");
			// client & server must have the same publicKey and email, password must be validated on client
	    	User deUser = decryptPublicKey(user);
			
	        if (findByEmail(deUser.getEmail()) != null)
	        	throw new ServiceException(ServiceStatus.DUPLICATE_USER, "Duplicate user email");

	        // validate password
	    	PasswordUtils.validate(deUser.getPassword());
	    	String pwd = PasswordUtils.createHash(deUser.getPassword());
	    	String salt = pwd.split(":")[PasswordUtils.SALT_INDEX];
			String pwod = pwd.split(":")[PasswordUtils.PBKDF2_INDEX];
	        
	        Long time = System.currentTimeMillis();
	        
	        deUser.setPassword(pwod);
	        deUser.setPasswordHash(salt);
	        deUser.setCreateAt(time);
	        deUser.setModifyAt(time);
	        deUser.setLoginAt(time);
	        deUser.setToken(UUID.randomUUID().toString());
	        
	        // aesKey
	        KeyGeneration keyGen = new KeyGeneration(2048);
	        keyGen.createKeys();
	        String pubKey = keyGen.getStr64PublicKey();
	        String priKey = keyGen.getStr64PrivateKey();
	        String aesKey = RandomStringUtils.randomAlphabetic(16);
	        
	        //deUser.setAES(Crypto.encryptAESKeyByPublicKeyString(aesKey, pubKey));
	        deUser.setAES(cryptoService.encryptSystem(aesKey));
	        deUser.setPublicKey(pubKey);
	        deUser.setPrivateKey(priKey);

	        save(deUser);
	        
	        user.setPassword(null);
	        user.setFirstName(null);
	        user.setLastName(null);
	        user.setStatus(null);
	        user.setAES(null);
	        
	        String token = UUID.randomUUID().toString();
	        // send email
	        Mail mail = new Mail(deUser.getEmail(), deUser.getFirstName(), token, coreConfig.hostUri(), coreConfig.registerSubject(), "register-mail.vm", priKey);
	        mailService.send(mail);
	        
	        // cache session
	        Session session = new Session(token, deUser.getToken(), aesKey, 60*24);
	        cacheService.setCache(session);
	        	        
	    	return user;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Register failed, " + e.getMessage());
		}
	}
	
	@Override
	public User validateToken(String token) throws ServiceException {
		String deToken = cryptoService.decryptNetwork(token);

		Session session = cacheService.getCache(deToken);
		if (session == null) {
			throw new ServiceException(ServiceStatus.REQUEST_TIME_OUT, "Your token had been expired!");
		}
		User user = findByToken(session.getUserToken());
		if (user == null)
			throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found user by token " + deToken);
		
		user.setStatus("A");
		save(user);
		
		cacheService.resetExpiration(session, coreConfig.sessionExpired());
		cacheService.setCache(session);
		
		return response(user, deToken);
	}
	
	@Override
	public User verify(String email, String password) throws ServiceException{
		try {
			if (email == null || email.isEmpty() || password == null || password.isEmpty())
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing email or password");
			email = cryptoService.decryptNetwork(email);
			password = cryptoService.decryptNetwork(password);
			
			User user = findByEmail(email);
			if (user == null) {
				logger.warn("Not found user by email " + email);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Invalid userid or password");
			}
			String correctHash = PasswordUtils.PBKDF2_ITERATIONS + ":" + user.getPasswordHash() + ":" + user.getPassword();
			if (!PasswordUtils.validatePassword(password, correctHash)) {
				logger.warn("Invalid password");
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Invalid userid or password");
			}
			if (!user.getStatus().equals("A")) {
				logger.warn("None Active user");
				throw new ServiceException(ServiceStatus.INACTIVE_USER, "None active user");
			}
			user.setLoginAt(System.currentTimeMillis());
			save(user);
			
			String token = UUID.randomUUID().toString();
			String aesKey = "testtesttts";//cryptoService.decryptSystem(user.getAES()); // TODO
			Session session = new Session(token, user.getToken(), aesKey, coreConfig.sessionExpired());
	        cacheService.setCache(session);
	        
			return response(user, token);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Sign-in failed, " + e.getMessage());
		}
	}
	
	@Override
	public User changePassword(User user) throws ServiceException {
		try {
			if (user.getToken() == null || user.getToken().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty() || user.getStatus() == null || user.getStatus().isEmpty()) {
				logger.warn("Missing token or password");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing token or password");
			}
			String token = user.getStatus();
			Session session = cacheService.getCache(token);
			if (session == null) {
				throw new ServiceException(ServiceStatus.REQUEST_TIME_OUT, "Your token had been expired!");
			}
			User userC = findByToken(session.getUserToken());
			if (userC == null) {
				logger.warn("Not found user by token " + token);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found user by token " + token);
			}
			String[] pair = cryptoService.decryptNetwork(user.getPassword()).split(":");
			if (pair.length != 2) {
				logger.warn("Invalid passwords");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Invalid passwords");
			}
			String _old = pair[0];
			String _new = pair[1];
			
			String correctHash = PasswordUtils.PBKDF2_ITERATIONS + ":" + userC.getPasswordHash() + ":" + userC.getPassword();
			if (!PasswordUtils.validatePassword(_old, correctHash)) {
				logger.warn("Invalid current password");
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Invalid current password");
			}
			
	    	PasswordUtils.validate(_new);
	    	String pwd = PasswordUtils.createHash(_new);
	    	String salt = pwd.split(":")[PasswordUtils.SALT_INDEX];
			String pwod = pwd.split(":")[PasswordUtils.PBKDF2_INDEX];
			
			userC.setPassword(pwod);
			userC.setPasswordHash(salt);
			userC.setModifyAt(System.currentTimeMillis());
			save(userC);
			
//			Session session = new Session(token, cryptoService.decryptSystem(userC.getAES()), coreConfig.sessionExpired());
//			cacheService.setCache(session);
			
			return response(userC, token);			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Change-Password failed, " + e.getMessage());
		}
	}
	
	@Override
	public User getAccountByToken(String accessToken, String userToken) throws ServiceException {
		try {
			if (accessToken == null || accessToken.isEmpty() || userToken == null || userToken.isEmpty()) {
				logger.warn("Missing tokens");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing tokens");
			}
			Session session = cacheService.getCache(accessToken);
			if (session == null) {
				throw new ServiceException(ServiceStatus.REQUEST_TIME_OUT, "Your token had been expired!");
			}
			String to = cryptoService.decryptNetwork(userToken);
			User userC = findByToken(to);
			if (userC == null) {
				logger.warn("Not found user by token " + to);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found user by token " + userToken);
			}
			return account(userC);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Get Account failed, " + e.getMessage());
		}
	}

	@Override
	public User encryptPublicKey(User user) throws ServiceException {
		User enUser = new User();
		enUser.setEmail(cryptoService.encryptNetwork(user.getEmail()));
		enUser.setFirstName(cryptoService.encryptNetwork(user.getFirstName()));
		enUser.setLastName(cryptoService.encryptNetwork(user.getLastName()));
		enUser.setPassword(cryptoService.encryptNetwork(user.getPassword()));
		enUser.setToken(cryptoService.encryptNetwork(user.getToken()));
		return enUser;
	}

	@Override
	public User decryptPublicKey(User user) throws ServiceException {
		User deUser = new User();
		deUser.setEmail(cryptoService.decryptNetwork(user.getEmail()));
		deUser.setFirstName(cryptoService.decryptNetwork(user.getFirstName()));
		deUser.setLastName(cryptoService.decryptNetwork(user.getLastName()));
		deUser.setPassword(cryptoService.decryptNetwork(user.getPassword()));
		deUser.setToken(cryptoService.decryptNetwork(user.getToken()));
		return deUser;
	}

	@Override
	public User encryptPrivateKey(User user) throws ServiceException {
		AES aes = new AES(coreConfig.privateKey());
		
		User enUser = new User();
		enUser.setEmail(aes.encryptString(user.getEmail()));
		enUser.setFirstName(aes.encryptString(user.getFirstName()));
		enUser.setLastName(aes.encryptString(user.getLastName()));
		enUser.setPassword(aes.encryptString(user.getPassword()));
		enUser.setToken(aes.encryptString(user.getToken()));
		
		return enUser;
	}

	@Override
	public User decryptPrivateKey(User user) throws ServiceException {
		AES aes = new AES(coreConfig.privateKey());
		
		User deUser = new User();
		deUser.setEmail(aes.decryptString(user.getEmail()));
		deUser.setFirstName(aes.decryptString(user.getFirstName()));
		deUser.setLastName(aes.decryptString(user.getLastName()));
		deUser.setPassword(aes.decryptString(user.getPassword()));
		deUser.setToken(aes.decryptString(user.getToken()));
		
		return deUser;
	}

	@Override
	public boolean okUser(User user) {
		if (user.getEmail() == null || user.getEmail().isEmpty()
				|| user.getFirstName() == null || user.getFirstName().isEmpty()
				|| user.getLastName() == null || user.getLastName().isEmpty()
				|| user.getPassword() == null || user.getPassword().isEmpty())
			return false;
		return true;
	}

	private User response(User user, String token) throws ServiceException {
		User response = new User();
		response.setFirstName(cryptoService.encryptNetwork(user.getFirstName()));
		response.setLastName(cryptoService.encryptNetwork(user.getLastName()));
		response.setEmail(cryptoService.encryptNetwork(user.getEmail()));
		response.setToken(cryptoService.encryptNetwork(user.getToken()));
		response.setStatus(token); // status is token session
		return response;
	}
	
	private User account(User user) throws ServiceException {
		User response = new User();
		response.setFirstName(user.getFirstName());
		response.setLastName(user.getLastName());
		response.setDob(user.getDob());
		response.setHomeAddress(user.getHomeAddress());
		response.setJobTitle(user.getJobTitle());
		response.setPhone(user.getPhone());
		response.setSex(user.getSex());
		response.setWorkAddress(user.getWorkAddress());
		return response;
	}
}
