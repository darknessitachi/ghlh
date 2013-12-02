package com.ghlh.autotrade;

/*@author Robin*/

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ghlh.data.db.MonitorstockDAO;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.strategy.stair.StairIntradayStrategy;
import com.ghlh.ui.StatusField;
import com.ghlh.ui.autotradestart.AutoTradeMonitor;
import com.ghlh.ui.autotradestart.AutoTradeSwitch;
import com.ghlh.util.StockMarketUtil;
import com.ghlh.util.TimeUtil;

public class StockTradeIntradyMonitoringJob {
	private static Logger logger = Logger
			.getLogger(StockTradeIntradyMonitoringJob.class);

	MonitorstockDAO monitorstockDAO = null;

	private int monitoringCount = 0;

	public void monitoring() {
		if (StockMarketUtil.isMarketRest()) {
			return;
		}
		try {
			List monitorStocksList = this.monitorstockDAO.getMonitorStock();
		
			Map stockTrade = getStockTrades(monitorStocksList);
			
			while (AutoTradeSwitch.getInstance().isStart()) {
				if (StockMarketUtil.isMarketBreak()) {
					break;
				}
				setMonitoringStatus();
				for (int i = 0; i < monitorStocksList.size(); i++) {
					MonitorstockVO monitorstockVO = (MonitorstockVO) monitorStocksList
							.get(i);
					if (!Boolean.valueOf(monitorstockVO.getOnmonitoring())) {
						continue;
					}
					AutoTradeMonitor.getInstance().setMonitorStock(
							monitorstockVO.getStockid(),
							monitorstockVO.getName());

					String tradeAlgorithm = monitorstockVO.getTradealgorithm();
					StairIntradayStrategy sis = new StairIntradayStrategy();
					sis.processStockTrade(monitorstockVO,
							(List) stockTrade.get(monitorstockVO));
					TimeUtil.pause(200);
				}
				TimeUtil.pause(200);
			}
			if (!AutoTradeSwitch.getInstance().isStart()) {
				AutoTradeMonitor.getInstance().showStopSuccessful();
			}
		} catch (Exception ex) {
			logger.error("Stock Monitoring Trade throw : ", ex);
		}
	}

	public Map getStockTrades(List monitorStocksList) {
		Map stockTrade = new HashMap();
		for (int i = 0; i < monitorStocksList.size(); i++) {
			MonitorstockVO monitorstockVO = (MonitorstockVO) monitorStocksList
					.get(i);
			List holdStocks = StocktradeDAO.getHoldStocks(
					monitorstockVO.getStockid(),
					monitorstockVO.getTradealgorithm());
			stockTrade.put(monitorstockVO, holdStocks);
		}
		return stockTrade;
	}

	private void setMonitoringStatus() {
		int pointCount = monitoringCount % 6;
		String sPoint = "";
		for (int i = 0; i < pointCount; i++) {
			sPoint += ".";
		}
		monitoringCount++;
		StatusField.getInstance().setPromptMessage("自动交易监控已启动， 现在监控中" + sPoint);
	}

}
