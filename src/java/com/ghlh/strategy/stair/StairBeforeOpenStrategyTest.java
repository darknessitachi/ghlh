package com.ghlh.strategy.stair;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import com.common.util.IDGenerator;
import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.SinaStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.TradeConstants;

public class StairBeforeOpenStrategyTest {

	@org.junit.Test
	public void test4StairsWithNoHoldingStocks() {
		MonitorstockVO monitorStockVO = prepareMonitorstockVO();
		cleanTestingData();
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setCurrentPrice(12);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new StairBeforeOpenStrategy().processStockTrade(monitorStockVO);
		List stockTradeList = StocktradeDAO.getHoldStocks("601118",
				StairConstants.STAIR_STRATEGY_NAME);
		if (stockTradeList.size() > 0) {
			fail("Something wrong");
		}

		sqb.setCurrentPrice(10.3);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new StairBeforeOpenStrategy().processStockTrade(monitorStockVO);
		stockTradeList = StocktradeDAO.getHoldStocks("601118",
				StairConstants.STAIR_STRATEGY_NAME);
		if (stockTradeList.size() != 2) {
			fail("Something wrong");
		}

		for (int i = 0; i < stockTradeList.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(i);
			if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
				fail("Something wrong");
			}
		}
		cleanTestingData();
		sqb.setCurrentPrice(9.8);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new StairBeforeOpenStrategy().processStockTrade(monitorStockVO);
		stockTradeList = StocktradeDAO.getHoldStocks("601118",
				StairConstants.STAIR_STRATEGY_NAME);
		if (stockTradeList.size() != 3) {
			fail("Something wrong");
		}
	}

	@org.junit.Test
	public void test4StairsWithLowestPriceNoPossibleSell() {
		MonitorstockVO monitorStockVO = prepareMonitorstockVO();
		cleanTestingData();
		prepareDataWith4Stair();
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new StairBeforeOpenStrategy().processStockTrade(monitorStockVO);
		List stockTradeList = StocktradeDAO.getHoldStocks("601118",
				StairConstants.STAIR_STRATEGY_NAME);
		for (int i = 0; i < stockTradeList.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(i);
			if (stocktradeVO.getStatus() == TradeConstants.STATUS_POSSIBLE_SELL
					|| stocktradeVO.getStatus() == TradeConstants.STATUS_PENDING_BUY) {
				fail("Something wrong");
			}
		}
	}

	@org.junit.Test
	public void test4StairsWithLowestPriceAnd1PossibleSell() {
		MonitorstockVO monitorStockVO = prepareMonitorstockVO();
		cleanTestingData();
		prepareDataWith4Stair();
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setCurrentPrice(7.4);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new StairBeforeOpenStrategy().processStockTrade(monitorStockVO);
		List stockTradeList = StocktradeDAO.getHoldStocks("601118",
				StairConstants.STAIR_STRATEGY_NAME);
		for (int i = 0; i < stockTradeList.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(i);
			if (i != stockTradeList.size() - 1) {
				if (stocktradeVO.getStatus() == TradeConstants.STATUS_POSSIBLE_SELL
						|| stocktradeVO.getStatus() == TradeConstants.STATUS_PENDING_BUY) {
					fail("Something wrong");
				}
			} else {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
					fail("Something wrong");
				}
			}
		}
	}

	@org.junit.Test
	public void test4StairsWithLowestPriceAnd2PossibleSell() {
		MonitorstockVO monitorStockVO = prepareMonitorstockVO();
		cleanTestingData();
		prepareDataWith4Stair();
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setCurrentPrice(7.8);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new StairBeforeOpenStrategy().processStockTrade(monitorStockVO);
		List stockTradeList = StocktradeDAO.getHoldStocks("601118",
				StairConstants.STAIR_STRATEGY_NAME);
		for (int i = 0; i < stockTradeList.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(i);
			if (i != stockTradeList.size() - 1
					&& i != stockTradeList.size() - 2) {
				if (stocktradeVO.getStatus() == TradeConstants.STATUS_POSSIBLE_SELL
						|| stocktradeVO.getStatus() == TradeConstants.STATUS_PENDING_BUY) {
					fail("Something wrong");
				}
			} else {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
					fail("Something wrong");
				}
			}
		}
	}

	@org.junit.Test
	public void test3StairsWithLowestPriceWith1PossibleSell() {
		MonitorstockVO monitorStockVO = prepareMonitorstockVO();
		cleanTestingData();
		prepareDataWith3Stair();
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setCurrentPrice(8.03);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new StairBeforeOpenStrategy().processStockTrade(monitorStockVO);
		List stockTradeList = StocktradeDAO.getHoldStocks("601118",
				StairConstants.STAIR_STRATEGY_NAME);
		for (int i = 0; i < stockTradeList.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(i);
			if (i == stockTradeList.size() - 1) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
					fail("something wrong");
				}
			} else if (i == stockTradeList.size() - 2) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
					fail("something wrong");
				}
			} else {
				if (stocktradeVO.getStatus() == TradeConstants.STATUS_POSSIBLE_SELL
						|| stocktradeVO.getStatus() == TradeConstants.STATUS_PENDING_BUY) {
					fail("Something wrong");
				}
			}
		}
	}

	@org.junit.Test
	public void test3StairsWithLowestPriceWith2PossibleSell() {
		MonitorstockVO monitorStockVO = prepareMonitorstockVO();
		cleanTestingData();
		prepareDataWith3Stair();
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setCurrentPrice(8.2);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new StairBeforeOpenStrategy().processStockTrade(monitorStockVO);
		List stockTradeList = StocktradeDAO.getHoldStocks("601118",
				StairConstants.STAIR_STRATEGY_NAME);
		for (int i = 0; i < stockTradeList.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(i);
			if (i == stockTradeList.size() - 1) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
					fail("something wrong");
				}
			} else if (i == stockTradeList.size() - 2) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
					fail("something wrong");
				}
			} else if (i == stockTradeList.size() - 3) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
					fail("something wrong");
				}
			} else {
				if (stocktradeVO.getStatus() == TradeConstants.STATUS_POSSIBLE_SELL
						|| stocktradeVO.getStatus() == TradeConstants.STATUS_PENDING_BUY) {
					fail("Something wrong");
				}
			}
		}
	}

	@org.junit.Test
	public void test2StairsWithLowestPriceWith2PossibleSell() {
		MonitorstockVO monitorStockVO = prepareMonitorstockVO();
		cleanTestingData();
		prepareDataWith2Stair();
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setCurrentPrice(8.5);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new StairBeforeOpenStrategy().processStockTrade(monitorStockVO);
		List stockTradeList = StocktradeDAO.getHoldStocks("601118",
				StairConstants.STAIR_STRATEGY_NAME);
		for (int i = 0; i < stockTradeList.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(i);
			if (i == stockTradeList.size() - 1) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
					fail("something wrong");
				}
			} else if (i == stockTradeList.size() - 2) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
					fail("something wrong");
				}
			} else if (i == stockTradeList.size() - 3) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
					fail("something wrong");
				}
			}
		}
	}

	@org.junit.Test
	public void test2StairsWithLowestPriceWith1PossibleSell() {
		MonitorstockVO monitorStockVO = prepareMonitorstockVO();
		cleanTestingData();
		prepareDataWith2Stair();
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setCurrentPrice(8.3);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new StairBeforeOpenStrategy().processStockTrade(monitorStockVO);
		List stockTradeList = StocktradeDAO.getHoldStocks("601118",
				StairConstants.STAIR_STRATEGY_NAME);
		for (int i = 0; i < stockTradeList.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(i);
			if (i == stockTradeList.size() - 1) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
					fail("something wrong");
				}
			} else if (i == stockTradeList.size() - 2) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
					fail("something wrong");
				}
			} else if (i == stockTradeList.size() - 3) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
					fail("something wrong");
				}
			} else {
				if (stocktradeVO.getStatus() == TradeConstants.STATUS_POSSIBLE_SELL
						|| stocktradeVO.getStatus() == TradeConstants.STATUS_PENDING_BUY) {
					fail("Something wrong");
				}
			}
		}
	}

	@org.junit.Test
	public void test1StairsWithLowestPriceWith1PossibleBuy() {
		MonitorstockVO monitorStockVO = prepareMonitorstockVO();
		cleanTestingData();
		prepareDataWith1Stair();
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setCurrentPrice(9.2);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new StairBeforeOpenStrategy().processStockTrade(monitorStockVO);
		List stockTradeList = StocktradeDAO.getHoldStocks("601118",
				StairConstants.STAIR_STRATEGY_NAME);
		for (int i = 0; i < stockTradeList.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(i);
			if (i == stockTradeList.size() - 1) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
					fail("something wrong");
				}

			} else if (i == stockTradeList.size() - 2) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
					fail("something wrong");
				}
			}
		}
	}

	@org.junit.Test
	public void test1StairsWithLowestPriceWith2PossibleBuy() {
		MonitorstockVO monitorStockVO = prepareMonitorstockVO();
		cleanTestingData();
		prepareDataWith1Stair();
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setCurrentPrice(8.8);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new StairBeforeOpenStrategy().processStockTrade(monitorStockVO);
		List stockTradeList = StocktradeDAO.getHoldStocks("601118",
				StairConstants.STAIR_STRATEGY_NAME);
		for (int i = 0; i < stockTradeList.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(i);
			if (i == stockTradeList.size() - 1) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
					fail("something wrong");
				}

			} else if (i == stockTradeList.size() - 2) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
					fail("something wrong");
				}

			} else if (i == stockTradeList.size() - 3) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
					fail("something wrong");
				}
			}
		}
	}

	@org.junit.Test
	public void test2StairsWith2PossibleBuyAnd2PossibleSell() {
		MonitorstockVO monitorStockVO = StairTestDataGenerator
				.prepareMonitorstockVO(0.04, 4);
		cleanTestingData();
		prepareDataWith2Stair();
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setCurrentPrice(8.5);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new StairBeforeOpenStrategy().processStockTrade(monitorStockVO);
		List stockTradeList = StocktradeDAO.getHoldStocks("601118",
				StairConstants.STAIR_STRATEGY_NAME);
		for (int i = 0; i < stockTradeList.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(i);
			if (i == stockTradeList.size() - 1) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
					fail("something wrong");
				}

			} else if (i == stockTradeList.size() - 2) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
					fail("something wrong");
				}

			} else if (i == stockTradeList.size() - 3) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
					fail("something wrong");
				}
			} else {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
					fail("something wrong");
				}
			}
		}
	}

	@org.junit.Test
	public void test6StairsWith3PossibleBuyAnd2PossibleSell() {
		MonitorstockVO monitorStockVO = StairTestDataGenerator
				.prepareMonitorstockVO(0.03, 6);
		cleanTestingData();
		prepareDataWith2Stair();
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setCurrentPrice(8.5);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new StairBeforeOpenStrategy().processStockTrade(monitorStockVO);
		List stockTradeList = StocktradeDAO.getHoldStocks("601118",
				StairConstants.STAIR_STRATEGY_NAME);
		if (stockTradeList.size() != 5) {
			fail("something wrong");
		}
		for (int i = 0; i < stockTradeList.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(i);
			if (i == stockTradeList.size() - 1
					|| i == stockTradeList.size() - 2
					|| i == stockTradeList.size() - 3) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
					fail("something wrong");
				}
			} else {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
					fail("something wrong");
				}
			}
		}
	}

	@org.junit.Test
	public void test4StairsWith3PossibleBuyAnd2PossibleSell() {
		MonitorstockVO monitorStockVO = StairTestDataGenerator
				.prepareMonitorstockVO(0.03, 4);
		cleanTestingData();
		prepareDataWith2Stair();
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setCurrentPrice(8.5);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new StairBeforeOpenStrategy().processStockTrade(monitorStockVO);
		List stockTradeList = StocktradeDAO.getHoldStocks("601118",
				StairConstants.STAIR_STRATEGY_NAME);
		if (stockTradeList.size() != 4) {
			fail("something wrong");
		}
		for (int i = 0; i < stockTradeList.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(i);
			if (i == stockTradeList.size() - 1
					|| i == stockTradeList.size() - 2) {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
					fail("something wrong");
				}
			} else {
				if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
					fail("something wrong");
				}
			}
		}
	}

	private MonitorstockVO prepareMonitorstockVO() {
		return StairTestDataGenerator.prepareMonitorstockVO(0.05, 4);
	}

	private void cleanTestingData() {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setStockid("601118");
		stocktradeVO1.setWhereStockid(true);
		GhlhDAO.remove(stocktradeVO1);
	}

	private void prepareDataWith4Stair() {
		prepareDataWith3Stair();
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(IDGenerator.generateId("stocktrade"));
		stocktradeVO1.setStockid("601118");
		stocktradeVO1.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setBuybaseprice(7.6);
		stocktradeVO1.setBuyprice(7.6);
		stocktradeVO1.setNumber(3200);
		stocktradeVO1.setSellprice(8);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);

	}

	private void prepareDataWith3Stair() {
		prepareDataWith2Stair();
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(IDGenerator.generateId("stocktrade"));
		stocktradeVO1.setStockid("601118");
		stocktradeVO1.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setBuybaseprice(8);
		stocktradeVO1.setBuyprice(8);
		stocktradeVO1.setNumber(3100);
		stocktradeVO1.setSellprice(8.42);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);

	}

	private void prepareDataWith2Stair() {
		prepareDataWith1Stair();
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(IDGenerator.generateId("stocktrade"));
		stocktradeVO1.setStockid("601118");
		stocktradeVO1.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setBuybaseprice(8.42);
		stocktradeVO1.setBuyprice(8.42);
		stocktradeVO1.setNumber(2900);
		stocktradeVO1.setSellprice(8.86);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
	}

	private void prepareDataWith1Stair() {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(IDGenerator.generateId("stocktrade"));
		stocktradeVO1.setStockid("601118");
		stocktradeVO1.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setBuybaseprice(8.86);
		stocktradeVO1.setBuyprice(8.86);
		stocktradeVO1.setNumber(2800);
		stocktradeVO1.setSellprice(9.3);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
	}
}
