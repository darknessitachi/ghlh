package com.ghlh.strategy.once;

import java.util.List;

import com.ghlh.autotrade.EventRecorder;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.AdditionInfoUtil;
import com.ghlh.strategy.BuyStockBean;
import com.ghlh.strategy.OneTimeStrategy;
import com.ghlh.strategy.TradeUtil;
import com.ghlh.util.MathUtil;
import com.ghlh.util.StockMarketUtil;

public class OnceIntradyFirstBuyStrategy implements OneTimeStrategy {
	public void processStockTrade(MonitorstockVO monitorstockVO) {
		if (!Boolean.valueOf(monitorstockVO.getOnmonitoring())) {
			return;
		}
		List stockTradeList = StocktradeDAO
				.getUnfinishedTradeRecords(monitorstockVO.getStockid(),
						monitorstockVO.getTradealgorithm());
		if (stockTradeList.size() == 0) {
			AdditionalInfoBean aib = (AdditionalInfoBean) AdditionInfoUtil
					.parseAdditionalInfoBean(monitorstockVO.getAdditioninfo(),
							monitorstockVO.getTradealgorithm());
			StockQuotesBean sqb = InternetStockQuotesInquirer.getInstance()
					.getStockQuotesBean(monitorstockVO.getStockid());
			if (aib.getBuyPriceStrategy().equals("¿ªÅÌ¼Û")
					&& StockMarketUtil.isMorningOpen()
					|| aib.getBuyPriceStrategy().equals("ÎçÅÌ¼Û")
					&& StockMarketUtil.isAfternoonOpen()) {
				int priceType = TradeUtil.getPriceType(aib
						.getBuyPriceStrategy());
				if(TradeUtil.isStopTrade(sqb)){
					return;
				}
				double winSellPrice = sqb.getCurrentPrice() * (1 + aib.getTargetZf());
				winSellPrice = MathUtil.formatDoubleWith2QuanShe(winSellPrice);
				String message = TradeUtil.getIntradyPriceBuyMessage(
						monitorstockVO.getStockid(),
						TradeUtil.getTradeNumber(aib.getTradeMoney(),
								sqb.getCurrentPrice()), sqb.getCurrentPrice(),
						priceType);
				EventRecorder.recordEvent(this.getClass(), message);
				BuyStockBean buyStockBean = new BuyStockBean();
				buyStockBean.setStockId(monitorstockVO.getStockid());
				buyStockBean.setTradeMoney(aib.getTradeMoney());
				buyStockBean.setBuyPrice(sqb.getCurrentPrice());
				buyStockBean.setWinSellPrice(winSellPrice);
				if (aib.getLostDf() > 0) {
					double lostSellPrice = sqb.getCurrentPrice()
							* (1 - aib.getLostDf());
					lostSellPrice = MathUtil
							.formatDoubleWith2QuanShe(lostSellPrice);
					buyStockBean.setLostSellPrice(lostSellPrice);
				}
				buyStockBean.setStrategy(monitorstockVO.getTradealgorithm());
				TradeUtil.dealBuyStockSuccessfully(buyStockBean);
			}
		}
	}
}
