package com.ghlh.stockpool;

import java.util.List;

public interface StockPoolAccessor {
	List<MonitorStockBean> getMonitorStocks() throws StockPoolAccessorException;

	void writeMonitorStocks(List<MonitorStockBean> monitorStocks)
			throws StockPoolAccessorException;

	void updateMonitorStock(MonitorStockBean monitorStockBean)
			throws StockPoolAccessorException;

	public void addMonitorStock(MonitorStockBean monitorStockBean)
			throws StockPoolAccessorException;
}
