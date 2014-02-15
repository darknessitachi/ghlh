package com.ghlh.stockquotes;

public interface StockQuotesInquirer {
	StockQuotesBean getStockQuotesBean(String stockId);
	void setTestingInjectStockQuotesBean(StockQuotesBean stockQuotesBean);
	
	double getLatestPrice(String stockId);
}
