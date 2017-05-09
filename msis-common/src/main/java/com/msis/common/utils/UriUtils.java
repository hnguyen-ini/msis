package com.msis.common.utils;

public class UriUtils {
	static final String testUri = "/patient/%s/record/%s/test/%s";
	
	public static String buildTestUri(String patientId, String recordId, String fileName) {
		if (recordId == null || recordId.isEmpty())
			recordId = "default";
		return String.format(testUri, patientId, recordId, fileName);
	}
}
