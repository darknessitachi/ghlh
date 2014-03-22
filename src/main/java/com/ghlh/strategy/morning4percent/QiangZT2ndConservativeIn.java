package com.ghlh.strategy.morning4percent;

import java.util.Date;
import java.util.List;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.MathUtil;

public class QiangZT2ndConservativeIn {

	public static void main(String[] args) {
		Date date = DateUtil.getDate(2014, 2, 7);
		int zdts = 20;
		double minZdf = -5;
		double maxZdf = 5;
		double maxAvg = 0.3;
		double minAvg = -0.3;
		double closeZT = 9;
		double winPercentage = 0.08;
		double lostPercentage = 0.1;

		Date now = new Date();
		while (date.before(now)) {
			String sDate = DateUtil.formatDay(date);
			String zdStartDate = DateUtil.formatDay(DateUtil
					.getPreviousMarketOpenDay(date, zdts));
			String sql = "SELECT * FROM "
					+ " (SELECT stockId,COUNT(zdf) tradedays, MAX(zdf) maxzdf, MIN(zdf) minzdf, AVG(zdf) avgzdf FROM stockdailyinfo WHERE DATE > '"
					+ zdStartDate + "' AND DATE < '" + sDate
					+ "' GROUP BY stockId) a,"
					+ " (SELECT * FROM stockdailyinfo WHERE zdf > " + closeZT
					+ " AND DATE LIKE '" + sDate
					+ "%' AND highestprice <> lowestprice) b"
					+ " WHERE a.stockid = b.stockid AND maxzdf < " + maxZdf
					+ " AND minzdf > " + minZdf + " AND avgzdf > " + minAvg
					+ " and avgzdf < " + maxAvg;
			// System.out.println("SQL = " + sql);
			List list = GhlhDAO.list(sql);
			if (list == null) {
				date = DateUtil.getNextMarketOpenDay(date);
				continue;
			}

			for (int i = 0; i < list.size(); i++) {
				String stockId = (String) list.get(i);
				StockQuotesBean sqb = InternetStockQuotesInquirer.getInstance()
						.getStockQuotesBean(stockId);
				if (sqb.getName().indexOf("ST") >= 0) {
					continue;
				}
				Date inDate = DateUtil.getNextMarketOpenDay(date);
				String sql1 = "SELECT * FROM stockdailyinfo WHERE DATE LIKE '"
						+ DateUtil.formatDay(inDate) + "%' AND stockId = '"
						+ stockId + "'";
				List dailyInfoList = GhlhDAO.list(sql1,
						"com.ghlh.data.db.StockdailyinfoVO", 0, 1);
				if (dailyInfoList == null || dailyInfoList.size() == 0) {
					continue;
				}
				StockdailyinfoVO stockdailyinfoVO = (StockdailyinfoVO) dailyInfoList
						.get(0);

				if (buyCondition(stockdailyinfoVO)) {
					pickup++;
					System.out.println(date + " " + stockId
							+ "is picked up, 买入价"
							+ stockdailyinfoVO.getCurrentprice());
					processBuyStockResult(stockdailyinfoVO, sqb, inDate,
							winPercentage, lostPercentage, "0");
				}
			}
			date = DateUtil.getNextMarketOpenDay(date);
		}
		System.out.println("盈利 " + yinLi + "笔" + " 亏损" + kuiSun + "笔"
				+ " Pick up : " + pickup + "笔");
	}

	private static double inPrice = 0;
	public static boolean buyCondition(StockdailyinfoVO stockdailyinfoVO) {
		boolean result = false;
		inPrice = stockdailyinfoVO.getTodayopenprice();
		if (false) {
			result = stockdailyinfoVO.getTodayopenprice() < stockdailyinfoVO
					.getYesterdaycloseprice();
			inPrice = stockdailyinfoVO.getTodayopenprice();
		}
		if (false) {
			double price = stockdailyinfoVO.getYesterdaycloseprice()
					* 0.97;
			price = MathUtil.formatDoubleWith2QuanJin(price);
			result = price > stockdailyinfoVO.getLowestprice();
			inPrice = price;
		}
		if (true) {
			double price = stockdailyinfoVO.getTodayopenprice()
					* 0.97;
			price = MathUtil.formatDoubleWith2QuanJin(price);
			result = price > stockdailyinfoVO.getLowestprice();
			inPrice = price;
		}

		
		//Condition1#, low open
		//Condition2#, some percentage low as yesterday close
		//Condition3#, some percentage low as today's open.
//		double price = stockdailyinfoVO.getTodayopenprice()
//				* 0.97;
//		price = MathUtil.formatDoubleWith2QuanJin(price);
//		boolean result = price > stockdailyinfoVO.getLowestprice();
//		inPrice = price;
		return result;
	}

	private static int kuiSun = 0;
	private static int yinLi = 0;
	private static int pickup = 0;

	public static void processBuyStockResult(StockdailyinfoVO stockdailyinfoVO,
			StockQuotesBean sqb, Date date, double winPercentage,
			double lostPercentage, String sCount) {
		double buyPrice = inPrice;
		double winPrice = MathUtil.formatDoubleWith2QuanShe(buyPrice
				* (1 + winPercentage));
		double lostPrice = MathUtil.formatDoubleWith2QuanJin(buyPrice
				* (1 - lostPercentage));

		Date next = DateUtil.getNextDay(date);
		String sNext = DateUtil.formatDay(next);
		String sql1 = "SELECT * FROM stockdailyinfo WHERE stockid = '"
				+ stockdailyinfoVO.getStockid() + "' AND DATE > '" + sNext
				+ "' ORDER BY Date";

		List dailyInfoList = GhlhDAO.list(sql1,
				"com.ghlh.data.db.StockdailyinfoVO");
		for (int j = 0; j < dailyInfoList.size(); j++) {
			StockdailyinfoVO stockdailyinfoVO1 = (StockdailyinfoVO) dailyInfoList
					.get(j);
			if (stockdailyinfoVO1.getCurrentprice() == 0) {
				continue;
			}
			double highPrice = stockdailyinfoVO1.getHighestprice();
			double lowPrice = stockdailyinfoVO1.getLowestprice();
			if (highPrice >= winPrice && lowPrice <= lostPrice) {
				System.out.println("同时在一天达到盈利 和 亏损 价格");
				break;
			}
			if (highPrice >= winPrice) {
				System.out.println(date + " " + sqb.getStockId() + " "
						+ sqb.getName() + "在第 " + (j + 1) + " 天盈利成交, 买入价:"
						+ buyPrice + " 卖出价 :" + winPrice + " 卖出当日涨幅:"
						+ stockdailyinfoVO1.getZdf() + " 买入当日数量:" + sCount);
				yinLi++;
				break;
			}
			if (lowPrice <= lostPrice) {
				System.out.println(date + " " + sqb.getStockId() + " "
						+ sqb.getName() + "在第 " + (j + 1) + " 天亏损成交, 买入价:"
						+ buyPrice + " 卖出价 :" + lostPrice + " 卖出当日涨幅:"
						+ stockdailyinfoVO1.getZdf() + " 买入当日数量:" + sCount);
				kuiSun++;
				break;
			}
		}
	}

}
