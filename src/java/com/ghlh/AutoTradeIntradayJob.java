package com.ghlh;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AutoTradeIntradayJob implements Job {
	private static Logger logger = Logger.getLogger(AutoTradeIntradayJob.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.error("Enter monitoring at " + Calendar.getInstance().getTime());
		new StockTradeIntradyNewMonitoringJob().monitoring();
		logger.error("Exit monitoring at " + Calendar.getInstance().getTime());
	}
}
