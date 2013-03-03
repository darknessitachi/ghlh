package com.ghlh.stockquotes;

import org.junit.Test;

import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;

public class InternetStockQuotesInquirerTest {

	@Test
	public void testGetStockQuotesBean() {
		try {
			String[][] stocks = {{"600036", "招商银行"}, {"000002", "万科A"},
					{"300077", "国民技术"}};

			for (int i = 0; i < stocks.length; i++) {
				InternetStockQuotesInquirer internetStockQuotesInquirer = new InternetStockQuotesInquirer();
				StockQuotesBean stockQuotesBean = internetStockQuotesInquirer
						.getStockQuotesBean(stocks[i][0]);
				assert (stockQuotesBean.getStockId().equals(stocks[i][0]));
				assert (stockQuotesBean.getName().equals(stocks[i][1]));
				assert (stockQuotesBean.getCurrentPrice() >= 0);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
