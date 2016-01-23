package com.ghlh.strategy.bigzfin10days;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ghlh.analysis.Constants;
import com.ghlh.data.db.StockdailyinfoDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.KLineUtil;
import com.ghlh.util.MathUtil;
import com.ghlh.util.StockMarketUtil;

public class BigIncreasingIn10DaysAnalyst {
	public final static int DAY_SIZE = 10;
	public final static double PERCENTAGE = 0.60;
	
	public final static double winPercentage = 0.2;
	public final static double lostPercentage = 0.2;

	public static void main(String[] args) {
		 List<StockQuotesBean> list2 = StockMarketUtil.getShenShiStockList();

//		List<StockQuotesBean> list2 = new ArrayList<StockQuotesBean>();
//
//		StockQuotesBean sqb1 = new StockQuotesBean();
//		sqb1.setStockId("000971");
//		sqb1.setName("abc");
		// sqb1 = new StockQuotesBean();
		// sqb1.setName("bcd");
		//
		// sqb1.setStockId("002722");

		//list2.add(sqb1);

		for (int i = 0; i < list2.size(); i++) {
			StockQuotesBean sqb = (StockQuotesBean) list2.get(i);
			String stockId = sqb.getStockId();
			List stockDailyInfo = StockdailyinfoDAO.getDaysInfo(stockId);
			if (stockDailyInfo.size() == 0) {
				System.out.println("停牌中StockId = " + stockId);
				continue;
			}

			for (int j = 0; j < stockDailyInfo.size(); j++) {
				StockdailyinfoVO sdiVO = (StockdailyinfoVO) stockDailyInfo
						.get(j);
				double startClosePrice = sdiVO.getCurrentprice();
				int inDay = 0;
				for (int k = j + 3; k <= j + DAY_SIZE; k++) {
					if (k >= stockDailyInfo.size()) {
						break;
					}

					StockdailyinfoVO sdiVO1 = (StockdailyinfoVO) stockDailyInfo
							.get(k);
					if (sdiVO1.getCurrentprice() == sdiVO1.getHighestprice()
							&& sdiVO1.getHighestprice() == sdiVO1
									.getLowestprice()) {
						break;
					}

					double endClosePrice = sdiVO1.getCurrentprice();
					double percentage = (endClosePrice - startClosePrice)
							/ startClosePrice;
					if (percentage > PERCENTAGE) {
						int yiziCount = 0;
						for (int h = j + 1; h <= k; h++) {
							StockdailyinfoVO sdiVO2 = (StockdailyinfoVO) stockDailyInfo
									.get(h);
							if (sdiVO2.getCurrentprice() == sdiVO2
									.getHighestprice()
									&& sdiVO2.getHighestprice() == sdiVO2
											.getLowestprice()) {
								yiziCount++;
							}
						}

						if (yiziCount <= 0) {
							inDay = k + 1;
//							System.out.println("StockID = " + sdiVO.getStockid() + " on " + sdiVO.getDate());
							break;
						}
					}
				}
				if (inDay > 0) {
//					buyInCount++;
					System.out.println("StockID = " + sdiVO.getStockid() + "  \t  " + sdiVO.getDate());
				
//					processBuyIn(sqb, stockDailyInfo, inDay);
					break;
				}
			}
		}

		System.out.println("In number : " + buyInCount + " winIn5Days : "
				+ winIn5Days);
	}

	public static int buyInCount;
	public static int winIn5Days;

	private static void processBuyIn(StockQuotesBean sqb, List stockDailyInfo,
			int inDay) {
		if (stockDailyInfo.size() > inDay) {
			StockdailyinfoVO sdiVO1 = (StockdailyinfoVO) stockDailyInfo
					.get(inDay);
			double buyPrice = sdiVO1.getTodayopenprice();
			Date inDate = sdiVO1.getDate();
			double winPrice = MathUtil.formatDoubleWith2QuanShe(buyPrice
					* (1 + winPercentage));
			double lostPrice = MathUtil.formatDoubleWith2QuanJin(buyPrice
					* (1 - lostPercentage));

			for (int k = inDay + 1; k < stockDailyInfo.size(); k++) {
				StockdailyinfoVO stockdailyinfoVO1 = (StockdailyinfoVO) stockDailyInfo
						.get(k);
				if (stockdailyinfoVO1.getCurrentprice() == 0) {
					continue;
				}
				double highPrice = stockdailyinfoVO1.getHighestprice();
				double lowPrice = stockdailyinfoVO1.getLowestprice();
				if (highPrice >= winPrice && lowPrice <= lostPrice) {
					System.out.println("同时在一天达到盈利 和 亏损 价格");
					break;
				} else if (highPrice >= winPrice) {
					if ((k - inDay) <= 5) {
						winIn5Days++;
					}
					System.out.println(inDate + " " + sqb.getStockId() + " "
							+ sqb.getName() + "在第 " + (k - inDay)
							+ " 天盈利*******************成交, 买入价:" + buyPrice
							+ " 卖出价 :" + winPrice + " 卖出当日涨幅:"
							+ stockdailyinfoVO1.getZdf());
					KLineUtil.saveUrlAs(sqb.getStockId(), "qzt",
							DateUtil.formatDay(inDate) + "_" + (k - inDay)
									+ "天" + "成功" + "_" + sqb.getStockId() + "_"
									+ sqb.getName());
					break;
				} else if (lowPrice <= lostPrice) {
					System.out.println(inDate + " " + sqb.getStockId() + " "
							+ sqb.getName() + "在第 " + (k - inDay)
							+ " 天亏损_________________________成交, 买入价:"
							+ buyPrice + " 卖出价 :" + lostPrice + " 卖出当日涨幅:"
							+ stockdailyinfoVO1.getZdf());
					KLineUtil.saveUrlAs(sqb.getStockId(), "qzt",
							DateUtil.formatDay(inDate) + "_" + (k - inDay)
									+ "天" + "失败" + "_" + sqb.getStockId() + "_"
									+ sqb.getName());
					break;
				}

				if (k == stockDailyInfo.size() - 1) {
					double currentPrice = stockdailyinfoVO1.getCurrentprice();
					double yikui = MathUtil
							.formatDoubleWith2((currentPrice - buyPrice)
									/ buyPrice * 100);
					System.out.println(inDate + " " + sqb.getStockId() + " "
							+ sqb.getName() + "在第 " + (k - inDay)
							+ " 天未达目标, 买入价:" + buyPrice
							+ " 当前-------------------------------盈亏: " + yikui);

					KLineUtil.saveUrlAs(
							sqb.getStockId(),
							"from25to50",
							DateUtil.formatDay(inDate) + "_" + (k - inDay)
									+ "天" + "当前盈亏_" + yikui + "_"
									+ sqb.getStockId() + "_" + sqb.getName());

				}
			}

		}
	}
}
