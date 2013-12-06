package com.ghlh.autotrade;

import java.util.List;

import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.OneTimeStrategy;
import com.ghlh.strategy.TradeConstants;
import com.ghlh.strategy.TradeUtil;
import com.ghlh.strategy.stair.StairConstants;
import com.ghlh.tradeway.SoftwareTrader;
import com.ghlh.util.ReflectUtil;

public class StockTradeIntradyMonitor {
	public StockTradeIntradyMonitor(MonitorstockVO monitorstockVO,
			List possibleSellList, List pendingBuyList) {
		this.monitorstockVO = monitorstockVO;
		this.possibleSellList = possibleSellList;
		this.pendingBuyList = pendingBuyList;
	}

	private MonitorstockVO monitorstockVO;

	public MonitorstockVO getMonitorstockVO() {
		return monitorstockVO;
	}

	public void setMonitorstockVO(MonitorstockVO monitorstockVO) {
		this.monitorstockVO = monitorstockVO;
	}

	public List getPendingBuyList() {
		return pendingBuyList;
	}

	public void setPendingBuyList(List pendingBuyList) {
		this.pendingBuyList = pendingBuyList;
	}

	private List possibleSellList;

	public List getPossibleSellList() {
		return possibleSellList;
	}

	public void setPossibleSellList(List possibleSellList) {
		this.possibleSellList = possibleSellList;
	}

	private List pendingBuyList;

	public void processSell(StockQuotesBean sqb) {
		for (int i = 0; i < possibleSellList.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) possibleSellList.get(i);
			if (sqb.getHighestPrice() >= stocktradeVO.getSellprice()) {
				String message = TradeUtil.getConfirmedSellMessage(
						stocktradeVO.getStockid(), stocktradeVO.getNumber(),
						stocktradeVO.getSellprice());
				EventRecorder.recordEvent(this.getClass(), message);
				StocktradeDAO.updateStocktradeStatus(stocktradeVO.getId(),
						TradeConstants.STATUS_FINISH);
				if (Boolean.valueOf(monitorstockVO.getOnmonitoring())) {
					reBuy(stocktradeVO);
				}
			}
		}
	}

	private void reBuy(StocktradeVO stocktradeVO) {
		TradeUtil.dealBuyStock(stocktradeVO.getStockid(),
				stocktradeVO.getBuyprice(), stocktradeVO.getSellprice(),
				stocktradeVO.getTradealgorithm(), stocktradeVO.getNumber());
		refreshPendingBuyList();
	}

	private void refreshPendingBuyList() {
		int size = pendingBuyList.size();
		for (int i = 0; i < size; i++) {
			pendingBuyList.remove(0);
		}

		List newPendingBuy = StocktradeDAO
				.getPendingBuyTradeRecords(monitorstockVO.getStockid(),
						StairConstants.STAIR_STRATEGY_NAME);
		pendingBuyList.addAll(newPendingBuy);

	}

	public void processBuy(StockQuotesBean sqb) {
		for (int j = 0; j < pendingBuyList.size(); j++) {
			StocktradeVO stVO = (StocktradeVO) pendingBuyList.get(j);
			if (sqb.getCurrentPrice() <= stVO.getBuyprice()) {
				String message = TradeUtil.getConfirmedBuyMessage(
						monitorstockVO.getStockid(), stVO.getNumber(),
						stVO.getBuyprice());

				EventRecorder.recordEvent(this.getClass(), message);
				SoftwareTrader.getInstance().buyStock(stVO.getStockid(),
						stVO.getNumber());
				StocktradeDAO.updateStocktradeStatus(stVO.getId(),
						TradeConstants.STATUS_T_0_BUY);
				pendingBuyList.remove(j);
				break;
			}
		}
	}

}
