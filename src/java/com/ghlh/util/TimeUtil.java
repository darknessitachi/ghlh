package com.ghlh.util;

import org.apache.log4j.Logger;

import com.ghlh.conf.ConfigurationAccessor;

public class TimeUtil {
	private static Logger logger = Logger
			.getLogger(ConfigurationAccessor.class);

	public static void pause(long minisecond) {
		try {
			Thread.sleep(minisecond);
		} catch (Exception ex) {
			logger.error("Pause throw exception : ", ex);
		}
	}

	public static void pauseSeconds(long second) {
		pause(second * 1000);
	}

}
