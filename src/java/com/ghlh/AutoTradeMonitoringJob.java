package com.ghlh;

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
		//logger.error("CronJob Start job at " + Calendar.getInstance().getTime());
		//new StockTradeMonitoringJob().startMonitoring();
		while(AutoTradeSwitch.getInstance().isStart()){
			TimeUtil.pause(1000);
			System.out.println("testing");
		}
	}
}
