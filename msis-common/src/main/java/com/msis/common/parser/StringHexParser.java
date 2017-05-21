package com.msis.common.parser;

import java.nio.charset.StandardCharsets;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Hex;

import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceStatus;

public class StringHexParser {
	
	public static String convertStringToHex(String str) throws ServiceException {
		try {
			char[] chars = str.toCharArray();
	
			StringBuffer hex = new StringBuffer();
			for(int i = 0; i < chars.length; i++){
				hex.append(Integer.toHexString((int)chars[i]));
			}
	
			return hex.toString();
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Converting String to Hex Failed");
		}
	}
	
	public static String convertHexToString(String hex) throws ServiceException {
		try {
			StringBuilder sb = new StringBuilder();
			StringBuilder temp = new StringBuilder();
	
			//49204c6f7665204a617661 split into two characters 49, 20, 4c...
			for(int i = 0; i < hex.length()-1; i+= 2){
	
			    //grab the hex in pairs
			    String output = hex.substring(i, (i + 2));
			    //convert hex to decimal
			    int decimal = Integer.parseInt(output, 16);
			    //convert the decimal to character
			    sb.append((char)decimal);
	
			    temp.append(decimal);
			}
			System.out.println("Decimal : " + temp.toString());
	
			return sb.toString();
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Converting Hex to String Failed");
		}
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
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		StringHexParser strToHex = new StringHexParser();
		  System.out.println("\n***** Convert ASCII to Hex *****");
		  String str = "I Love Java!";
		  System.out.println("Original input : " + str);

		  String hex = strToHex.convertStringToHex(str);

		  System.out.println("Hex : " + hex);
		  
		  System.out.println("\n***** Convert Hex to ASCII *****");
		  System.out.println("Hex : " + hex);
		  System.out.println("ASCII : " + strToHex.convertHexToString(hex));
		  
		  byte[] bytes = Hex.decodeHex("253D3FB468A0E24677C28A624BE0F939".toCharArray());
		  System.out.println(new String(bytes, "UTF-8"));
		  
		  byte[] b = DatatypeConverter.parseBase64Binary("253D3FB468A0E24677C28A624BE0F939");
		  
		  String hexString = Hex.encodeHexString("hello".getBytes(StandardCharsets.UTF_8));
		  str += str;
	}

}
