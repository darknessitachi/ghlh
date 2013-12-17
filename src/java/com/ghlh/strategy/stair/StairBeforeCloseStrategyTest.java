package com.ghlh.strategy.stair;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.common.util.IDGenerator;
import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.strategy.TradeConstants;
import com.ghlh.strategy.once.OnceConstants;

public class StairBeforeCloseStrategyTest {

	@Test
	public void test() {
		StairBeforeCloseStrategy sbcs = new StairBeforeCloseStrategy();
		MonitorstockVO msVO = this.prepareMonitorstockVO();
		prepareStockTradeData();
		sbcs.processStockTrade(msVO);
		
		//fail("Not yet implemented");
	}

	public static MonitorstockVO prepareMonitorstockVO() {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(1);
		monitorstockVO.setStockid("002471");
		monitorstockVO.setName("中超电缆");
		monitorstockVO.setTradealgorithm("Stair");
		monitorstockVO.setAdditioninfo("5000.0,4,0.05,开盘价,10.0");
		monitorstockVO.setOnmonitoring("true");
		return monitorstockVO;
	}

	private void prepareStockTradeData() {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(IDGenerator.generateId("stocktrade"));
		stocktradeVO1.setStockid("002471");
		stocktradeVO1.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setStatus(TradeConstants.STATUS_PENDING_BUY);
		stocktradeVO1.setBuybaseprice(8.78);
		stocktradeVO1.setBuyprice(8.78);
		stocktradeVO1.setNumber(500);
		stocktradeVO1.setSellprice(9.21);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		stocktradeVO1.setPrevioustradeid(1);
		GhlhDAO.create(stocktradeVO1);
		
	}
}
