package com.ghlh.strategy.stair;

import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JTextField;

import com.ghlh.Constants;
import com.ghlh.stockpool.MonitorStockBean;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.TradeResult;
import com.ghlh.strategy.TradeStrategy;
import com.ghlh.util.StringUtil;

public class StairTradeStrategy implements TradeStrategy {
	public TradeResult processStockTrade(MonitorStockBean monitorStockBean,
			StockQuotesBean stockQuotesBean) {
		TradeResult tradeResult = new TradeResult();
		AdditionalInfoBean additionalInfoBean = parseAdditionalInfoBean(monitorStockBean
				.getAdditionInfo());
		tradeResult.setStockId(monitorStockBean.getStockId());
		tradeResult.setTradePrice(stockQuotesBean.getCurrentPrice());
		if (isReachSellCondition(monitorStockBean, stockQuotesBean,
				additionalInfoBean)) {
			tradeResult.setCmd(Constants.SELL);
			int tradeNumber = com.ghlh.util.MathUtil.getNSquareM(2,
					additionalInfoBean.getRank() - 1)
					* additionalInfoBean.getTradeNumber();
			tradeResult.setNumber(tradeNumber);

			additionalInfoBean.setRank(additionalInfoBean.getRank() - 1);

			syncMonitorStock(monitorStockBean, tradeResult, additionalInfoBean);

		} else if (isReachBuyCondition(monitorStockBean, stockQuotesBean,
				additionalInfoBean)) {
			tradeResult.setCmd(Constants.BUY);
			int tradeNumber = com.ghlh.util.MathUtil.getNSquareM(2,
					additionalInfoBean.getRank())
					* additionalInfoBean.getTradeNumber();
			tradeResult.setNumber(tradeNumber);
			additionalInfoBean.setRank(additionalInfoBean.getRank() + 1);

			syncMonitorStock(monitorStockBean, tradeResult, additionalInfoBean);
		}
		return tradeResult;
	}

	private void syncMonitorStock(MonitorStockBean monitorStockBean,
			TradeResult tradeResult, AdditionalInfoBean additionalInfoBean) {
		if (tradeResult.getCmd() == Constants.BUY) {
			monitorStockBean.setCurrentNumber(monitorStockBean
					.getCurrentNumber() + tradeResult.getNumber());
		}
		if (tradeResult.getCmd() == Constants.SELL) {
			monitorStockBean.setCurrentNumber(monitorStockBean
					.getCurrentNumber() - tradeResult.getNumber());
			monitorStockBean.setCanSellNumber(monitorStockBean
					.getCanSellNumber() - tradeResult.getNumber());

		}
		monitorStockBean.setStandardPrice(tradeResult.getTradePrice());
		monitorStockBean
				.setAdditionInfo(parseAdditionalInfoBeanBack(additionalInfoBean));
	}

	private boolean isReachSellCondition(MonitorStockBean monitorStockBean,
			StockQuotesBean stockQuotesBean,
			AdditionalInfoBean additionalInfoBean) {
		double sellingPrice = monitorStockBean.getStandardPrice()
				* (additionalInfoBean.getStairZDF() + 1);
		boolean result = stockQuotesBean.getCurrentPrice() >= sellingPrice
				&& monitorStockBean.getCanSellNumber() > 0;
		return result;
	}

	private boolean isReachBuyCondition(MonitorStockBean monitorStockBean,
			StockQuotesBean stockQuotesBean,
			AdditionalInfoBean additionalInfoBean) {
		double buyingPrice = monitorStockBean.getStandardPrice()
				* (1 - additionalInfoBean.getStairZDF());
		boolean result = stockQuotesBean.getCurrentPrice() <= buyingPrice
				&& additionalInfoBean.getRank() < additionalInfoBean
						.getMaxRank();
		return result;
	}

	private AdditionalInfoBean parseAdditionalInfoBean(String additionalInfo) {
		AdditionalInfoBean result = new AdditionalInfoBean();
		Pattern pattern = Pattern.compile(",");
		String[] additionalInfoSegs = pattern.split(additionalInfo);
		result.setStairZDF(Double.parseDouble(additionalInfoSegs[0].trim()));
		result.setTradeNumber(Integer.parseInt(additionalInfoSegs[1].trim()));
		result.setRank(Integer.parseInt(additionalInfoSegs[2].trim()));
		result.setMaxRank(Integer.parseInt(additionalInfoSegs[3].trim()));
		return result;
	}

	private String parseAdditionalInfoBeanBack(
			AdditionalInfoBean additionalInfoBean) {
		String line = additionalInfoBean.getStairZDF() + ",     "
				+ additionalInfoBean.getTradeNumber() + ",        "
				+ additionalInfoBean.getRank() + ",   "
				+ additionalInfoBean.getMaxRank();
		return line;
	}

	public String collectAdditionalInfoFromUIComponents(List uiComponents) {
		AdditionalInfoBean additionalInfoBean = new AdditionalInfoBean();
		additionalInfoBean.setStairZDF(Integer.parseInt(StringUtil
				.getDefaultNumberWithZero(((JTextField) uiComponents.get(6))
						.getText())));
		additionalInfoBean.setTradeNumber(Integer.parseInt(StringUtil
				.getDefaultNumberWithZero(((JTextField) uiComponents.get(7))
						.getText())));
		additionalInfoBean.setRank(Integer.parseInt(StringUtil
				.getDefaultNumberWithZero(((JTextField) uiComponents.get(8))
						.getText())));
		additionalInfoBean.setMaxRank(Integer.parseInt(StringUtil
				.getDefaultNumberWithZero(((JTextField) uiComponents.get(9))
						.getText())));
		return parseAdditionalInfoBeanBack(additionalInfoBean);
	}

	private class AdditionalInfoBean {
		private double stairZDF;

		public double getStairZDF() {
			return stairZDF;
		}

		public void setStairZDF(double stairZDF) {
			this.stairZDF = stairZDF;
		}

		public int getTradeNumber() {
			return tradeNumber;
		}

		public void setTradeNumber(int tradeNumber) {
			this.tradeNumber = tradeNumber;
		}

		public int getRank() {
			return rank;
		}

		public void setRank(int rank) {
			this.rank = rank;
		}

		private int tradeNumber;
		private int rank;

		private int maxRank;

		public int getMaxRank() {
			return maxRank;
		}

		public void setMaxRank(int maxRank) {
			this.maxRank = maxRank;
		}

	}
}