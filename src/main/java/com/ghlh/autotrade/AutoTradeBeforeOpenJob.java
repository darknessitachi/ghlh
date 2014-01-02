package com.ghlh.autotrade;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ghlh.ui.autotradestart.AutoTradeSwitch;
import com.ghlh.util.TimeUtil;

public class AutoTradeBeforeOpenJob implements Job {
	private static Logger logger = Logger
			.getLogger(AutoTradeBeforeOpenJob.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String message = "开始8:00盘前处理";
		EventRecorder.recordEvent(this.getClass(), message);
		String oneTimeType = "BeforeOpenStrategy";
		OneTimeJobUtil.processOneTimeStrategy(oneTimeType);
		message = "结束8:00盘前处理";
		EventRecorder.recordEvent(this.getClass(), message);
	}
}
