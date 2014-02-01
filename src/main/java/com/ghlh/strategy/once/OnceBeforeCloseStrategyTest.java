package com.ghlh.strategy.once;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.common.util.IDGenerator;
import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.TradeConstants;
import com.ghlh.strategy.stair.StairTestDataGenerator;

public class OnceBeforeCloseStrategyTest {

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
		monitorstockVO.setAdditioninfo("设定价,10.0,0.05,10000.0,0.02");
		monitorstockVO.setOnmonitoring("true");
		return monitorstockVO;
	}

	private void prepareStockTradeData() {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(IDGenerator.generateId("stocktrade"));
		stocktradeVO1.setStockid("601118");
		stocktradeVO1.setTradealgorithm(OnceConstants.ONCE_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_SUCCESS);
		stocktradeVO1.setBuybaseprice(7.6);
		stocktradeVO1.setBuyprice(7.6);
		stocktradeVO1.setNumber(3200);
		stocktradeVO1.setWinsellprice(8);
		stocktradeVO1.setSelldate(new Date());
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		int id = stocktradeVO1.getId();

		stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(IDGenerator.generateId("stocktrade"));
		stocktradeVO1.setStockid("601118");
		stocktradeVO1.setTradealgorithm(OnceConstants.ONCE_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_PENDING_BUY);
		stocktradeVO1.setBuybaseprice(7.6);
		stocktradeVO1.setBuyprice(7.6);
		stocktradeVO1.setNumber(3200);
		stocktradeVO1.setWinsellprice(8);
		stocktradeVO1.setPrevioustradeid(id);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);

	}

	@Test
	public void testProcessStockTrade() {
		cleanTestingData();
		prepareStockTradeData();
		MonitorstockVO monitorstockVO = prepareMonitorstockVO();
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setCurrentPrice(8);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new OnceBeforeCloseStrategy().processStockTrade(monitorstockVO);
		List t_0_list = StocktradeDAO
				.getT_0_TradeRecords(monitorstockVO.getStockid(),
						monitorstockVO.getTradealgorithm());
		assertTrue(t_0_list.size() == 0);

		sqb.setCurrentPrice(7.8);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new OnceBeforeCloseStrategy().processStockTrade(monitorstockVO);

		t_0_list = StocktradeDAO
				.getT_0_TradeRecords(monitorstockVO.getStockid(),
						monitorstockVO.getTradealgorithm());
		assertTrue(t_0_list.size() == 1);
		StocktradeVO stocktradeVO = (StocktradeVO) t_0_list.get(0);
		if (stocktradeVO.getStatus() != TradeConstants.STATUS_T_0_BUY) {
			fail("Not yet implemented");
		}
	}

}
