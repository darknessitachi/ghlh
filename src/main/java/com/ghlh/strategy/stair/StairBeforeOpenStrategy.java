package com.ghlh.strategy.stair;

import java.util.List;

import com.ghlh.autotrade.EventRecorder;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.AdditionInfoUtil;
import com.ghlh.strategy.OneTimeStrategy;
import com.ghlh.strategy.TradeConstants;
import com.ghlh.strategy.TradeUtil;
import com.ghlh.util.MathUtil;

public class StairBeforeOpenStrategy implements OneTimeStrategy {
	public void processStockTrade(MonitorstockVO monitorstockVO) {
		String stockId = monitorstockVO.getStockid();
		AdditionalInfoBean aib = (AdditionalInfoBean) AdditionInfoUtil
				.parseAdditionalInfoBean(monitorstockVO.getAdditioninfo(),
						StairConstants.STAIR_STRATEGY_NAME);
		StockQuotesBean stockQuotesBean = InternetStockQuotesInquirer
				.getInstance().getStockQuotesBean(stockId);
		List stockTradeList = StocktradeDAO.getUnfinishedTradeRecords(stockId,
				monitorstockVO.getTradealgorithm());
		double currentPrice = stockQuotesBean.getCurrentPrice();
		if (currentPrice == 0) {
			currentPrice = stockQuotesBean.getYesterdayClose();
		}

		dealSell(stockId, stockTradeList, currentPrice);

		dealBuy(monitorstockVO, aib, stockTradeList, currentPrice);

	}

	public void dealBuy(MonitorstockVO monitorstockVO, AdditionalInfoBean aib,
			List stockTradeList, double currentPrice) {
		if (Boolean.valueOf(monitorstockVO.getOnmonitoring())) {
			double basePrice = getBasePrice(aib, stockTradeList);
			if (basePrice == 0) {
				return;
			}
			int spaceNumber = aib.getStairNumber() - stockTradeList.size();
			StairUtil.dealBuy(monitorstockVO.getStockid(), aib, basePrice,
					currentPrice, spaceNumber);
		}
	}

	public double getBasePrice(AdditionalInfoBean aib, List stockTradeList) {
		double basePrice = 0;
		if (stockTradeList.size() > 0) {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList
					.get(stockTradeList.size() - 1);
			basePrice = MathUtil.formatDoubleWith2QuanJin(stocktradeVO
					.getBuyprice() * (1 - aib.getStairZDF()));
		} else {
			if (aib.getFirstBuyPriceStrategy().equals("Éè¶¨¼Û")
					&& aib.getFirstBuyPrice() > 0) {
				basePrice = aib.getFirstBuyPrice();

			} else {
				return 0;
			}
		}
		return basePrice;
	}

	private void dealSell(String stockId, List stockTradeList,
			double currentPrice) {
		double possibleMaxPrice = currentPrice * TradeConstants.MAX_ZF;
		for (int i = 0; i < stockTradeList.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(i);
			if (stocktradeVO.getWinsellprice() < possibleMaxPrice) {
				String message = TradeUtil.getPendingSellMessage(
						stocktradeVO.getStockid(), stocktradeVO.getNumber(),
						stocktradeVO.getWinsellprice());
				EventRecorder.recordEvent(this.getClass(), message);
				TradeUtil.dealSell(stocktradeVO);
			}
		}
	}

}
