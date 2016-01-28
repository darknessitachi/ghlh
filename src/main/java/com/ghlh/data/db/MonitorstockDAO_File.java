package com.ghlh.data.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MonitorstockDAO_File implements MonitorstockDAO_I {

	public List getMonitorStock() {
		String[] stocks = FileUtil.getFilesList();
		List result = new ArrayList();
		for (int i = 0; i < stocks.length; i++) {
			if (!FileUtil.isHidenFile(stocks[i])) {
				result.add(FileUtil.convertPropertiesToObject(
						FileUtil.loadPropertiesFromFile(stocks[i]),
						"com.ghlh.data.db.MonitorstockVO"));
			}
		}
		return result;
	}

	public List getMonitorStock(String strategyName) {
		String sql = "SELECT * FROM monitorstock where tradeAlgorithm = '"
				+ strategyName + "'";
		List result = GhlhDAO.list(sql, "com.ghlh.data.db.MonitorstockVO");
		return result;
	}

	public List getOnlyMonitoringStocks() {
		String sql = "SELECT * FROM monitorstock where onMonitoring = 'true' ";
		List result = GhlhDAO.list(sql, "com.ghlh.data.db.MonitorstockVO");
		return result;
	}

	public List getOnlyMonitoringStocks(String strategyName) {
		String sql = "SELECT * FROM monitorstock where onMonitoring = 'true' and tradeAlgorithm = '"
				+ strategyName + "'";
		List result = GhlhDAO.list(sql, "com.ghlh.data.db.MonitorstockVO");
		return result;
	}

	public List getBackMA10MonitorStock() {
		String sql = "SELECT * FROM monitorstock where tradeAlgorithm = 'BackMA10'";
		List result = GhlhDAO.list(sql, "com.ghlh.data.db.MonitorstockVO");
		return result;
	}

	public void turnOnorOffMonitorStock(int id, boolean on) {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(id);
		monitorstockVO.setWhereId(true);
		monitorstockVO.setOnmonitoring(on + "");
		GhlhDAO.edit(monitorstockVO);
	}

	public void save(MonitorstockVO monitorstockVO) {
		Properties props = FileUtil.convertObjectToProperties(monitorstockVO);
		FileUtil.updatePropertiesToFile(monitorstockVO.getStockid(), props);
	}

	public MonitorstockVO get(String stockId) {
		Properties props = FileUtil.loadPropertiesFromFile(stockId);
		MonitorstockVO result = (MonitorstockVO) FileUtil
				.convertPropertiesToObject(props,
						"com.ghlh.data.db.MonitorstockVO");
		return result;
	}

}
