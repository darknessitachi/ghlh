package com.ghlh;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AutoTradeIntradayJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String message = "��ʼ���м��";
		EventRecorder.recordEvent(this.getClass(), message);
		new StockTradeIntradyNewMonitoringJob().monitoring();
		message = "�������м��";
		EventRecorder.recordEvent(this.getClass(), message);
	}
}
