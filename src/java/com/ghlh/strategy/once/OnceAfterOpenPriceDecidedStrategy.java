package com.ghlh.strategy.once;

import java.util.List;

import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.AdditionInfoUtil;
import com.ghlh.strategy.OneTimeStrategy;
import com.ghlh.strategy.TradeUtil;
import com.ghlh.util.MathUtil;

public class OnceAfterOpenPriceDecidedStrategy implements OneTimeStrategy {
	public void processStockTrade(MonitorstockVO monitorstockVO) {
		if (!Boolean.valueOf(monitorstockVO.getOnmonitoring())) {
			return;
		}
		List stockTradeList = StocktradeDAO
				.getHoldStocks(monitorstockVO.getStockid(),
						monitorstockVO.getTradealgorithm());
		if (stockTradeList.size() == 0) {
			AdditionalInfoBean aib = (AdditionalInfoBean) AdditionInfoUtil
					.parseAdditionalInfoBean(monitorstockVO.getAdditioninfo(),
							monitorstockVO.getTradealgorithm());
			StockQuotesBean sqb = InternetStockQuotesInquirer.getInstance()
					.getStockQuotesBean(monitorstockVO.getStockid());
			double sellPrice = sqb.getCurrentPrice() * (1 + aib.getTargetZf());
			sellPrice = MathUtil.formatDoubleWith2QuanShe(sellPrice);
			if (aib.getBuyPriceStrategy().equals("¿ªÅÌ¼Û")) {
				TradeUtil.dealBuyStock(monitorstockVO.getStockid(),
						aib.getTradeMoney(), sqb.getCurrentPrice(), sellPrice,
						monitorstockVO.getTradealgorithm());
			}
		}
	}
}
