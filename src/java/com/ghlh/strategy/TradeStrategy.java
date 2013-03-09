package com.ghlh.strategy;

import java.util.List;

import com.ghlh.stockpool.MonitorStockBean;
import com.ghlh.stockquotes.StockQuotesBean;

public interface TradeStrategy {
	public TradeResult processStockTrade(MonitorStockBean monitorStockBean,
			StockQuotesBean stockQuotesBean);

	public String collectAdditionalInfoFromUIComponents(List uiComponents);

	public String updateCurrentNumber(String additionInfo, int cmd,
			int tradeNumber);

	public void setAdditionalInfoToUIComponents(List uiComponents,
			String additionInfo);

	public boolean hasChangedValueInAdditionalUIComponents(List uiComponents,
			MonitorStockBean currentMsb);

}
