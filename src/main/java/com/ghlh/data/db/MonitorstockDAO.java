package com.ghlh.data.db;

import java.util.List;

public class MonitorstockDAO {
	private static MonitorstockDAO_I monitorStockDao = new MonitorstockDAO_File();

	public static List getMonitorStock() {
		return monitorStockDao.getMonitorStock();
	}

	public static List getMonitorStock(String strategyName) {
		return monitorStockDao.getMonitorStock(strategyName);
	}

	public static List getOnlyMonitoringStocks() {
		return monitorStockDao.getOnlyMonitoringStocks();
	}

	public static List getOnlyMonitoringStocks(String strategyName) {
		return monitorStockDao.getOnlyMonitoringStocks(strategyName);
	}

	public static List getBackMA10MonitorStock() {
		return monitorStockDao.getBackMA10MonitorStock();
	}

	public static void turnOnorOffMonitorStock(int id, boolean on) {
		monitorStockDao.turnOnorOffMonitorStock(id, on);
	}
	public static void save(MonitorstockVO monitorstockVO){
		monitorStockDao.save(monitorstockVO);
	}

	public static MonitorstockVO get(String stockId){
		return monitorStockDao.get(stockId);
	}
	

}
