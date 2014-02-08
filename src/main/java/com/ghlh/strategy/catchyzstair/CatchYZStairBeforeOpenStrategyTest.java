package com.ghlh.strategy.catchyzstair;

import static org.junit.Assert.fail;

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
import com.ghlh.strategy.once.OnceConstants;

public class CatchYZStairBeforeOpenStrategyTest {

	@Test
	public void testProcessStockTrade() {
		cleanTestingData();
		MonitorstockVO msv = prepareMonitorstockVO();
		StockQuotesBean sqb = getMockStockQuotestBean();

		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new CatchYZStairBeforeOpenStrategy().processStockTrade(msv);
		List stockTradeList = StocktradeDAO.getUnfinishedTradeRecords("002354",
				CatchYZStairConstants.CATCHYZSTAIR_STRATEGY_NAME);
		if (stockTradeList.size() != 1) {
			fail("Something wong");
		} else {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(0);
			if (stocktradeVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
				fail("Something wrong");
			}
		}
		cleanTestingData();
		prepareStockTradeData();
		sqb = getMockStockQuotestBean();
		sqb.setCurrentPrice(49.9);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new CatchYZStairBeforeOpenStrategy().processStockTrade(msv);
		stockTradeList = StocktradeDAO.getUnfinishedTradeRecords("002354",
				CatchYZStairConstants.CATCHYZSTAIR_STRATEGY_NAME);
		if (stockTradeList.size() != 1) {
			fail("Something wong");
		} else {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(0);
			if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
				fail("Something wrong");
			}
		}
		
		cleanTestingData();
		prepareStockTradeData();
		sqb = getMockStockQuotestBean();
		sqb.setCurrentPrice(46);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);
		new CatchYZStairBeforeOpenStrategy().processStockTrade(msv);
		stockTradeList = StocktradeDAO.getUnfinishedTradeRecords("002354",
				CatchYZStairConstants.CATCHYZSTAIR_STRATEGY_NAME);
		if (stockTradeList.size() != 1) {
			fail("Something wong");
		} else {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(0);
			if (stocktradeVO.getStatus() != TradeConstants.STATUS_POSSIBLE_SELL) {
				fail("Something wrong");
			}
		}
		
		
		
	}

	private void prepareStockTradeData() {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(IDGenerator.generateId("stocktrade"));
		stocktradeVO1.setStockid("002354");
		stocktradeVO1.setTradealgorithm(CatchYZStairConstants.CATCHYZSTAIR_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_SUCCESS);
		stocktradeVO1.setBuybaseprice(46.83);
		stocktradeVO1.setBuyprice(46.83);
		stocktradeVO1.setNumber(400);
		stocktradeVO1.setWinsellprice(53.85);
		stocktradeVO1.setLostsellprice(42.14);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		int id = stocktradeVO1.getId();
	}

	private void cleanTestingData() {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setStockid("002354");
		stocktradeVO1.setWhereStockid(true);
		GhlhDAO.remove(stocktradeVO1);
	}

	public static StockQuotesBean getMockStockQuotestBean() {
		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setStockId("002354");
		sqb.setName("科冕木业");
		sqb.setCurrentPrice(45.02);
		return sqb;
	}

	public static MonitorstockVO prepareMonitorstockVO() {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(1);
		monitorstockVO.setStockid("002354");
		monitorstockVO.setName("科冕木业");
		monitorstockVO.setTradealgorithm("CatchYZStair");
		monitorstockVO.setAdditioninfo("0.15,20000.0,0.04,0.1");
		monitorstockVO.setOnmonitoring("true");
		return monitorstockVO;
	}

}
