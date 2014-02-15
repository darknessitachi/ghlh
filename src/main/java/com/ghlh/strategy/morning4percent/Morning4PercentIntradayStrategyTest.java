package com.ghlh.strategy.morning4percent;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.ghlh.autotrade.StockTradeIntradyMonitor;
import com.ghlh.conf.ConfigurationAccessor;
import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.TradeConstants;
import com.ghlh.strategy.stair.StairConstants;

public class Morning4PercentIntradayStrategyTest {

	static {
		ConfigurationAccessor.getInstance().setOpenSoftwareTrade(false);
	}

	private void cleanTestingData002709() {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setStockid("002709");
		stocktradeVO1.setWhereStockid(true);
		stocktradeVO1
				.setTradealgorithm(Morning4PercentConstants.MORNING4PERCENT_STRATEGY_NAME);
		stocktradeVO1.setWhereTradealgorithm(true);
		GhlhDAO.remove(stocktradeVO1);
	}

	public static MonitorstockVO prepareMonitorstockVO() {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(20);
		monitorstockVO.setStockid("002709");
		monitorstockVO.setName("天赐材料");
		monitorstockVO.setTradealgorithm("Morning4Percent");
		monitorstockVO.setAdditioninfo("0.0,20000.0,0.0");
		monitorstockVO.setOnmonitoring("false");
		return monitorstockVO;
	}

	public static StockQuotesBean getMockStockQuotestBean() {
		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setStockId("002709");
		sqb.setName("天赐材料");
		sqb.setCurrentPrice(34.9);
		return sqb;
	}

	public static List get002709Morning4PercentPossibleBuyOpenPrice() {
		List result = new ArrayList();
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(23);
		stocktradeVO1.setStockid("002709");
		stocktradeVO1
				.setTradealgorithm(Morning4PercentConstants.MORNING4PERCENT_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_POSSIBLE_SELL);
		stocktradeVO1.setBuybaseprice(33.9);
		stocktradeVO1.setBuyprice(33.9);
		stocktradeVO1.setNumber(500);
		stocktradeVO1.setWinsellprice(0);
		stocktradeVO1.setLostsellprice(0);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		result.add(stocktradeVO1);
		return result;
	}

	public static List get002709Morning4PercentPossibleBuyWithTargetPrice() {
		List result = new ArrayList();
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(23);
		stocktradeVO1.setStockid("002709");
		stocktradeVO1
				.setTradealgorithm(Morning4PercentConstants.MORNING4PERCENT_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_POSSIBLE_SELL);
		stocktradeVO1.setBuybaseprice(33.9);
		stocktradeVO1.setBuyprice(33.9);
		stocktradeVO1.setNumber(500);
		stocktradeVO1.setWinsellprice(37.28);
		stocktradeVO1.setLostsellprice(30.5);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		result.add(stocktradeVO1);
		return result;
	}

	@Test
	public void testProcessSellWinWithOpenPrice() {
		cleanTestingData002709();
		MonitorstockVO msv002709 = this.prepareMonitorstockVO();
		List pendingBuy = new ArrayList();
		List possibleSell = this.get002709Morning4PercentPossibleBuyOpenPrice();
		StockTradeIntradyMonitor stim = new StockTradeIntradyMonitor(msv002709,
				possibleSell, pendingBuy);
		StockQuotesBean sqb = this.getMockStockQuotestBean();
		sqb.setHighestPrice(34.9);// Sell means highest price.
		sqb.setLowestPrice(34.3);
		sqb.setCurrentPrice(34.5);
		Morning4PercentIntradayStrategy mis = new Morning4PercentIntradayStrategy();
		mis.processSell(stim, sqb);

		List result = StocktradeDAO.getSuccessfulTradeRecords("002709",
				Morning4PercentConstants.MORNING4PERCENT_STRATEGY_NAME);
		if (result.size() != 1) {
			fail("Something wrong");
		}
		StocktradeVO stVO = (StocktradeVO) result.get(0);
		if (stVO.getStatus() != TradeConstants.STATUS_SUCCESS) {
			fail("Something wrong");
		}
		if (stVO.getWinsellprice() != 34.5) {
			fail("Something wrong");
		}
	}

	@Test
	public void testProcessSellLoseWithOpenPrice() {
		cleanTestingData002709();
		MonitorstockVO msv002709 = this.prepareMonitorstockVO();
		List pendingBuy = new ArrayList();
		List possibleSell = this.get002709Morning4PercentPossibleBuyOpenPrice();
		StockTradeIntradyMonitor stim = new StockTradeIntradyMonitor(msv002709,
				possibleSell, pendingBuy);
		StockQuotesBean sqb = this.getMockStockQuotestBean();
		sqb.setHighestPrice(32.9);// Sell means highest price.
		sqb.setLowestPrice(32.9);
		sqb.setCurrentPrice(32.9);
		Morning4PercentIntradayStrategy mis = new Morning4PercentIntradayStrategy();
		mis.processSell(stim, sqb);

		List result = StocktradeDAO.getFailedTradeRecords("002709",
				Morning4PercentConstants.MORNING4PERCENT_STRATEGY_NAME);
		if (result.size() != 1) {
			fail("Something wrong");
		}
		StocktradeVO stVO = (StocktradeVO) result.get(0);
		if (stVO.getStatus() != TradeConstants.STATUS_FAILURE) {
			fail("Something wrong");
		}
		if (stVO.getLostsellprice() != 32.9) {
			fail("Something wrong");
		}
	}

	@Test
	public void testProcessSellWinWithTargetZF() {
		cleanTestingData002709();
		MonitorstockVO msv002709 = this.prepareMonitorstockVO();
		msv002709.setAdditioninfo("0.1,20000.0,0.1");
		List pendingBuy = new ArrayList();
		List possibleSell = this
				.get002709Morning4PercentPossibleBuyWithTargetPrice();
		StockTradeIntradyMonitor stim = new StockTradeIntradyMonitor(msv002709,
				possibleSell, pendingBuy);
		StockQuotesBean sqb = this.getMockStockQuotestBean();
		sqb.setHighestPrice(38);// Sell means highest price.
		sqb.setLowestPrice(38);
		sqb.setCurrentPrice(38);
		Morning4PercentIntradayStrategy mis = new Morning4PercentIntradayStrategy();
		mis.processSell(stim, sqb);

		List result = StocktradeDAO.getSuccessfulTradeRecords("002709",
				Morning4PercentConstants.MORNING4PERCENT_STRATEGY_NAME);
		if (result.size() != 1) {
			fail("Something wrong");
		}
		StocktradeVO stVO = (StocktradeVO) result.get(0);
		if (stVO.getStatus() != TradeConstants.STATUS_SUCCESS) {
			fail("Something wrong");
		}
	}

	@Test
	public void testProcessSellLoseWithTargetZF() {
		cleanTestingData002709();
		MonitorstockVO msv002709 = this.prepareMonitorstockVO();
		msv002709.setAdditioninfo("0.1,20000.0,0.1");
		List pendingBuy = new ArrayList();
		List possibleSell = this
				.get002709Morning4PercentPossibleBuyWithTargetPrice();
		StockTradeIntradyMonitor stim = new StockTradeIntradyMonitor(msv002709,
				possibleSell, pendingBuy);
		StockQuotesBean sqb = this.getMockStockQuotestBean();
		sqb.setHighestPrice(30);// Sell means highest price.
		sqb.setLowestPrice(30);
		sqb.setCurrentPrice(30);
		Morning4PercentIntradayStrategy mis = new Morning4PercentIntradayStrategy();
		mis.processSell(stim, sqb);

		List result = StocktradeDAO.getFailedTradeRecords("002709",
				Morning4PercentConstants.MORNING4PERCENT_STRATEGY_NAME);
		if (result.size() != 1) {
			fail("Something wrong");
		}
		StocktradeVO stVO = (StocktradeVO) result.get(0);
		if (stVO.getStatus() != TradeConstants.STATUS_FAILURE) {
			fail("Something wrong");
		}
	}

}
