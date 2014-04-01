package com.ghlh.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

public class HttpUtil {
	public static Logger logger = Logger.getLogger(HttpUtil.class);

	public static String accessInternet(String stockQuotesURL) {
		String line = null;
		HttpMethod method = new GetMethod(stockQuotesURL);
		HttpClient client = new HttpClient();
		try {
			client.executeMethod(method);
			InputStream in = method.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in,
					"utf-8"));
			line = br.readLine();
			method.releaseConnection();
		} catch (Exception ex) {
			logger.error("Quote stock info from " + stockQuotesURL
					+ " throw : ", ex);
		} finally {
			method.releaseConnection();
		}
		return line;
	}
}
