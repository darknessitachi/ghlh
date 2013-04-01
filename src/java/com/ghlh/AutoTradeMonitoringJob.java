package com.ghlh;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ghlh.ui.autotradestart.AutoTradeSwitch;
import com.ghlh.util.TimeUtil;

public class AutoTradeMonitoringJob implements Job {
	private static Logger logger = Logger
			.getLogger(AutoTradeMonitoringJob.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.error("Enter monitoring at " + Calendar.getInstance().getTime());
		new StockTradeMonitoringJob().monitoring();
		logger.error("Exit monitoring at " + Calendar.getInstance().getTime());
	}
}
