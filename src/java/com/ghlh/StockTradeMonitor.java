package com.ghlh;

/*@author Robin*/

import java.util.List;

import com.ghlh.stockpool.MonitorStockBean;
import com.ghlh.stockpool.StockPoolAccessor;
import com.ghlh.stockpool.StockPoolAccessorException;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.stockquotes.StockQuotesInquirer;
import com.ghlh.strategy.StandardPriceAndFixedZDEStrategy;
import com.ghlh.strategy.TradeContext;
import com.ghlh.strategy.TradeResult;
import com.ghlh.tradeway.SoftwareTrader;
import com.ghlh.tradeway.StockTrader;
import com.ghlh.util.StockMarketUtil;
import com.ghlh.util.TimeUtil;

public class StockTradeMonitor {

	StockQuotesInquirer stockQuotesInquirer = new InternetStockQuotesInquirer();
	StockTrader stockTrader = new SoftwareTrader();
	TradeContext tradeContext = new TradeContext(
			new StandardPriceAndFixedZDEStrategy());
	StockPoolAccessor stockPoolAccessor = null;// new FileStockPoolAccessor();

	public void monitor() throws Exception {

		List<MonitorStockBean> monitorStocksList = stockPoolAccessor
				.getMonitorStocks();
		while (true) {
			if (StockMarketUtil.isMarketOpenning()) {
				for (int i = 0; i < monitorStocksList.size(); i++) {
					MonitorStockBean monitorStockBean = (MonitorStockBean) monitorStocksList
							.get(i);

					StockQuotesBean stockQuotesBean = stockQuotesInquirer
							.getStockQuotesBean(monitorStockBean.getStockId());
					String tradeAlgorithm = monitorStockBean
							.getTradeAlgorithm();

					TradeResult tradeResult = tradeContext.processStockTrade(
							monitorStockBean, stockQuotesBean);

					boolean traded = processStockTrade(tradeResult,
							monitorStockBean);
					if (traded) {
						stockPoolAccessor.writeMonitorStocks(monitorStocksList);
					}
				}
			}
			TimeUtil.pause(Constants.MONITORING_PAUSE_SECONDS);
		}
	}

	private boolean processStockTrade(TradeResult tradeResult,
			MonitorStockBean monitorStockBean)
			throws StockPoolAccessorException {
		boolean traded = false;
		if (tradeResult.getCmd() == Constants.SELL) {
			stockTrader.sellStock(tradeResult.getStockId(),
					tradeResult.getNumber());
//			monitorStockBean.writeBackCurrentNumber(tradeResult.getCmd(),
//					tradeResult.getNumber());
			traded = true;
		} else if (tradeResult.getCmd() == Constants.BUY) {
			stockTrader.buyStock(tradeResult.getStockId(),
					tradeResult.getNumber());
//			monitorStockBean.writeBackCurrentNumber(tradeResult.getCmd(),
//					tradeResult.getNumber());
			traded = true;
		}
		return traded;
	}

	public static void main(String[] args) {
		StockTradeMonitor controller = new StockTradeMonitor();
		try {
			controller.monitor();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
