package com.ghlh.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.ghlh.stockquotes.StockQuotesException;

public class HttpUtil {
	public static Logger logger = Logger.getLogger(HttpUtil.class);

	public static String accessInternet(String stockQuotesURL) {
		String line = null;
		try {
			HttpClient client = new HttpClient();
			HttpMethod method = new GetMethod(stockQuotesURL);
			client.executeMethod(method);
			InputStream in = method.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			line = br.readLine();
		} catch (Exception ex) {
			logger.error("Quote stock info from " + stockQuotesURL
					+ " throw : ", ex);
			return null;
		}
		return line;
	}

}
