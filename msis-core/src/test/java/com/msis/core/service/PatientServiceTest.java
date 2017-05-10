package com.msis.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.msis.core.config.EsConfig;
import com.msis.core.model.Patient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EsConfig.class})
@ActiveProfiles("test")
@FixMethodOrder(value=MethodSorters.NAME_ASCENDING)
public class PatientServiceTest {
	static Logger log = LoggerFactory.getLogger(PatientServiceTest.class);
	
	@Autowired
    private ElasticsearchTemplate esTemplate;
	@Autowired
	private PatientService patientService;
	
	private Patient patient;
	
	@Before
    public void before() throws Exception {
		log.info("Init PatientServiceTest");
        esTemplate.deleteIndex(Patient.class);
        esTemplate.createIndex(Patient.class);
        esTemplate.putMapping(Patient.class);
        esTemplate.refresh(Patient.class);
        
        patient = new Patient("123456789", "Test Name", 30, "Male", "123 address", "creatorId");
        
        create();
	}
	
	public void create() throws Exception {
		Patient createPatient = patientService.create(patient);
		assertEquals(patient.getIdn(), createPatient.getIdn());
		assertEquals(patient.getName(), createPatient.getName());
	}
	
	@Test
	public void test1UpdatePatient() throws Exception {
		Patient upd = new Patient(patient);
		upd.setName("Test Name Update");
		Patient p = patientService.update(upd);
		assertEquals(upd.getName(), p.getName());
	}
	
	@Test
	public void test2Delete() throws Exception {
		// make sure there is one
		Patient p1 = patientService.findOne(patient.getId());
		assertEquals(patient.getIdn(), p1.getIdn());
		
		patientService.deleteById(patient.getId());
		
		p1 = patientService.findOne(patient.getId());
		assertEquals(null, p1);
	}
	
	@Test
	public void test3DeleteCreator() throws Exception {
		// make sure there is one
		List<Patient> p1 = patientService.findByCreator(patient.getCreator());
		assertEquals(patient.getIdn(), p1.get(0).getIdn());
		
		patientService.deleteById(patient.getId());
		
		p1 = patientService.findByCreator(patient.getCreator());
		assertEquals(0, p1.size());
	}
	
	@Test
	public void test9BDelete() throws Exception {
		patientService.deleteAll();
		Patient p = patientService.findOne(patient.getId());
		assertNull(p);
		log.info("patientBDelete Test Case Passed");
	}
}
