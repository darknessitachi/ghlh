package com.ghlh.util;

public class StringUtil {

	public static String getDefaultNumberWithZero(String number) {
		if (number == null || number.trim().equals("")) {
			return "0";
		} else {
			return number;
		}
	}

}
