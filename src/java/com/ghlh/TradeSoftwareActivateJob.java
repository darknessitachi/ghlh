package com.ghlh;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ghlh.tradeway.SoftwareTrader;

public class TradeSoftwareActivateJob implements Job {

	private static Logger logger = Logger
			.getLogger(TradeSoftwareActivateJob.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.error("Activating TradeSoftware at "
				+ Calendar.getInstance().getTime());

		new SoftwareTrader().activateTradeSoft();
		logger.error("Finished Activating TradeSoftware at "
				+ Calendar.getInstance().getTime());

	}
}
