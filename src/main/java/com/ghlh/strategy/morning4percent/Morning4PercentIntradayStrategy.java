package com.ghlh.strategy.morning4percent;

import com.ghlh.autotrade.StockTradeIntradyMonitor;
import com.ghlh.autotrade.StockTradeIntradyUtil;
import com.ghlh.data.db.MonitorstockDAO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.MonitoringStrategy;

public class Morning4PercentIntradayStrategy implements MonitoringStrategy {
	public void processSell(StockTradeIntradyMonitor monitor,
			StockQuotesBean sqb) {
//		StockTradeIntradyUtil.processSell(monitor, sqb);
	}

	public void processBuy(StockTradeIntradyMonitor monitor, StockQuotesBean sqb) {
//		boolean buySuccessfully = StockTradeIntradyUtil
//				.processBuy(monitor, sqb);
//		if (buySuccessfully) {
//			MonitorstockDAO.turnOnorOffMonitorStock(monitor.getMonitorstockVO()
//					.getId(), false);
//		}
	}

}
