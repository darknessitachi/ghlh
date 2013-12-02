package com.ghlh.autotrade;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ghlh.tradeway.SoftwareTrader;

public class AutoTradeSoftwareActivateJob implements Job {
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String message = "��ʼ��������";
		EventRecorder.recordEvent(this.getClass(), message);
		new SoftwareTrader().activateTradeSoft();
		message = "������������";
		EventRecorder.recordEvent(this.getClass(), message);
	}
}
