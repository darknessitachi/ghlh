package com.ghlh.strategy.once;

import static org.junit.Assert.fail;

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
import com.ghlh.strategy.stair.StairTestDataGenerator;

public class OnceBeforeOpenStrategyTest {
	
	static {
		ConfigurationAccessor.getInstance().setOpenSoftwareTrade(false);
	}
	
	@Test
	public void testProcessStockTrade() {
		cleanTestingData();
		MonitorstockVO monitorstockVO = prepareMonitorstockVO();
		OnceBeforeOpenStrategy onceBeforeOpenStrategy = new OnceBeforeOpenStrategy();
		onceBeforeOpenStrategy.processStockTrade(monitorstockVO);
		List stockTradeList = StocktradeDAO.getUnfinishedTradeRecords("601118",
				OnceConstants.ONCE_STRATEGY_NAME);
		if (stockTradeList.size() != 1) {
			fail("Something wong");
		} else {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(0);
			if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
				fail("Something wrong");
			}
		}
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setCurrentPrice(9);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);

		onceBeforeOpenStrategy.processStockTrade(monitorstockVO);
		stockTradeList = StocktradeDAO.getUnfinishedTradeRecords("601118",
				OnceConstants.ONCE_STRATEGY_NAME);
		if (stockTradeList.size() != 1) {
			fail("Something wong");
		} else {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(0);
			if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
				fail("Something wrong");
			}
		}
		sqb.setCurrentPrice(10.2);
		onceBeforeOpenStrategy.processStockTrade(monitorstockVO);

		stockTradeList = StocktradeDAO.getUnfinishedTradeRecords("601118",
				OnceConstants.ONCE_STRATEGY_NAME);
		if (stockTradeList.size() != 1) {
			fail("Something wong");
		} else {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(0);
			if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
				fail("Something wrong");
			}
		}

	}

	private void cleanTestingData() {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setStockid("601118");
		stocktradeVO1.setWhereStockid(true);
		GhlhDAO.remove(stocktradeVO1);
	}

	public static StockQuotesBean getMockStockQuotestBean() {
		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setStockId("601118");
		sqb.setName("海南橡胶");
		sqb.setCurrentPrice(7);
		return sqb;
	}

	public static MonitorstockVO prepareMonitorstockVO() {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(1);
		monitorstockVO.setStockid("601118");
		monitorstockVO.setName("海南橡胶");
		monitorstockVO.setTradealgorithm("Once");
		monitorstockVO.setAdditioninfo("设定价,10.0,0.05,10000.0,0.0");
		monitorstockVO.setOnmonitoring("true");
		return monitorstockVO;
	}
}
