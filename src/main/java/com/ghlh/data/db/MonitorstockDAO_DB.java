package com.ghlh.data.db;

import java.util.List;

public class MonitorstockDAO_DB implements MonitorstockDAO_I {

	public List getMonitorStock() {
		String sql = "SELECT * FROM monitorstock ";
		List result = GhlhDAO.list(sql, "com.ghlh.data.db.MonitorstockVO");
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
		// TODO Auto-generated method stub
		
	}

	public MonitorstockVO get(String stockId) {
		// TODO Auto-generated method stub
		return null;
	}
}
