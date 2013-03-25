package com.ghlh.strategy;

import java.util.List;

import com.ghlh.Constants;
import com.ghlh.stockpool.MonitorStockBean;
import com.ghlh.stockquotes.StockQuotesBean;

public class StandardPriceAndFixedZDEStrategy implements TradeStrategy {
	public TradeResult processStockTrade(MonitorStockBean monitorStockBean,
			StockQuotesBean stockQuotesBean) {
		TradeResult tradeResult = new TradeResult();
		if (isReachSellCondition(monitorStockBean, stockQuotesBean)) {
			tradeResult.setCmd(Constants.SELL);
			setTradeStockInfo(monitorStockBean, tradeResult);
		} else if (isReachBuyCondition(monitorStockBean, stockQuotesBean)) {
			tradeResult.setCmd(Constants.BUY);
			setTradeStockInfo(monitorStockBean, tradeResult);
		}
		return tradeResult;
	}

	private void setTradeStockInfo(MonitorStockBean monitorStockBean,
			TradeResult tradeResult) {
		tradeResult.setStockId(monitorStockBean.getStockId());
		// tradeResult.setNumber(monitorStockBean.());
	}

	private boolean isReachSellCondition(MonitorStockBean monitorStockBean,
			StockQuotesBean stockQuotesBean) {
		// double sellingPrice = monitorStockBean.getStandardPrice();
		// // * (monitorStockBean.getZf() + 1);
		// boolean result = stockQuotesBean.getCurrentPrice() > sellingPrice
		// && monitorStockBean.getCurrentNumber() > 0;
		return false;// result;
	}

	private boolean isReachBuyCondition(MonitorStockBean monitorStockBean,
			StockQuotesBean stockQuotesBean) {
		// double buyingPrice = monitorStockBean.getStandardPrice();
		// // * (1 - monitorStockBean.getDf());
		// boolean result = stockQuotesBean.getCurrentPrice() < buyingPrice
		// && monitorStockBean.getCurrentNumber() == 0;
		// return result;
		return false;
	}

	public String collectAdditionalInfoFromUIComponents(List uiComponents) {
		return null;
	}

	public void setAdditionalInfoToUIComponents(List uiComponents,
			String additionInfo) {

	}

	public boolean hasChangedValueInAdditionalUIComponents(List uiComponents,
			MonitorStockBean currentMsb) {
		// TODO Auto-generated method stub
		return false;
	}

}
