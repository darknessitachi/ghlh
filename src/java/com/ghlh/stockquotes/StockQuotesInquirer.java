package com.ghlh.stockquotes;

public interface StockQuotesInquirer {
	StockQuotesBean getStockQuotesBean(String stockId) throws StockQuotesException;
}
