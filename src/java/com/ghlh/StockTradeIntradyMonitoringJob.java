package com.ghlh;

/*@author Robin*/

import java.util.List;

import org.apache.log4j.Logger;

import com.ghlh.data.FileStockPoolAccessor;
import com.ghlh.data.MonitorStockBean;
import com.ghlh.data.StockPoolAccessor;
import com.ghlh.data.StockPoolAccessorException;
import com.ghlh.data.db.MonitorstockDAO;
import com.ghlh.stockquotes.SohuStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.TradeContext;
import com.ghlh.strategy.TradeResult;
import com.ghlh.strategy.TradeStrategy;
import com.ghlh.tradeway.SoftwareTrader;
import com.ghlh.tradeway.StockTrader;
import com.ghlh.ui.StatusField;
import com.ghlh.ui.autotrade.MainPanel;
import com.ghlh.ui.autotrade.stocksetting.StockSettingContentPanel;
import com.ghlh.ui.autotradestart.AutoTradeSwitch;
import com.ghlh.util.ReflectUtil;
import com.ghlh.util.StockMarketUtil;
import com.ghlh.util.TimeUtil;

public class StockTradeIntradyMonitoringJob {
	private static Logger logger = Logger
			.getLogger(StockTradeIntradyMonitoringJob.class);

	StockTrader stockTrader = new SoftwareTrader();
	TradeContext tradeContext = null;

	StockPoolAccessor stockPoolAccessor = new FileStockPoolAccessor();
	private int monitoringCount = 0;

	public void monitoring() {
		if (isMarketRest()) {
			return;
		}
		try {
			List<MonitorStockBean> monitorStocksList = stockPoolAccessor
					.getMonitorStocks();
			while (AutoTradeSwitch.getInstance().isStart()) {
				if (isMarketBreak()) {
					break;
				}
				setMonitoringStatus();
				for (int i = 0; i < monitorStocksList.size(); i++) {
					MonitorStockBean monitorStockBean = (MonitorStockBean) monitorStocksList
							.get(i);
					if (!monitorStockBean.isOnMonitoring()) {
						continue;
					}
					AutoTradeSwitch.getInstance().setMonitorInfo(
							monitorStockBean.getStockId() + " "
									+ monitorStockBean.getName());

					StockQuotesBean stockQuotesBean = new SohuStockQuotesInquirer()
							.getStockQuotesBean(monitorStockBean.getStockId());
					if (stockQuotesBean == null) {
						continue;
					}
					String tradeAlgorithm = monitorStockBean
							.getTradeAlgorithm();

					TradeStrategy ts = (TradeStrategy) ReflectUtil
							.getClassInstance("com.ghlh.strategy",
									tradeAlgorithm, "TradeStrategy");
					tradeContext = new TradeContext(ts);
					TradeResult tradeResult = tradeContext.processStockTrade(
							monitorStockBean, stockQuotesBean);

					boolean traded = processStockTrade(tradeResult,
							monitorStockBean);
					if (traded) {
						logger.error(tradeResult);
//						((StockSettingContentPanel) MainPanel.getInstance()
//								.getStockSettingUI())
//								.refreshUIMsbList(monitorStocksList);
						stockPoolAccessor.writeMonitorStocks(monitorStocksList);
					}
					TimeUtil.pause(200);
				}
				TimeUtil.pause(200);
			}
			if (!AutoTradeSwitch.getInstance().isStart()) {
				AutoTradeSwitch.getInstance().showStopSuccessful();
			}
		} catch (Exception ex) {
			logger.error("Stock Monitoring Trade throw : ", ex);
		}
	}

	private boolean isMarketRest() {
		boolean result = false;
		String cause = StockMarketUtil.getMarketRestCause();
		if (cause != null) {
			String message = "自动交易监控已启动， " + cause;
			setStatus(message);
			result = true;
		}
		return result;
	}

	private boolean isMarketBreak() {
		boolean result = false;
		String cause = StockMarketUtil.getCloseCause();
		if (cause != null) {
			String message = "自动交易监控已启动， " + cause;
			setStatus(message);
			result = true;
		}
		return result;
	}

	private void setStatus(String message) {
		AutoTradeSwitch.getInstance().setMonitorInfo(message);
		StatusField.getInstance().setPromptMessage(message);
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

	private boolean processStockTrade(TradeResult tradeResult,
			MonitorStockBean monitorStockBean)
			throws StockPoolAccessorException {
		boolean traded = false;
		if (tradeResult.getCmd() == Constants.SELL) {
			stockTrader.sellStock(tradeResult.getStockId(),
					tradeResult.getNumber());
			traded = true;
		} else if (tradeResult.getCmd() == Constants.BUY) {
			stockTrader.buyStock(tradeResult.getStockId(),
					tradeResult.getNumber());
			traded = true;
		}
		return traded;
	}
}
