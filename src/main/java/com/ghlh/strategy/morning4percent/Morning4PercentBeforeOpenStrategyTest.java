package com.ghlh.strategy.morning4percent;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.ghlh.conf.ConfigurationAccessor;
import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.TradeConstants;
import com.ghlh.strategy.once.OnceConstants;
import com.ghlh.strategy.stair.StairTestDataGenerator;

public class Morning4PercentBeforeOpenStrategyTest {

	static {
		ConfigurationAccessor.getInstance().setOpenSoftwareTrade(false);
	}

	@Test
	public void testProcessStockTrade() {
		cleanTestingData();
		MonitorstockVO monitorstockVO = prepareMonitorstockVO();
		get002709Morning4PercentPendingBuy();
		Morning4PercentBeforeOpenStrategy morning4PercentBeforeOpenStrategy = new Morning4PercentBeforeOpenStrategy();
		morning4PercentBeforeOpenStrategy.processStockTrade(monitorstockVO);
		List stockTradeList = StocktradeDAO.getUnfinishedTradeRecords("002709",
				Morning4PercentConstants.MORNING4PERCENT_STRATEGY_NAME);
		if (stockTradeList.size() != 1) {
			fail("Something wong");
		} else {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(0);
			if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
				fail("Something wrong");
			}
			if (stocktradeVO.getWinsellprice() != 0) {
				fail("Something wrong");
			}

		}
		cleanTestingData();
		monitorstockVO = prepareMonitorstockVO();
		monitorstockVO.setAdditioninfo("0.1,20000.0,0.1");
		get002709Morning4PercentPendingBuy();
		morning4PercentBeforeOpenStrategy = new Morning4PercentBeforeOpenStrategy();
		morning4PercentBeforeOpenStrategy.processStockTrade(monitorstockVO);
		stockTradeList = StocktradeDAO.getUnfinishedTradeRecords("002709",
				Morning4PercentConstants.MORNING4PERCENT_STRATEGY_NAME);
		if (stockTradeList.size() != 1) {
			fail("Something wong");
		} else {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(0);
			if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
				fail("Something wrong");
			}
			if (stocktradeVO.getWinsellprice() == 0) {
				fail("Something wrong");
			}
		}

	}

	private void cleanTestingData() {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setStockid("002709");
		stocktradeVO1.setWhereStockid(true);
		GhlhDAO.remove(stocktradeVO1);
	}

	public static StockQuotesBean getMockStockQuotestBean() {
		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setStockId("002709");
		sqb.setName("天赐材料");
		sqb.setCurrentPrice(33.9);
		return sqb;
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

	public static List get002709Morning4PercentPendingBuy() {
		List result = new ArrayList();
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(23);
		stocktradeVO1.setStockid("002709");
		stocktradeVO1
				.setTradealgorithm(Morning4PercentConstants.MORNING4PERCENT_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_HOLDING);
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

}
