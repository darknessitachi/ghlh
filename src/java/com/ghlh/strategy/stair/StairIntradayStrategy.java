package com.ghlh.strategy.stair;

import java.util.Date;
import java.util.List;

import com.ghlh.data.db.GhlhDAO;
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
			List stocktradeList) {
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
		for (int i = 0; i < stocktradeList.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) stocktradeList.get(i);
			if (stocktradeVO.getStatus() == TradeConstants.STATUS_POSSIBLE_SELL) {
				if (highestPrice >= stocktradeVO.getSellprice()) {
					StocktradeVO stocktradeVO1 = new StocktradeVO();
					stocktradeVO1.setId(stocktradeVO.getId());
					stocktradeVO1.setWhereId(true);
					stocktradeVO1.setStatus(TradeConstants.STATUS_FINISH);
					stocktradeVO1.setSelldate(new Date());
					GhlhDAO.edit(stocktradeVO1);
					reBuyStock(monitorstockVO, stocktradeVO);
					hasSell = true;
				}
			}
		}
		refreshStockTradeList(monitorstockVO, stocktradeList, hasSell);

	}

	private void refreshStockTradeList(MonitorstockVO monitorstockVO,
			List stocktradeList, boolean hasSell) {
		if (hasSell) {
			for (int i = 0; i < stocktradeList.size(); i++) {
				stocktradeList.remove(i);
			}
			List stocktradeList1 = StocktradeDAO.getHoldStocks(
					monitorstockVO.getStockid(),
					StairConstants.STAIR_STRATEGY_NAME);

			stocktradeList.addAll(stocktradeList1);
		}
	}

	private void reBuyStock(MonitorstockVO monitorstockVO,
			StocktradeVO stocktradeVO) {
		TradeUtil.dealBuyStock(monitorstockVO.getStockid(),
				stocktradeVO.getBuyprice(), stocktradeVO.getSellprice(),
				StairConstants.STAIR_STRATEGY_NAME, stocktradeVO.getNumber());

	}
}
