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

	private AutoTradeStockQuartzServer() {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			JobDetail morningAutoTradeJob = new JobDetail(
					"MorningAutoTradeMonitoringJob", Scheduler.DEFAULT_GROUP,
					AutoTradeMonitoringJob.class);
			Trigger morningAutoTradeTrigger = TriggerUtils.makeDailyTrigger(
					"MorningAutoTradeMonitoringJob", 9, 30);
			scheduler.scheduleJob(morningAutoTradeJob, morningAutoTradeTrigger);
			Trigger afternoonAutoTradeTrigger = TriggerUtils.makeDailyTrigger(
					"SendThanksSMJob", 13, 0);
			JobDetail afternoonAutoTradeJob = new JobDetail(
					"AfternoonAutoTradeMonitoringJob", Scheduler.DEFAULT_GROUP,
					AutoTradeMonitoringJob.class);
			scheduler.scheduleJob(afternoonAutoTradeJob,
					afternoonAutoTradeTrigger);
		} catch (SchedulerException ex) {
			logger.error("Make auto trade scheduler throw: ", ex);
		}
	}

	public void addRightNowJob() {
		try {
			Trigger rightNowStart = TriggerUtils.makeImmediateTrigger(
					"RightNow", 1, 1);
			JobDetail rightNowAutoTradeJob = new JobDetail(
					"RightNowAutoTradeMonitoringJob", Scheduler.DEFAULT_GROUP,
					AutoTradeMonitoringJob.class);
			scheduler.scheduleJob(rightNowAutoTradeJob, rightNowStart);
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