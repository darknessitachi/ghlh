package com.ghlh.strategy.stair;

import java.util.List;

import com.ghlh.autotrade.EventRecorder;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.AdditionInfoUtil;
import com.ghlh.strategy.MonitoringStrategy;
import com.ghlh.strategy.TradeConstants;
import com.ghlh.strategy.TradeUtil;

public class StairIntradayStrategy implements MonitoringStrategy {
	public void processStockTrade(MonitorstockVO monitorstockVO,
			List possibleSell, List pendingBuy) {
		if (!Boolean.valueOf(monitorstockVO.getOnmonitoring())) {
			return;
		}
		AdditionalInfoBean aib = (AdditionalInfoBean) AdditionInfoUtil
				.parseAdditionalInfoBean(monitorstockVO.getAdditioninfo(),
						StairConstants.STAIR_STRATEGY_NAME);

		StockQuotesBean stockQuotesBean = InternetStockQuotesInquirer
				.getInstance().getStockQuotesBean(monitorstockVO.getStockid());
		double highestPrice = stockQuotesBean.getHighestPrice();
		boolean hasSell = false;
		for (int i = 0; i < possibleSell.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) possibleSell.get(i);
			if (highestPrice >= stocktradeVO.getSellprice()) {
				String message = "盘中监控股票  : " + monitorstockVO.getStockid()
						+ " 已达卖出价" + highestPrice + "成交, 重新设置买入, 买入价:"
						+ stocktradeVO.getBuyprice() + " 数量:"
						+ stocktradeVO.getNumber();
				EventRecorder.recordEvent(this.getClass(), message);
				StocktradeDAO.updateStocktradeFinished(stocktradeVO.getId());
				reBuyStock(monitorstockVO, stocktradeVO);
				refreshPendingBuyList(monitorstockVO, pendingBuy);
			}
		}
	}

	private void refreshPendingBuyList(MonitorstockVO monitorstockVO,
			List pendingBuy) {
		int size = pendingBuy.size();
		for (int i = 0; i < size; i++) {
			pendingBuy.remove(0);
		}

		List newPendingBuy = StocktradeDAO
				.getPendingBuyTradeRecords(monitorstockVO.getStockid(),
						StairConstants.STAIR_STRATEGY_NAME);

		pendingBuy.addAll(newPendingBuy);

	}

	private void reBuyStock(MonitorstockVO monitorstockVO,
			StocktradeVO stocktradeVO) {
		TradeUtil.dealBuyStock(monitorstockVO.getStockid(),
				stocktradeVO.getBuyprice(), stocktradeVO.getSellprice(),
				StairConstants.STAIR_STRATEGY_NAME, stocktradeVO.getNumber());

	}
}
