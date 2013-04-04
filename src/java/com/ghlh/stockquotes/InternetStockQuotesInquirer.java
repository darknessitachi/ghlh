package com.ghlh.stockquotes;

import org.apache.log4j.Logger;

public abstract class InternetStockQuotesInquirer implements
		StockQuotesInquirer {
	public static Logger logger = Logger
			.getLogger(InternetStockQuotesInquirer.class);

	public StockQuotesBean getStockQuotesBean(String stockId) {
		String stockQuotesURL = getStockQuotesURL(stockId);
		String stockInfo = queryStockInfo(stockQuotesURL);
		if (stockInfo == null) {
			return null;
		}
		StockQuotesBean result = parseStockQuotes(stockInfo);
		if (result == null) {
			return null;
		}
		result.setStockId(stockId);
		return result;
	}

	protected abstract String queryStockInfo(String stockQuotesURL);

	protected abstract String getStockQuotesURL(String stockId);

	protected boolean isFromShenzhenMarket(String stockId) {
		boolean result = false;
		if (stockId.indexOf("0") == 0 || stockId.indexOf("3") == 0) {
			result = true;
		}
		return result;
	}

	protected abstract StockQuotesBean parseStockQuotes(String stockInfo);

	private static StockQuotesInquirer sinaSQ = new SinaStockQuotesInquirer();
	private static StockQuotesInquirer sohuSQ = new SohuStockQuotesInquirer();

	public static StockQuotesBean queryStock(String stockId) {
		if (stockId == null || stockId.equals("")) {
			return null;
		}
		StockQuotesBean result = null;
		try {
			result = sinaSQ.getStockQuotesBean(stockId);
			if (result == null) {
				result = sohuSQ.getStockQuotesBean(stockId);
			}
		} catch (Exception ex) {
			logger.error("queryStock throw : ", ex);
		}
		return result;

	}

	public static void main(String[] args) {
		while (true) {
			StockQuotesBean sqb = queryStock("600036");
			System.out.println(sqb);
		}
	}
}
