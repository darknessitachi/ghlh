package com.ghlh.autotrade;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ghlh.tradeway.SoftwareTrader;

public class AutoTradeSoftwareActivateJob implements Job {
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String message = "开始激活交易软件";
		EventRecorder.recordEvent(this.getClass(), message);
		new SoftwareTrader().activateTradeSoft();
		message = "结束激活交易软件";
		EventRecorder.recordEvent(this.getClass(), message);
	}
}
