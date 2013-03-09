package com.ghlh.util;

public class MathUtil {
	public static int getNSquareM(int n, int m) {
		int result = 1;
		for (int i = 0; i < m; i++) {
			result *= n;
		}
		return result;
	}

	public static boolean isInt(String abc) {
		try {
			Integer.parseInt(abc);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	public static boolean isFloat(String abc) {
		try {
			Float.parseFloat(abc);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
