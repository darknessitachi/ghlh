package com.ghlh;

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
			scheduleJob(9, 30, "MorningAutoTradeMonitoring",
					AutoTradeMonitoringJob.class);
			scheduleJob(13, 0, "AfternoonAutoTradeMonitoring",
					AutoTradeMonitoringJob.class);

		} catch (SchedulerException ex) {
			logger.error("Make auto trade scheduler throw: ", ex);
		}
	}

	private void scheduleRightNowJob(Class jobClass, String jobName) {
		try {
			Trigger rightNowStart = TriggerUtils.makeImmediateTrigger(jobName
					+ "Trigger", 0, 0);
			JobDetail rightNowAutoTradeJob = new JobDetail(jobName + "Job",
					Scheduler.DEFAULT_GROUP, jobClass);
			scheduler.scheduleJob(rightNowAutoTradeJob, rightNowStart);
		} catch (SchedulerException ex) {
			logger.error("Make RightNowAutoTradeMonitoringJob job : throw: ",
					ex);
		}
	}

	public void addRightNowJob() {
		scheduleRightNowJob(AutoTradeMonitoringJob.class,
				"RightNowAutoTradeMonitoring");
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