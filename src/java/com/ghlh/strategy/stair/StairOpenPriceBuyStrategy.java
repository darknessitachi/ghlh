package com.ghlh.strategy.stair;

import java.util.List;

import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.AdditionInfoUtil;
import com.ghlh.strategy.OneTimeStrategy;

public class StairOpenPriceBuyStrategy implements OneTimeStrategy {
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
		if (aib.getFirstBuyPriceStrategy().equals("¿ªÅÌ¼Û")) {
			dealBuy(monitorstockVO, aib, stockTradeList);
		}
	}

	public void dealBuy(MonitorstockVO monitorstockVO, AdditionalInfoBean aib,
			List stockTradeList) {
		StockQuotesBean sqb = InternetStockQuotesInquirer.getInstance()
				.getStockQuotesBean(monitorstockVO.getStockid());
		double basePrice = sqb.getTodayOpen();
		double currentPrice = sqb.getYesterdayClose();
		int spaceNumber = aib.getStairNumber() - stockTradeList.size();
		StairUtil.dealBuyWithOpenPrice(monitorstockVO.getStockid(), aib,
				basePrice, currentPrice, spaceNumber);
	}
}
