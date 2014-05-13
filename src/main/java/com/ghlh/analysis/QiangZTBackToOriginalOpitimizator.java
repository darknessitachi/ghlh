package com.ghlh.analysis;

/**
 * 抓涨停， 回调几天后回到涨停启动点
 */
import java.util.Date;
import java.util.List;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.MathUtil;

public class QiangZTBackToOriginalOpitimizator {

	public QiangZTResultBean calculateResult() {
		pickup = 0;
		yinLi = 0;
		kuiSun = 0;

		Date date = DateUtil.getDate(2014, 3, 10);
		int zdts = 20;
		double minZdf = -5;
		double maxZdf = 5;
		double maxAvg = 0.3;
		double minAvg = -0.3;
		double closeZT = 9;
		double winPercentage = 0.03;
		double lostPercentage = 0.2;
		
		double ztGap = 0.0;

		Date now = new Date();
		while (date.before(now)) {
			String sDate = DateUtil.formatDay(date);
			String zdStartDate = DateUtil.formatDay(DateUtil
					.getPreviousMarketOpenDay(date, zdts));
			String sql = "SELECT a.*, b.currentprice FROM "
					+ " (SELECT stockId,COUNT(zdf) tradedays, MAX(zdf) maxzdf, MIN(zdf) minzdf, AVG(zdf) avgzdf FROM stockdailyinfo WHERE DATE > '"
					+ zdStartDate + "' AND DATE < '" + sDate
					+ "' GROUP BY stockId) a,"
					+ " (SELECT * FROM stockdailyinfo WHERE zdf > " + closeZT
					+ " AND DATE LIKE '" + sDate
					+ "%' AND highestprice <> lowestprice) b"
					+ " WHERE a.stockid = b.stockid AND maxzdf < " + maxZdf
					+ " AND minzdf > " + minZdf + " AND avgzdf > " + minAvg
					+ " and avgzdf < " + maxAvg;

			// System.out.println("sql = " + sql);
			List<QiangZTBean> list = GhlhDAO.list(sql,
					"com.ghlh.analysis.QiangZTBean");

			if (list == null) {
				date = DateUtil.getNextMarketOpenDay(date);
				continue;
			}

			for (int i = 0; i < list.size(); i++) {
				QiangZTBean zhuZTBean = list.get(i);
				String stockId = zhuZTBean.getStockid();

				StockQuotesBean sqb = InternetStockQuotesInquirer.getInstance()
						.getStockQuotesBean(stockId);
				if (sqb.getName().indexOf("ST") >= 0) {
					continue;
				}
				String sql1 = "SELECT * FROM stockdailyinfo WHERE DATE > '"
						+ sDate + "' AND stockId = '" + stockId
						+ "' ORDER BY DATE";
				List dailyInfoList = GhlhDAO.list(sql1,
						"com.ghlh.data.db.StockdailyinfoVO");
				if (dailyInfoList != null && dailyInfoList.size() > 2) {
					StockdailyinfoVO sdiVO = (StockdailyinfoVO) dailyInfoList
							.get(0);
					double originalZTJG = MathUtil
							.formatDoubleWith2QuanJin(sdiVO
									.getYesterdaycloseprice() * (1 + ztGap));
					boolean isPickedup = false;
					int buyIndex = 0;
					for (int j = 1; j < dailyInfoList.size(); j++) {
						StockdailyinfoVO sdiVO1 = (StockdailyinfoVO) dailyInfoList
								.get(j);
						if (isPickedup) {
							double winPrice = originalZTJG
									* (1 + winPercentage);
							double losePrice = originalZTJG
									* (1 - lostPercentage);
							if (sdiVO1.getHighestprice() >= winPrice
									&& sdiVO1.getLowestprice() <= losePrice) {
								System.out.println("同时在一天达到盈利 和 亏损 价格");
								break;
							} else if (sdiVO1.getHighestprice() >= winPrice) {
								System.out.println(sdiVO1.getDate() + " "
										+ sqb.getStockId() + " "
										+ sqb.getName() + "在第 "
										+ (j - buyIndex) + " 天盈利成交, 买入价:"
										+ originalZTJG + " 卖出价 :" + winPrice
										+ " 卖出当日涨幅:" + sdiVO1.getZdf());
								yinLi++;
								break;

							} else if (sdiVO1.getLowestprice() <= losePrice) {
								System.out.println(sdiVO1.getDate() + " "
										+ sqb.getStockId() + " "
										+ sqb.getName() + "在第 "
										+ (j - buyIndex) + " 天亏损成交, 买入价:"
										+ originalZTJG + " 卖出价 :" + losePrice
										+ " 卖出当日涨幅:" + sdiVO1.getZdf());
								kuiSun++;
								break;
							}
							if (j == dailyInfoList.size() - 1) {
								double currentPrice = sdiVO1.getCurrentprice();
								double currentPercentage = MathUtil
										.formatDoubleWith2((currentPrice - originalZTJG)
												/ originalZTJG * 100);
								System.out.println(sdiVO1.getDate() + " "
										+ sqb.getStockId() + " "
										+ sqb.getName() + "在第 "
										+ (j - buyIndex) + " 天, 买入价:"
										+ originalZTJG + " 当前价 :"
										+ currentPrice + " 卖出当日涨幅:"
										+ sdiVO1.getZdf() + " 当前----------------------------------盈利亏损幅度 :"
										+ currentPercentage);
							}
						} else {
							if (sdiVO1.getLowestprice() <= originalZTJG) {
								System.out.println(sdiVO1.getDate() + " "
										+ sdiVO1.getStockid()
										+ " is picked up, 买入价:" + originalZTJG + "**********" + sdiVO1.getDate());
								isPickedup = true;
								buyIndex = j;
								pickup++;
								continue;
							}
						}

					}
				}
			}
			date = DateUtil.getNextMarketOpenDay(date);
		}
		QiangZTResultBean result = new QiangZTResultBean();
		result.setKuiSun(kuiSun);
		result.setYinLi(yinLi);
		result.setPickUp(pickup);
		return result;
	}

	private int kuiSun = 0;
	private int yinLi = 0;
	private int pickup = 0;

	public static void main(String[] args) {
		QiangZTResultBean result = new QiangZTBackToOriginalOpitimizator()
				.calculateResult();
		System.out.println("Result = " + result);
	}
}
