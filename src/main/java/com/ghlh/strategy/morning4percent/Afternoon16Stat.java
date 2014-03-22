package com.ghlh.strategy.morning4percent;

import java.util.Date;
import java.util.List;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.MathUtil;

public class Afternoon16Stat {

	public static void main(String[] args) {
		Date date = DateUtil.getDate(2014, 1, 7);
		double previousMaxPercentage = 5;
		double continuos2DaysPercentage = 16;
		double lostPercentage = 0.16;
		double winPercentage = 0.08;
		Date now = new Date();
		Date nextDate = DateUtil.getNextMarketOpenDay(date);

		while (date.before(now)) {
			String sDate = DateUtil.formatDay(date);
			System.out.println("sDate = " + sDate);
			String sNext2Date = DateUtil.formatDay(DateUtil
					.getNext2MarketOpenDay(date));
			String sPrevious3Date = DateUtil.formatDay(DateUtil
					.getPrevious3MarketOpenDay(date));
			String sql = "SELECT * FROM "
					+ "(SELECT stockid, SUM(zdf) zdf FROM stockdailyinfo WHERE  DATE > '"
					+ sDate
					+ "'"
					+ " AND DATE < '"
					+ sNext2Date
					+ "' GROUP BY stockid) a,"
					+ "(SELECT stockid, MAX(zdf) zdf FROM stockdailyinfo WHERE  DATE > '"
					+ sPrevious3Date + "'" + " AND DATE < '" + sDate
					+ "' GROUP BY stockid) b"
					+ " WHERE a.stockid = b.stockid AND a.zdf >= "
					+ continuos2DaysPercentage + " AND b.zdf <= "
					+ previousMaxPercentage + " ORDER BY a.zdf DESC";

			//System.out.println("SQL = " + sql);
			List list = GhlhDAO.list(sql);
			if(list == null){
				date = DateUtil.getNextMarketOpenDay(date);
				nextDate = DateUtil.getNextMarketOpenDay(date);
				continue;
			}

			for (int i = 0; i < 1; i++) {
				String stockId = (String) list.get(i);
				System.out.println("stockId = " + stockId);
				StockQuotesBean sqb = InternetStockQuotesInquirer.getInstance()
						.getStockQuotesBean(stockId);
				if (sqb.getName().indexOf("ST") >= 0) {
					continue;
				}
				String sql1 = "SELECT * FROM stockdailyinfo14 WHERE DATE LIKE '"
						+ DateUtil.formatDay(nextDate)
						+ "%' AND stockId = '"
						+ stockId + "'";
				List dailyInfoList = GhlhDAO.list(sql1,
						"com.ghlh.data.db.StockdailyinfoVO", 0, 1);
				StockdailyinfoVO stockdailyinfoVO = (StockdailyinfoVO) dailyInfoList
						.get(0);
				processBuyStockResult(stockdailyinfoVO, sqb, nextDate,
						winPercentage, lostPercentage, "0");
			}
			date = DateUtil.getNextMarketOpenDay(date);
			nextDate = DateUtil.getNextMarketOpenDay(date);
		}

	}

	public static void processBuyStockResult(StockdailyinfoVO stockdailyinfoVO,StockQuotesBean sqb, Date date,
			double winPercentage, double lostPercentage, String sCount) {
		double buyPrice = stockdailyinfoVO.getCurrentprice();
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
				break;
			}
			if (lowPrice <= lostPrice) {
				System.out.println(date + " " + sqb.getStockId() + " "
						+ sqb.getName() + "在第 " + (j + 1) + " 天亏损成交, 买入价:"
						+ buyPrice + " 卖出价 :" + lostPrice + " 卖出当日涨幅:"
						+ stockdailyinfoVO1.getZdf() + " 买入当日数量:" + sCount);
				break;
			}
		}
	}

}
