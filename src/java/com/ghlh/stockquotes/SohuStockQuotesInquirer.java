package com.ghlh.stockquotes;

import java.util.regex.Pattern;

import com.ghlh.util.HttpUtil;

public class SohuStockQuotesInquirer extends InternetStockQuotesInquirer {

	protected String queryStockInfo(String stockQuotesURL) {
		String line = HttpUtil.accessInternet(stockQuotesURL);
		if (line.indexOf("[") < 0) {
			return null;
		}
		if (line != null) {
			line = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
			line = line.substring(line.indexOf("[") + 2);
		}
		return line;
	}

	protected String getStockQuotesURL(String stockId) {
		boolean isSZ = isFromShenzhenMarket(stockId);
		String postStockID = stockId.substring(3);
		String type = "cn";
		if (isSZ) {
			type = "zs";
		}
		String stockQuotesURL = "http://hq.stock.sohu.com/" + type + "/"
				+ postStockID + "/" + type + "_" + stockId + "-1.html";
		return stockQuotesURL;
	}

	protected StockQuotesBean parseStockQuotes(String stockInfo) {
		StockQuotesBean result = new StockQuotesBean();
		Pattern pattern = Pattern.compile("[',]+");
		String[] stockInfoPieces = pattern.split(stockInfo);
		result.setStockId(stockInfoPieces[STOCKID_POSITION].substring(3));
		result.setName(stockInfoPieces[NAME_POSITION]);
		result.setCurrentPrice(Double
				.parseDouble(stockInfoPieces[CURRENT_PRICE_POSITION]));
		result.setZdf(Double.parseDouble(stockInfoPieces[ZDF_POSITION]
				.substring(0, stockInfoPieces[ZDF_POSITION].length() - 1)));
		result.setZde(Double.parseDouble(stockInfoPieces[ZDE_POSITION]));
		return result;
	}

	private final static int STOCKID_POSITION = 0;
	private final static int NAME_POSITION = 1;
	private final static int CURRENT_PRICE_POSITION = 2;
	private final static int ZDF_POSITION = 3;
	private final static int ZDE_POSITION = 4;

}
