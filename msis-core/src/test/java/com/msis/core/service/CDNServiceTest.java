package com.msis.core.service;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.msis.core.config.BeanConfig;
import com.msis.core.config.EsConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EsConfig.class, BeanConfig.class})
@ActiveProfiles("test")
@FixMethodOrder(value=MethodSorters.NAME_ASCENDING)

public class CDNServiceTest {
	
	@Autowired
	private CDNService cdnService;
	
	@Test
	public void cdn1ReadContent() throws Exception {
		InputStream is = cdnService.readContent("device/17/profile/14111/userprofile.dtscs");
		assertNotNull(is);
	}
	
	@Test
	public void cdn2SaveContent() throws Exception {
		InputStream is = cdnService.readContent("device/17/profile/14111/userprofile.dtscs");
		long length = cdnService.saveContent(is, "device/17/profile/14111/userprofile1.dtscs");
		assertTrue(length > 0);
	}
}
