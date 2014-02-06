package com.ghlh.strategy.morning4percent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.EastMoneyUtil;

public class DataCollecter {
	public static Logger logger = Logger.getLogger(DataCollecter.class);

	private final static int INIT_COUNT = 2000;

	public void collectData() {
		List<StockQuotesBean> list = EastMoneyUtil.collectData(INIT_COUNT);
		List<StockQuotesBean> targetStocks = new ArrayList<StockQuotesBean>();
		for (int i = 0; i < list.size(); i++) {
			StockQuotesBean sqb = list.get(i);
			boolean meetZDRate = isMeetZDRate(sqb);
			boolean meetKPZDRate = isMeetKPZDF(sqb);
			boolean meetHPRate = false;
			if(sqb.getHighestPrice() <= sqb.getCurrentPrice()){
				meetHPRate = true;
			}else{
				double gap = sqb.getHighestPrice() - sqb.getCurrentPrice();
				double gapRate = gap/sqb.getCurrentPrice();
				if(gapRate < 0.02){
					meetHPRate = true;
				}
			}

			if (meetKPZDRate) {

			}
		}

		Date now = new Date();
		for (int i = 0; i < list.size(); i++) {
			StockQuotesBean sqb = list.get(i);
			GhlhDAO.createStockDailyIinfo(sqb, now);
		}

	}

	private boolean isMeetKPZDF(StockQuotesBean sqb) {
		boolean meetKPZDRate;
		double kpZD = sqb.getTodayOpen() - sqb.getYesterdayClose();
		double kpZDF = kpZD / sqb.getYesterdayClose();
		meetKPZDRate = kpZDF <= 0.02 && kpZDF >= -0.02;
		return meetKPZDRate;
	}

	private boolean isMeetZDRate(StockQuotesBean sqb) {
		return sqb.getZdf() >= 3.5 && sqb.getZdf() <= 5.5;
	}

	public static void main(String[] args) {
		new DataCollecter().collectData();
	}
}
