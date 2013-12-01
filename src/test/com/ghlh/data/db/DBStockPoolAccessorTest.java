package com.ghlh.data.db;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ghlh.data.MonitorStockBean;
import com.ghlh.data.StockPoolAccessor;

public class DBStockPoolAccessorTest {

	@Test
	public void test() {
		try {
			StockPoolAccessor dbStockPoolAccessorTest = new GhlhDAO();
			MonitorStockBean monitorStockBean = new MonitorStockBean();
			monitorStockBean.setStockId("600036");
			monitorStockBean.setName("’–…Ã“¯––");
			monitorStockBean.setTradeAlgorithm("Once");
			monitorStockBean.setOnMonitoring(true);
			monitorStockBean.setAdditionInfo("10, 102,3");
			dbStockPoolAccessorTest.addMonitorStock(monitorStockBean);
			MonitorStockBean msb = dbStockPoolAccessorTest.getMonitorStock("600036");
			assertEquals(msb.getStockId(), monitorStockBean.getStockId());
			assertEquals(msb.getName(), monitorStockBean.getName());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
	}

}
