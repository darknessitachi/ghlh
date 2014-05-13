package com.ghlh.strategy.ztCount;

import java.util.Date;
import java.util.List;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.KLineUtil;
import com.ghlh.util.StockMarketUtil;

public class GetAllKline {

	public static void main(String[] args) {
		List<StockQuotesBean> list2 = StockMarketUtil.getShenShiStockList();
		Date now = new Date();
		for (int j = 0; j < list2.size(); j++) {
			StockQuotesBean bean = (StockQuotesBean) list2.get(j);
			if (bean.getCje() > 5000 && bean.getHsl() > 1 && bean.getCurrentPrice() != 0) {
				KLineUtil.saveUrlAs(bean.getStockId(),
						"allklines\\" + DateUtil.formatDay(now),
						bean.getStockId());
			}
		}

	}
}
