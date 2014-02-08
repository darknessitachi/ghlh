package com.ghlh.strategy.catchyzstair;

import java.util.List;

import com.ghlh.autotrade.EventRecorder;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.AdditionInfoUtil;
import com.ghlh.strategy.BuyStockBean;
import com.ghlh.strategy.OneTimeStrategy;
import com.ghlh.strategy.TradeConstants;
import com.ghlh.strategy.TradeUtil;
import com.ghlh.util.MathUtil;

public class CatchYZStairBeforeOpenStrategy implements OneTimeStrategy {
	public void processStockTrade(MonitorstockVO monitorstockVO) {
		List stockTradeList = StocktradeDAO
				.getUnfinishedTradeRecords(monitorstockVO.getStockid(),
						monitorstockVO.getTradealgorithm());
		if (stockTradeList.size() == 0) {
			if (Boolean.parseBoolean(monitorstockVO.getOnmonitoring())) {
				dealBuy(monitorstockVO);
			}
		} else {
			dealSell(monitorstockVO, stockTradeList);
		}
	}

	private void dealSell(MonitorstockVO monitorstockVO, List stockTradeList) {
		StockQuotesBean stockQuotesBean = InternetStockQuotesInquirer
				.getInstance().getStockQuotesBean(monitorstockVO.getStockid());
		double currentPrice = stockQuotesBean.getCurrentPrice();
		double possibleMaxPrice = currentPrice * TradeConstants.MAX_ZF;
		double possibleMinPrice = currentPrice * TradeConstants.MAX_DF;
		StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(0);
		if (stocktradeVO.getWinsellprice() < possibleMaxPrice
				|| stocktradeVO.getLostsellprice() > possibleMinPrice) {
			String message = TradeUtil.getPendingSellMessage(
					stocktradeVO.getStockid(), stocktradeVO.getNumber(),
					stocktradeVO.getWinsellprice(),
					stocktradeVO.getLostsellprice());
			EventRecorder.recordEvent(this.getClass(), message);
			TradeUtil.dealSell(stocktradeVO);
		}
	}

	private void dealBuy(MonitorstockVO monitorstockVO) {
		AdditionalInfoBean aib = (AdditionalInfoBean) AdditionInfoUtil
				.parseAdditionalInfoBean(monitorstockVO.getAdditioninfo(),
						CatchYZStairConstants.CATCHYZSTAIR_STRATEGY_NAME);
		StockQuotesBean sqb = InternetStockQuotesInquirer
				.getEastMoneyInstance().getStockQuotesBean(
						monitorstockVO.getStockid());
		double buyPrice = sqb.getCurrentPrice() * (1 + aib.getBuyZdf());
		buyPrice = MathUtil.formatDoubleWith2QuanJin(buyPrice);
		
		double winSellPrice = buyPrice * (1 + aib.getTargetZf());
		winSellPrice = MathUtil.formatDoubleWith2QuanShe(winSellPrice);

		String message = TradeUtil.getPendingBuyMessage(
				monitorstockVO.getStockid(),
				TradeUtil.getTradeNumber(aib.getTradeMoney(), buyPrice),
				buyPrice);
		EventRecorder.recordEvent(this.getClass(), message);
		BuyStockBean buyStockBean = new BuyStockBean();
		buyStockBean.setStockId(monitorstockVO.getStockid());
		buyStockBean.setTradeMoney(aib.getTradeMoney());
		buyStockBean.setBuyPrice(buyPrice);
		buyStockBean.setWinSellPrice(winSellPrice);
		if (aib.getLostDf() > 0) {
			double lostSellPrice = buyPrice * (1 - aib.getLostDf());
			lostSellPrice = MathUtil.formatDoubleWith2QuanShe(lostSellPrice);
			buyStockBean.setLostSellPrice(lostSellPrice);
		}
		buyStockBean
				.setStrategy(CatchYZStairConstants.CATCHYZSTAIR_STRATEGY_NAME);
		TradeUtil.dealBuyStock(buyStockBean);
	}

}
