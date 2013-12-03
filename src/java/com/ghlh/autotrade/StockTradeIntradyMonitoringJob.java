package com.ghlh.autotrade;

/*@author Robin*/

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
import com.ghlh.strategy.TradeConstants;
import com.ghlh.strategy.stair.StairIntradayStrategy;
import com.ghlh.tradeway.SoftwareTrader;
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

			Map possbileSellMap = new HashMap();
			Map pendingBuyMap = new HashMap();
			retrieveStockTrades(monitorStocksList, possbileSellMap,
					pendingBuyMap);

			while (AutoTradeSwitch.getInstance().isStart()) {
				if (StockMarketUtil.isMarketBreak()) {
					break;
				}
				setMonitoringStatus();
				monitoring(monitorStocksList, possbileSellMap, pendingBuyMap);
				TimeUtil.pause(200);
			}
			if (!AutoTradeSwitch.getInstance().isStart()) {
				AutoTradeMonitor.getInstance().showStopSuccessful();
			}
		} catch (Exception ex) {
			logger.error("Stock Monitoring Trade throw : ", ex);
		}
	}

	private void monitoring(List monitorStocksList, Map possbileSellMap,
			Map pendingBuyMap) {
		for (int i = 0; i < monitorStocksList.size(); i++) {
			MonitorstockVO monitorstockVO = (MonitorstockVO) monitorStocksList
					.get(i);
			if (!Boolean.valueOf(monitorstockVO.getOnmonitoring())) {
				continue;
			}
			AutoTradeMonitor.getInstance().setMonitorStock(
					monitorstockVO.getStockid(),
					monitorstockVO.getName());

			processBuyLogic(monitorstockVO, pendingBuyMap);
			processSpecificLogic(possbileSellMap, pendingBuyMap,
					monitorstockVO);
			TimeUtil.pause(200);
		}
	}

	private void processBuyLogic(MonitorstockVO monitorstockVO,
			Map pendingBuyMap) {
		StockQuotesBean sqb = InternetStockQuotesInquirer.getInstance()
				.getStockQuotesBean(monitorstockVO.getStockid());
		List pendingBuy = (List) pendingBuyMap.get(monitorstockVO);
		for (int j = 0; j < pendingBuy.size(); j++) {
			StocktradeVO stVO = (StocktradeVO) pendingBuy.get(j);
			if (sqb.getCurrentPrice() <= stVO.getBuyprice()) {
				SoftwareTrader.getInstance().buyStock(stVO.getStockid(),
						stVO.getNumber());
				StocktradeDAO.updateStocktradeStatus(stVO.getId(),
						TradeConstants.STATUS_HOLDING);
				pendingBuy.remove(j);
				break;
			}
		}
	}

	private void processSpecificLogic(Map possbileSellMap, Map pendingBuyMap,
			MonitorstockVO monitorstockVO) {
		String tradeAlgorithm = monitorstockVO.getTradealgorithm();
		if (tradeAlgorithm.equals("Stair")) {
			StairIntradayStrategy sis = new StairIntradayStrategy();
			sis.processStockTrade(monitorstockVO,
					(List) possbileSellMap.get(monitorstockVO),
					(List) pendingBuyMap.get(monitorstockVO));
		}
	}

	private void retrieveStockTrades(List monitorStocksList,
			Map possbileSellMap, Map pendingBuyMap) {
		Map stockTrade = new HashMap();
		for (int i = 0; i < monitorStocksList.size(); i++) {
			MonitorstockVO monitorstockVO = (MonitorstockVO) monitorStocksList
					.get(i);
			List possibleSell = StocktradeDAO.getPossibleSellTradeRecords(
					monitorstockVO.getStockid(),
					monitorstockVO.getTradealgorithm());
			possbileSellMap.put(monitorstockVO, possibleSell);
			List pendingBuy = StocktradeDAO.getPendingBuyTradeRecords(
					monitorstockVO.getStockid(),
					monitorstockVO.getTradealgorithm());
			pendingBuyMap.put(monitorstockVO, pendingBuy);

		}
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
