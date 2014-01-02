package com.ghlh.strategy;

import com.ghlh.data.db.MonitorstockVO;

public interface OneTimeStrategy {
	void processStockTrade(MonitorstockVO monitorstockVO);
}
