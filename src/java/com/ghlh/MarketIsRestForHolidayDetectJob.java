package com.ghlh;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ghlh.util.StockMarketUtil;

public class MarketIsRestForHolidayDetectJob implements Job {
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		/*MarketIsRestFlag.getInstance().setMarketRest(
				StockMarketUtil.isMarketInHoliday());*/
	}
}
