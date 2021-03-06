package com.ghlh.autotrade;

/*@author Robin*/

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ghlh.data.db.MonitorstockDAO;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.OneTimeStrategy;
import com.ghlh.strategy.TradeConstants;
import com.ghlh.strategy.TradeUtil;
import com.ghlh.strategy.stair.StairConstants;
import com.ghlh.tradeway.SoftwareTrader;
import com.ghlh.ui.StatusField;
import com.ghlh.ui.autotradestart.AutoTradeMonitor;
import com.ghlh.ui.autotradestart.AutoTradeSwitch;
import com.ghlh.util.ReflectUtil;
import com.ghlh.util.StockMarketUtil;
import com.ghlh.util.TimeUtil;

public class StockTradeIntradyMonitoringJob {
	private static Logger logger = Logger
			.getLogger(StockTradeIntradyMonitoringJob.class);

	private int monitoringCount = 0;

	public void monitoring() {
		if (StockMarketUtil.isMarketRest()) {
			return;
		}
		try {
			List monitorStocksList = MonitorstockDAO.getMonitorStock();
			List stockMonitors = retrieveStockMonitors(monitorStocksList);

			while (AutoTradeSwitch.getInstance().isStart()) {
				try {
					if (StockMarketUtil.isMarketBreak()) {
						break;
					}
					setMonitoringStatus();
					monitoringIntrady(stockMonitors);
					TimeUtil.pause(2000);
				} catch (Exception ex) {
					logger.error("Stock Monitoring Trade throw : ", ex);
				}
			}
			if (!AutoTradeSwitch.getInstance().isStart()) {
				AutoTradeMonitor.getInstance().showStopSuccessful();
			}
		} catch (Exception ex) {
			logger.error("Stock Monitoring Trade throw : ", ex);
		} finally {
			AutoTradeMonitor.getInstance().setMonitorStock("0", "");
		}
	}

	private void processIntraFirstBuy(List monitorStocksList) {
		String message = "刚开盘开盘价买入处理开始";
		EventRecorder.recordEvent(this.getClass(), message);
		for (int i = 0; i < monitorStocksList.size(); i++) {
			MonitorstockVO monitorstockVO = (MonitorstockVO) monitorStocksList
					.get(i);
			OneTimeStrategy ts = (OneTimeStrategy) ReflectUtil
					.getClassInstance("com.ghlh.strategy",
							monitorstockVO.getTradealgorithm(),
							"IntradyFirstBuyStrategy");
			ts.processStockTrade(monitorstockVO);
		}
		message = "刚开盘开盘价买入处理结束";
		EventRecorder.recordEvent(this.getClass(), message);
	}

	private void processBeforeCloseBuy(List monitorStocksList) {
		String message = "快收盘收盘价买入处理开始";
		EventRecorder.recordEvent(this.getClass(), message);
		for (int i = 0; i < monitorStocksList.size(); i++) {
			MonitorstockVO monitorstockVO = (MonitorstockVO) monitorStocksList
					.get(i);
			OneTimeStrategy ts = (OneTimeStrategy) ReflectUtil
					.getClassInstance("com.ghlh.strategy",
							monitorstockVO.getTradealgorithm(),
							"BeforeCloseStrategy");
			ts.processStockTrade(monitorstockVO);
		}
		message = "快收盘收盘价买入处理结束";
		EventRecorder.recordEvent(this.getClass(), message);
	}

	public void monitoringIntrady(List stockMonitors) {
		for (int i = 0; i < stockMonitors.size(); i++) {
			StockTradeIntradyMonitor monitor = (StockTradeIntradyMonitor) stockMonitors
					.get(i);
			try {
				StockQuotesBean sqb = InternetStockQuotesInquirer.getInstance()
						.getStockQuotesBean(
								monitor.getMonitorstockVO().getStockid());
				if (TradeUtil.isStopTrade(sqb)) {
					AutoTradeMonitor.getInstance().setMonitorStock(
							sqb.getStockId(), sqb.getName() + " 停牌 ");
					TimeUtil.pause(200);
					continue;
				}
				AutoTradeMonitor.getInstance().setMonitorStock(
						sqb.getStockId(), sqb.getName());
				monitor.processBuy(sqb);
				monitor.processSell(sqb);
				TimeUtil.pause(200);
			} catch (Exception ex) {
				logger.error("monitoringIntrady with stock : "
						+ monitor.getMonitorstockVO().getStockid(), ex);
			}
		}
	}

	public List retrieveStockMonitors(List monitorStocksList) {
		List stockMonitors = new ArrayList();
		for (int i = 0; i < monitorStocksList.size(); i++) {
			MonitorstockVO monitorstockVO = (MonitorstockVO) monitorStocksList
					.get(i);
			List canSellList = StocktradeDAO
					.readCanSellStockTrade(monitorstockVO.getStockid());
			StockTradeIntradyMonitor stockTradeIntradyMonitor = new StockTradeIntradyMonitor(
					monitorstockVO, canSellList);
			stockMonitors.add(stockTradeIntradyMonitor);
		}
		return stockMonitors;
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
