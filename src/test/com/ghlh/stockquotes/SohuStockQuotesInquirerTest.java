package com.ghlh.stockquotes;

import org.junit.Test;

import com.ghlh.stockquotes.SohuStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;

public class SohuStockQuotesInquirerTest {

	@Test
	public void testGetStockQuotesBean() {
		try {
			String[][] stocks = {{"600036", "��������"}, {"000002", "���A"},
					{"300077", "������"}};

			for (int i = 0; i < stocks.length; i++) {
				SohuStockQuotesInquirer internetStockQuotesInquirer = new SohuStockQuotesInquirer();
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
