package com.ghlh.util;

import java.util.Calendar;
import java.util.Date;

public class StockMarketUtil {

	public static boolean isMarketOpenning() {
		Date currentTime = Calendar.getInstance().getTime();
		boolean result = (isMarketMorningOpenning(currentTime) || isMarketAfternoonOpenning(currentTime))
				&& isNotWeekend();
		return result;
	}

	private static boolean isNotWeekend() {
		Calendar cal = Calendar.getInstance();
		int weekday = cal.get(Calendar.DAY_OF_WEEK);
		return (weekday != Calendar.SATURDAY && weekday != Calendar.SUNDAY);
	}

	private static boolean isMarketMorningOpenning(Date currentTime) {
		Date morningOpeningTime = getCentainTime(9, 30);
		Date morningClosingTime = getCentainTime(11, 30);
		boolean result = (currentTime.equals(morningOpeningTime) || currentTime
				.after(morningOpeningTime))
				&& currentTime.before(morningClosingTime);
		return result;
	}

	private static boolean isMarketAfternoonOpenning(Date currentTime) {
		Date afternoonOpeningTime = getCentainTime(13, 0);
		Date afternoonClosingTime = getCentainTime(15, 0);
		boolean result = (currentTime.equals(afternoonOpeningTime) || currentTime
				.after(afternoonOpeningTime))
				&& currentTime.before(afternoonClosingTime);
		return result;
	}

	private static Date getCentainTime(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		Date result = calendar.getTime();
		return result;
	}
}
