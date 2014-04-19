package com.ghlh.strategy.box;

import java.util.Date;
import java.util.List;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.MathUtil;
import com.ghlh.util.StockMarketUtil;

/**
 * 找出连续在箱体里震荡的票， 并且当前处于低位
 * 
 * @author Robin
 * 
 */
public class BoxBottomSelector {
	static double percentageHighTop = 1.15;
	static double percentageHighGap = 0.04;
	static double percentageLowBottom = 0.98;
	static double percentageLowGap = 0.04;
	static int zhenDangDays = 20;

	public static void main(String[] args) {
		Date date = DateUtil.getDate(2014, 3, 8);
		String sDate = DateUtil.formatDate(DateUtil.getNextMarketOpenDay(date));
		List result = StockMarketUtil.getHuShiStockList();

		for (int i = 0; i < result.size(); i++) {
			StockQuotesBean sqb = (StockQuotesBean) result.get(i);
			String stockId = sqb.getStockId();
			String sql = "SELECT * FROM stockdailyinfo WHERE stockid = '"
					+ stockId + "' AND DATE < '" + sDate
					+ "' ORDER BY DATE DESC";
			List list = GhlhDAO.list(sql, "com.ghlh.data.db.StockdailyinfoVO",
					0, zhenDangDays + 1);
			StockdailyinfoVO sdiVO = (StockdailyinfoVO) list.get(0);
			double jibunPrice = sdiVO.getCurrentprice();
			if (jibunPrice == 0) {
				continue;
			}
			boolean meetPickup = true;
			int status = 0;

			for (int j = 1; j < list.size(); j++) {
				StockdailyinfoVO sdiVO1 = (StockdailyinfoVO) list.get(j);
				double previousPrice = sdiVO1.getCurrentprice();
				double lowPrice = sdiVO1.getLowestprice();
				double highPrice = sdiVO1.getHighestprice();

				if (!meetScope(jibunPrice, previousPrice)) {
					break;
				}
				if (status == 4) {
					break;
				}
				if (meetHighestPrice(jibunPrice, highPrice)
						&& (status == 1 || status == 3)) {
					if (sdiVO1.getStockid().equals("601799")) {
						System.out.println(sdiVO1.getDate()
								+ " meet highest price : "
								+ highPrice);
					}
					status++;
				}
				if (meetLowestPrice(jibunPrice, lowPrice)
						&& (status == 0 || status == 2)) {
					if (sdiVO1.getStockid().equals("601799")) {
						System.out.println(sdiVO1.getDate()
								+ " meet lowest price : "
								+ lowPrice);
					}
					status++;

				}
			}
			if (status == 4) {
				System.out.println(date + " Pick up Stock : " + stockId + " price = " + jibunPrice);
			}
		}

	}

	public static boolean meetHighestPrice(double jibunPrice,
			double previousPrice) {
		boolean result = false;
		double highestPriceTop = jibunPrice * percentageHighTop;
		double highestPriceBottom = jibunPrice
				* (percentageHighTop - percentageHighGap);
		if (previousPrice <= highestPriceTop
				&& previousPrice >= highestPriceBottom) {
			result = true;
		}
		return result;
	}

	public static boolean meetLowestPrice(double jibunPrice,
			double previousPrice) {
		boolean result = false;
		double lowestPriceTop = jibunPrice
				* (percentageLowBottom + percentageLowGap);
		double lowestPriceBottom = jibunPrice * percentageLowBottom;
		if (previousPrice <= lowestPriceTop
				&& previousPrice >= lowestPriceBottom) {
			result = true;
		}
		return result;
	}

	public static boolean meetScope(double jibunPrice, double previousPrice) {
		boolean result = false;
		double highestPriceTop = jibunPrice * percentageHighTop;
		double lowestPriceBottom = jibunPrice * percentageLowBottom;
		if (previousPrice >= lowestPriceBottom
				&& previousPrice <= highestPriceTop) {
			result = true;
		}
		return result;
	}

}
