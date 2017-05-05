package com.msis.common.parser;

import com.google.gson.Gson;

public class JsonHelper {
	
	public static String toString(Object object) {
		try {
			return new Gson().toJson(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
