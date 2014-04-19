package com.ghlh.analysis;

import java.util.Date;
import java.util.List;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.KLineUtil;
import com.ghlh.util.MathUtil;

public class AnalysisUtil {
	public static int processBuyStockResult(StockQuotesBean sqb, Date inDate,
			double buyPrice, double winPercentage, double lostPercentage,
			boolean log) {
		int result = Constants.PENDING;
		double winPrice = MathUtil.formatDoubleWith2QuanShe(buyPrice
				* (1 + winPercentage));
		double lostPrice = MathUtil.formatDoubleWith2QuanJin(buyPrice
				* (1 - lostPercentage));

		Date next = DateUtil.getNextDay(inDate);
		String sNext = DateUtil.formatDay(next);
		String sql1 = "SELECT * FROM stockdailyinfo WHERE stockid = '"
				+ sqb.getStockId() + "' AND DATE > '" + sNext
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
				if (log) {
					System.out.println("同时在一天达到盈利 和 亏损 价格");
				}
				return result;
			} else if (highPrice >= winPrice) {
				if (log) {
					System.out.println(inDate + " " + sqb.getStockId() + " "
							+ sqb.getName() + "在第 " + (j + 1)
							+ " 天盈利*******************成交, 买入价:" + buyPrice
							+ " 卖出价 :" + winPrice + " 卖出当日涨幅:"
							+ stockdailyinfoVO1.getZdf());
					KLineUtil.saveUrlAs(
							sqb.getStockId(),
							"qzt",
							DateUtil.formatDay(inDate) + "_" + (j + 1) + "天"
									+ "成功" + "_" + sqb.getStockId() + "_"
									+ sqb.getName());

				}
				result = Constants.WIN;
				return result;
			} else if (lowPrice <= lostPrice) {
				if (log) {
					System.out.println(inDate + " " + sqb.getStockId() + " "
							+ sqb.getName() + "在第 " + (j + 1)
							+ " 天亏损_________________________成交, 买入价:"
							+ buyPrice + " 卖出价 :" + lostPrice + " 卖出当日涨幅:"
							+ stockdailyinfoVO1.getZdf());
					KLineUtil.saveUrlAs(
							sqb.getStockId(),
							"qzt",
							DateUtil.formatDay(inDate) + "_" + (j + 1) + "天"
									+ "失败" + "_" + sqb.getStockId() + "_"
									+ sqb.getName());
				}
				result = Constants.LOSE;
				return result;
			}
			if (j == dailyInfoList.size() - 1) {
				double currentPrice = stockdailyinfoVO1.getCurrentprice();
				double yikui = MathUtil
						.formatDoubleWith2((currentPrice - buyPrice) / buyPrice
								* 100);
				System.out.println(inDate + " " + sqb.getStockId() + " "
						+ sqb.getName() + "在第 " + (j + 1) + " 天未达目标, 买入价:"
						+ buyPrice + " 当前-------------------------------盈亏: "
						+ yikui);

				KLineUtil.saveUrlAs(sqb.getStockId(), "qzt",
						DateUtil.formatDay(inDate) + "_" + (j + 1) + "天"
								+ "当前盈亏_" + yikui + "_" + sqb.getStockId()
								+ "_" + sqb.getName());

			}
		}
		return result;
	}
}
