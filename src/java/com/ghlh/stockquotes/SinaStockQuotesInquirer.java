package com.ghlh.stockquotes;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.ghlh.util.HttpUtil;
import com.ghlh.util.MathUtil;

public class SinaStockQuotesInquirer extends InternetStockQuotesInquirer {
	public static Logger logger = Logger
			.getLogger(SinaStockQuotesInquirer.class);

	protected String queryStockInfo(String stockQuotesURL) {
		String line = HttpUtil.accessInternet(stockQuotesURL);
		return line;
	}

	protected String getStockQuotesURL(String stockId) {
		if (stockId.charAt(0) != 's') {
			boolean isSZ = isFromShenzhenMarket(stockId);
			if (isSZ) {
				stockId = "sz" + stockId;
			} else {
				stockId = "sh" + stockId;
			}
		}
		String stockQuotesURL = "http://hq.sinajs.cn/list=" + stockId;
		return stockQuotesURL;
	}

	protected StockQuotesBean parseStockQuotes(String stockInfo) {
		if (stockInfo.indexOf(',') < 0) {
			return null;
		}
		StockQuotesBean result = new StockQuotesBean();
		Pattern pattern = Pattern.compile(",");
		String[] stockInfoPieces = pattern.split(stockInfo);
		result.setName(stockInfoPieces[0].substring(stockInfoPieces[0]
				.indexOf("\"") + 1));
		result.setCurrentPrice(Double.parseDouble(stockInfoPieces[3]));
		double zuoShou = Double.parseDouble(stockInfoPieces[2]);
		result.setZde(MathUtil.formatDoubleWith4(result.getCurrentPrice()
				- zuoShou));
		if (zuoShou > 0) {
			result.setZdf(MathUtil.formatDoubleWith4(result.getZde() / zuoShou));
		}
		return result;
	}
}
