package com.ghlh.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static String formatDate(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return df.format(date);
	}

	public static String formatDay(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	public static Date getNextDay(Date date) {
		Date result = new Date(date.getTime() + 24 * 60 * 60 * 1000);
		return result;
	}

	public static Date getDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date date = calendar.getTime();
		return date;
	}

	public static void main(String[] args) {
		Date now = new Date();
		System.out.println(formatDate(now));

	}
}
