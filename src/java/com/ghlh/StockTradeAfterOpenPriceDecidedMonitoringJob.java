package com.ghlh;

/*@author Robin*/

import org.apache.log4j.Logger;

public class StockTradeAfterOpenPriceDecidedMonitoringJob {
	private static Logger logger = Logger
			.getLogger(StockTradeAfterOpenPriceDecidedMonitoringJob.class);

	public void monitoring() {
		String oneTimeType = "AfterOpenPriceDecidedStrategy";
		OneTimeJobUtil.processOneTimeStrategy(oneTimeType);
	}

}
