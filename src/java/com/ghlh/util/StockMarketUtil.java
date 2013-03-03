package com.ghlh.util;

import java.util.Calendar;
import java.util.Date;

public class StockMarketUtil {

	public static boolean isMarketOpenning() {
		Date currentTime = Calendar.getInstance().getTime();
		boolean result = isMarketMorningOpenning(currentTime)
				|| isMarketAfternoonOpenning(currentTime);
		return result;
	}

	private static boolean isMarketMorningOpenning(Date currentTime) {
		Date morningOpeningTime = getCentainTime(9, 30);
		Date morningClosingTime = getCentainTime(11, 30);
		boolean result = currentTime.after(morningOpeningTime)
				&& currentTime.before(morningClosingTime);
		return result;
	}

	private static boolean isMarketAfternoonOpenning(Date currentTime) {
		Date afternoonOpeningTime = getCentainTime(13, 0);
		Date afternoonClosingTime = getCentainTime(15, 0);
		boolean result = currentTime.after(afternoonOpeningTime)
				&& currentTime.before(afternoonClosingTime);
		return result;
	}

	private static Date getCentainTime(int hour, int minute) {
		Calendar.getInstance().set(Calendar.HOUR_OF_DAY, hour);
		Calendar.getInstance().set(Calendar.HOUR_OF_DAY, minute);
		Date result = Calendar.getInstance().getTime();
		return result;
	}
}
