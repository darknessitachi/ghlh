package com.ghlh;

/*@author Robin*/

import java.util.List;

import org.apache.log4j.Logger;

import com.ghlh.stockpool.FileStockPoolAccessor;
import com.ghlh.stockpool.MonitorStockBean;
import com.ghlh.stockpool.StockPoolAccessor;
import com.ghlh.stockpool.StockPoolAccessorException;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.TradeContext;
import com.ghlh.strategy.TradeResult;
import com.ghlh.strategy.TradeStrategy;
import com.ghlh.tradeway.SoftwareTrader;
import com.ghlh.tradeway.StockTrader;
import com.ghlh.ui.StatusField;
import com.ghlh.ui.autotradestart.AutoTradeSwitch;
import com.ghlh.util.ReflectUtil;
import com.ghlh.util.StockMarketUtil;
import com.ghlh.util.TimeUtil;

public class StockTradeMonitoringJob {
	private static Logger logger = Logger
			.getLogger(StockTradeMonitoringJob.class);

	StockTrader stockTrader = new SoftwareTrader();
	TradeContext tradeContext = null;
	StockPoolAccessor stockPoolAccessor = new FileStockPoolAccessor();

	public void monitor() {
		try {
			List<MonitorStockBean> monitorStocksList = stockPoolAccessor
					.getMonitorStocks();
			boolean setStatus = false;
			while (AutoTradeSwitch.getInstance().isStart()) {
				if (!StockMarketUtil.isMarketOpenning()) {
					AutoTradeSwitch.getInstance().setMonitorInfo(
							"自动交易监控已启动， 现在休市中(交易结束休市)...");
					StatusField.getInstance().setPromptMessage(
							"自动交易监控已启动， 现在休市中(交易结束休市)...");
					break;
				} else {
					if (!setStatus) {
						setStatus = true;
						StatusField.getInstance().setPromptMessage(
								"自动交易监控已启动，现在监控中(在交易时间外启动的)...");
					}
				}
				for (int i = 0; i < monitorStocksList.size(); i++) {
					MonitorStockBean monitorStockBean = (MonitorStockBean) monitorStocksList
							.get(i);

					AutoTradeSwitch.getInstance().setMonitorInfo(
							monitorStockBean.getStockId() + " "
									+ monitorStockBean.getName());
					StockQuotesBean stockQuotesBean = InternetStockQuotesInquirer
							.queryStock(monitorStockBean.getStockId());
					if (stockQuotesBean == null) {
						break;
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
						stockPoolAccessor.writeMonitorStocks(monitorStocksList);
					}
					TimeUtil.pause(Constants.MONITORING_PAUSE_SECONDS * 500);
				}

			}
			if (!AutoTradeSwitch.getInstance().isStart()) {
				AutoTradeSwitch.getInstance().showStopSuccessful();
			}
		} catch (Exception ex) {
			logger.error("Stock Monitoring Trade throw : ", ex);
		}
	}

	private boolean processStockTrade(TradeResult tradeResult,
			MonitorStockBean monitorStockBean)
			throws StockPoolAccessorException {
		boolean traded = false;
		if (tradeResult.getCmd() == Constants.SELL) {
			stockTrader.sellStock(tradeResult.getStockId(),
					tradeResult.getNumber());
			// monitorStockBean.writeBackCurrentNumber(tradeResult.getCmd(),
			// tradeResult.getNumber());
			traded = true;
		} else if (tradeResult.getCmd() == Constants.BUY) {
			stockTrader.buyStock(tradeResult.getStockId(),
					tradeResult.getNumber());
			// monitorStockBean.writeBackCurrentNumber(tradeResult.getCmd(),
			// tradeResult.getNumber());
			traded = true;
		}
		return traded;
	}
}
