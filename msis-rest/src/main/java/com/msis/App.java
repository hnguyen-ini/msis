package com.msis;

import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;

import com.msis.common.crypto.AES;
import com.msis.common.service.ServiceException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ServiceException
    {
        String privateKey = "DSFA2SF63AW8HFEO";
        String publicKey = "DFGSDG233DJDPR3S";
    	AES aes = new AES(publicKey);
    	
    	String email = aes.encryptString("hien.nguyen1@dts.com");
    	String first = aes.encryptString("Hien");
    	String last = aes.encryptString("Nguyen");
    	String pass = aes.encryptString("hien.nguyen123");
    	
    	String random = RandomStringUtils.randomAlphabetic(16);
    	String rannum = RandomStringUtils.randomNumeric(16);
    	String token = UUID.randomUUID().toString();
    	
    	System.out.println(email);
    	System.out.println(first);
    	System.out.println(last);
    	System.out.println(pass);
    	System.out.println(random);
    	System.out.println(rannum);
    	System.out.println(token);
    }
}
