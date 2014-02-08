package com.ghlh.strategy.catchyzstair;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.common.util.IDGenerator;
import com.ghlh.autotrade.StockTradeIntradyMonitor;
import com.ghlh.conf.ConfigurationAccessor;
import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.TradeConstants;
import com.ghlh.strategy.once.OnceConstants;
import com.ghlh.strategy.once.OnceIntradayStrategy;

public class CatchYZStairIntradayStrategyTest {

	static {
		ConfigurationAccessor.getInstance().setOpenSoftwareTrade(false);
	}	
	
	@Test
	public void testProcessSellWin() {
		cleanTestingData();
		MonitorstockVO msv002354 = this.prepareMonitorstockVO();
		msv002354.setOnmonitoring("false");
		List pendingBuy = new ArrayList();
		List possibleSell = this.preparePossibleSellStockTradeData();
		StockTradeIntradyMonitor stim = new StockTradeIntradyMonitor(msv002354,
				possibleSell, pendingBuy);
		StockQuotesBean sqb = this.getMockStockQuotestBean();
		sqb.setHighestPrice(53.9);//Sell means highest price.
		sqb.setLowestPrice(52.8);
		CatchYZStairIntradayStrategy ois = new CatchYZStairIntradayStrategy();
		ois.processSell(stim, sqb);
		
		List result = StocktradeDAO.getSuccessfulTradeRecords("002354",
				CatchYZStairConstants.CATCHYZSTAIR_STRATEGY_NAME);
		if (result.size() != 1) {
			fail("Something wrong");
		}
		StocktradeVO stVO = (StocktradeVO) result.get(0);
		if (stVO.getStatus() != TradeConstants.STATUS_SUCCESS) {
			fail("Something wrong");
		}
	}
	

	@Test
	public void testProcessSellLost() {
		cleanTestingData();
		MonitorstockVO msv002354 = this.prepareMonitorstockVO();
		List pendingBuy = new ArrayList();
		List possibleSell = this.preparePossibleSellStockTradeData();
		StockTradeIntradyMonitor stim = new StockTradeIntradyMonitor(msv002354,
				possibleSell, pendingBuy);
		StockQuotesBean sqb = this.getMockStockQuotestBean();
		sqb.setHighestPrice(44.9);//Sell means highest price.
		sqb.setLowestPrice(42.1);
		CatchYZStairIntradayStrategy ois = new CatchYZStairIntradayStrategy();
		ois.processSell(stim, sqb);
		
		List result = StocktradeDAO.getFailedTradeRecords("002354",
				CatchYZStairConstants.CATCHYZSTAIR_STRATEGY_NAME);
		if (result.size() != 1) {
			fail("Something wrong");
		}
		StocktradeVO stVO = (StocktradeVO) result.get(0);
		if (stVO.getStatus() != TradeConstants.STATUS_FAILURE) {
			fail("Something wrong");
		}
	}

	
	
	@Test
	public void testProcessBuy() {
		cleanTestingData();
		MonitorstockVO msv002354 = this.prepareMonitorstockVO();
		List pendingBuy = this.preparePendingBuyStockTradeData();
		List possibleSell = new ArrayList();
		StockTradeIntradyMonitor stim = new StockTradeIntradyMonitor(msv002354,
				possibleSell, pendingBuy);
		StockQuotesBean sqb = this.getMockStockQuotestBean();
		sqb.setCurrentPrice(46.83);
		CatchYZStairIntradayStrategy ois = new CatchYZStairIntradayStrategy();
		ois.processBuy(stim, sqb);
		
		List result = StocktradeDAO.getT_0_TradeRecords("002354",
				CatchYZStairConstants.CATCHYZSTAIR_STRATEGY_NAME);
		if (result.size() != 1) {
			fail("Something wrong");
		}
		StocktradeVO stVO = (StocktradeVO) result.get(0);
		if (stVO.getStatus() != TradeConstants.STATUS_T_0_BUY) {
			fail("Something wrong");
		}

	}
	
	private List preparePendingBuyStockTradeData() {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(IDGenerator.generateId("stocktrade"));
		stocktradeVO1.setStockid("002354");
		stocktradeVO1.setTradealgorithm(CatchYZStairConstants.CATCHYZSTAIR_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_PENDING_BUY);
		stocktradeVO1.setBuybaseprice(46.83);
		stocktradeVO1.setBuyprice(46.83);
		stocktradeVO1.setNumber(400);
		stocktradeVO1.setWinsellprice(53.85);
		stocktradeVO1.setLostsellprice(42.14);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		int id = stocktradeVO1.getId();
		List result = new ArrayList();
		result.add(stocktradeVO1);
		return result;
	}

	private List preparePossibleSellStockTradeData() {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(IDGenerator.generateId("stocktrade"));
		stocktradeVO1.setStockid("002354");
		stocktradeVO1.setTradealgorithm(CatchYZStairConstants.CATCHYZSTAIR_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_POSSIBLE_SELL);
		stocktradeVO1.setBuybaseprice(46.83);
		stocktradeVO1.setBuyprice(46.83);
		stocktradeVO1.setNumber(400);
		stocktradeVO1.setWinsellprice(53.85);
		stocktradeVO1.setLostsellprice(42.14);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		int id = stocktradeVO1.getId();
		List result = new ArrayList();
		result.add(stocktradeVO1);
		return result;
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
		monitorstockVO.setId(5);
		monitorstockVO.setStockid("002354");
		monitorstockVO.setName("科冕木业");
		monitorstockVO.setTradealgorithm("CatchYZStair");
		monitorstockVO.setAdditioninfo("0.15,20000.0,0.04,0.1");
		monitorstockVO.setOnmonitoring("true");
		return monitorstockVO;
	}

}
