package com.ghlh.strategy.once;

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

public class OnceBeforeOpenStrategy implements OneTimeStrategy {
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
		StocktradeVO stocktradeVO = (StocktradeVO) stockTradeList.get(0);
		if (stocktradeVO.getSellprice() < possibleMaxPrice) {
			String message = "盘前卖出股票并下单  : " + stocktradeVO.getStockid()
					+ " 价格:" + stocktradeVO.getSellprice() + " 数量:"
					+ stocktradeVO.getNumber();
			EventRecorder.recordEvent(this.getClass(), message);
			TradeUtil.dealSell(stocktradeVO);
		}
	}

	private void dealBuy(MonitorstockVO monitorstockVO) {
		AdditionalInfoBean aib = (AdditionalInfoBean) AdditionInfoUtil
				.parseAdditionalInfoBean(monitorstockVO.getAdditioninfo(),
						OnceConstants.ONCE_STRATEGY_NAME);
		if (aib.getBuyPriceStrategy().equals("设定价")) {
			double sellPrice = aib.getBuyPrice() * (1 + aib.getTargetZf());
			sellPrice = MathUtil.formatDoubleWith2QuanShe(sellPrice);
			String message = "盘前买入股票不下单  : "
					+ monitorstockVO.getStockid()
					+ " 价格:"
					+ aib.getBuyPrice()
					+ " 数量:"
					+ TradeUtil.getTradeNumber(aib.getTradeMoney(),
							aib.getBuyPrice()) + "(盘中监控实际下单)";
			EventRecorder.recordEvent(this.getClass(), message);

			TradeUtil.dealBuyStock(monitorstockVO.getStockid(),
					aib.getTradeMoney(), aib.getBuyPrice(), sellPrice,
					OnceConstants.ONCE_STRATEGY_NAME);
		}
	}
}
