package com.msis.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.msis.common.crypto.AES;
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
         // client & server must have the same publicKey
    	AES aes = new AES(coreConfig.publicKey()); 
    	String email = aes.decryptString(user.getEmail());
        String password = aes.decryptString(user.getPassword());
        String valid = PasswordUtils.validate(password);
        if (valid != null) 
        	throw new ServiceException(ServiceStatus.BAD_PASSWORD, valid);
        
        // reset AES with privateKey
        aes = new AES(coreConfig.privateKey());
        email = aes.encryptString(email);
        User regUser = findByEmail(email);
        if (regUser != null)
        	throw new ServiceException(ServiceStatus.DUPLICATE_USER);
        Long time = System.currentTimeMillis();
        password = aes.encryptString(password);
    	regUser = new User(user);
    	regUser.setEmail(email);
    	regUser.setPassword(password);
    	regUser.setCreateAt(time);
    	regUser.setModifyAt(time);
    	save(user);
    
    	return user;
	}

	@Override
	public User verify(String userId, String password) throws ServiceException{
		try {
			User user = findByEmail(userId);
			if (user == null)
				throw new ServiceException(ServiceStatus.BAD_USER_ID, "Not Found User by userId: " + userId);
			String correctHash = PasswordUtils.PBKDF2_ITERATIONS + ":" + user.getPassword();
			if (PasswordUtils.validatePassword(password, correctHash))
				return user;
			else
				throw new ServiceException(ServiceStatus.BAD_PASSWORD, "Invalid Password");
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.BAD_REQUEST, e.getMessage());
		}
	}

	


	

}
