package com.ghlh.strategy.stair;

import com.ghlh.Constants;
import com.ghlh.stockpool.MonitorStockBean;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.AdditionInfoUtil;
import com.ghlh.strategy.TradeResult;
import com.ghlh.strategy.TradeStrategy;

public class StairTradeStrategy implements TradeStrategy {
	public TradeResult processStockTrade(MonitorStockBean monitorStockBean,
			StockQuotesBean stockQuotesBean) {
		TradeResult tradeResult = new TradeResult();
		AdditionalInfoBean additionalInfoBean = (AdditionalInfoBean) AdditionInfoUtil
				.parseAdditionalInfoBean(monitorStockBean.getAdditionInfo(),
						"stair");
		tradeResult.setStockId(monitorStockBean.getStockId());
		tradeResult.setTradePrice(stockQuotesBean.getCurrentPrice());
		if (isReachSellCondition(monitorStockBean, stockQuotesBean,
				additionalInfoBean)) {
			tradeResult.setCmd(Constants.SELL);
			int tradeNumber = com.ghlh.util.MathUtil.getNSquareM(2,
					additionalInfoBean.getRank() - 1)
					* additionalInfoBean.getTradeNumber();
			tradeResult.setNumber(tradeNumber);

			additionalInfoBean.setRank(additionalInfoBean.getRank() - 1);

			syncMonitorStock(monitorStockBean, tradeResult, additionalInfoBean);

		} else if (isReachBuyCondition(monitorStockBean, stockQuotesBean,
				additionalInfoBean)) {
			tradeResult.setCmd(Constants.BUY);
			int tradeNumber = com.ghlh.util.MathUtil.getNSquareM(2,
					additionalInfoBean.getRank())
					* additionalInfoBean.getTradeNumber();
			tradeResult.setNumber(tradeNumber);
			additionalInfoBean.setRank(additionalInfoBean.getRank() + 1);

			syncMonitorStock(monitorStockBean, tradeResult, additionalInfoBean);
		}
		return tradeResult;
	}

	private void syncMonitorStock(MonitorStockBean monitorStockBean,
			TradeResult tradeResult, AdditionalInfoBean additionalInfoBean) {
		if (tradeResult.getCmd() == Constants.BUY) {
			additionalInfoBean.setCurrentNumber(additionalInfoBean
					.getCurrentNumber() + tradeResult.getNumber());
		}
		if (tradeResult.getCmd() == Constants.SELL) {
			additionalInfoBean.setCurrentNumber(additionalInfoBean
					.getCurrentNumber() - tradeResult.getNumber());
			additionalInfoBean.setCanSellNumber(additionalInfoBean
					.getCanSellNumber() - tradeResult.getNumber());

		}
		additionalInfoBean.setStandardPrice(tradeResult.getTradePrice());
		monitorStockBean.setAdditionInfo(AdditionInfoUtil
				.parseAdditionalInfoBeanBack(additionalInfoBean, "stair"));
	}

	private boolean isReachSellCondition(MonitorStockBean monitorStockBean,
			StockQuotesBean stockQuotesBean,
			AdditionalInfoBean additionalInfoBean) {
		double sellingPrice = additionalInfoBean.getStandardPrice()
				* (additionalInfoBean.getStairZDF() + 1);
		boolean result = stockQuotesBean.getCurrentPrice() >= sellingPrice
				&& additionalInfoBean.getCanSellNumber() > 0;
		return result;
	}

	private boolean isReachBuyCondition(MonitorStockBean monitorStockBean,
			StockQuotesBean stockQuotesBean,
			AdditionalInfoBean additionalInfoBean) {
		double buyingPrice = additionalInfoBean.getStandardPrice()
				* (1 - additionalInfoBean.getStairZDF());
		boolean result = stockQuotesBean.getCurrentPrice() <= buyingPrice
				&& additionalInfoBean.getRank() < additionalInfoBean
						.getMaxRank();
		return result;
	}
}