package com.ghlh.analysis;

/**
 * 抓涨停， 几天小阳调整
 */
import java.util.Date;
import java.util.List;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.MathUtil;

public class QiangZTWithCoupleSmallYang {

	public QiangZTResultBean calculateResult() {
		pickup = 0;
		yinLi = 0;
		kuiSun = 0;

		double winPercentage = 0.06;
		double lostPercentage = 0.2;
		Date date = DateUtil.getDate(2014, 3, 8);
		int ZTBeforeToday = 3;
		double xyLow = -2;
		double xyHigh = 2;
		
		Date now = new Date();
		while (date.before(now)) {
			Date ztDate = DateUtil
					.getPreviousMarketOpenDay(date, ZTBeforeToday);
			String ztDay = DateUtil.formatDay(ztDate);

			String sql = "SELECT * FROM stockdailyinfo WHERE zdf > 9.9 AND DATE LIKE '"
					+ ztDay + "%'";

			// System.out.println("sql = " + sql);
			List<StockdailyinfoVO> list = GhlhDAO.list(sql,
					"com.ghlh.data.db.StockdailyinfoVO");

			if (list == null) {
				date = DateUtil.getNextMarketOpenDay(date);
				continue;
			}
			Date ztNextDate = DateUtil.getNextMarketOpenDay(ztDate);
			String ztNextDay = DateUtil.formatDay(ztNextDate);

			for (int i = 0; i < list.size(); i++) {
				StockdailyinfoVO ztDailyinfoVO = list.get(i);
				String stockId = ztDailyinfoVO.getStockid();

				StockQuotesBean sqb = InternetStockQuotesInquirer.getInstance()
						.getStockQuotesBean(stockId);
				if (sqb.getName().indexOf("ST") >= 0) {
					continue;
				}
				String sql1 = "SELECT * FROM stockdailyinfo WHERE DATE > '"
						+ ztNextDay + "' AND stockId = '" + stockId
						+ "' ORDER BY DATE";
				List dailyInfoList = GhlhDAO.list(sql1,
						"com.ghlh.data.db.StockdailyinfoVO");

				boolean meetPickup = true;
				if (dailyInfoList.size() < ZTBeforeToday) {
					continue;
				}
				for (int j = 0; j < ZTBeforeToday; j++) {
					StockdailyinfoVO sdiVO1 = (StockdailyinfoVO) dailyInfoList
							.get(j);
					if (sdiVO1.getZdf() <= xyLow || sdiVO1.getZdf() >= xyHigh) {
						meetPickup = false;
						break;
					}
				}
				if (meetPickup) {
					System.out.println(date + " pickup Stock : " + stockId);
					pickup++;
					if (dailyInfoList.size() > ZTBeforeToday) {
						double buyPrice = ((StockdailyinfoVO) dailyInfoList
								.get(ZTBeforeToday)).getTodayopenprice();
						int result = AnalysisUtil.processBuyStockResult(sqb,
								date, buyPrice, winPercentage, lostPercentage,
								true);
						if (result == Constants.WIN) {
							yinLi++;
						}
						if (result == Constants.LOSE) {
							kuiSun++;
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
		QiangZTResultBean result = new QiangZTWithCoupleSmallYang()
				.calculateResult();
		System.out.println("Result = " + result);
	}
}
