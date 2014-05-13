package com.ghlh.strategy.catchyzstair;

/**
 * 抓涨停， 几天小阳调整
 */
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ghlh.analysis.QiangZTResultBean;
import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.KLineUtil;
import com.ghlh.util.MathUtil;

public class CatchYZOpenBuyAnalyst {

	static double yinliPercentage = 0;
	static double kuiSunPercentage = 0;
	static double minPercentageSum = 0;
	static double maxPercentageSum = 0;

	public QiangZTResultBean calculateResult() {
		pickup = 0;
		yinLi = 0;
		kuiSun = 0;

		double winPercentage = 0.04;
		double lostPercentage = 0.3;
		double openPercentage = 0.0;
		Date date = DateUtil.getDate(2014, 2, 1);
		Date now = DateUtil.getDate(2014, 3, 1);
		Map alreadyAnaystedMap = new HashMap();

		while (date.before(now)) {
			String sDate = DateUtil.formatDay(date);
			// System.out.println("process date = " + sDate);
			String sql = "SELECT * FROM stockdailyinfo WHERE todayopenprice = highestprice AND highestprice = lowestprice AND todayopenprice > 0 and zdf > 0 AND DATE LIKE '"
					+ sDate + "%' ";

			List<StockdailyinfoVO> list = GhlhDAO.list(sql,
					"com.ghlh.data.db.StockdailyinfoVO");

			if (list == null) {
				date = DateUtil.getNextMarketOpenDay(date);
				continue;
			}

			for (int i = 0; i < list.size(); i++) {
				StockdailyinfoVO ztDailyinfoVO = list.get(i);
				String stockId = ztDailyinfoVO.getStockid();
				if (alreadyAnaystedMap.get(stockId) != null) {
					continue;
				}
				StockQuotesBean sqb = InternetStockQuotesInquirer.getEastMoneyInstance()
						.getStockQuotesBean(stockId);
				if (sqb.getName().indexOf("ST") >= 0) {
					continue;
				}
				Date nextDate = DateUtil.getNextMarketOpenDay(date);
				String sNextDay = DateUtil.formatDay(nextDate);
				String sql1 = "SELECT * FROM stockdailyinfo WHERE DATE > '"
						+ sNextDay + "' AND stockId = '" + stockId
						+ "' ORDER BY DATE";
				List dailyInfoList = GhlhDAO.list(sql1,
						"com.ghlh.data.db.StockdailyinfoVO");
				double buyPrice = 0;
				double inPos = 0;
				Date inDate = null;

				for (int j = 0; j < dailyInfoList.size(); j++) {
					StockdailyinfoVO sdiVO1 = (StockdailyinfoVO) dailyInfoList
							.get(j);
					if (buyPrice != 0) {
						if (sdiVO1.getCurrentprice() > 0) {
							double highPrice = sdiVO1.getHighestprice();
							double highPercentage = MathUtil
									.formatDoubleWith2((highPrice - sdiVO1
											.getYesterdaycloseprice())
											/ sdiVO1.getYesterdaycloseprice()
											* 100);
							maxPercentageSum += highPercentage;

							double percentage = (highPrice - buyPrice)
									/ buyPrice;
							percentage = MathUtil
									.formatDoubleWith2(percentage * 100);

							if (percentage > 0) {
								System.out.println(sqb.getStockId() + " "
										+ sqb.getName() + "在"
										+ DateUtil.formatDay(inDate)
										+ "最低价买入, 在第 " + (j - inPos)
										+ " 天后盈利**************成交, 买入价:"
										+ buyPrice + " 卖出最高价 :" + highPrice
										+ " 盈利幅度:" + percentage);
								KLineUtil.saveUrlAs(sqb.getStockId(),
										"catchYZ", sqb.getStockId() + "_"
												+ "盈利_" + percentage);

								yinliPercentage += percentage;
								yinLi++;
								break;
							} else {
								System.out
										.println(sqb.getStockId()
												+ " "
												+ sqb.getName()
												+ "在"
												+ DateUtil.formatDay(inDate)
												+ "最低价买入, 在第 "
												+ (j - inPos)
												+ " 天后亏损-----------------------------成交, 买入价:"
												+ buyPrice + " 卖出最高价 :"
												+ highPrice + " 亏损幅度:"
												+ percentage);
								kuiSun++;
								kuiSunPercentage += percentage;
								KLineUtil.saveUrlAs(sqb.getStockId(),
										"catchYZ", sqb.getStockId() + "_"
												+ "亏损_" + percentage);

								break;
							}
						}
					}

					if (sdiVO1.getTodayopenprice() > 0
							&& sdiVO1.getTodayopenprice() == sdiVO1
									.getHighestprice()
							&& sdiVO1.getHighestprice() == sdiVO1
									.getLowestprice()) {
						continue;
					} else {
						if (sdiVO1.getTodayopenprice() > 0) {
							if (sdiVO1.getHighestprice() != sdiVO1
									.getLowestprice()) {
								// double openPercentage1 = MathUtil
								// .formatDoubleWith2((sdiVO1
								// .getTodayopenprice() - sdiVO1
								// .getYesterdaycloseprice())
								// / sdiVO1.getYesterdaycloseprice()
								// * 100);
								// if (openPercentage1 > 7) {
								// break;
								// }

								double openPercentage1 = MathUtil
										.formatDoubleWith2((sdiVO1
												.getCurrentprice() - sdiVO1
												.getTodayopenprice())
												/ sdiVO1.getYesterdaycloseprice()
												* 100);
								if(openPercentage1 >= -2){
									break;
								}
								
								double inPrice = sdiVO1.getLowestprice();
								double inPercentage = MathUtil
										.formatDoubleWith2((inPrice - sdiVO1
												.getYesterdaycloseprice())
												/ sdiVO1.getYesterdaycloseprice()
												* 100);
								minPercentageSum += inPercentage;

								inPrice = MathUtil
										.formatDoubleWith2QuanJin(inPrice);
								buyPrice = inPrice;
								inPos = j;
								inDate = sdiVO1.getDate();
								System.out.println(stockId + " 在  " + (j + 1)
										+ " 个一字后, 在"
										+ DateUtil.formatDay(sdiVO1.getDate())
										+ "涨停打开，当日最低价:" + inPrice);
								pickup++;
								alreadyAnaystedMap.put(stockId, "already");
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
		QiangZTResultBean result = new CatchYZOpenBuyAnalyst()
				.calculateResult();
		System.out.println("Result = " + result + " 盈利总百分比  : "
				+ MathUtil.formatDoubleWith2(yinliPercentage) + " 亏损总百分比 : "
				+ MathUtil.formatDoubleWith2(kuiSunPercentage));

		double minAvgPercentage = MathUtil.formatDoubleWith2(minPercentageSum
				/ result.getPickUp());
		double highAvgPercentage = MathUtil.formatDoubleWith2(maxPercentageSum
				/ result.getPickUp());
		System.out.println(" 买入平均百分比  : " + minAvgPercentage + " 卖出平均百分比 : "
				+ highAvgPercentage);

	}
}
