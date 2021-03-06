package com.ghlh.strategy.stair;

import java.util.List;

import com.ghlh.autotrade.EventRecorder;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.AdditionInfoUtil;
import com.ghlh.strategy.OneTimeStrategy;
import com.ghlh.strategy.TradeUtil;
import com.ghlh.util.MathUtil;
import com.ghlh.util.StockMarketUtil;

public class StairBeforeCloseStrategy implements OneTimeStrategy {
	public void processStockTrade(MonitorstockVO monitorstockVO) {
		if (!Boolean.valueOf(monitorstockVO.getOnmonitoring())) {
			return;
		}
		List holdingTradeList = StocktradeDAO
				.getIntradyHoldingTradeRecords(monitorstockVO.getStockid(),
						monitorstockVO.getTradealgorithm());

		if (holdingTradeList.size() == 0) {
			List rebuyTradeList = StocktradeDAO.getPendingRebuyTradeRecords(
					monitorstockVO.getStockid(),
					monitorstockVO.getTradealgorithm());
			if (rebuyTradeList.size() > 0) {
				StocktradeVO stocktradeVO = (StocktradeVO) rebuyTradeList
						.get(0);
				StockQuotesBean sqb = InternetStockQuotesInquirer.getInstance()
						.getStockQuotesBean(monitorstockVO.getStockid());
				if(TradeUtil.isStopTrade(sqb)){
					return;
				}
				if (sqb.getCurrentPrice() < stocktradeVO.getWinsellprice()) {
					AdditionalInfoBean aib = (AdditionalInfoBean) AdditionInfoUtil
							.parseAdditionalInfoBean(
									monitorstockVO.getAdditioninfo(),
									monitorstockVO.getTradealgorithm());
					double sellPrice = sqb.getCurrentPrice()
							* (1 + aib.getStairZDF());
					sellPrice = MathUtil.formatDoubleWith2QuanShe(sellPrice);
					String message = TradeUtil.getIntradyPriceBuyMessage(
							monitorstockVO.getStockid(),
							TradeUtil.getTradeNumber(aib.getStairMoney(),
									sqb.getCurrentPrice()),
							sqb.getCurrentPrice(), TradeUtil.PRICE_CLOSE);
					EventRecorder.recordEvent(this.getClass(), message);
					TradeUtil.dealBuyStockSuccessfully(
							monitorstockVO.getStockid(), aib.getStairMoney(),
							sqb.getCurrentPrice(), sellPrice,
							monitorstockVO.getTradealgorithm());
				}
			}
		}
	}
}
