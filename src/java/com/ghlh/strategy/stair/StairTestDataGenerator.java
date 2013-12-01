package com.ghlh.strategy.stair;

import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.stockquotes.StockQuotesBean;

public class StairTestDataGenerator {

	public static StockQuotesBean getMockStockQuotestBean() {
		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setStockId("601118");
		sqb.setName("海南橡胶");
		sqb.setCurrentPrice(7);
		return sqb;
	}

	public static MonitorstockVO prepareMonitorstockVO(double zdf,
			int stairNumber) {
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setId(1);
		monitorstockVO.setStockid("601118");
		monitorstockVO.setName("海南橡胶");
		monitorstockVO.setTradealgorithm(StairConstants.STAIR_STRATEGY_NAME);
		monitorstockVO.setAdditioninfo("25000," + stairNumber + "," + zdf
				+ ",设定价,10");
		monitorstockVO.setOnmonitoring("true");
		return monitorstockVO;
	}
}
