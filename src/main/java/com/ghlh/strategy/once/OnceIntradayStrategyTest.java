package com.ghlh.strategy.once;

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

public class OnceIntradayStrategyTest {

	static {
		ConfigurationAccessor.getInstance().setOpenSoftwareTrade(false);
	}


	private void cleanTestingData600036Stair() {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setStockid("601118");
		stocktradeVO1.setWhereStockid(true);
		stocktradeVO1.setTradealgorithm(OnceConstants.ONCE_STRATEGY_NAME);
		stocktradeVO1.setWhereTradealgorithm(true);

		GhlhDAO.remove(stocktradeVO1);
	}
	
	public static MonitorstockVO getMonitorstockVO600036Stair() {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(1);
		monitorstockVO.setStockid("601118");
		monitorstockVO.setName("海南橡胶");
		monitorstockVO.setTradealgorithm(OnceConstants.ONCE_STRATEGY_NAME);
		monitorstockVO.setAdditioninfo("设定价,8.8,0.05,20000.0,0.02");
		monitorstockVO.setOnmonitoring("true");
		return monitorstockVO;
	}
	
	@Test
	public void testProcessBuy() {
		cleanTestingData600036Stair();
		MonitorstockVO msv601118 = this.getMonitorstockVO600036Stair();
		List pendingBuy = this.get601118OncePendingBuy();
		List possibleSell = new ArrayList();
		StockTradeIntradyMonitor stim = new StockTradeIntradyMonitor(msv601118,
				possibleSell, pendingBuy);
		StockQuotesBean sqb = this.getMockStockQuotestBean601118();
		OnceIntradayStrategy ois = new OnceIntradayStrategy();
		ois.processBuy(stim, sqb);
		
		List result = StocktradeDAO.getT_0_TradeRecords("601118",
				OnceConstants.ONCE_STRATEGY_NAME);
		if (result.size() != 1) {
			fail("Something wrong");
		}
		StocktradeVO stVO = (StocktradeVO) result.get(0);
		if (stVO.getStatus() != TradeConstants.STATUS_T_0_BUY) {
			fail("Something wrong");
		}
	}
	
	@Test
	public void testProcessSellWin() {
		cleanTestingData600036Stair();
		MonitorstockVO msv601118 = this.getMonitorstockVO600036Stair();
		List pendingBuy = new ArrayList();
		List possibleSell = this.get601118OncePossibleSell();
		StockTradeIntradyMonitor stim = new StockTradeIntradyMonitor(msv601118,
				possibleSell, pendingBuy);
		StockQuotesBean sqb = this.getMockStockQuotestBean601118();
		sqb.setHighestPrice(9.25);//Sell means highest price.
		sqb.setLowestPrice(9.23);
		OnceIntradayStrategy ois = new OnceIntradayStrategy();
		ois.processSell(stim, sqb);
		
		List result = StocktradeDAO.getSuccessfulTradeRecords("601118",
				OnceConstants.ONCE_STRATEGY_NAME);
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
		cleanTestingData600036Stair();
		MonitorstockVO msv601118 = this.getMonitorstockVO600036Stair();
		List pendingBuy = new ArrayList();
		List possibleSell = this.get601118OncePossibleSell();
		StockTradeIntradyMonitor stim = new StockTradeIntradyMonitor(msv601118,
				possibleSell, pendingBuy);
		StockQuotesBean sqb = this.getMockStockQuotestBean601118();
		sqb.setCurrentPrice(8.61);
		sqb.setHighestPrice(8.8);//Sell according lowestPrice
		sqb.setLowestPrice(8.61);
		OnceIntradayStrategy ois = new OnceIntradayStrategy();
		ois.processSell(stim, sqb);
		
		List result = StocktradeDAO.getFailedTradeRecords("601118",
				OnceConstants.ONCE_STRATEGY_NAME);
		if (result.size() != 1) {
			fail("Something wrong");
		}
		StocktradeVO stVO = (StocktradeVO) result.get(0);
		if (stVO.getStatus() != TradeConstants.STATUS_FAILURE) {
			fail("Something wrong");
		}
	}

	
	public static StockQuotesBean getMockStockQuotestBean601118() {
		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setStockId("601118");
		sqb.setName("海南橡胶");
		sqb.setCurrentPrice(8.7);
		sqb.setTodayOpen(8.9);
		sqb.setYesterdayClose(8.91);
		return sqb;
	}

	public static List get601118OncePendingBuy() {
		List result = new ArrayList();
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(5);
		stocktradeVO1.setStockid("601118");
		stocktradeVO1.setTradealgorithm(OnceConstants.ONCE_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_PENDING_BUY);
		stocktradeVO1.setBuybaseprice(8.8);
		stocktradeVO1.setBuyprice(8.8);
		stocktradeVO1.setNumber(1100);
		stocktradeVO1.setWinsellprice(9.24);
		stocktradeVO1.setLostsellprice(8.62);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		result.add(stocktradeVO1);
		return result;
	}
	
	
	public static List get601118OncePossibleSell() {
		List result = new ArrayList();
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(5);
		stocktradeVO1.setStockid("601118");
		stocktradeVO1.setTradealgorithm(OnceConstants.ONCE_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_POSSIBLE_SELL);
		stocktradeVO1.setBuybaseprice(8.8);
		stocktradeVO1.setBuyprice(8.8);
		stocktradeVO1.setNumber(1100);
		stocktradeVO1.setWinsellprice(9.24);
		stocktradeVO1.setLostsellprice(8.62);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		result.add(stocktradeVO1);
		return result;
	}

}
