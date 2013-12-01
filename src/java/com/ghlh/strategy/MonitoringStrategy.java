package com.ghlh.strategy;

import java.util.List;

import com.ghlh.data.db.MonitorstockVO;

public interface MonitoringStrategy {
	void processStockTrade(MonitorstockVO monitorstockVO, List stocktradeList);
}
