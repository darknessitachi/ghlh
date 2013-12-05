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
			double sellPrice = sqb.getTodayOpen() * (1 + aib.getTargetZf());
			sellPrice = MathUtil.formatDoubleWith2QuanShe(sellPrice);
			if (aib.getBuyPriceStrategy().equals("¿ªÅÌ¼Û")) {
				String message = TradeUtil.getOpenPriceBuyMessage(
						monitorstockVO.getStockid(),
						TradeUtil.getTradeNumber(aib.getTradeMoney(),
								aib.getBuyPrice()), sqb.getTodayOpen());
				EventRecorder.recordEvent(this.getClass(), message);
				TradeUtil.dealBuyStockSuccessfully(monitorstockVO.getStockid(),
						aib.getTradeMoney(), sqb.getTodayOpen(), sellPrice,
						monitorstockVO.getTradealgorithm());
			}
		}
	}
}
