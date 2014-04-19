package com.ghlh.report;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockreportVO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.StockMarketUtil;

public class ReportUtil {

	public static Logger logger = Logger.getLogger(ReportUtil.class);

	public static void main(String[] args) {
		List result = StockMarketUtil.getShenShiStockList();
//		List result = new ArrayList();
//		StockQuotesBean sqb1 = new StockQuotesBean();
//		sqb1.setStockId("002104");
//		result.add(sqb1);
		Date now = new Date();

		for (int i = 0; i < result.size(); i++) {
			StockQuotesBean sqb = (StockQuotesBean) result.get(i);
			String stockId = sqb.getStockId();
			System.out.println("stockId = " + stockId);
			String content = accessInternet("http://data.eastmoney.com/report/"
					+ stockId + ".html");
			// System.out.println("Content = " + content);
			Pattern pattern = Pattern.compile("<td>");
			String[] reportNumbers = pattern.split(content);
			for (int j = 1; j < reportNumbers.length; j = j + 5) {
				try {
					StockreportVO reportVO = new StockreportVO();
					reportVO.setCreatedtime(now);
					reportVO.setLastmodifiedtime(now);
					reportVO.setDate(now);
					reportVO.setStockid(stockId);
					reportVO.setType(j);
					reportVO.setMairu(Integer
							.parseInt(reportNumbers[j].trim().substring(0,
									reportNumbers[j].trim().indexOf("<"))));
					reportVO.setZengchi(Integer.parseInt(reportNumbers[j + 1]
							.trim().substring(0,
									reportNumbers[j + 1].trim().indexOf("<"))));
					reportVO.setZhongxin(Integer.parseInt(reportNumbers[j + 2]
							.trim().substring(0,
									reportNumbers[j + 2].trim().indexOf("<"))));
					reportVO.setJianchi(Integer.parseInt(reportNumbers[j + 3]
							.trim().substring(0,
									reportNumbers[j + 3].trim().indexOf("<"))));
					reportVO.setMaichu(Integer.parseInt(reportNumbers[j + 4]
							.trim().substring(0,
									reportNumbers[j + 4].trim().indexOf("<"))));
					GhlhDAO.create(reportVO);
				} catch (Exception ex) {
					System.out.println("StockId = " + stockId
							+ " throw exception: ");
					ex.printStackTrace();
				}
			}
		}
	}

	public static String accessInternet(String stockQuotesURL) {
		String line = null;
		HttpMethod method = new GetMethod(stockQuotesURL);
		HttpClient client = new HttpClient();
		String result = "";
		try {
			client.executeMethod(method);
			InputStream in = method.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));// ,
			// "utf-8"));
			line = br.readLine();
			boolean isStart = false;
			int no = 0;
			while (line != null) {
				if (isStart) {
					result += line;
					no++;
					if (no == 5) {
						isStart = false;
						no = 0;
					}
				}
				if (line.indexOf("一个月内") > 0 || line.indexOf("三个月内") > 0
						|| line.indexOf("半年内") > 0 || line.indexOf("一年内") > 0) {
					isStart = true;
				}
				line = br.readLine();
			}
			method.releaseConnection();
		} catch (Exception ex) {
			logger.error("Quote stock info from " + stockQuotesURL
					+ " throw : ", ex);
		} finally {
			method.releaseConnection();
		}
		return result;
	}

}
