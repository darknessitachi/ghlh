package com.ghlh.strategy.morning4percent;

import java.util.Date;
import java.util.List;

import com.ghlh.analysis.QiangZTBean;
import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.KLineUtil;
import com.ghlh.util.MathUtil;

public class QiangZTMA5In {

	public static void main(String[] args) {
		Date date = DateUtil.getDate(2014, 3, 4);
		double minZdf = -5;
		double maxZdf = 5;
		double maxAvg = 0.3;
		double minAvg = -0.3;
		int zdts = 20;
		double closeZT = 9;
		 Date now = new Date();
		//Date now = DateUtil.getDate(2014, 2, 31);

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

			// System.out.println("SQL = " + sql);
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
				MA5ResultBean result = getMA4(stockId, date);
				if (result != null) {
					System.out
							.println(DateUtil.formatDay(result.getReachDate())
									+ " " + stockId + "back to MA5, 买入价"
									+ result.getMa5());
					pickup++;
					Date nextDate = DateUtil.getNextMarketOpenDay(result
							.getReachDate());
					String sql2 = "SELECT * FROM stockdailyinfo WHERE DATE > '"
							+ DateUtil.formatDay(nextDate)
							+ "' AND stockId = '" + stockId
							+ "' and todayopenprice != 0 order by date ";
					List dailyInfoList2 = GhlhDAO.list(sql2,
							"com.ghlh.data.db.StockdailyinfoVO", 0, 1);
					if (dailyInfoList2.size() < 1) {
						System.out
								.println(stockId
										+ " has no more future Data to decide Win or Lose");
					} else {
						StockdailyinfoVO sdiVO = (StockdailyinfoVO) dailyInfoList2
								.get(0);
						KLineUtil.saveUrlAs(stockId, "ma5buy\\April", pickup
								+ "_" + stockId);

						double maxWinPercentage = ((sdiVO.getHighestprice() - result
								.getMa5()) / result.getMa5()) * 100;
						maxWinPercentage = MathUtil
								.formatDoubleWith2(maxWinPercentage);
						if (maxWinPercentage > 0) {
							yinLi++;
							yinLiPercentage += maxWinPercentage;

						} else {
							kuiSun++;
							kuiSunPercentage += maxWinPercentage;
						}
						System.out.println(stockId
								+ " ------------------------------第二天最大的盈利百分比:"
								+ maxWinPercentage);

					}
				}
			}
			date = DateUtil.getNextMarketOpenDay(date);
		}
		System.out.println("盈利 " + yinLi + "笔, 总百分比:" + yinLiPercentage + " 亏损"
				+ kuiSun + "笔,总百分比:" + kuiSunPercentage + " Pick up : "
				+ pickup + "笔");
	}

	private static int kuiSun = 0;
	private static int yinLi = 0;
	private static int pickup = 0;
	private static double kuiSunPercentage = 0;
	private static double yinLiPercentage = 0;

	public static void main1(String[] args) {
		Date date = DateUtil.getDate(2014, 3, 16);
		String stockId = "600477";
		MA5ResultBean jieruPrice = getMA4(stockId, date);
		System.out.println("jieruPrice = " + jieruPrice);
	}

	public static MA5ResultBean getMA4(String stockId, Date ztd) {
		MA5ResultBean result = null;
		double ma4 = 0;
		Date nextOfZtd = DateUtil.getNextMarketOpenDay(ztd);
		String sql1 = "SELECT * FROM stockdailyinfo WHERE DATE < '"
				+ DateUtil.formatDay(nextOfZtd) + "' AND stockId = '" + stockId
				+ "' and todayopenprice != 0 order by date desc ";

		List dailyInfoList = GhlhDAO.list(sql1,
				"com.ghlh.data.db.StockdailyinfoVO", 0, 10);
		if (dailyInfoList.size() < 4) {
			System.out
					.println("No Enough previous data to calculate MA4 for StockId = "
							+ stockId);

		} else {
			double sum = 0;
			for (int i = 0; i < 4; i++) {
				StockdailyinfoVO sdiVO = (StockdailyinfoVO) dailyInfoList
						.get(i);
				sum += sdiVO.getCurrentprice();
			}
			ma4 = MathUtil.formatDoubleWith2QuanJin(sum / 4);
		}

		if (ma4 != 0) {
			String sql2 = "SELECT * FROM stockdailyinfo WHERE DATE > '"
					+ DateUtil.formatDay(nextOfZtd) + "' AND stockId = '"
					+ stockId + "' and todayopenprice != 0 order by date ";
			List dailyInfoList2 = GhlhDAO.list(sql2,
					"com.ghlh.data.db.StockdailyinfoVO", 0, 1);
			if (dailyInfoList2.size() < 1) {
				System.out
						.println("No Enough future trade day calculate MA4 for StockId = "
								+ stockId);
			} else {
				StockdailyinfoVO sdiVO = (StockdailyinfoVO) dailyInfoList2
						.get(0);
				if (sdiVO.getLowestprice() <= ma4) {
					result = new MA5ResultBean();
					result.setMa5(ma4);
					result.setReachDate(nextOfZtd);
				} else {
					result = getMA4(stockId, DateUtil.getNextMarketOpenDay(ztd));
				}
			}
		}
		return result;
	}
}
