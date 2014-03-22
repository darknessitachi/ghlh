package com.ghlh.strategy.morning4percent;

import java.util.Date;
import java.util.List;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.MathUtil;

public class QiangZTOpitimization {

	public QiangZTResultBean calculateResult(FactorsBean bean) {
		pickup = 0;
		yinLi = 0;
		kuiSun = 0;
		Date date = bean.getDate();
		int zdts = bean.getZdts();
		double minZdf = bean.getMinZdf();
		double maxZdf = bean.getMaxZdf();
		double maxAvg = bean.getMaxAvg();
		double minAvg = bean.getMinAvg();
		double closeZT = bean.getCloseZT();
		double winPercentage = bean.getWinPercentage();
		double lostPercentage = bean.getLostPercentage();

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

				if (buyCondition(stockdailyinfoVO, bean)) {
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
		// System.out.println("盈利 " + yinLi + "笔" + " 亏损" + kuiSun + "笔"
		// + " Pick up : " + pickup + "笔");

		QiangZTResultBean result = new QiangZTResultBean();
		result.setKuiSun(kuiSun);
		result.setYinLi(yinLi);
		result.setPickUp(pickup);
		return result;
	}

	private double buyPrice;

	public boolean buyCondition(StockdailyinfoVO stockdailyinfoVO,
			FactorsBean bean) {
		boolean result = true;
		buyPrice = stockdailyinfoVO.getTodayopenprice();
		if (bean.isLowOpen()) {
			result = stockdailyinfoVO.getTodayopenprice() < stockdailyinfoVO
					.getYesterdaycloseprice();
			buyPrice = stockdailyinfoVO.getTodayopenprice();
		}
		if (bean.getBasedonYesterdayClosePercentage() != 0) {
			double price = stockdailyinfoVO.getYesterdaycloseprice()
					* (100 + bean.getBasedonYesterdayClosePercentage()) / 100;
			price = MathUtil.formatDoubleWith2QuanJin(price);
			result = price > stockdailyinfoVO.getLowestprice();
			buyPrice = price;
		}
		if (bean.getBasedonTodayOpenPercentage() != 0) {
			double price = stockdailyinfoVO.getTodayopenprice()
					* (100 + bean.getBasedonTodayOpenPercentage()) / 100;
			price = MathUtil.formatDoubleWith2QuanJin(price);
			result = price > stockdailyinfoVO.getLowestprice();
			buyPrice = price;
		}

		return result;
		// Condition1#, low open
		// Condition2#, some percentage low as yesterday close
		// Condition3#, some percentage low as today's open.
		// double buyPrice = stockdailyinfoVO.getYesterdaycloseprice() * 0.97;
		// boolean inCondition = buyPrice >= stockdailyinfoVO.getLowestprice();
		// return inCondition;
	}

	private int kuiSun = 0;
	private int yinLi = 0;
	private int pickup = 0;

	public void processBuyStockResult(StockdailyinfoVO stockdailyinfoVO,
			StockQuotesBean sqb, Date date, double winPercentage,
			double lostPercentage, String sCount) {
		double buyPrice = this.buyPrice;
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
