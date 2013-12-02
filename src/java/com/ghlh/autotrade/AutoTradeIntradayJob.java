package com.ghlh.autotrade;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AutoTradeIntradayJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String message = "开始盘中监控";
		EventRecorder.recordEvent(this.getClass(), message);
		new StockTradeIntradyMonitoringJob().monitoring();
		message = "结束盘中监控";
		EventRecorder.recordEvent(this.getClass(), message);
	}
}
