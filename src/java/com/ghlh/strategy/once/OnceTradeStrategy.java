package com.ghlh.strategy.once;

import com.ghlh.Constants;
import com.ghlh.stockpool.MonitorStockBean;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.AdditionInfoUtil;
import com.ghlh.strategy.TradeResult;
import com.ghlh.strategy.TradeStrategy;

public class OnceTradeStrategy implements TradeStrategy {
	private String getStrategyName() {
		return "Once";
	}

	public TradeResult processStockTrade(MonitorStockBean monitorStockBean,
			StockQuotesBean stockQuotesBean) {
		TradeResult tradeResult = new TradeResult();
		AdditionalInfoBean additionalInfoBean = (AdditionalInfoBean) AdditionInfoUtil
				.parseAdditionalInfoBean(monitorStockBean.getAdditionInfo(),
						getStrategyName());
		tradeResult.setStockId(monitorStockBean.getStockId());
		tradeResult.setTradePrice(stockQuotesBean.getCurrentPrice());
		tradeResult.setNumber(additionalInfoBean.getNumber());
		if ("δ����".equals(additionalInfoBean.getStatus())) {
			if (isReachTradeCondition(stockQuotesBean, additionalInfoBean)) {
				if (additionalInfoBean.getCmd().equals("����")) {
					tradeResult.setCmd(Constants.BUY);
				} else {
					tradeResult.setCmd(Constants.SELL);
				}
				syncMonitorStock(monitorStockBean, tradeResult,
						additionalInfoBean);
			}
		}
		return tradeResult;
	}

	private boolean isReachTradeCondition(StockQuotesBean stockQuotesBean,
			AdditionalInfoBean additionalInfoBean) {
		return additionalInfoBean.getRelationShipWithTargetPrice().equals("����")
				&& stockQuotesBean.getCurrentPrice() >= additionalInfoBean
						.getTargetPrice()
				|| additionalInfoBean.getRelationShipWithTargetPrice().equals(
						"С��")
				&& stockQuotesBean.getCurrentPrice() <= additionalInfoBean
						.getTargetPrice();
	}

	private void syncMonitorStock(MonitorStockBean monitorStockBean,
			TradeResult tradeResult, AdditionalInfoBean additionalInfoBean) {
		additionalInfoBean.setStatus("�ɹ�");
		monitorStockBean.setAdditionInfo(AdditionInfoUtil
				.parseAdditionalInfoBeanBack(additionalInfoBean,
						getStrategyName()));
	}
}
