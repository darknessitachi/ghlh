package com.ghlh.autotrade;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.ghlh.conf.ConfigurationAccessor;
import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.strategy.TradeConstants;

public class AutoTradeBeforeOpenJobTest {

	static {
		ConfigurationAccessor.getInstance().setOpenSoftwareTrade(false);
	}
	
	
	@Test
	public void testExecute() {
		prepareMonitorstockVO();
		AutoTradeBeforeOpenJob autoTradeBeforeOpenJob = new AutoTradeBeforeOpenJob();
		try {
			autoTradeBeforeOpenJob.execute(null);
		} catch (Exception ex) {
			fail("Not yet implemented");
		}
		List tradeStockList = StocktradeDAO.getPendingBuyTradeRecords("600036",
				"Stair");
		for (int i = 0; i < tradeStockList.size(); i++) {
			StocktradeVO tradestockVO = (StocktradeVO) tradeStockList.get(i);
			if(tradestockVO.getStatus() != TradeConstants.STATUS_PENDING_BUY){
				fail("Somehting wrong");
			}
		}
		
		tradeStockList = StocktradeDAO.getPendingBuyTradeRecords("600519",
				"Once");
		if (tradeStockList.size() == 0) {
			fail("Somehting wrong");
		}
		for (int i = 0; i < tradeStockList.size(); i++) {
			StocktradeVO tradestockVO = (StocktradeVO) tradeStockList.get(i);
			if(tradestockVO.getStatus() != TradeConstants.STATUS_PENDING_BUY){
				fail("Somehting wrong");
			}
		}
	}

	public static void prepareMonitorstockVO() {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setStockid("600036");
		monitorstockVO.setWhereStockid(true);
		monitorstockVO.setTradealgorithm("Stair");
		monitorstockVO.setWhereTradealgorithm(true);
		GhlhDAO.remove(monitorstockVO);

		monitorstockVO = new MonitorstockVO();
		monitorstockVO.setStockid("600519");
		monitorstockVO.setWhereStockid(true);
		monitorstockVO.setTradealgorithm("Once");
		monitorstockVO.setWhereTradealgorithm(true);
		GhlhDAO.remove(monitorstockVO);

		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setStockid("600519");
		stocktradeVO1.setWhereStockid(true);
		GhlhDAO.remove(stocktradeVO1);

		stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setStockid("600036");
		stocktradeVO1.setWhereStockid(true);
		GhlhDAO.remove(stocktradeVO1);

		monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(1);
		monitorstockVO.setStockid("600036");
		monitorstockVO.setName("招商银行");
		monitorstockVO.setTradealgorithm("Stair");
		monitorstockVO.setAdditioninfo("10000.0,4,0.05,设定价,11.07");
		monitorstockVO.setOnmonitoring("true");
		GhlhDAO.create(monitorstockVO);
		monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(2);
		monitorstockVO.setStockid("600519");
		monitorstockVO.setName("贵州茅台");
		monitorstockVO.setTradealgorithm("Once");
		monitorstockVO.setAdditioninfo("设定价,137.46,0.05,20000.0,0.02");
		monitorstockVO.setOnmonitoring("true");
		GhlhDAO.create(monitorstockVO);

	}

}
