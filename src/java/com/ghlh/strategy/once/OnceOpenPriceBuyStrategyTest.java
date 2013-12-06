package com.ghlh.strategy.once;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.TradeConstants;
import com.ghlh.strategy.stair.StairTestDataGenerator;
import com.ghlh.util.StockMarketUtil;

public class OnceOpenPriceBuyStrategyTest {

	private void cleanTestingData() {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setStockid("601118");
		stocktradeVO1.setWhereStockid(true);
		GhlhDAO.remove(stocktradeVO1);
	}

	@Test
	public void testProcessStockTradeForOpenPrice() {
		cleanTestingData();
		MonitorstockVO monitorstockVO = prepareMonitorstockVO();
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setCurrentPrice(9);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		OnceOpenPriceBuyStrategy onceJustOpenStrategy = new OnceOpenPriceBuyStrategy();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 9);

		StockMarketUtil.setTestCalendar(calendar);
		onceJustOpenStrategy.processStockTrade(monitorstockVO);
		List stockTradeList = StocktradeDAO.getUnfinishedTradeRecords("601118",
				OnceConstants.ONCE_STRATEGY_NAME);
		if (stockTradeList.size() != 1) {
			fail("Something wong");
		} else {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(0);
			if (stocktradeVO.getStatus() != TradeConstants.STATUS_HOLDING) {
				fail("Something wrong");
			}
		}
	}
	
	@Test
	public void testProcessStockTradeForNoonPrice() {
		cleanTestingData();
		MonitorstockVO monitorstockVO = prepareMonitorstockVOForNoon();
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setCurrentPrice(9);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		OnceOpenPriceBuyStrategy onceJustOpenStrategy = new OnceOpenPriceBuyStrategy();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 13);

		StockMarketUtil.setTestCalendar(calendar);
		onceJustOpenStrategy.processStockTrade(monitorstockVO);
		List stockTradeList = StocktradeDAO.getUnfinishedTradeRecords("601118",
				OnceConstants.ONCE_STRATEGY_NAME);
		if (stockTradeList.size() != 1) {
			fail("Something wong");
		} else {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(0);
			if (stocktradeVO.getStatus() != TradeConstants.STATUS_HOLDING) {
				fail("Something wrong");
			}
		}
	}

	public static MonitorstockVO prepareMonitorstockVO() {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(1);
		monitorstockVO.setStockid("601118");
		monitorstockVO.setName("海南橡胶");
		monitorstockVO.setTradealgorithm("Once");
		monitorstockVO.setAdditioninfo("开盘价,11.0,0.05,10000.0");
		monitorstockVO.setOnmonitoring("true");
		return monitorstockVO;
	}
	
	public static MonitorstockVO prepareMonitorstockVOForNoon() {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(1);
		monitorstockVO.setStockid("601118");
		monitorstockVO.setName("海南橡胶");
		monitorstockVO.setTradealgorithm("Once");
		monitorstockVO.setAdditioninfo("午盘价,11.0,0.05,10000.0");
		monitorstockVO.setOnmonitoring("true");
		return monitorstockVO;
	}
}
