package com.ghlh.stockquotes;

import java.util.regex.Pattern;

public class EastMoneyStockQuotesInquirer extends InternetStockQuotesInquirer {
	protected String getStockQuotesPageURL(String stockId, boolean isSZ) {
		String url = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/CompatiblePage.aspx?Type=f&jsName=js&stk=";
		String url1 = "&Reference=xml&rt=0.7897900253331537";
		
		if (stockId.startsWith("6")) {
			stockId += "1";
		} else {
			stockId += "2";
		} 
		url = url + stockId + url1;
		return url;
		
		
	}

	protected String getCharset(){
		return "utf-8";
	}

	protected StockQuotesBean parseStockQuotes(String stockInfo)
			throws StockQuotesException {
		try {
			StockQuotesBean result = new StockQuotesBean();
			stockInfo = stockInfo.substring(stockInfo.indexOf("\"")+1);
			stockInfo = stockInfo.substring(0,stockInfo.indexOf("\""));
			if("".equals(stockInfo)){
				return null;
			}
			Pattern pattern = Pattern.compile(",");
			String[] stockInfoPieces = pattern.split(stockInfo);
			if (stockInfoPieces.length == 0 || stockInfoPieces.length == 1) {
				return null;
			}
			result.setStockId(stockInfoPieces[1]);
			result.setName(stockInfoPieces[2]);
			result.setCurrentPrice(Double.parseDouble(stockInfoPieces[3]));
			result.setZde(Double.parseDouble(stockInfoPieces[4]));
			String sZdf = stockInfoPieces[5].substring(0, stockInfoPieces[5].length()-1);
			result.setZdf(Double.parseDouble(sZdf));
			result.setTodayOpen(Double.parseDouble(stockInfoPieces[8]));
			result.setYesterdayClose(Double.parseDouble(stockInfoPieces[9]));
			result.setHighestPrice(Double.parseDouble(stockInfoPieces[10]));
			result.setLowestPrice(Double.parseDouble(stockInfoPieces[11]));
			String sHsl = stockInfoPieces[12].substring(0, stockInfoPieces[12].length()-1);
			result.setHsl(Double.parseDouble(sHsl));

			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new StockQuotesException(
					"There come up with error while parsing " + stockInfo, ex);
		}
	}

	public static void main(String[] args) {
		try {
			StockQuotesInquirer internetStockQuotesInquirer = new EastMoneyStockQuotesInquirer();
			StockQuotesBean stockQuotesBean = internetStockQuotesInquirer
					.getStockQuotesBean("300342");
			System.out.println(stockQuotesBean);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
