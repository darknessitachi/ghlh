package com.ghlh.strategy.backma10;

import java.util.Date;
import java.util.List;

import com.ghlh.autotrade.Constants;
import com.ghlh.data.db.StockdailyinfoDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.EastMoneyUtil;
import com.ghlh.util.MathUtil;

public class MA10MA5Analyst {
	public final static int DAY_SIZE = 26;

	public static void main(String[] args) {
		// List<StockQuotesBean> list1 = EastMoneyUtil.collectData(
		// Constants.SZ_STOCK_COUNT, 10);
		List<StockQuotesBean> list2 = EastMoneyUtil.collectData(
				Constants.SZ_STOCK_COUNT, 20);

		int days_MA5ONMA10 = 9;
		int days_MA5UnderMA10 = 1;
		Date startDate = DateUtil.getDate(2014, 2, 24);
		Date nextDay = DateUtil.getNextMarketOpenDay(startDate);
		// List<StockQuotesBean> list2 = new ArrayList<StockQuotesBean>();
		// StockQuotesBean sqb1 = new StockQuotesBean();
		// sqb1.setStockId("000002");
		// list2.add(sqb1);
		int no = 0;

		for (int i = 0; i < list2.size(); i++) {
			StockQuotesBean sqb = (StockQuotesBean) list2.get(i);
			String stockId = sqb.getStockId();
			List stockDailyInfo = StockdailyinfoDAO.getDaysInfo(stockId,
					DAY_SIZE, nextDay);
			boolean reachCondition = true;
			for (int currentDay = 0; currentDay < days_MA5ONMA10
					+ days_MA5UnderMA10; currentDay++) {
				MaBean maBean = calculateMA(stockDailyInfo, currentDay);
				if (maBean.getMa5() <= maBean.getMa10()
						&& currentDay < days_MA5ONMA10) {
					reachCondition = false;
					break;
				}
				if (currentDay >= days_MA5ONMA10
						&& maBean.getMa5() >= maBean.getMa10()) {
					reachCondition = false;
					break;
				}
			}
			if (reachCondition) {
				no++;
				System.out.println("no = " + no + "stockId = " + stockId);
			}
		}
	}

	private static MaBean calculateMA(List stockDailyInfo, int currentDay) {
		double ma5Sum = 0;
		int ma5SumTimes = 0;
		double ma10Sum = 0;
		int ma10SumTimes = 0;
		double currentPrice = 0;
		MaBean result = new MaBean();
		boolean dataNotEnough = false;
		for (int j = currentDay; j < stockDailyInfo.size(); j++) {
			StockdailyinfoVO sdiVO = (StockdailyinfoVO) stockDailyInfo.get(j);
			if (sdiVO.getZde() == 0) {
				continue;
			}
			result.setStockId(sdiVO.getStockid());
			if (sdiVO.getCurrentprice() != 0 && currentPrice == 0) {
				currentPrice = sdiVO.getCurrentprice();
			}
			if (sdiVO.getCurrentprice() != 0 && ma5SumTimes < 5) {
				ma5Sum += sdiVO.getCurrentprice();
				ma5SumTimes++;
			}
			if (sdiVO.getCurrentprice() != 0 && ma10SumTimes < 10) {
				ma10Sum += sdiVO.getCurrentprice();
				ma10SumTimes++;
			}
			if (ma10SumTimes >= 10) {
				break;
			}
		}
		if (ma10SumTimes >= 10) {
			double ma5 = MathUtil.formatDoubleWith2(ma5Sum / 5);
			double ma10 = MathUtil.formatDoubleWith2(ma10Sum / 10);

			result.setCurrentPrice(currentPrice);
			result.setMa10(ma10);
			result.setMa5(ma5);
		} else {
			result.setNoEnoughData(true);
		}
		return result;
	}
}
