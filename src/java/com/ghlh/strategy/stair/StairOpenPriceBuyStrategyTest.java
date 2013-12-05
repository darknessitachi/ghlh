package com.ghlh.strategy.stair;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.TradeConstants;

public class StairOpenPriceBuyStrategyTest {

	@Test
	public void testProcessStockTrade() {
		cleanTestingData();
		MonitorstockVO monitorstockVO = prepareMonitorstockVO();
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setYesterdayClose(8.9);
		sqb.setTodayOpen(9.1);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		StairOpenPriceBuyStrategy stairJustOpenStrategy = new StairOpenPriceBuyStrategy();
		stairJustOpenStrategy.processStockTrade(monitorstockVO);
		List stockTradeList = StocktradeDAO.getUnfinishedTradeRecords("601118",
				StairConstants.STAIR_STRATEGY_NAME);
		if (stockTradeList.size() != 3) {
			fail("Something wong");
		} else {
			for (int i = 0; i < stockTradeList.size(); i++) {
				StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList
						.get(i);
				if (i == 0) {
					if (stocktradeVO.getStatus() != TradeConstants.STATUS_HOLDING) {
						fail("Something wrong");
					}
				} else {
					if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
						fail("Something wrong");
					}
				}
			}
		}
		cleanTestingData();
		sqb.setYesterdayClose(9.3);
		stairJustOpenStrategy.processStockTrade(monitorstockVO);
		stockTradeList = StocktradeDAO.getUnfinishedTradeRecords("601118",
				StairConstants.STAIR_STRATEGY_NAME);
		if (stockTradeList.size() != 2) {
			fail("Something wong");
		} else {
			for (int i = 0; i < stockTradeList.size(); i++) {
				StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList
						.get(i);
				if (i == 0) {
					if (stocktradeVO.getStatus() != TradeConstants.STATUS_HOLDING) {
						fail("Something wrong");
					}
				} else {
					if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
						fail("Something wrong");
					}
				}
			}
		}

	}

	public static MonitorstockVO prepareMonitorstockVO() {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(1);
		monitorstockVO.setStockid("601118");
		monitorstockVO.setName("海南橡胶");
		monitorstockVO.setTradealgorithm("Stair");
		monitorstockVO.setAdditioninfo("25000.0,4,0.05,开盘价,25.0");
		monitorstockVO.setOnmonitoring("true");
		return monitorstockVO;
	}

	private void cleanTestingData() {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setStockid("601118");
		stocktradeVO1.setWhereStockid(true);
		GhlhDAO.remove(stocktradeVO1);
	}

}
