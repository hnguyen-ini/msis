package com.msis.common.utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils {
	private Pattern pattern;
	private Matcher matcher;

	private static final String patternStr = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public EmailUtils() {
		pattern = Pattern.compile(patternStr);
	}

	public boolean validate(final String email) {
		matcher = pattern.matcher(email);
		return matcher.matches();
	}
}
