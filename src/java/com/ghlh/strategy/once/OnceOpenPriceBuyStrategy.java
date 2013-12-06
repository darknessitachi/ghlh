package com.ghlh.strategy.once;

import java.util.List;

import com.ghlh.autotrade.EventRecorder;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.AdditionInfoUtil;
import com.ghlh.strategy.OneTimeStrategy;
import com.ghlh.strategy.TradeUtil;
import com.ghlh.util.MathUtil;
import com.ghlh.util.StockMarketUtil;

public class OnceOpenPriceBuyStrategy implements OneTimeStrategy {
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
			if (aib.getBuyPriceStrategy().equals("开盘价")
					&& StockMarketUtil.isMorningOpen()
					|| aib.getBuyPriceStrategy().equals("午盘价")
					&& StockMarketUtil.isAfternoonOpen()) {
				int priceType = TradeUtil.getPriceType(aib
						.getBuyPriceStrategy());
				// if (aib.getBuyPriceStrategy().equals("收盘价")) {
				// priceType = TradeUtil.PRICE_CLOSE;
				// }
				double sellPrice = sqb.getCurrentPrice() * (1 + aib.getTargetZf());
				sellPrice = MathUtil.formatDoubleWith2QuanShe(sellPrice);
				String message = TradeUtil.getIntradyPriceBuyMessage(
						monitorstockVO.getStockid(),
						TradeUtil.getTradeNumber(aib.getTradeMoney(),
								sqb.getCurrentPrice()), sqb.getCurrentPrice(),
						priceType);
				EventRecorder.recordEvent(this.getClass(), message);
				TradeUtil.dealBuyStockSuccessfully(monitorstockVO.getStockid(),
						aib.getTradeMoney(), sqb.getCurrentPrice(), sellPrice,
						monitorstockVO.getTradealgorithm());
			}
		}
	}
}
