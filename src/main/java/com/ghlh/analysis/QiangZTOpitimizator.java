package com.ghlh.analysis;

/**
 * 抓涨停， 第二天介入， 根据qiangztfactors.properties里的参数来设置
 */
import java.util.Date;
import java.util.List;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.MathUtil;

public class QiangZTOpitimizator {

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

				Date inDate = DateUtil.getNextMarketOpenDay(date);

				String sql_3days = "SELECT * FROM stockdailyinfo WHERE stockid = '"
						+ stockId
						+ "' AND DATE < '"
						+ DateUtil.formatDay(inDate) + "  ' and todayopenprice != 0 ORDER BY DATE DESC";
				List dailyInfo_3days = GhlhDAO.list(sql_3days,
						"com.ghlh.data.db.StockdailyinfoVO", 0, 4);
				// if (!meetZF(dailyInfo_3days)) {
				// continue;
				// }
				if (meetFJL(dailyInfo_3days)) {
					System.out.println("放巨量 : " + stockId);
					continue;
				}

				String sql1 = "SELECT * FROM stockdailyinfo WHERE DATE > '"
						+ DateUtil.formatDay(inDate) + "' AND stockId = '"
						+ stockId + "'  and todayopenprice != 0 ORDER BY DATE";
				List dailyInfoList = GhlhDAO.list(sql1,
						"com.ghlh.data.db.StockdailyinfoVO", 0, 1);
				StockdailyinfoVO stockdailyinfoVO = null;
				if (dailyInfoList != null && dailyInfoList.size() != 0) {
					stockdailyinfoVO = (StockdailyinfoVO) dailyInfoList.get(0);
					if(stockdailyinfoVO.getTodayopenprice() == stockdailyinfoVO.getHighestprice() &&
							stockdailyinfoVO.getHighestprice() == stockdailyinfoVO.getLowestprice()){
						System.out.println("一字板 : " + stockId);
						continue;
					}
					
					inDate = stockdailyinfoVO.getDate();
					inDate = DateUtil.parseDay(DateUtil.formatDay(inDate));
					if (buyCondition(stockdailyinfoVO, bean)) {
						pickup++;
						System.out.println(inDate + " " + stockId
								+ "is picked up, 买入价" + this.buyPrice
								+ " TradeDays:" + zhuZTBean.getTradedays());
						int result = AnalysisUtil.processBuyStockResult(sqb,
								inDate, buyPrice, winPercentage,
								lostPercentage, true);
						if (result == Constants.WIN) {
							yinLi++;
						}
						if (result == Constants.LOSE) {
							kuiSun++;
						}
					}
				} else {
					pickup++;
					System.out.println(inDate + " " + stockId
							+ "is picked up, 买入价" + " TBD " + " TradeDays:"
							+ zhuZTBean.getTradedays());
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

	private double buyPrice;

	public boolean meetZF(List dailyInfo_3days) {
		boolean result = true;
		for (int k = 1; k < dailyInfo_3days.size(); k++) {
			StockdailyinfoVO sdiVO3days = (StockdailyinfoVO) dailyInfo_3days
					.get(k);
			double zfE = sdiVO3days.getHighestprice()
					- sdiVO3days.getLowestprice();
			double zF = zfE / sdiVO3days.getYesterdaycloseprice() * 100;
			if (zF > 3) {
				result = false;
				break;
			}
		}
		return result;
	}

	public boolean meetFJL(List dailyInfo_3days) {
		if(dailyInfo_3days.size() < 2){
			return false;
		}
		StockdailyinfoVO sdiVO3days = (StockdailyinfoVO) dailyInfo_3days.get(0);
		StockdailyinfoVO sdiVO3daysPrevious = (StockdailyinfoVO) dailyInfo_3days
				.get(1);
		if (sdiVO3days.getHsl() / sdiVO3daysPrevious.getHsl() > 8.5) {
			return true;
		}
		return false;
	}

	public boolean buyCondition(StockdailyinfoVO stockdailyinfoVO,
			FactorsBean bean) {
		boolean result = true;
		buyPrice = stockdailyinfoVO.getTodayopenprice();
		if (bean.isLowOpen()) {
			result = stockdailyinfoVO.getTodayopenprice() < stockdailyinfoVO
					.getYesterdaycloseprice() * 1.07;
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
	}

	private int kuiSun = 0;
	private int yinLi = 0;
	private int pickup = 0;
}
