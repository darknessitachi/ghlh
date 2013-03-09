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
			additionalInfoBean.setCurrentNumber(additionalInfoBean
					.getCurrentNumber() + tradeResult.getNumber());
		}
		if (tradeResult.getCmd() == Constants.SELL) {
			additionalInfoBean.setCurrentNumber(additionalInfoBean
					.getCurrentNumber() - tradeResult.getNumber());
			additionalInfoBean.setCanSellNumber(additionalInfoBean
					.getCanSellNumber() - tradeResult.getNumber());

		}
		additionalInfoBean.setStandardPrice(tradeResult.getTradePrice());
		monitorStockBean
				.setAdditionInfo(parseAdditionalInfoBeanBack(additionalInfoBean));
	}

	private boolean isReachSellCondition(MonitorStockBean monitorStockBean,
			StockQuotesBean stockQuotesBean,
			AdditionalInfoBean additionalInfoBean) {
		double sellingPrice = additionalInfoBean.getStandardPrice()
				* (additionalInfoBean.getStairZDF() + 1);
		boolean result = stockQuotesBean.getCurrentPrice() >= sellingPrice
				&& additionalInfoBean.getCanSellNumber() > 0;
		return result;
	}

	private boolean isReachBuyCondition(MonitorStockBean monitorStockBean,
			StockQuotesBean stockQuotesBean,
			AdditionalInfoBean additionalInfoBean) {
		double buyingPrice = additionalInfoBean.getStandardPrice()
				* (1 - additionalInfoBean.getStairZDF());
		boolean result = stockQuotesBean.getCurrentPrice() <= buyingPrice
				&& additionalInfoBean.getRank() < additionalInfoBean
						.getMaxRank();
		return result;
	}

	private AdditionalInfoBean parseAdditionalInfoBean(String additionalInfo) {
		if (additionalInfo == null) {
			return null;
		}
		AdditionalInfoBean result = new AdditionalInfoBean();
		Pattern pattern = Pattern.compile(",");
		String[] additionalInfoSegs = pattern.split(additionalInfo);
		result.setStandardPrice(Double.parseDouble(additionalInfoSegs[0].trim()));
		result.setCurrentNumber(Integer.parseInt(additionalInfoSegs[1].trim()));
		result.setCanSellNumber(Integer.parseInt(additionalInfoSegs[2].trim()));
		result.setStairZDF(Double.parseDouble(additionalInfoSegs[3].trim()));
		result.setTradeNumber(Integer.parseInt(additionalInfoSegs[4].trim()));
		result.setRank(Integer.parseInt(additionalInfoSegs[5].trim()));
		result.setMaxRank(Integer.parseInt(additionalInfoSegs[6].trim()));
		return result;
	}

	private String parseAdditionalInfoBeanBack(
			AdditionalInfoBean additionalInfoBean) {
		String line = additionalInfoBean.getStandardPrice() + ",       "
				+ additionalInfoBean.getCanSellNumber() + ",          "
				+ additionalInfoBean.getCurrentNumber() + ",         "
				+ additionalInfoBean.getStairZDF() + ",     "
				+ additionalInfoBean.getTradeNumber() + ",        "
				+ additionalInfoBean.getRank() + ",   "
				+ additionalInfoBean.getMaxRank();
		return line;
	}

	public String collectAdditionalInfoFromUIComponents(List uiComponents) {
		AdditionalInfoBean additionalInfoBean = new AdditionalInfoBean();
		additionalInfoBean.setStandardPrice(Double.parseDouble(StringUtil
				.getDefaultNumberWithZero(((JTextField) uiComponents.get(3))
						.getText())));
		additionalInfoBean.setCanSellNumber(Integer.parseInt(StringUtil
				.getDefaultNumberWithZero(((JTextField) uiComponents.get(4))
						.getText())));
		additionalInfoBean.setCurrentNumber(Integer.parseInt(StringUtil
				.getDefaultNumberWithZero(((JTextField) uiComponents.get(5))
						.getText())));
		additionalInfoBean.setStairZDF(Double.parseDouble(StringUtil
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

	public boolean hasChangedValueInAdditionalUIComponents(List uiComponents,
			MonitorStockBean currentMsb) {
		String additionalInfo = currentMsb.getAdditionInfo();
		Pattern pattern = Pattern.compile(",");
		String[] additionalInfoSegs = pattern.split(additionalInfo);
		for (int i = 3; i <= 9; i++) {
			String text = ((JTextField) uiComponents.get(i)).getText();
			if (!additionalInfoSegs[i - 3].trim().equals(text)) {
				return true;
			}
		}
		return false;
	}

	public String updateCurrentNumber(String additionInfo, int cmd,
			int tradeNumber) {
		AdditionalInfoBean additionalBean = parseAdditionalInfoBean(additionInfo);
		if (cmd == Constants.SELL) {
			additionalBean.setCurrentNumber(additionalBean.getCurrentNumber()
					- tradeNumber);
		} else if (cmd == Constants.BUY) {
			additionalBean.setCurrentNumber(additionalBean.getCurrentNumber()
					- tradeNumber);
		}
		String result = parseAdditionalInfoBeanBack(additionalBean);
		return result;
	}

	public void setAdditionalInfoToUIComponents(List uiComponents,
			String additionInfo) {
		AdditionalInfoBean additionalInfoBean = parseAdditionalInfoBean(additionInfo);
		((JTextField) uiComponents.get(3)).setText(additionalInfoBean
				.getStandardPrice() + "");
		((JTextField) uiComponents.get(4)).setText(additionalInfoBean
				.getCanSellNumber() + "");
		((JTextField) uiComponents.get(5)).setText(additionalInfoBean
				.getCurrentNumber() + "");
		((JTextField) uiComponents.get(6)).setText(additionalInfoBean
				.getStairZDF() + "");
		((JTextField) uiComponents.get(7)).setText(additionalInfoBean
				.getTradeNumber() + "");
		((JTextField) uiComponents.get(8)).setText(additionalInfoBean.getRank()
				+ "");
		((JTextField) uiComponents.get(9)).setText(additionalInfoBean
				.getMaxRank() + "");
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

		private double standardPrice;

		public double getStandardPrice() {
			return standardPrice;
		}

		public void setStandardPrice(double standardPrice) {
			this.standardPrice = standardPrice;
		}

		public int getCanSellNumber() {
			return canSellNumber;
		}

		public void setCanSellNumber(int canSellNumber) {
			this.canSellNumber = canSellNumber;
		}

		public int getCurrentNumber() {
			return currentNumber;
		}

		public void setCurrentNumber(int currentNumber) {
			this.currentNumber = currentNumber;
		}

		private int canSellNumber;
		private int currentNumber;
	}
}