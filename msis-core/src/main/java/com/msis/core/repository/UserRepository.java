package com.msis.core.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.msis.core.model.User;

public interface UserRepository extends ElasticsearchRepository<User, String>{
	User findById(String id);
	User findByEmail(String email);
	User findByToken(String token);
	
	List<User> findByLastName(String lastName);
	Page<User> findByLastName(String lastName, Pageable pageable);
	List<User> findByLastNameOrderByLastName(String lastName);
	List<User> findByLastNameOrderByLastNameDesc(String lastName);
	
	List<User> findByFirstName(String firstName);
	Page<User> findByFirstName(String firstName, Pageable pageable);
	List<User> findByFirstNameOrderByFirstName(String firstName);
	List<User> findByFirstNameOrderByFirstNameDesc(String firstName);
} 
