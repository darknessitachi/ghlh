package com.ghlh.strategy.stair;

import java.util.List;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.strategy.TradeUtil;
import com.ghlh.util.MathUtil;

public class StairAnalysis {

	public final static int STAIR_COUNT = 4;
	public final static double STAIR_PERCENTAGE = 0.05;
	public final static double STAIR_MONEY = 80000;
	public static int buyTimes = 0;
	public static int sellTimes = 0;

	public static void main(String[] args) {

		String sql = "SELECT * FROM stockdailyinfo WHERE  Date > '2014-02-24'  AND stockid = '002103'";
		List list = GhlhDAO.list(sql, "com.ghlh.data.db.StockdailyinfoVO");
		StockdailyinfoVO firstDay = (StockdailyinfoVO) list.get(0);
		initBuy(firstDay);

		for (int i = 1; i < list.size(); i++) {
			System.out.println(firstDay.getDate() + " Acount : "
					+ StairAnalysisAccount.getInstance());

			firstDay = (StockdailyinfoVO) list.get(i);
			if (StairAnalysisAccount.getInstance().getCurrentStairPrice() == 0) {
				initBuy(firstDay);
				System.out.println(firstDay.getDate() + " Acount : "
						+ StairAnalysisAccount.getInstance());

				continue;
			}

			boolean hasSell = StairAnalysisAccount.getInstance().sellStock(
					firstDay);
			if (hasSell) {
				System.out.println(firstDay.getDate() + " Acount : "
						+ StairAnalysisAccount.getInstance());

				continue;
			}

			double nextDownPrice = MathUtil
					.formatDoubleWith2QuanJin(StairAnalysisAccount
							.getInstance().getCurrentStairPrice()
							* (1 - STAIR_PERCENTAGE));

			while (StairAnalysisAccount.getInstance().getCurrentStair() < STAIR_COUNT
					&& firstDay.getLowestprice() <= nextDownPrice) {
				buyStock(nextDownPrice, firstDay);
				nextDownPrice = MathUtil.formatDoubleWith2QuanJin(nextDownPrice
						* (1 - STAIR_PERCENTAGE));
			}
			System.out.println(firstDay.getDate() + " Acount : "
					+ StairAnalysisAccount.getInstance());

		}

		System.out.println("buyTimes = " + buyTimes + " sellTimes = "
				+ sellTimes);
	}

	private static void initBuy(StockdailyinfoVO firstDay) {
		double buyPrice = firstDay.getTodayopenprice();
		buyStock(buyPrice, firstDay);

		double nextDownPrice = MathUtil
				.formatDoubleWith2QuanJin(StairAnalysisAccount.getInstance()
						.getCurrentStairPrice() * (1 - STAIR_PERCENTAGE));
		while (firstDay.getLowestprice() < nextDownPrice) {
			buyStock(nextDownPrice, firstDay);
			nextDownPrice = MathUtil.formatDoubleWith2QuanJin(nextDownPrice
					* (1 - STAIR_PERCENTAGE));
		}
	}

	private static void buyStock(double buyPrice, StockdailyinfoVO firstDay) {
		System.out.println(firstDay.getDate()
				+ " buyPrice ********************" + buyPrice);
		buyTimes++;

		StairAnalysisAccount.getInstance().setCurrentStair(
				StairAnalysisAccount.getInstance().getCurrentStair() + 1);
		StairAnalysisAccount.getInstance().setCurrentStairPrice(buyPrice);
		StairAnalysisAccount.getInstance()
				.buyStock(getStairTradeBean(buyPrice));
	}

	private static StairAnalysisBean getStairTradeBean(double buyPrice) {
		StairAnalysisBean sab = new StairAnalysisBean();
		sab.setBuyPrice(buyPrice);
		sab.setStockCount(TradeUtil.getTradeNumber(STAIR_MONEY, buyPrice));
		sab.setMoney(sab.getBuyPrice() * sab.getStockCount());
		sab.setSellPrice(MathUtil.formatDoubleWith2QuanShe(buyPrice
				* (1 + STAIR_PERCENTAGE)));
		return sab;
	}

}
