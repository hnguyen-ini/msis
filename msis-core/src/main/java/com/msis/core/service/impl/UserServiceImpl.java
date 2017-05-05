package com.msis.core.service.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.msis.common.crypto.AES;
import com.msis.common.crypto.Crypto;
import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceStatus;
import com.msis.common.utils.ListUtils;
import com.msis.common.utils.PasswordUtils;
import com.msis.core.config.CoreConfig;
import com.msis.core.model.User;
import com.msis.core.repository.UserRepository;
import com.msis.core.service.UserService;

/**
 * 
 * @author hien.nguyen
 *
 */
@Service(value="userService")
public class UserServiceImpl implements UserService{
	static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private UserRepository userRepository;
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	private CoreConfig coreConfig;
	@Autowired
	public void setCoreConfig(CoreConfig coreConfig) {
		this.coreConfig = coreConfig;
	}
	
	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public void delete(User user) {
		userRepository.delete(user);
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

	@SuppressWarnings("unchecked")
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
	    	User enUser = encryptPrivateKey(deUser);
			
	        if (findByEmail(deUser.getEmail()) != null)
	        	throw new ServiceException(ServiceStatus.DUPLICATE_USER, "Duplicate user email");

	        // validate password
	    	PasswordUtils.validate(deUser.getPassword());
	    	String pwd = PasswordUtils.createHash(deUser.getPassword());
	    	String salt = pwd.split(":")[PasswordUtils.SALT_INDEX];
			String pwod = pwd.split(":")[PasswordUtils.PBKDF2_INDEX];
	        
	        String token = UUID.randomUUID().toString();
	        Long time = System.currentTimeMillis();
	        
	        deUser.setPassword(pwod);
	        deUser.setPasswordHash(salt);
	        deUser.setCreateAt(time);
	        deUser.setModifyAt(time);
	        deUser.setLoginAt(time);
	        deUser.setToken(token);
	        save(deUser);
	        
	        user.setPassword(null);
	        user.setFirstName(null);
	        user.setLastName(null);
	        user.setToken(Crypto.encryptString(coreConfig.publicKey(), token));
	    	return user;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Register failed, " + e.getMessage());
		}
	}

	@Override
	public User verify(String email, String password) throws ServiceException{
		try {
			if (email == null || email.isEmpty() || password == null || password.isEmpty())
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing email or password");
			email = Crypto.decryptString(coreConfig.publicKey(), email);
			password = Crypto.decryptString(coreConfig.publicKey(), password);
			
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
			if (user.getStatus().equals("I")) {
				logger.warn("Inactive user");
				throw new ServiceException(ServiceStatus.INACTIVE_USER, "Inactive user");
			}
			String token = UUID.randomUUID().toString();
			user.setToken(token);
			user.setLoginAt(System.currentTimeMillis());
			save(user);
			
	        user = encryptPublicKey(user);
			user.setPassword(null);
	        user.setCreateAt(null);
	        user.setId(null);
	        user.setModifyAt(null);
	        user.setStatus(null);
	        return user;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Sign-in failed, " + e.getMessage());
		}
	}

	@Override
	public User encryptPublicKey(User user) throws ServiceException {
		AES aes = new AES(coreConfig.publicKey());
		
		User enUser = new User();
		enUser.setEmail(aes.encryptString(user.getEmail()));
		enUser.setFirstName(aes.encryptString(user.getFirstName()));
		enUser.setLastName(aes.encryptString(user.getLastName()));
		enUser.setPassword(aes.encryptString(user.getPassword()));
		enUser.setToken(aes.encryptString(user.getToken()));
		
		return enUser;
	}

	@Override
	public User decryptPublicKey(User user) throws ServiceException {
		AES aes = new AES(coreConfig.publicKey());
		
		User deUser = new User();
		deUser.setEmail(aes.decryptString(user.getEmail()));
		deUser.setFirstName(aes.decryptString(user.getFirstName()));
		deUser.setLastName(aes.decryptString(user.getLastName()));
		deUser.setPassword(aes.decryptString(user.getPassword()));
		deUser.setToken(aes.decryptString(user.getToken()));
		
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

	
}
