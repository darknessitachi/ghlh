package com.ghlh.util;

import java.math.BigDecimal;

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

	private static double formatDouble(double abc, int servedNumber) {
		BigDecimal bd = new BigDecimal(abc);
		BigDecimal bd1 = bd.setScale(servedNumber, bd.ROUND_HALF_UP);
		double result = bd1.doubleValue();
		return result;
	}

	public static double formatDoubleWith4(double abc) {
		return formatDouble(abc, 4);
	}

	public static double formatDoubleWith2(double abc) {
		return formatDouble(abc, 2);
	}

}
