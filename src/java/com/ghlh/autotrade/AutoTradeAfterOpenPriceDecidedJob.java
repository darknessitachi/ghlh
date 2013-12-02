package com.ghlh.autotrade;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AutoTradeAfterOpenPriceDecidedJob implements Job {
	private static Logger logger = Logger
			.getLogger(AutoTradeAfterOpenPriceDecidedJob.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String message = "��ʼ 9:25(�����̼ۺ�)��ǰ����";
		EventRecorder.recordEvent(this.getClass(), message);
		String oneTimeType = "AfterOpenPriceDecidedStrategy";
		OneTimeJobUtil.processOneTimeStrategy(oneTimeType);
		message = "���� 9:25(�����̼ۺ�)��ǰ����";
		EventRecorder.recordEvent(this.getClass(), message);
	}
}
