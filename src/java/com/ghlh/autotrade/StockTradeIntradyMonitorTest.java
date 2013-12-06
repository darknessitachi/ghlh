package com.ghlh.autotrade;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ghlh.stockquotes.StockQuotesBean;

public class StockTradeIntradyMonitorTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private StockTradeIntradyMonitor stockTradeIntradyMonitor ;
	
	@Before
	public void setUp() throws Exception {
		
		//stockTradeIntradyMonitor = new StockTradeIntradyMonitor();
		
	}

	@After
	public void tearDown() throws Exception {
	}

	public static StockQuotesBean getMockStockQuotestBean() {
		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setStockId("601118");
		sqb.setName("º£ÄÏÏð½º");
		sqb.setCurrentPrice(8);
		sqb.setTodayOpen(7.9);
		sqb.setYesterdayClose(8.1);
		return sqb;
	}
	
	@Test
	public void testProcessSell() {
		fail("Not yet implemented");
	}

	@Test
	public void testProcessBuy() {
		fail("Not yet implemented");
	}

}
