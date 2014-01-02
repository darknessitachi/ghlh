package com.ghlh.autotrade;

import java.util.List;

import org.junit.Test;

import com.ghlh.data.db.MonitorstockDAO;

public class StockTradeIntradyMonitoringJobTest {

	@Test
	public void testMonitoringIntrady() {
		List monitorStocksList = MonitorstockDAO.getMonitorStock();
		StockTradeIntradyMonitoringJob stimj = new StockTradeIntradyMonitoringJob();
		List monitors = stimj.retrieveStockMonitors(monitorStocksList);
		stimj.monitoringIntrady(monitors);
	}


}
