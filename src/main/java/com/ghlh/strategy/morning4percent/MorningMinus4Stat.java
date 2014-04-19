package com.ghlh.strategy.morning4percent;

import java.util.Date;
import java.util.List;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.MathUtil;

public class MorningMinus4Stat {

	public static void main(String[] args) {
		Date date = DateUtil.getDate(2014, 3, 1);
		double lowPercentage = -5.5;
		double highPercentage = -3.5;
		double lostPercentage = 0.15;
		double winPercentage = 0.06;
		int stockCount = 1;

		Date now = new Date();

		while (date.before(now)) {
			String sDate = DateUtil.formatDay(date);
			//String[] marketPrex = {   "002" };
			String[] marketPrex = { "" };
			for (int j = 0; j < marketPrex.length; j++) {
				String sql = "SELECT * FROM stockdailyinfo10 WHERE DATE LIKE '"
						+ sDate + "%' AND zdf >= " + lowPercentage
						+ " AND zdf <= " + highPercentage
						+ " AND stockid LIKE '" + marketPrex[j]
						+ "%' ORDER BY hsl";
				// System.out.println("sql = " + sql);

				List list = GhlhDAO.list(sql,
						"com.ghlh.data.db.StockdailyinfoVO", 0, stockCount + 2);

				int no = 0;

				for (int i = 0; i < list.size(); i++) {
					StockdailyinfoVO stockdailyinfoVO = (StockdailyinfoVO) list
							.get(i);
					StockQuotesBean sqb = InternetStockQuotesInquirer
							.getInstance().getStockQuotesBean(
									stockdailyinfoVO.getStockid());
					if (sqb.getName().indexOf("ST") >= 0) {
						continue;
					}
					no++;
					processBuyStockResult(stockdailyinfoVO, sqb, date,
							winPercentage, lostPercentage, "0");
					if (no >= stockCount) {
						break;
					}
				}
			}
			date = DateUtil.getNextDay(date);

		}
		System.out.println("winTimes = " + winTimes + " lostTimes = "
				+ lostTimes + " pickupTimes = " + pickupTimes);

	}

	private static int lostTimes = 0;
	private static int winTimes = 0;
	private static int pickupTimes = 0;

	public static void processBuyStockResult(StockdailyinfoVO stockdailyinfoVO,
			StockQuotesBean sqb, Date date, double winPercentage,
			double lostPercentage, String sCount) {
		pickupTimes++;
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
		if(dailyInfoList.size() == 0){
			System.out.println(date + sqb.getStockId() + "尚未有后续交易");
		}
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
				winTimes++;
				break;
			}
			if (lowPrice <= lostPrice) {
				System.out.println(date + " " + sqb.getStockId() + " "
						+ sqb.getName() + "在第 " + (j + 1) + " 天亏损成交, 买入价:"
						+ buyPrice + " 卖出价 :" + lostPrice + " 卖出当日涨幅:"
						+ stockdailyinfoVO1.getZdf() + " 买入当日数量:" + sCount);
				lostTimes++;
				break;
			}
			if (j == dailyInfoList.size() - 1) {
				double currentPrice = stockdailyinfoVO1.getCurrentprice();
				double yikui = MathUtil
						.formatDoubleWith2((currentPrice - buyPrice) / buyPrice
								* 100);
				System.out.println(date + sqb.getStockId() + " "
						+ sqb.getName() + "在第 " + (j + 1) + " 天未达目标, 买入价:"
						+ buyPrice + " 当前-------------------------------盈亏: "
						+ yikui);

			}

		}
	}

}
