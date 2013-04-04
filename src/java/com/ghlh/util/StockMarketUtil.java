package com.ghlh.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;

public class StockMarketUtil {
	private static Logger logger = Logger.getLogger(StockMarketUtil.class);

	public static String getMarketRestCause() {
		Date currentTime = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("MM-dd");
		String sDate = dateFormat.format(currentTime);
		Map holidays = readMarketHoliday();
		String holidayName = (String) holidays.get(sDate);
		String result = null;
		if (holidayName != null) {
			result = "今天" + holidayName + "休市";
		} else if (isWeekend()) {
			result = "今天周末休市";
		}
		return result;
	}

	public static String getCloseCause() {
		Date currentTime = Calendar.getInstance().getTime();
		String result = null;
		if (isMarketBeforeMorningOpenning(currentTime)) {
			result = "现在尚未开市";
		}
		if (isMarketNoonClose(currentTime)) {
			result = "现在午间休市";
		}
		if (isMarketAfterAfternoonClosing(currentTime)) {
			result = "现在已闭市";
		}
		return result;
	}

	private static boolean isWeekend() {
		Calendar cal = Calendar.getInstance();
		int weekday = cal.get(Calendar.DAY_OF_WEEK);
		return (weekday == Calendar.SATURDAY || weekday == Calendar.SUNDAY);
	}

	private static boolean isMarketBeforeMorningOpenning(Date currentTime) {
		Date morningOpeningTime = getCentainTime(9, 30);
		return currentTime.before(morningOpeningTime);
	}

	private static boolean isMarketAfterAfternoonClosing(Date currentTime) {
		Date afternoonClosingTime = getCentainTime(15, 0);
		return currentTime.equals(afternoonClosingTime)
				|| currentTime.after(afternoonClosingTime);
	}

	private static boolean isMarketNoonClose(Date currentTime) {
		Date morningClosingTime = getCentainTime(11, 30);
		Date afternoonOpeningTime = getCentainTime(13, 0);
		boolean result = (currentTime.equals(morningClosingTime) || currentTime
				.after(morningClosingTime))
				&& currentTime.before(afternoonOpeningTime);
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

	private static Map readMarketHoliday() {
		Map result = new HashMap();
		InputStream is = StockMarketUtil.class
				.getResourceAsStream("marketholidays.properties");
		String line = null;
		if (is != null) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "UTF-8"));
				while ((line = br.readLine()) != null
						&& !line.trim().equals("")) {
					String date = line.substring(0, line.indexOf('='));
					String holidayName = line.substring(line.indexOf('=') + 1);
					result.put(date, holidayName);
				}
				br.close();
				is.close();
			} catch (Exception e) {
				logger.error("Read stragegies file: strategies.properties,"
						+ " throw : ", e);
			}
		}
		return result;
	}
}
