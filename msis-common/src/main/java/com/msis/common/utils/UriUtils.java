package com.msis.common.utils;

public class UriUtils {
	static final String testUri = "/patients/%s/records/%s/tests/%s";
	
	public static String buildTestUri(String patientId, String recordId, String fileName) {
		return String.format(testUri, patientId, recordId, fileName);
	}
}
