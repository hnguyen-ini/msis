package com.msis.core.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.msis.common.service.ServiceException;
import com.msis.core.model.User;

public interface UserService {
	User save(User user);
	void delete(User user);
	void deleteByEmail(String email) throws ServiceException;
	void deleteAll();
	User findById(String id);
	User findByEmail(String email);
	User findByToken(String token);
	
	List<User> findAll();
	Page<User> findAll(Pageable pageable);
	
	List<User> findByLastName(String lastName);
	Page<User> findByLastName(String lastName, Pageable pageable);
	
	
	List<User> findByFirstName(String firstName);
	Page<User> findByFirstName(String firstName, Pageable pageable);
	
	// login section
	User regUser(User user) throws ServiceException;
	User verify(String email, String password) throws ServiceException;
	User validateToken(String token) throws ServiceException;
	User changePassword(User user) throws ServiceException;
	
	User getAccountByToken(String token) throws ServiceException;
	
	User encryptPublicKey(User user) throws ServiceException;
	User decryptPublicKey(User user) throws ServiceException;
	User encryptPrivateKey(User user) throws ServiceException;
	User decryptPrivateKey(User user) throws ServiceException;
	
	boolean okUser(User user);
}
