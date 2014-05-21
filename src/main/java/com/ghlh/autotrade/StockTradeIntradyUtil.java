package com.ghlh.autotrade;

import java.util.Date;
import java.util.List;

import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.BuyStockBean;
import com.ghlh.strategy.TradeConstants;
import com.ghlh.strategy.TradeUtil;
import com.ghlh.strategy.stair.StairConstants;
import com.ghlh.tradeway.SoftwareTrader;

public class StockTradeIntradyUtil {

	public static void processSell(StockTradeIntradyMonitor monitor,
			StockQuotesBean sqb) {
		List possibleSellList = monitor.getPossibleSellList();
		List pendingBuyList = monitor.getPendingBuyList();
		for (int i = 0; i < possibleSellList.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) possibleSellList.get(i);
			if (stocktradeVO.getStatus() != TradeConstants.STATUS_SUCCESS
					&& stocktradeVO.getStatus() != TradeConstants.STATUS_FAILURE) {
				if (sqb.getHighestPrice() >= stocktradeVO.getWinsellprice()) {
					processWin(monitor, pendingBuyList, stocktradeVO);
				}

				if (sqb.getLowestPrice() <= stocktradeVO.getLostsellprice()) {
					processLost(stocktradeVO);
				}
			}
		}
	}

	private static void processLost(StocktradeVO stocktradeVO) {
		String message = TradeUtil.getConfirmedSellMessage(
				stocktradeVO.getStockid(), stocktradeVO.getNumber(),
				stocktradeVO.getLostsellprice());
		EventRecorder.recordEvent(StockTradeIntradyUtil.class, message);
		SoftwareTrader.getInstance().sellStock(stocktradeVO.getStockid(),
				stocktradeVO.getNumber());
		stocktradeVO.setSelldate(new Date());
		stocktradeVO.setStatus(TradeConstants.STATUS_FAILURE);

		StocktradeDAO.updateStocktradeFailure(stocktradeVO.getId());
	}

	private static void processWin(StockTradeIntradyMonitor monitor,
			List pendingBuyList, StocktradeVO stocktradeVO) {
		String message = TradeUtil.getConfirmedSellMessage(
				stocktradeVO.getStockid(), stocktradeVO.getNumber(),
				stocktradeVO.getWinsellprice());
		EventRecorder.recordEvent(StockTradeIntradyUtil.class, message);
		SoftwareTrader.getInstance().sellStock(stocktradeVO.getStockid(),
				stocktradeVO.getNumber());
		stocktradeVO.setSelldate(new Date());
		stocktradeVO.setStatus(TradeConstants.STATUS_SUCCESS);

		StocktradeDAO.updateStocktradeFinished(stocktradeVO.getId());
		if (Boolean.valueOf(monitor.getMonitorstockVO().getOnmonitoring())) {
			reBuy(stocktradeVO, pendingBuyList);
		}
	}

	private static void reBuy(StocktradeVO stocktradeVO, List pendingBuyList) {
		String message = TradeUtil.getPendingBuyMessage(
				stocktradeVO.getStockid(), stocktradeVO.getNumber(),
				stocktradeVO.getBuyprice());
		EventRecorder.recordEvent(StockTradeIntradyUtil.class, message);

		BuyStockBean buyStockBean = new BuyStockBean();
		buyStockBean.setStockId(stocktradeVO.getStockid());
		buyStockBean.setBuyPrice(stocktradeVO.getBuyprice());
		buyStockBean.setWinSellPrice(stocktradeVO.getWinsellprice());
		buyStockBean.setStrategy(stocktradeVO.getTradealgorithm());
		buyStockBean.setNumber(stocktradeVO.getNumber());
		buyStockBean.setPreviousTradeId(stocktradeVO.getId());
		buyStockBean.setLostSellPrice(stocktradeVO.getLostsellprice());
		TradeUtil.dealBuyStock(buyStockBean);

		refreshPendingBuyList(stocktradeVO.getStockid(), pendingBuyList,
				stocktradeVO.getTradealgorithm());
	}

	private static void refreshPendingBuyList(String stockId,
			List pendingBuyList, String strategy) {
		int size = pendingBuyList.size();
		for (int i = 0; i < size; i++) {
			pendingBuyList.remove(0);
		}

		List newPendingBuy = StocktradeDAO.getPendingBuyTradeRecords(stockId,
				StairConstants.STAIR_STRATEGY_NAME);
		pendingBuyList.addAll(newPendingBuy);

	}

	public static boolean processBuy(StockTradeIntradyMonitor monitor,
			StockQuotesBean sqb) {
		boolean result = false;
		List pendingBuyList = monitor.getPendingBuyList();
		for (int j = 0; j < pendingBuyList.size(); j++) {
			StocktradeVO stVO = (StocktradeVO) pendingBuyList.get(j);
			if (stVO.getStatus() != TradeConstants.STATUS_T_0_BUY) {
				if (TradeUtil.isStopTrade(sqb)) {
					return false;
				}
				if (sqb.getLowestPrice() <= stVO.getBuyprice()) {
					String message = TradeUtil.getConfirmedBuyMessage(monitor
							.getMonitorstockVO().getStockid(),
							stVO.getNumber(), stVO.getBuyprice());

					EventRecorder.recordEvent(StockTradeIntradyUtil.class,
							message);
					SoftwareTrader.getInstance().buyStock(stVO.getStockid(),
							stVO.getNumber());
					StocktradeDAO.updateStocktradeStatus(stVO.getId(),
							TradeConstants.STATUS_T_0_BUY);
					stVO.setStatus(TradeConstants.STATUS_T_0_BUY);
					result = true;
				}
			}
		}
		return result;
	}

}
