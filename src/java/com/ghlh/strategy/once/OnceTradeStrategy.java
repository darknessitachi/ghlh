package com.ghlh.strategy.once;

import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.ghlh.stockpool.MonitorStockBean;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.TradeResult;
import com.ghlh.strategy.TradeStrategy;
import com.ghlh.util.StringUtil;

public class OnceTradeStrategy implements TradeStrategy {

	@Override
	public TradeResult processStockTrade(MonitorStockBean monitorStockBean,
			StockQuotesBean stockQuotesBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String collectAdditionalInfoFromUIComponents(List uiComponents) {
		AdditionalInfoBean additionalInfoBean = new AdditionalInfoBean();
		additionalInfoBean.setCmd(((JComboBox) uiComponents.get(3))
				.getSelectedItem().toString());
		additionalInfoBean.setTargetPrice((Float.parseFloat(StringUtil
				.getDefaultNumberWithZero(((JTextField) uiComponents.get(4))
						.getText()))));
		additionalInfoBean.setNumber(Integer.parseInt(StringUtil
				.getDefaultNumberWithZero(((JTextField) uiComponents.get(5))
						.getText())));
		additionalInfoBean.setStatus(((JComboBox) uiComponents.get(6))
				.getSelectedItem().toString());
		return parseAdditionalInfoBeanBack(additionalInfoBean);
	}

	public void setAdditionalInfoToUIComponents(List uiComponents,
			String additionInfo) {
		AdditionalInfoBean additionalInfoBean = parseAdditionalInfoBean(additionInfo);
		((JComboBox) uiComponents.get(3)).setSelectedItem(additionalInfoBean
				.getCmd());
		((JTextField) uiComponents.get(4)).setText(additionalInfoBean
				.getTargetPrice() + "");
		((JTextField) uiComponents.get(5)).setText(additionalInfoBean
				.getNumber() + "");
		((JComboBox) uiComponents.get(6)).setSelectedItem(additionalInfoBean
				.getStatus());
	}

	public boolean hasChangedValueInAdditionalUIComponents(List uiComponents,
			MonitorStockBean currentMsb) {
		String additionalInfo = currentMsb.getAdditionInfo();
		Pattern pattern = Pattern.compile(",");
		String[] additionalInfoSegs = pattern.split(additionalInfo);
		for (int i = 3; i <= 6; i++) {
			String text = null;
			if (uiComponents.get(i) instanceof JTextField) {
				text = ((JTextField) uiComponents.get(i)).getText();
			} else if (uiComponents.get(i) instanceof JComboBox) {
				text = (String) ((JComboBox) uiComponents.get(i))
						.getSelectedItem();
			}
			if (!additionalInfoSegs[i - 3].trim().equals(text)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String updateCurrentNumber(String additionInfo, int cmd,
			int tradeNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	private AdditionalInfoBean parseAdditionalInfoBean(String additionalInfo) {
		if (additionalInfo == null) {
			return null;
		}
		AdditionalInfoBean result = new AdditionalInfoBean();
		Pattern pattern = Pattern.compile(",");
		String[] additionalInfoSegs = pattern.split(additionalInfo);
		result.setCmd(additionalInfoSegs[0].trim());
		result.setTargetPrice(Float.parseFloat(additionalInfoSegs[1].trim()));
		result.setNumber(Integer.parseInt(additionalInfoSegs[2].trim()));
		result.setStatus(additionalInfoSegs[3].trim());
		return result;
	}

	private String parseAdditionalInfoBeanBack(
			AdditionalInfoBean additionalInfoBean) {
		String line = additionalInfoBean.getCmd() + ",       "
				+ additionalInfoBean.getTargetPrice() + ",          "
				+ additionalInfoBean.getNumber() + ",         "
				+ additionalInfoBean.getStatus();
		return line;
	}

	private class AdditionalInfoBean {
		private String cmd;

		public String getCmd() {
			return cmd;
		}

		public void setCmd(String cmd) {
			this.cmd = cmd;
		}

		public float getTargetPrice() {
			return targetPrice;
		}

		public void setTargetPrice(float targetPrice) {
			this.targetPrice = targetPrice;
		}

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		private float targetPrice;
		private int number;
		private String status;
	}
}
