package com.ghlh.stockquotes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

public class InternetStockQuotesInquirer implements StockQuotesInquirer {
	private static HttpClient client = new HttpClient();
	private static HttpMethod method = null;

	public StockQuotesBean getStockQuotesBean(String stockId)
			throws StockQuotesException {
		boolean isSZ = isFromShenzhenMarket(stockId);
		String stockQuotesInfo = getStockQuotesInfoFromSohu(stockId, isSZ);
		StockQuotesBean result = parseStockQuotes(stockQuotesInfo);
		return result;
	}

	private boolean isFromShenzhenMarket(String stockId) {
		boolean result = false;
		if (stockId.indexOf("0") == 0) {
			result = true;
		}
		return result;
	}

	private String getStockQuotesInfoFromSohu(String stockID, boolean isSZ)
			throws StockQuotesException {
		try {
			String url = getStockQuotesPageURL(stockID, isSZ);
			method = new GetMethod(url);
			client.executeMethod(method);
			InputStream in = method.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = br.readLine();
			line = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
			line = line.substring(line.indexOf("[") + 2);
			return line;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new StockQuotesException(
					"There is an exception while reading quotes from sohu ", ex);

		}
	}

	private String getStockQuotesPageURL(String stockID, boolean isSZ) {
		String postStockID = stockID.substring(3);
		String type = "cn";
		if (isSZ) {
			type = "zs";
		}
		String url = "http://hq.stock.sohu.com/" + type + "/" + postStockID
				+ "/" + type + "_" + stockID + "-1.html";
		return url;
	}

	private final static int STOCKID_POSITION = 0;
	private final static int NAME_POSITION = 1;
	private final static int CURRENT_PRICE_POSITION = 2;
	private final static int ZDF_POSITION = 3;
	private final static int ZDE_POSITION = 4;

	private static StockQuotesBean parseStockQuotes(String stockInfo)
			throws StockQuotesException {
		try {
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
		} catch (Exception ex) {

			ex.printStackTrace();
			throw new StockQuotesException(
					"There come up with error while parsing " + stockInfo, ex);
		}
	}
	
	
	public static void main(String[] args){
		try{
		InternetStockQuotesInquirer internetStockQuotesInquirer = new InternetStockQuotesInquirer();
		StockQuotesBean stockQuotesBean = internetStockQuotesInquirer.getStockQuotesBean("600036");
		System.out.println(stockQuotesBean);
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
