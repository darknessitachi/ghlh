package com.ghlh.strategy.stair;

import java.util.List;

import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.AdditionInfoUtil;
import com.ghlh.strategy.OneTimeStrategy;
import com.ghlh.strategy.TradeUtil;
import com.ghlh.util.StockMarketUtil;

public class StairIntradyFirstBuyStrategy implements OneTimeStrategy {
	public void processStockTrade(MonitorstockVO monitorstockVO) {
		if (!Boolean.valueOf(monitorstockVO.getOnmonitoring())) {
			return;
		}
		List stockTradeList = StocktradeDAO
				.getUnfinishedTradeRecords(monitorstockVO.getStockid(),
						monitorstockVO.getTradealgorithm());
		if (stockTradeList.size() != 0) {
			return;
		}

		AdditionalInfoBean aib = (AdditionalInfoBean) AdditionInfoUtil
				.parseAdditionalInfoBean(monitorstockVO.getAdditioninfo(),
						monitorstockVO.getTradealgorithm());
		if (aib.getFirstBuyPriceStrategy().equals("开盘价")
				&& StockMarketUtil.isMorningOpen()
				|| aib.getFirstBuyPriceStrategy().equals("午盘价")
				&& StockMarketUtil.isAfternoonOpen()) {
			int priceType = TradeUtil.getPriceType(aib
					.getFirstBuyPriceStrategy());
			// if (aib.getBuyPriceStrategy().equals("收盘价")) {
			// priceType = TradeUtil.PRICE_CLOSE;
			// }
			// if (aib.getFirstBuyPriceStrategy().equals("开盘价")) {
			dealBuy(monitorstockVO, aib, stockTradeList, priceType);
		}
	}

	public void dealBuy(MonitorstockVO monitorstockVO, AdditionalInfoBean aib,
			List stockTradeList, int priceType) {
		StockQuotesBean sqb = InternetStockQuotesInquirer.getInstance()
				.getStockQuotesBean(monitorstockVO.getStockid());
		double basePrice = sqb.getCurrentPrice();
		double currentPrice = sqb.getYesterdayClose();
		int spaceNumber = aib.getStairNumber() - stockTradeList.size();
		StairUtil.dealBuyIntradyFirstBuy(monitorstockVO.getStockid(), aib,
				basePrice, currentPrice, spaceNumber, priceType);
	}
}
