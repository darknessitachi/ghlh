package com.ghlh.autotrade;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

public class AutoTradeStockQuartzServer {
	private static Logger logger = Logger
			.getLogger(AutoTradeStockQuartzServer.class);

	private static AutoTradeStockQuartzServer instance = new AutoTradeStockQuartzServer();

	public static AutoTradeStockQuartzServer getInstance() {
		return instance;
	}

	private Scheduler scheduler = null;

	private void scheduleJob(int hour, int mins, String jobName, Class jobClass) {
		try {
			JobDetail job = new JobDetail(jobName + "Job",
					Scheduler.DEFAULT_GROUP, jobClass);
			Trigger trigger = TriggerUtils.makeDailyTrigger(jobName + "Triger",
					hour, mins);
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException ex) {
			logger.error("Make auto job : " + jobName + "throw: ", ex);
		}
	}

	private AutoTradeStockQuartzServer() {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduleJob(8, 0, "BeforeOpenAutoTradeJob",
					AutoTradeBeforeOpenJob.class);
			scheduleJob(9, 30, "MorningAutoTradeMonitoring",
					AutoTradeIntradayJob.class);
			scheduleJob(13, 0, "AfternoonAutoTradeMonitoring",
					AutoTradeIntradayJob.class);
			scheduleJob(15, 2, "AfterCloseAutoTradeJob",
					AutoTradeAfterCloseJob.class);

			for (int i = 0; i < 8; i++) {
				scheduleJob(i * 3, 15, "TradeSoftwareActivate" + i,
						AutoTradeSoftwareActivateJob.class);
			}

			scheduleJob(10, 0, "AutoDataCollectingJob1",
					AutoDataCollectingJob.class);
			scheduleJob(11, 0, "AutoDataCollectingJob2",
					AutoDataCollectingJob.class);
			scheduleJob(13, 30, "AutoDataCollectingJob3",
					AutoDataCollectingJob.class);
			scheduleJob(14, 30, "AutoDataCollectingJob4",
					AutoDataCollectingJob.class);
			
		
		} catch (SchedulerException ex) {
			logger.error("Make auto trade scheduler throw: ", ex);
		}
	}

	public void startJob() {
		try {
			scheduler.start();
		} catch (SchedulerException ex) {
			logger.error("Start AutoJob throw: ", ex);
		}
	}

	public void stopJob() {
		try {
			scheduler.standby();
		} catch (SchedulerException ex) {
			logger.error("Stop AutoJob throw: ", ex);
		}
	}
}