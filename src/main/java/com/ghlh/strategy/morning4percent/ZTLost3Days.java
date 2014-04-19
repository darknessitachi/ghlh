package com.ghlh.strategy.morning4percent;

import java.util.Date;
import java.util.List;

import com.ghlh.analysis.QiangZTBean;
import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.MathUtil;

public class ZTLost3Days {

	public static void main(String[] args) {
		Date date = DateUtil.getDate(2014, 2, 4);
		Date now = new Date();
		double closeZT = 8;
		double winPercentage = 0.1;
		double lostPercentage = 0.1;
		while (date.before(now)) {
			Date nextDate = DateUtil.getNextMarketOpenDay(date);
			Date lastDate = DateUtil.getPreviousMarketOpenDay(date, 1);
			Date zdDate = DateUtil.getPreviousMarketOpenDay(date, 2);
			String sql = "SELECT a.stockid, b.zdf avgzdf FROM "
					+ " (SELECT * FROM stockdailyinfo WHERE zdf > "
					+ closeZT
					+ " AND DATE LIKE '"
					+ DateUtil.formatDay(zdDate)
					+ "%' AND currentPrice > todayopenPrice) a, "
					+ " (SELECT stockid, SUM(zdf) zdf FROM stockdailyinfo WHERE DATE > '"
					+ DateUtil.formatDay(lastDate) + "' AND DATE < '"
					+ DateUtil.formatDay(nextDate) + "'  GROUP BY stockid) b "
					+ " WHERE a.stockid = b.stockid ORDER BY b.zdf ";
			//System.out.println("SQL = " + sql);
			List<QiangZTBean> list = GhlhDAO.list(sql,
					"com.ghlh.analysis.QiangZTBean");
			if (list == null) {
				date = DateUtil.getNextMarketOpenDay(date);
				continue;
			}
			if (list.size() > 0) {
				for (int i = 0; i < 1; i++) {
					QiangZTBean zhuZTBean = list.get(i);
					String stockId = zhuZTBean.getStockid();

					StockQuotesBean sqb = InternetStockQuotesInquirer
							.getInstance().getStockQuotesBean(stockId);
					if (sqb.getName().indexOf("ST") >= 0) {
						continue;
					}
					String sql1 = "SELECT * FROM stockdailyinfo WHERE DATE LIKE '"
							+ DateUtil.formatDay(nextDate)
							+ "%' AND stockId = '" + stockId + "'";
					List dailyInfoList = GhlhDAO.list(sql1,
							"com.ghlh.data.db.StockdailyinfoVO", 0, 1);
					if (dailyInfoList.size() == 0) {
						break;
					}
					StockdailyinfoVO stockdailyinfoVO = (StockdailyinfoVO) dailyInfoList
							.get(0);
					System.out.println(nextDate + " " + stockId
							+ "is picked up, 买入价"
							+ stockdailyinfoVO.getTodayopenprice());
					pickup++;
					processBuyStockResult(stockdailyinfoVO, sqb, nextDate,
							winPercentage, lostPercentage, "0");
				}
				date = DateUtil.getNextMarketOpenDay(date);
			}
		}
		System.out.println("盈利 " + yinLi + "笔" + " 亏损" + kuiSun + "笔"
				+ " Pick up : " + pickup + "笔");
	}

	private static int kuiSun = 0;
	private static int yinLi = 0;
	private static int pickup = 0;

	public static void processBuyStockResult(StockdailyinfoVO stockdailyinfoVO,
			StockQuotesBean sqb, Date date, double winPercentage,
			double lostPercentage, String sCount) {
		double buyPrice = stockdailyinfoVO.getTodayopenprice();
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
