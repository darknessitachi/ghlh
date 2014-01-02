package com.ghlh.strategy;

import com.ghlh.data.MonitorStockBean;
import com.ghlh.stockquotes.StockQuotesBean;

public class TradeContext {
	private TradeStrategy tradeStrategy;

	public TradeContext(TradeStrategy tradeStrategy) {
		this.tradeStrategy = tradeStrategy;
	}

	public TradeResult processStockTrade(MonitorStockBean monitorStockBean,
			StockQuotesBean stockQuotesBean) {
		return this.tradeStrategy.processStockTrade(monitorStockBean,
				stockQuotesBean);
	}
}
