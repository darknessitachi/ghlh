package com.ghlh.strategy.stair;

import java.util.List;

import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.AdditionInfoUtil;
import com.ghlh.strategy.OneTimeStrategy;

public class StairAfterOpenPriceDecidedStrategy implements OneTimeStrategy{
	public void processStockTrade(MonitorstockVO monitorstockVO) {
		if (!Boolean.valueOf(monitorstockVO.getOnmonitoring())) {
			return;
		}
		List stockTradeList = StocktradeDAO
				.getHoldStocks(monitorstockVO.getStockid(),
						monitorstockVO.getTradealgorithm());
		if (stockTradeList.size() != 0) {
			return;
		}

		AdditionalInfoBean aib = (AdditionalInfoBean) AdditionInfoUtil
				.parseAdditionalInfoBean(monitorstockVO.getAdditioninfo(),
						monitorstockVO.getTradealgorithm());
		if (aib.getFirstBuyPriceStrategy().equals("���̼�")) {
			StockQuotesBean sqb = InternetStockQuotesInquirer.getInstance()
					.getStockQuotesBean(monitorstockVO.getStockid());
			double currentPrice = sqb.getCurrentPrice();
			dealBuy(monitorstockVO, aib, stockTradeList, sqb);

		}
	}

	public void dealBuy(MonitorstockVO monitorstockVO, AdditionalInfoBean aib,
			List stockTradeList, StockQuotesBean sqb) {
		double basePrice = sqb.getCurrentPrice();
		double currentPrice = sqb.getYesterdayClose();
		int spaceNumber = aib.getStairNumber() - stockTradeList.size();
		StairUtil.dealBuy(monitorstockVO.getStockid(), aib, basePrice,
				currentPrice, spaceNumber);
	}
}