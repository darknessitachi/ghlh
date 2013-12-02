package com.ghlh.autotrade;

import org.apache.log4j.Logger;

import com.ghlh.ui.autotradestart.AutoTradeMonitor;

public class EventRecorder {
	private static Logger logger = Logger.getLogger(EventRecorder.class);

	public static void recordEvent(Class happenedClass, String message) {
		logger.info(happenedClass + message);
		AutoTradeMonitor.getInstance().appendMonitorInfo(message);
	}
}
