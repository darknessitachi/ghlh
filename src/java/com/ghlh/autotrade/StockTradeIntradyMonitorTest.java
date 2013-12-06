package com.ghlh.autotrade;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.common.util.IDGenerator;
import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.TradeConstants;
import com.ghlh.strategy.once.OnceConstants;
import com.ghlh.strategy.stair.StairConstants;

public class StockTradeIntradyMonitorTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private void cleanTestingData600036Stair() {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setStockid("600036");
		stocktradeVO1.setWhereStockid(true);
		stocktradeVO1.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		stocktradeVO1.setWhereTradealgorithm(true);

		GhlhDAO.remove(stocktradeVO1);
	}

	 @Test
	public void test600036StairProcessBuy() {
		cleanTestingData600036Stair();
		MonitorstockVO msv600036 = this.getMonitorstockVO600036Stair();
		List pendingBuy = this.get600036StairPendingBuy();
		List possibleSell = new ArrayList();
		StockTradeIntradyMonitor stim = new StockTradeIntradyMonitor(msv600036,
				possibleSell, pendingBuy);
		StockQuotesBean sqb = this.getMockStockQuotestBean600036();
		sqb.setCurrentPrice(10.43);
		stim.processBuy(sqb);
		List result = StocktradeDAO.getT_0_TradeRecords("600036",
				StairConstants.STAIR_STRATEGY_NAME);
		if (result.size() != 1) {
			fail("Something wrong");
		}
		StocktradeVO stVO = (StocktradeVO) result.get(0);
		if (stVO.getStatus() != TradeConstants.STATUS_T_0_BUY) {
			fail("Something wrong");
		}
	}

	@Test
	public void test600036StairProcessSell() {
		cleanTestingData600036Stair();
		MonitorstockVO msv600036 = this.getMonitorstockVO600036Stair();
		List pendingBuy = new ArrayList();
		List possibleSell = this.get600036StairPossibleSell();
		StockTradeIntradyMonitor stim = new StockTradeIntradyMonitor(msv600036,
				possibleSell, pendingBuy);
		StockQuotesBean sqb = this.getMockStockQuotestBean600036();

		sqb.setHighestPrice(10.97);
		stim.processSell(sqb);

		List finishedList = StocktradeDAO.getFinishedTradeRecords("600036",
				StairConstants.STAIR_STRATEGY_NAME);
		if (finishedList.size() != 1) {
			fail("Something wrong");
		}
		StocktradeVO stVO = (StocktradeVO) finishedList.get(0);
		if (stVO.getStatus() != TradeConstants.STATUS_FINISH) {
			fail("Something wrong");
		}

		List pendingBuyListNew = StocktradeDAO.getPendingBuyTradeRecords(
				"600036", StairConstants.STAIR_STRATEGY_NAME);
		if (pendingBuyListNew.size() != 1) {
			fail("Something wrong");
		}
		stVO = (StocktradeVO) pendingBuyListNew.get(0);
		if (stVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
			fail("Something wrong");
		}

		if (pendingBuy.size() != 1) {
			fail("Something wrong");
		}
		stVO = (StocktradeVO) pendingBuy.get(0);
		if (stVO.getStatus() != TradeConstants.STATUS_PENDING_BUY) {
			fail("Something wrong");
		}

	}

	public static StockQuotesBean getMockStockQuotestBean600036() {
		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setStockId("600036");
		sqb.setName("招商银行");
		sqb.setCurrentPrice(10.98);
		sqb.setTodayOpen(11);
		sqb.setYesterdayClose(11.04);
		return sqb;
	}

	public static MonitorstockVO getMonitorstockVO600036Stair() {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(1);
		monitorstockVO.setStockid("600036");
		monitorstockVO.setName("招商银行");
		monitorstockVO.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		monitorstockVO.setAdditioninfo("10000.0,4,0.05,设定价,10.97");
		monitorstockVO.setOnmonitoring("true");
		return monitorstockVO;
	}

	public static List get600036StairPendingBuy() {
		List result = new ArrayList();
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(1);
		stocktradeVO1.setStockid("600036");
		stocktradeVO1.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_PENDING_BUY);
		stocktradeVO1.setBuybaseprice(10.97);
		stocktradeVO1.setBuyprice(10.97);
		stocktradeVO1.setNumber(900);
		stocktradeVO1.setSellprice(11.51);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		result.add(stocktradeVO1);
		stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(2);
		stocktradeVO1.setStockid("600036");
		stocktradeVO1.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_PENDING_BUY);
		stocktradeVO1.setBuybaseprice(10.43);
		stocktradeVO1.setBuyprice(10.43);
		stocktradeVO1.setNumber(900);
		stocktradeVO1.setSellprice(10.97);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		result.add(stocktradeVO1);
		stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(3);
		stocktradeVO1.setStockid("600036");
		stocktradeVO1.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_PENDING_BUY);
		stocktradeVO1.setBuybaseprice(9.91);
		stocktradeVO1.setBuyprice(9.91);
		stocktradeVO1.setNumber(1000);
		stocktradeVO1.setSellprice(10.43);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		result.add(stocktradeVO1);
		return result;
	}

	public static List get600036StairPossibleSell() {
		List result = new ArrayList();
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(1);
		stocktradeVO1.setStockid("600036");
		stocktradeVO1.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_HOLDING);
		stocktradeVO1.setBuybaseprice(10.97);
		stocktradeVO1.setBuyprice(10.97);
		stocktradeVO1.setNumber(900);
		stocktradeVO1.setSellprice(11.51);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		result.add(stocktradeVO1);
		stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(2);
		stocktradeVO1.setStockid("600036");
		stocktradeVO1.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_POSSIBLE_SELL);
		stocktradeVO1.setBuybaseprice(10.43);
		stocktradeVO1.setBuyprice(10.43);
		stocktradeVO1.setNumber(900);
		stocktradeVO1.setSellprice(10.97);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		result.add(stocktradeVO1);
		return result;
	}

	public static MonitorstockVO getMonitorstockVO600036Once() {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(2);
		monitorstockVO.setStockid("600036");
		monitorstockVO.setName("招商银行");
		monitorstockVO.setTradealgorithm(OnceConstants.ONCE_STRATEGY_NAME);
		monitorstockVO.setAdditioninfo("设定价,10.95,0.05,5000.0");
		monitorstockVO.setOnmonitoring("true");
		return monitorstockVO;
	}

	public static List get600036OncePendingBuy() {
		List result = new ArrayList();
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(4);
		stocktradeVO1.setStockid("600036");
		stocktradeVO1.setTradealgorithm(OnceConstants.ONCE_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_PENDING_BUY);
		stocktradeVO1.setBuybaseprice(10.95);
		stocktradeVO1.setBuyprice(10.95);
		stocktradeVO1.setNumber(400);
		stocktradeVO1.setSellprice(11.49);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		result.add(stocktradeVO1);
		return result;
	}

	public static StockQuotesBean getMockStockQuotestBean601118() {
		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setStockId("601118");
		sqb.setName("海南橡胶");
		sqb.setCurrentPrice(8);
		sqb.setTodayOpen(7.9);
		sqb.setYesterdayClose(8.1);
		return sqb;
	}

	public static MonitorstockVO getMonitorstockVO601118Stair() {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(3);
		monitorstockVO.setStockid("601118");
		monitorstockVO.setName("海南橡胶");
		monitorstockVO.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		monitorstockVO.setAdditioninfo("10000.0,4,0.05,开盘价,8.79");
		monitorstockVO.setOnmonitoring("true");
		return monitorstockVO;
	}

	public static List get601118StairPendingBuy() {
		List result = new ArrayList();
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(5);
		stocktradeVO1.setStockid("601118");
		stocktradeVO1.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_T_0_BUY);
		stocktradeVO1.setBuybaseprice(8.8);
		stocktradeVO1.setBuyprice(8.8);
		stocktradeVO1.setNumber(1100);
		stocktradeVO1.setSellprice(9.24);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		result.add(stocktradeVO1);
		stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(6);
		stocktradeVO1.setStockid("601118");
		stocktradeVO1.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_PENDING_BUY);
		stocktradeVO1.setBuybaseprice(8.36);
		stocktradeVO1.setBuyprice(8.36);
		stocktradeVO1.setNumber(1100);
		stocktradeVO1.setSellprice(8.8);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		result.add(stocktradeVO1);
		return result;
	}

	public static MonitorstockVO getMonitorstockVO601118Once() {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(4);
		monitorstockVO.setStockid("601118");
		monitorstockVO.setName("海南橡胶");
		monitorstockVO.setTradealgorithm(OnceConstants.ONCE_STRATEGY_NAME);
		monitorstockVO.setAdditioninfo("开盘价,8.8,0.05,5000.0");
		monitorstockVO.setOnmonitoring("true");
		return monitorstockVO;
	}

	public static List get601118OncePendingBuy() {
		List result = new ArrayList();
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(7);
		stocktradeVO1.setStockid("601118");
		stocktradeVO1.setTradealgorithm(OnceConstants.ONCE_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_T_0_BUY);
		stocktradeVO1.setBuybaseprice(8.8);
		stocktradeVO1.setBuyprice(8.8);
		stocktradeVO1.setNumber(500);
		stocktradeVO1.setSellprice(9.24);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		result.add(stocktradeVO1);
		return result;
	}

	public static StockQuotesBean getMockStockQuotestBean002199() {
		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setStockId("002199");
		sqb.setName("东晶电子");
		sqb.setCurrentPrice(15.42);
		sqb.setTodayOpen(16.97);
		sqb.setYesterdayClose(16.83);
		return sqb;
	}

	public static MonitorstockVO getMonitorstockVO002199Stair() {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(5);
		monitorstockVO.setStockid("002199");
		monitorstockVO.setName("东晶电子");
		monitorstockVO.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		monitorstockVO.setAdditioninfo("10000.0,4,0.05,午盘价,10.0");
		monitorstockVO.setOnmonitoring("true");
		return monitorstockVO;
	}

	public static List get002199StairPendingBuy() {
		List result = new ArrayList();
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(8);
		stocktradeVO1.setStockid("002199");
		stocktradeVO1.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_T_0_BUY);
		stocktradeVO1.setBuybaseprice(15.42);
		stocktradeVO1.setBuyprice(15.42);
		stocktradeVO1.setNumber(600);
		stocktradeVO1.setSellprice(16.19);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		result.add(stocktradeVO1);
		return result;
	}

	public static MonitorstockVO getMonitorstockVO002199Once() {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(6);
		monitorstockVO.setStockid("002199");
		monitorstockVO.setName("东晶电子");
		monitorstockVO.setTradealgorithm(OnceConstants.ONCE_STRATEGY_NAME);
		monitorstockVO.setAdditioninfo("午盘价,15.0,0.05,5000.0");
		monitorstockVO.setOnmonitoring("true");
		return monitorstockVO;
	}

	public static List get002199OncePendingBuy() {
		List result = new ArrayList();
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(9);
		stocktradeVO1.setStockid("002199");
		stocktradeVO1.setTradealgorithm(OnceConstants.ONCE_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_T_0_BUY);
		stocktradeVO1.setBuybaseprice(15.42);
		stocktradeVO1.setBuyprice(15.42);
		stocktradeVO1.setNumber(300);
		stocktradeVO1.setSellprice(16.19);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		GhlhDAO.create(stocktradeVO1);
		result.add(stocktradeVO1);
		return result;
	}

}
