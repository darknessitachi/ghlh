package com.ghlh.data.db;

import java.util.List;

public interface MonitorstockDAO_I {
	List getMonitorStock();

	List getMonitorStock(String strategyName);

	List getOnlyMonitoringStocks();

	List getOnlyMonitoringStocks(String strategyName);

	List getBackMA10MonitorStock();

	void turnOnorOffMonitorStock(int id, boolean on);
	
	void save(MonitorstockVO monitorstockVO);

	MonitorstockVO get(String stockId);

	
	
}
