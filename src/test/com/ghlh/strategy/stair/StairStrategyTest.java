package com.ghlh.strategy.stair;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ghlh.Constants;
import com.ghlh.stockpool.MonitorStockBean;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.TradeResult;
import com.ghlh.strategy.stair.StairTradeStrategy;

public class StairStrategyTest {
	public static StairTradeStrategy ss = new StairTradeStrategy();

	@Test
	public void testProcessStockNoTrade() {
		MonitorStockBean msb = new MonitorStockBean();
		msb.setStockId("600036");
		msb.setName("招商银行");
		msb.setTradeAlgorithm("Stair");
		msb.setAdditionInfo("10,  0,   0,  0.05,     100,        0,   4");

		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setCurrentPrice(10);
		sqb.setStockId("600036");
		sqb.setName("招商银行");

		TradeResult tr = ss.processStockTrade(msb, sqb);
		assertEquals(tr.getCmd(), 0);

	}

	@Test
	public void testProcessStockNoSellForRank0() {
		MonitorStockBean msb = new MonitorStockBean();
		msb.setStockId("600036");
		msb.setName("招商银行");
		msb.setTradeAlgorithm("Stair");
		msb.setAdditionInfo("10,   0,  0,  0.05,     100,        0,   4");

		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setCurrentPrice(10.5);
		sqb.setStockId("600036");
		sqb.setName("招商银行");

		TradeResult tr = ss.processStockTrade(msb, sqb);
		assertEquals(tr.getCmd(), 0);
	}

	@Test
	public void testProcessStockBuyRank1() {
		MonitorStockBean msb = new MonitorStockBean();
		msb.setStockId("600036");
		msb.setName("招商银行");
		msb.setTradeAlgorithm("Stair");
		msb.setAdditionInfo("10,   0,   0,  0.05,     100,        0,   4");

		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setCurrentPrice(9.5);
		sqb.setStockId("600036");
		sqb.setName("招商银行");

		TradeResult tr = ss.processStockTrade(msb, sqb);
		assertEquals(tr.getCmd(), Constants.BUY);
		assertEquals(tr.getTradePrice(), 9.5, 0.1);
		assertEquals(tr.getNumber(), 100);
		assertEquals(tr.getStockId(), "600036");

		assertEquals(msb.getAdditionInfo(),
				"9.5,       100,          0,         0.05,     100,        1,   4");

		sqb.setCurrentPrice(9.4);
		tr = ss.processStockTrade(msb, sqb);
		assertEquals(tr.getCmd(), 0);
	}

	@Test
	public void testProcessStockSellRank1() {
		MonitorStockBean msb = new MonitorStockBean();
		msb.setStockId("600036");
		msb.setName("招商银行");
		msb.setTradeAlgorithm("Stair");
		msb.setAdditionInfo("9.5, 100, 100,  0.05,     100,        1,   4");

		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setCurrentPrice(10);
		sqb.setStockId("600036");
		sqb.setName("招商银行");

		TradeResult tr = ss.processStockTrade(msb, sqb);
		assertEquals(tr.getCmd(), Constants.SELL);
		assertEquals(tr.getTradePrice(), 10, 0.1);
		assertEquals(tr.getNumber(), 100);
		assertEquals(tr.getStockId(), "600036");

		assertEquals(msb.getAdditionInfo(),
				"10.0,       0,          0,         0.05,     100,        0,   4");

	}

	@Test
	public void testProcessStockBuyRank2() {
		MonitorStockBean msb = new MonitorStockBean();
		msb.setStockId("600036");
		msb.setName("招商银行");
		msb.setTradeAlgorithm("Stair");
		msb.setAdditionInfo("9.5, 100, 100, 0.05,     100,        1,   4");

		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setCurrentPrice(9);
		sqb.setStockId("600036");
		sqb.setName("招商银行");

		TradeResult tr = ss.processStockTrade(msb, sqb);
		assertEquals(tr.getCmd(), Constants.BUY);
		assertEquals(tr.getTradePrice(), 9, 0.1);
		assertEquals(tr.getNumber(), 200);
		assertEquals(tr.getStockId(), "600036");

		assertEquals(msb.getAdditionInfo(),
				"9.0,       300,          100,         0.05,     100,        2,   4");

		sqb.setCurrentPrice(8.9);
		tr = ss.processStockTrade(msb, sqb);
		assertEquals(tr.getCmd(), 0);
	}

	@Test
	public void testProcessStockSellRank2() {
		MonitorStockBean msb = new MonitorStockBean();
		msb.setStockId("600036");
		msb.setName("招商银行");
		msb.setTradeAlgorithm("Stair");
		msb.setAdditionInfo("9, 300, 300, 0.05,     100,        2,   4");

		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setCurrentPrice(9.5);
		sqb.setStockId("600036");
		sqb.setName("招商银行");

		TradeResult tr = ss.processStockTrade(msb, sqb);
		assertEquals(tr.getCmd(), Constants.SELL);
		assertEquals(tr.getTradePrice(), 9.5, 0.1);
		assertEquals(tr.getNumber(), 200);
		assertEquals(tr.getStockId(), "600036");

		assertEquals(msb.getAdditionInfo(),
				"9.5,       100,          100,         0.05,     100,        1,   4");
	}

	@Test
	public void testProcessStockBuyRank3() {
		MonitorStockBean msb = new MonitorStockBean();
		msb.setStockId("600036");
		msb.setName("招商银行");
		msb.setTradeAlgorithm("Stair");
		msb.setAdditionInfo("9, 300, 300, 0.05,     100,        2,   4");

		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setCurrentPrice(8.54);
		sqb.setStockId("600036");
		sqb.setName("招商银行");

		TradeResult tr = ss.processStockTrade(msb, sqb);
		assertEquals(tr.getCmd(), Constants.BUY);
		assertEquals(tr.getTradePrice(), 8.54, 0.1);
		assertEquals(tr.getNumber(), 400);
		assertEquals(tr.getStockId(), "600036");

		assertEquals(msb.getAdditionInfo(),
				"8.54,       700,          300,         0.05,     100,        3,   4");

		sqb.setCurrentPrice(8.55);
		tr = ss.processStockTrade(msb, sqb);
		assertEquals(tr.getCmd(), 0);
	}

	public void testProcessStockSellRank3() {
		MonitorStockBean msb = new MonitorStockBean();
		msb.setStockId("600036");
		msb.setName("招商银行");
		msb.setTradeAlgorithm("Stair");
		msb.setAdditionInfo("8.54, 700, 700, 0.05,     100,        3,   4");

		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setCurrentPrice(9);
		sqb.setStockId("600036");
		sqb.setName("招商银行");

		TradeResult tr = ss.processStockTrade(msb, sqb);
		assertEquals(tr.getCmd(), Constants.SELL);
		assertEquals(tr.getTradePrice(), 9, 0.1);
		assertEquals(tr.getNumber(), 400);
		assertEquals(tr.getStockId(), "600036");

		assertEquals(msb.getAdditionInfo(), "0.05,     100,        2,   4");

	}

	@Test
	public void testProcessStockBuyRank4() {
		MonitorStockBean msb = new MonitorStockBean();
		msb.setStockId("600036");
		msb.setName("招商银行");
		msb.setTradeAlgorithm("Stair");
		msb.setAdditionInfo("8.54, 700, 700, 0.05,     100,        3,   4");

		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setCurrentPrice(8.11);
		sqb.setStockId("600036");
		sqb.setName("招商银行");

		TradeResult tr = ss.processStockTrade(msb, sqb);
		assertEquals(tr.getCmd(), Constants.BUY);
		assertEquals(tr.getTradePrice(), 8.11, 0.1);
		assertEquals(tr.getNumber(), 800);
		assertEquals(tr.getStockId(), "600036");

		assertEquals(msb.getAdditionInfo(),
				"8.11,       1500,          700,         0.05,     100,        4,   4");

		sqb.setCurrentPrice(8);
		tr = ss.processStockTrade(msb, sqb);
		assertEquals(tr.getCmd(), 0);
	}

	@Test
	public void testProcessStockSellRank4() {
		MonitorStockBean msb = new MonitorStockBean();
		msb.setStockId("600036");
		msb.setName("招商银行");
		msb.setTradeAlgorithm("Stair");
		msb.setAdditionInfo("8.11, 1500, 1500, 0.05,     100,        4,   4");

		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setCurrentPrice(8.54);
		sqb.setStockId("600036");
		sqb.setName("招商银行");

		TradeResult tr = ss.processStockTrade(msb, sqb);
		assertEquals(tr.getCmd(), Constants.SELL);
		assertEquals(tr.getTradePrice(), 8.54, 0.1);
		assertEquals(tr.getNumber(), 800);
		assertEquals(tr.getStockId(), "600036");

		assertEquals(msb.getAdditionInfo(),
				"8.54,       700,          700,         0.05,     100,        3,   4");

	}

	@Test
	public void testProcessStockReachMaxRank() {
		MonitorStockBean msb = new MonitorStockBean();
		msb.setStockId("600036");
		msb.setName("招商银行");
		msb.setTradeAlgorithm("Stair");
		msb.setAdditionInfo("8.11, 1500, 1500, 0.05,     100,        4,   4");

		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setCurrentPrice(7.5);
		sqb.setStockId("600036");
		sqb.setName("招商银行");

		TradeResult tr = ss.processStockTrade(msb, sqb);
		assertEquals(tr.getCmd(), 0);
	}

}
