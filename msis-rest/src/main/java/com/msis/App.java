package com.msis;

import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;

import sun.misc.BASE64Decoder;

import com.msis.common.crypto.AES;
import com.msis.common.service.ServiceException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
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
    	
    	String password = "Secret Passphrase";
        String salt = "495ae7e34b9da43b9b4bdac644529aa7";
        String iv = "9609f89caaa074264acf4df75122dbdc";
        String encrypted = "bS+sQL07X5tnKcH6Zon51NXPNKGsu/y4y3KsQmlf9VBN2PCeLqD3PXe4sqEoYYIx";

        byte[] saltBytes = hexStringToByteArray(salt);
        byte[] ivBytes = hexStringToByteArray(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);        
        SecretKeySpec sKey = (SecretKeySpec) generateKeyFromPassword(publicKey, saltBytes);
        System.out.println( decrypt( encrypted , sKey ,ivParameterSpec));
    	
    	System.out.println(email);
    	System.out.println(first);
    	System.out.println(last);
    	System.out.println(pass);
    	System.out.println(random);
    	System.out.println(rannum);
    	System.out.println(token);
    }
    public static String decrypt(String encryptedData, SecretKeySpec sKey, IvParameterSpec ivParameterSpec) throws Exception { 
    	
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, sKey, ivParameterSpec);
        
        byte[] plainBytes = c.doFinal(Base64.decodeBase64(encryptedData));
        return new String(plainBytes);
        
//        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
//        byte[] decValue = c.doFinal(decordedValue);
//        String decryptedValue = new String(decValue);
//
//        return decryptedValue;
    }
    
    public static SecretKey generateKeyFromPassword(String password, byte[] saltBytes) throws GeneralSecurityException {

        
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), saltBytes, 234, 128);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey secretKey = keyFactory.generateSecret(keySpec);

        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }
    
    public static byte[] hexStringToByteArray(String s) {

        int len = s.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }
    
    
}
