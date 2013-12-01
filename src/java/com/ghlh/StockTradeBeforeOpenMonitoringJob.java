package com.ghlh;

/*@author Robin*/

import org.apache.log4j.Logger;

public class StockTradeBeforeOpenMonitoringJob {
	private static Logger logger = Logger
			.getLogger(StockTradeBeforeOpenMonitoringJob.class);

	public void monitoring() {
		String oneTimeType = "BeforeOpenStrategy";
		OneTimeJobUtil.processOneTimeStrategy(oneTimeType);
	}

}
