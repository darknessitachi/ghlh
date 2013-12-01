package com.ghlh;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ghlh.ui.autotradestart.AutoTradeSwitch;
import com.ghlh.util.TimeUtil;

public class AutoTradeAfterOpenPriceDecidedJob implements Job {
	private static Logger logger = Logger
			.getLogger(AutoTradeAfterOpenPriceDecidedJob.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.error("Enter monitoring at " + Calendar.getInstance().getTime());
		new StockTradeAfterOpenPriceDecidedMonitoringJob().monitoring();
		logger.error("Exit monitoring at " + Calendar.getInstance().getTime());
	}
}
