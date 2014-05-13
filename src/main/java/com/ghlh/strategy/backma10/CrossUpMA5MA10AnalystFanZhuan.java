package com.ghlh.strategy.backma10;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ghlh.data.db.StockdailyinfoDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.KLineUtil;
import com.ghlh.util.MathUtil;
import com.ghlh.util.StockMarketUtil;

public class CrossUpMA5MA10AnalystFanZhuan {
	public final static int DAY_SIZE = 26;

	public static void main(String[] args) {
		List<StockQuotesBean> list2 = StockMarketUtil.getShenShiStockList();

		// List<StockQuotesBean> list2 = new ArrayList<StockQuotesBean>();
		// StockQuotesBean sqb1 = new StockQuotesBean();
		// sqb1.setStockId("002722");
		// list2.add(sqb1);
		// Date startDate = new Date();
		Date startDate = DateUtil.getDate(2014, 3, 29);
		Date now = new Date();
		while (startDate.before(now)) {

			Date nextDay = DateUtil.getNextMarketOpenDay(startDate);
			int no = 0;

			for (int i = 0; i < list2.size(); i++) {
				StockQuotesBean sqb = (StockQuotesBean) list2.get(i);
				String stockId = sqb.getStockId();
				List stockDailyInfo = StockdailyinfoDAO.getDaysInfo(stockId,
						DAY_SIZE, nextDay);
				boolean reachCondition = true;
				for (int currentDay = 0; currentDay < 1; currentDay++) {
					MaBean maBean = calculateMA(stockDailyInfo, currentDay);
					StockdailyinfoVO sdiVO = (StockdailyinfoVO) stockDailyInfo
							.get(currentDay);
					double maPercentage = 0;
					maPercentage = (maBean.getMa10() - maBean.getMa5())
							/ maBean.getMa5() * 100;
					if (sdiVO.getZdf() > 1 && sdiVO.getTodayopenprice() != 0
							&& sdiVO.getTodayopenprice() < maBean.getMa5()
							&& sdiVO.getCurrentprice() > maBean.getMa10()
							&& maBean.getMa5() <= maBean.getMa10()) {
						boolean ma5UnderMA10 = true;
						for (int j = 1; j < 6; j++) {
							maBean = calculateMA(stockDailyInfo, j);
							if (maBean.getMa5() > maBean.getMa10()) {
								ma5UnderMA10 = false;
								break;
							}
						}
						if (ma5UnderMA10) {
							KLineUtil.saveUrlAs(
									sdiVO.getStockid(),
									"crossupma5ma10\\fanzhuan\\"
											+ DateUtil.formatDay(startDate),
									sdiVO.getStockid());
						}
					}
				}
			}
			startDate = DateUtil.getNextMarketOpenDay(startDate);
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
