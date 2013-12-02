package com.ghlh.autotrade;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AutoTradeAfterOpenPriceDecidedJob implements Job {
	private static Logger logger = Logger
			.getLogger(AutoTradeAfterOpenPriceDecidedJob.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String message = "开始 9:25(定开盘价后)盘前处理";
		EventRecorder.recordEvent(this.getClass(), message);
		String oneTimeType = "AfterOpenPriceDecidedStrategy";
		OneTimeJobUtil.processOneTimeStrategy(oneTimeType);
		message = "结束 9:25(定开盘价后)盘前处理";
		EventRecorder.recordEvent(this.getClass(), message);
	}
}
