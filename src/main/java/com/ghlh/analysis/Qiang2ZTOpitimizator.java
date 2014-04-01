package com.ghlh.analysis;

import java.util.Date;
import java.util.List;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.MathUtil;

public class Qiang2ZTOpitimizator {

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
		Date next = DateUtil.getNextMarketOpenDay(date);

		while (next.before(now)) {
			String sDate = DateUtil.formatDay(date);
			String zdStartDate = DateUtil.formatDay(DateUtil
					.getPreviousMarketOpenDay(date, zdts));
			String sql = "SELECT a.* FROM "
					+ " (SELECT stockId,COUNT(zdf) tradedays, MAX(zdf) maxzdf, MIN(zdf) minzdf, AVG(zdf) avgzdf FROM stockdailyinfo WHERE DATE > '"
					+ zdStartDate
					+ "' AND DATE < '"
					+ sDate
					+ "' GROUP BY stockId) a,"
					+ " (SELECT * FROM ("
					+ "	SELECT stockid, SUM(zdf) zdf FROM stockdailyinfo WHERE  (DATE LIKE '"
					+ sDate + "%' " + " OR DATE LIKE '"
					+ DateUtil.formatDay(next)
					+ "%') GROUP BY stockid) aa WHERE aa.zdf >= " + closeZT
					+ " ORDER BY aa.zdf DESC) b "
					+ " WHERE a.stockid = b.stockid AND maxzdf < " + maxZdf
					+ " AND minzdf > " + minZdf + " AND avgzdf > " + minAvg
					+ " and avgzdf < " + maxAvg;

			//System.out.println("sql = " + sql);
			List<QiangZTBean> list = GhlhDAO.list(sql,
					"com.ghlh.analysis.QiangZTBean");

			if (list == null) {
				date = DateUtil.getNextMarketOpenDay(date);
				next = DateUtil.getNextMarketOpenDay(next);
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
				Date inDate = DateUtil.getNextMarketOpenDay(next);
				String sql1 = "SELECT * FROM stockdailyinfo WHERE DATE > '"
						+ DateUtil.formatDay(inDate) + "' AND stockId = '"
						+ stockId + "' ORDER BY DATE";
				List dailyInfoList = GhlhDAO.list(sql1,
						"com.ghlh.data.db.StockdailyinfoVO", 0, 1);
				StockdailyinfoVO stockdailyinfoVO = null;
				if (dailyInfoList != null && dailyInfoList.size() != 0) {
					stockdailyinfoVO = (StockdailyinfoVO) dailyInfoList.get(0);
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
			next = DateUtil.getNextMarketOpenDay(next);
		}
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
	}

	private int kuiSun = 0;
	private int yinLi = 0;
	private int pickup = 0;

	public static void main(String[] args) {
		FactorsBean bean = new FactorsBean();
		bean.setDate(DateUtil.getDate(2014, 2, 7));
		bean.setZdts(20);
		bean.setMinZdf(-5);
		bean.setMaxZdf(5);
		bean.setMaxAvg(0.3);
		bean.setMinAvg(-0.3);
		bean.setCloseZT(16);
		bean.setWinPercentage(0.08);
		bean.setLostPercentage(0.1);
		bean.setLowOpen(false);
		bean.setBasedonYesterdayClosePercentage(0);
		bean.setBasedonTodayOpenPercentage(0);
		QiangZTResultBean result = (new Qiang2ZTOpitimizator()).calculateResult(bean);
		System.out.println("result = " + result);
		
	}
}
