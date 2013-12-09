package com.ghlh.strategy;

import com.ghlh.autotrade.StockTradeIntradyMonitor;
import com.ghlh.stockquotes.StockQuotesBean;

public interface MonitoringStrategy {
	void processSell(StockTradeIntradyMonitor monitor, StockQuotesBean sqb);
	void processBuy(StockTradeIntradyMonitor monitor, StockQuotesBean sqb);
}
