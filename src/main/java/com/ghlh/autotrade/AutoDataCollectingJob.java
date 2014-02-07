package com.ghlh.autotrade;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.EastMoneyUtil;
import com.ghlh.util.StockMarketUtil;

public class AutoDataCollectingJob implements Job {
	private static Logger logger = Logger
			.getLogger(AutoDataCollectingJob.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int mins = Calendar.getInstance().get(Calendar.MINUTE);
		String sTime = hour + ":" + mins;
		String message = "¿ªÊ¼" + sTime + "Collecting Data";
		EventRecorder.recordEvent(this.getClass(), message);
		if (StockMarketUtil.isMarketRest()) {
			return;
		}
		List<StockQuotesBean> list = EastMoneyUtil
				.collectData(Constants.SZ_STOCK_COUNT);
		Date now = new Date();
		for (int i = 0; i < list.size(); i++) {
			StockQuotesBean sqb = list.get(i);
			GhlhDAO.createStockDailyIinfo(sqb, now);
		}
		message = "½áÊø" + sTime + "Collecting Data";
		EventRecorder.recordEvent(this.getClass(), message);
	}

	public static void main(String[] args) {
		AutoDataCollectingJob adcj = new AutoDataCollectingJob();
		try {
			adcj.execute(null);

		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}
}
