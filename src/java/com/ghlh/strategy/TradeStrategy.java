package com.ghlh.strategy;

import com.ghlh.data.MonitorStockBean;
import com.ghlh.stockquotes.StockQuotesBean;

public interface TradeStrategy {
	public TradeResult processStockTrade(MonitorStockBean monitorStockBean,
			StockQuotesBean stockQuotesBean);
}
