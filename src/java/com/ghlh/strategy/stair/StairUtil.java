package com.ghlh.strategy.stair;

import com.ghlh.autotrade.EventRecorder;
import com.ghlh.strategy.TradeConstants;
import com.ghlh.strategy.TradeUtil;
import com.ghlh.util.MathUtil;

public class StairUtil {

	public static void dealBuy(String stockId, AdditionalInfoBean aib,
			double basePrice, double currentPrice, int spaceNumber) {
		double possibleMinPrice = currentPrice * TradeConstants.MAX_DF;

		double sellPrice = MathUtil.formatDoubleWith2QuanShe(basePrice
				* (1 + aib.getStairZDF()));

		for (int i = 0; i < spaceNumber; i++) {
			boolean canBuy = StairUtil.buyAsCondition(stockId, aib,
					possibleMinPrice, basePrice, sellPrice);
			if (!canBuy) {
				break;
			}
			sellPrice = basePrice;
			basePrice = MathUtil.formatDoubleWith2QuanJin(basePrice
					* (1 - aib.getStairZDF()));

		}
	}

	public static boolean buyAsCondition(String stockId,
			AdditionalInfoBean aib, double possibleMinPrice, double basePrice,
			double sellPrice) {
		if (possibleMinPrice < basePrice) {
			String message = "盘前买入股票但不下单  : " + stockId + " 价格:" + basePrice
					+ " 数量:"
					+ TradeUtil.getTradeNumber(aib.getStairMoney(), basePrice)
					+ " (盘中监控价格达到下单)";
			EventRecorder.recordEvent(StairUtil.class, message);

			TradeUtil.dealBuyStock(stockId, aib.getStairMoney(), basePrice,
					sellPrice, StairConstants.STAIR_STRATEGY_NAME);
			return true;
		} else {
			return false;
		}
	}

}
