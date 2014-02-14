package com.ghlh.autotrade;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ghlh.strategy.morning4percent.Buyer;
import com.ghlh.util.StockMarketUtil;

public class AutoDataCollectingJob implements Job {
	private static Logger logger = Logger
			.getLogger(AutoDataCollectingJob.class);
	DataCollector dc = new DataCollector();

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int mins = Calendar.getInstance().get(Calendar.MINUTE);
		String sTime = hour + ":" + mins;
		String message = "¿ªÊ¼" + sTime + "Collecting Data";
		EventRecorder.recordEvent(this.getClass(), message);
		if (StockMarketUtil.isMarketRest()) {
			return;
		}
		Date now = new Date();
		dc.collectDailyInfo(now, true);
		message = "½áÊø" + sTime + "Collecting Data";
		EventRecorder.recordEvent(this.getClass(), message);
		if (hour == 10) {
			new Buyer().buy(now);
		}

	}

	public static void main(String[] args) {
		AutoDataCollectingJob adcj = new AutoDataCollectingJob();
		Date now = new Date();
		try {
			adcj.execute(null);
		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}
}
