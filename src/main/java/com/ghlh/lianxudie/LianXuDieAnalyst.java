package com.ghlh.lianxudie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ghlh.data.db.StockdailyinfoDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.StockMarketUtil;

public class LianXuDieAnalyst {
	public final static int DAY_SIZE = 10;

	public static void main(String[] args) {
		List<StockQuotesBean> list2 = StockMarketUtil.getShenShiStockList();

//		StockQuotesBean sqb1 = new StockQuotesBean();
//		sqb1.setStockId("000150");
//		List<StockQuotesBean> list2 = new ArrayList<StockQuotesBean>();
//		list2.add(sqb1);
		
		Date startDate = DateUtil.getDate(2014, 3, 17);
		Date nextDay = DateUtil.getNextMarketOpenDay(startDate);

		for (int i = 0; i < list2.size(); i++) {
			StockQuotesBean sqb = (StockQuotesBean) list2.get(i);
			String stockId = sqb.getStockId();
			List stockDailyInfo = StockdailyinfoDAO.getDaysInfo(stockId,
					DAY_SIZE, nextDay);
			boolean reachCondition = true;
			if (stockDailyInfo.size() < 3) {
				break;
			}
			for (int j = 0; j < 3; j++) {
				StockdailyinfoVO sdiVO = (StockdailyinfoVO) stockDailyInfo
						.get(j);
				if (j == 0 && sdiVO.getZdf() > -4) {
					reachCondition = false;
					break;
				}
				if ((j == 1 || j == 2) && sdiVO.getZdf() > 0) {
					reachCondition = false;
					break;
				}
			}

			if (reachCondition) {
				System.out.println("Stock : " + stockId + " reach condition!");
			}

		}
	}

}
