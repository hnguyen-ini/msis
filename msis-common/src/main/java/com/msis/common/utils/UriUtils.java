package com.msis.common.utils;

public class UriUtils {
	static final String testFolder = "/patients/%s/records/%s/tests";
	static final String testUri = "/patients/%s/records/%s/tests/%s";
	
	public static String buildTestFolder(String pId, String recordId) {
		return String.format(testFolder, pId, recordId);
	}
	
	public static String buildTestUri(String pId, String recordId, String fileName) {
		return String.format(testUri, pId, recordId, fileName);
	}
	
}
