package com.ghlh.ui.autotrade.stocksetting;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.ghlh.stockpool.FileStockPoolAccessor;
import com.ghlh.stockpool.MonitorStockBean;
import com.ghlh.strategy.TradeStrategy;
import com.ghlh.ui.AbstractButtonActionListener;
import com.ghlh.ui.StatusField;
import com.ghlh.ui.bean.UIComponentMetadata;
import com.ghlh.util.ReflectUtil;
import com.ghlh.util.StringUtil;

public class StockSettingButtonActionListener extends
		AbstractButtonActionListener {
	private static Logger logger = Logger
			.getLogger(StockSettingButtonActionListener.class);

	public StockSettingButtonActionListener() {
		this.setStatus("自动交易股票设置");
	}

	public void button1ActionPerformed(ActionEvent e) {
		System.out.println("click button1");
	}

	public void button2ActionPerformed(ActionEvent e) {
		System.out.println("click button2");

	}

	public void button3ActionPerformed(ActionEvent e) {
		if (validateData()) {
			MonitorStockBean msb = collectMonitorStockBean();
			try {
				new FileStockPoolAccessor().addMonitorStock(msb);
			} catch (Exception ex) {
				logger.error("click save throw exception:", ex);
			}
			((StockSettingContentPanel) this.getContentPanel())
					.addStockInTable(msb);
			StatusField.getInstance().setPromptMessage("保存成功");
		}
	}

	private boolean validateData() {
		for (int i = 0; i < this.getUIComponents().size(); i++) {
			UIComponentMetadata uiComponent = (UIComponentMetadata) this.getComponents().get(i);
			if (uiComponent.isNotAllowNull()) {
				if (this.getUIComponents().get(i) instanceof JTextField) {
					if (((JTextField) this.getUIComponents().get(i)).getText() == null
							|| ((JTextField) this.getUIComponents().get(i))
									.getText().trim().equals("")) {
						UIComponentMetadata labelUIComponent = (UIComponentMetadata) this
								.getComponents().get(i);
						StatusField.getInstance().setWarningMessage(
								labelUIComponent.getLabel() + "不能为空， 请输入");
						return false;
					}
				}
			}
		}
		return true;
	}

	private MonitorStockBean collectMonitorStockBean() {
		MonitorStockBean result = new MonitorStockBean();
		String strategyName = ((JComboBox) this.getUIComponents().get(0))
				.getSelectedItem().toString();
		strategyName = StockSettingUICompomentsImpl.getStrategy(strategyName);
		result.setTradeAlgorithm(strategyName);
		result.setStockId(((JTextField) this.getUIComponents().get(1))
				.getText());
		result.setName(((JTextField) this.getUIComponents().get(2)).getText());
		result.setStandardPrice(Double.parseDouble(StringUtil
				.getDefaultNumberWithZero(((JTextField) this.getUIComponents()
						.get(3)).getText())));
		result.setCanSellNumber(Integer.parseInt(StringUtil
				.getDefaultNumberWithZero(((JTextField) this.getUIComponents()
						.get(4)).getText())));
		result.setCurrentNumber(Integer.parseInt(StringUtil
				.getDefaultNumberWithZero(((JTextField) this.getUIComponents()
						.get(5)).getText())));
		TradeStrategy ts = (TradeStrategy) ReflectUtil.getClassInstance(
				"com.ghlh.strategy", strategyName, "TradeStrategy");
		String additionalInfo = (String) ReflectUtil.excuteClassMethod(ts,
				"collectAdditionalInfoFromUIComponents",
				new Class[] { List.class },
				new Object[] { this.getUIComponents() });
		result.setAdditionInfo(additionalInfo);

		return result;
	}

	public void button4ActionPerformed(ActionEvent e) {
		System.out.println("click button4");
	}

	public void button5ActionPerformed(ActionEvent e) {
		System.out.println("click button5");
	}

	protected void initButtonStatus() {
		this.enterNewStatus();
	}

	private void enterNewStatus() {
		((JButton) this.getjButtons().get(0)).setEnabled(false);
		((JButton) this.getjButtons().get(1)).setEnabled(false);
		((JButton) this.getjButtons().get(4)).setEnabled(false);
		for (int i = 0; i < this.getUIComponents().size(); i++) {
			((JComponent) this.getUIComponents().get(i)).setEnabled(true);
			String defaultValue = ((UIComponentMetadata) this.getComponents().get(i))
					.getDefaultValue();
			if (this.getUIComponents().get(i) instanceof JTextField) {
				if (defaultValue != null) {
					((JTextField) this.getUIComponents().get(i))
							.setText(defaultValue);
				}
			}
			if (this.getUIComponents().get(i) instanceof JComboBox) {
				if (defaultValue != null) {
					((JComboBox) this.getUIComponents().get(i))
							.setSelectedItem(defaultValue);
				}
			}
		}
		this.setStatus(STATUS_NEW);
	}

	public void setStatus(String status) {
		super.setStatus(status);
		StatusField.getInstance()
				.setPromptMessage(STOCK_SETTING + "-" + status);
	}

	private final static String STOCK_SETTING = "股票设置";
	private final static String STATUS_NEW = "新建";
	private final static String STATUS_EDIT = "修改";

}
