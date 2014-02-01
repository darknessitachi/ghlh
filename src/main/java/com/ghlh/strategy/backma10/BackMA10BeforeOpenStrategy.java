package com.ghlh.strategy.backma10;

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

public class BackMA10BeforeOpenStrategy implements OneTimeStrategy {
	public void processStockTrade(MonitorstockVO monitorstockVO) {
		List stockTradeList = StocktradeDAO
				.getUnfinishedTradeRecords(monitorstockVO.getStockid(),
						monitorstockVO.getTradealgorithm());
		if (stockTradeList.size() != 0) {
			dealSell(monitorstockVO, stockTradeList);
		}
	}

	private void dealSell(MonitorstockVO monitorstockVO, List stockTradeList) {
		StockQuotesBean stockQuotesBean = InternetStockQuotesInquirer
				.getInstance().getStockQuotesBean(monitorstockVO.getStockid());
		double currentPrice = stockQuotesBean.getCurrentPrice();
		double possibleMaxPrice = currentPrice * TradeConstants.MAX_ZF;
		StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(0);
		if (stocktradeVO.getWinsellprice() < possibleMaxPrice) {
			String message = TradeUtil.getPendingSellMessage(
					stocktradeVO.getStockid(), stocktradeVO.getNumber(),
					stocktradeVO.getWinsellprice());
			EventRecorder.recordEvent(this.getClass(), message);
			TradeUtil.dealSell(stocktradeVO);
		}
	}

}
