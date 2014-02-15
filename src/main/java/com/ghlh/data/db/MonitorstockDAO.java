package com.ghlh.data.db;

import java.util.List;

public class MonitorstockDAO {

	public static List getMonitorStock() {
		String sql = "SELECT * FROM monitorstock ";
		List result = GhlhDAO.list(sql, "com.ghlh.data.db.MonitorstockVO");
		return result;
	}

	public static List getOnlyMonitoringStocks() {
		String sql = "SELECT * FROM monitorstock where onMonitoring = 'true' ";
		List result = GhlhDAO.list(sql, "com.ghlh.data.db.MonitorstockVO");
		return result;
	}

	
	public static List getBackMA10MonitorStock() {
		String sql = "SELECT * FROM monitorstock where tradeAlgorithm = 'BackMA10'";
		List result = GhlhDAO.list(sql, "com.ghlh.data.db.MonitorstockVO");
		return result;
	}

	public static void turnOnorOffMonitorStock(int id, boolean on) {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(id);
		monitorstockVO.setWhereId(true);
		monitorstockVO.setOnmonitoring(on + "");
		GhlhDAO.edit(monitorstockVO);
	}
}
