package com.msis.core.service;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceStatus;
import com.msis.core.config.BeanConfig;
import com.msis.core.config.EsConfig;
import com.msis.core.model.User;
import com.msis.core.service.UserService;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EsConfig.class, BeanConfig.class})
@ActiveProfiles("test")
@FixMethodOrder(value=MethodSorters.NAME_ASCENDING)
public class UserServiceTest {
	static Logger log = LogManager.getLogger(UserServiceTest.class.getName());
	@Autowired
    private UserService userService;
	
	@Autowired
    private ElasticsearchTemplate esTemplate;
	
	@Before
    public void before() {
		log.info("Init UserServiceTest");
//        esTemplate.deleteIndex(User.class);
//        esTemplate.createIndex(User.class);
        esTemplate.putMapping(User.class);
        esTemplate.refresh(User.class);
    }
	
	@Test
    public void user1Save() throws Exception {
		User user= new User("Elasticsearch", "Rambabu", "Elasticsearch@gmail.com", "password");
        User testUser = userService.save(user);

        assertNotNull(testUser);
        assertEquals(testUser.getFirstName(), user.getFirstName());
        assertEquals(testUser.getLastName(), user.getLastName());
        assertEquals(testUser.getEmail(), user.getEmail());
        log.info(new Gson().toJson(testUser));
		log.info("user1Save Test Case Passed");
	}

	@Test
	public void user2Reg() throws Exception {
		// not encrypt email 
		try {
			User user = new User("Elasticsearch", "Rambabu", "Elasticsearch@gmail.com", "password");
			User test = userService.regUser(user);
			assertNotNull(test);
		} catch (ServiceException e) {
			assertEquals(ServiceStatus.RUNNING_TIME_ERROR.getCode(), e.getStatus().getCode());
			assertEquals(ServiceStatus.RUNNING_TIME_ERROR.getStatus(), e.getStatus().getStatus());
			assertTrue("AES: Decrypting String Failed Input length must be multiple of 16 when decrypting with padded cipher".equals(e.getMessage()));
		}
	}
	
	@Test
	public void user3FindByEmail() throws Exception {
		User user = userService.findByEmail("Elasticsearch@gmail.com");
		assertNotNull(user);
        assertEquals("Elasticsearch", user.getFirstName());
        assertEquals("Rambabu", user.getLastName());
        assertEquals("Elasticsearch@gmail.com", user.getEmail());
        log.info(new Gson().toJson(user));
		log.info("user3FindByEmail Test Case Passed");
	}
	
	@Test 
	public void user4FindByFirstName() throws Exception {
		List<User> user = userService.findByFirstName("Elasticsearch");
		assertNotNull(user);
        assertEquals("Elasticsearch", user.get(0).getFirstName());
        assertEquals("Rambabu", user.get(0).getLastName());
        assertEquals("Elasticsearch@gmail.com", user.get(0).getEmail());
        log.info(new Gson().toJson(user.get(0)));
		log.info("user4FindByFirstName Test Case Passed");
	}
	
	@Test 
	public void user5FindByFirstName() throws Exception {
		Page<User> user = userService.findByFirstName("Elasticsearch", new PageRequest(0, 10));
		assertNotNull(user);
        assertEquals("Elasticsearch", user.getContent().get(0).getFirstName());
        assertEquals("Rambabu", user.getContent().get(0).getLastName());
        assertEquals("Elasticsearch@gmail.com", user.getContent().get(0).getEmail());
        log.info(new Gson().toJson(user.getContent().get(0)));
		log.info("user5FindByFirstName Test Case Passed");
	}
	
	@Test 
	public void user6FindByLastName() throws Exception {
		List<User> user = userService.findByLastName("Rambabu");
		assertNotNull(user);
        assertEquals("Elasticsearch", user.get(0).getFirstName());
        assertEquals("Rambabu", user.get(0).getLastName());
        assertEquals("Elasticsearch@gmail.com", user.get(0).getEmail());
        log.info(new Gson().toJson(user.get(0)));
		log.info("user6FindByLastName Test Case Passed");
	}
	
	@Test 
	public void user7FindByFirstName() throws Exception {
		Page<User> user = userService.findByLastName("Rambabu", new PageRequest(0, 10));
		assertNotNull(user);
        assertEquals("Elasticsearch", user.getContent().get(0).getFirstName());
        assertEquals("Rambabu", user.getContent().get(0).getLastName());
        assertEquals("Elasticsearch@gmail.com", user.getContent().get(0).getEmail());
        log.info(new Gson().toJson(user.getContent().get(0)));
		log.info("user7FindByFirstName Test Case Passed");
	}
	
	@Test 
	public void user8FindAll() throws Exception {
		List<User> user = userService.findAll();
		assertNotNull(user);
        assertEquals("Elasticsearch", user.get(0).getFirstName());
        assertEquals("Rambabu", user.get(0).getLastName());
        assertEquals("Elasticsearch@gmail.com", user.get(0).getEmail());
        log.info(new Gson().toJson(user.get(0)));
		log.info("user8FindAll Test Case Passed");
	}
	
	@Test 
	public void user9FindAll() throws Exception {
		Page<User> user = userService.findAll(new PageRequest(0, 10));
		assertNotNull(user);
        assertEquals("Elasticsearch", user.getContent().get(0).getFirstName());
        assertEquals("Rambabu", user.getContent().get(0).getLastName());
        assertEquals("Elasticsearch@gmail.com", user.getContent().get(0).getEmail());
        log.info(new Gson().toJson(user.getContent().get(0)));
		log.info("user9FindAll Test Case Passed");
	}
	
	@Test 
	public void userAUpdateLastName() throws Exception {
		User user = userService.findByEmail("Elasticsearch@gmail.com");
		assertEquals("Rambabu", user.getLastName());
		
		user.setLastName("LastNameUpdate");
		userService.save(user);
		user = userService.findByEmail("Elasticsearch@gmail.com");
        assertEquals("Elasticsearch", user.getFirstName());
        assertEquals("LastNameUpdate", user.getLastName());
        assertEquals("Elasticsearch@gmail.com", user.getEmail());
        log.info(new Gson().toJson(user));
		log.info("userAUpdateLastName Test Case Passed");
	}
	
	@Test
	public void userBDelete() throws Exception {
		userService.deleteAll();
		List<User> user = userService.findAll();
		assertNull(user);
		log.info("userBDelete Test Case Passed");
	}
}
