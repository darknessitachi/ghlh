package com.ghlh.strategy.morning4percent;

import java.util.List;

import org.apache.log4j.Logger;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.EastMoneyUtil;
import com.ghlh.util.HttpUtil;

public class DataCollecter {
	public static Logger logger = Logger.getLogger(DataCollecter.class);

	private final static int INIT_COUNT = 2000;

	public void collectData() {
		List<StockQuotesBean> list = EastMoneyUtil.collectData(INIT_COUNT);
		for (int i = 0; i < list.size(); i++) {
			StockQuotesBean sqb = list.get(i);
			GhlhDAO.createStockDailyIinfo(sqb);
		}

	}

	public static void main(String[] args) {
		new DataCollecter().collectData();
	}
}
