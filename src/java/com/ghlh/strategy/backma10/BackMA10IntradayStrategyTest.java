package com.ghlh.strategy.backma10;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ghlh.autotrade.StockTradeIntradyMonitor;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.stockquotes.StockQuotesBean;

public class BackMA10IntradayStrategyTest {

	@Test
	public void testProcessSell() {

		fail("Not yet implemented");
	}

	@Test
	public void testProcessBuy() {
		MonitorstockVO monitorstockVO = prepareMonitorstockVO();
		StockQuotesBean sqb = this.getMockStockQuotestBean();
		List pendingBuy = new ArrayList();
		List possibleSell = new ArrayList();
		StockTradeIntradyMonitor monitor = new StockTradeIntradyMonitor(
				monitorstockVO, possibleSell, pendingBuy);
		BackMA10IntradayStrategy strategy = new BackMA10IntradayStrategy();
		strategy.processBuy(monitor, sqb);
		

	}

	public static StockQuotesBean getMockStockQuotestBean() {
		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setStockId("002199");
		sqb.setName("东晶电子");
		sqb.setCurrentPrice(14.1);
		return sqb;
	}

	public static MonitorstockVO prepareMonitorstockVO() {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(1);
		monitorstockVO.setStockid("002199");
		monitorstockVO.setName("东晶电子");
		monitorstockVO.setTradealgorithm("BackMA10");
		monitorstockVO.setAdditioninfo("0.05,20000.0");
		monitorstockVO.setOnmonitoring("true");
		return monitorstockVO;
	}
}
