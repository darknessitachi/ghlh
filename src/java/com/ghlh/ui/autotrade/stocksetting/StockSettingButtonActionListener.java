package com.ghlh.ui.autotrade.stocksetting;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.ghlh.stockpool.FileStockPoolAccessor;
import com.ghlh.stockpool.MonitorStockBean;
import com.ghlh.strategy.TradeStrategy;
import com.ghlh.ui.AbstractButtonActionListener;
import com.ghlh.ui.Launcher;
import com.ghlh.ui.StatusField;
import com.ghlh.ui.bean.UIComponentMetadata;
import com.ghlh.util.ReflectUtil;

public class StockSettingButtonActionListener extends
		AbstractButtonActionListener {
	private static Logger logger = Logger
			.getLogger(StockSettingButtonActionListener.class);

	public StockSettingButtonActionListener() {
		this.setStatus("自动交易股票设置");
	}

	public int showConfirmDialog(String message, String title) {
		int result = JOptionPane.showOptionDialog(Launcher.get_frame(),
				message, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		return result;

	}

	public void button1ActionPerformed(ActionEvent e) {
		int confirm = confirmInputOrChange();
		if (confirm == 0 || confirm == -1) {
			this.enterNewStatus();
			StatusField.getInstance().setPromptMessage("新建股票");
		}

	}

	private boolean hasInputForNew() {
		return STATUS_NEW.equals(this.getStatus())
				&& this.hasInputValueInUIComponent();
	}

	private boolean hasChangeForEdit() {
		return STATUS_EDIT.equals(this.getStatus())
				&& this.hasChangedValueInUIComponent();
	}

	public int confirmInputOrChange() {
		int confirm = -1;
		if (hasInputForNew() || hasChangeForEdit()) {
			String message = null;
			String title = null;
			if (STATUS_NEW.equals(this.getStatus())) {
				message = "您确认放弃当前输入吗";
				title = "放弃输入确认";
			}
			if (STATUS_EDIT.equals(this.getStatus())) {
				message = "您确认放弃当前修改吗";
				title = "放弃修改确认";
			}
			confirm = showConfirmDialog(message, title);
		}
		return confirm;
	}

	public void button2ActionPerformed(ActionEvent e) {
		if (validateData()) {
			if (STATUS_NEW.equals(this.getStatus())) {
				saveForNew();
			} else if (STATUS_EDIT.equals(this.getStatus())) {
				saveForEdit();

			}
			StatusField.getInstance().setPromptMessage("保存成功");
			this.enterNewStatus();
		}

	}

	private void saveForEdit() {
		MonitorStockBean msb = collectMonitorStockBean();
		try {
			new FileStockPoolAccessor()
					.updateMonitorStock(msb, this.currentRow);
		} catch (Exception ex) {
			logger.error("click save throw exception:", ex);
		}
		((StockSettingContentPanel) this.getContentPanel())
				.updateStockInTableRow(msb);
	}

	private void saveForNew() {
		MonitorStockBean msb = collectMonitorStockBean();
		try {
			new FileStockPoolAccessor().addMonitorStock(msb);
		} catch (Exception ex) {
			logger.error("click save throw exception:", ex);
		}
		((StockSettingContentPanel) this.getContentPanel())
				.addStockInTable(msb);
	}

	public void button3ActionPerformed(ActionEvent e) {
		int confirm = confirmInputOrChange();
		if (confirm == 0 || confirm == -1) {
			this.enterNewStatus();
		}
	}

	private boolean validateData() {
		for (int i = 0; i < this.getUIComponents().size(); i++) {
			UIComponentMetadata uiComponent = (UIComponentMetadata) this
					.getComponents().get(i);
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
		String message = "确认删除此股票吗";
		String title = "股票删除确认";
		int confirm = showConfirmDialog(message, title);
		if (confirm == 0) {
			try {
				new FileStockPoolAccessor().deleteMonitorStock(this.currentRow);
			} catch (Exception ex) {
				logger.error("click save throw exception:", ex);
			}
			((StockSettingContentPanel) this.getContentPanel())
					.deleteStockInTableRow();
			StatusField.getInstance().setPromptMessage("删除成功");
			this.enterNewStatus();
		}
	}

	protected void initButtonStatus() {
		this.enterNewStatus();
	}

	private void enterNewStatus() {
		((JButton) this.getjButtons().get(0)).setEnabled(false);
		((JButton) this.getjButtons().get(1)).setEnabled(true);
		((JButton) this.getjButtons().get(2)).setEnabled(true);
		((JButton) this.getjButtons().get(3)).setEnabled(false);
		for (int i = 0; i < this.getUIComponents().size(); i++) {
			setDefaultValueToUIComponent(i);
		}
		((StockSettingContentPanel) this.getContentPanel()).notSelectRow();
		this.setStatus(STATUS_NEW);
	}

	private void setDefaultValueToUIComponent(int i) {
		String defaultValue = ((UIComponentMetadata) this.getComponents()
				.get(i)).getDefaultValue();
		if (this.getUIComponents().get(i) instanceof JComboBox) {
			if (defaultValue != null) {
				String currentValue = ((JComboBox) this.getUIComponents()
						.get(i)).getSelectedItem().toString();
				if (!defaultValue.equals(currentValue)) {
					((JComboBox) this.getUIComponents().get(i))
							.setSelectedItem(defaultValue);
					if (i == 0) {
						((StockSettingContentPanel) this.getContentPanel())
								.switchTradeStrategy(defaultValue);
					}
				}
			}
		}
		if (this.getUIComponents().get(i) instanceof JTextField) {
			if (defaultValue != null) {
				((JTextField) this.getUIComponents().get(i))
						.setText(defaultValue);
			} else {
				((JTextField) this.getUIComponents().get(i)).setText("");
			}
		}
	}

	private boolean hasInputValueInUIComponent() {
		for (int i = 0; i < this.getUIComponents().size(); i++) {
			String defaultValue = ((UIComponentMetadata) this.getComponents()
					.get(i)).getDefaultValue();
			if (defaultValue != null) {
				if (this.getUIComponents().get(i) instanceof JComboBox) {
					String item = (String) ((JComboBox) this.getUIComponents()
							.get(i)).getSelectedItem();
					if (!item.equals(defaultValue)) {
						return true;
					}
				} else if (this.getUIComponents().get(i) instanceof JTextField) {
					String text = ((JTextField) this.getUIComponents().get(i))
							.getText();
					if (!defaultValue.equals(text)) {
						return true;
					}
				}
			} else {
				if (this.getUIComponents().get(i) instanceof JTextField) {
					String text = ((JTextField) this.getUIComponents().get(i))
							.getText();
					if (text != null && !text.equals("")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean hasChangedValueInUIComponent() {
		String strategyName = ((JComboBox) this.getUIComponents().get(0))
				.getSelectedItem().toString();
		strategyName = StockSettingUICompomentsImpl.getStrategy(strategyName);
		if (!this.currentMsb.getTradeAlgorithm().equals(strategyName)) {
			return true;
		}
		String stockId = ((JTextField) this.getUIComponents().get(1)).getText();
		if (!this.currentMsb.getStockId().equals(stockId)) {
			return true;
		}
		String name = ((JTextField) this.getUIComponents().get(2)).getText();
		if (!this.currentMsb.getName().equals(name)) {
			return true;
		}
		TradeStrategy ts = (TradeStrategy) ReflectUtil.getClassInstance(
				"com.ghlh.strategy", this.currentMsb.getTradeAlgorithm(),
				"TradeStrategy");
		boolean result = (boolean) ReflectUtil.excuteClassMethod(ts,
				"hasChangedValueInAdditionalUIComponents", new Class[] {
						List.class, MonitorStockBean.class }, new Object[] {
						this.getUIComponents(), this.currentMsb });

		return result;
	}

	public void enterEditStatus(MonitorStockBean currentMsb, int currentRow) {
		((JButton) this.getjButtons().get(0)).setEnabled(true);
		((JButton) this.getjButtons().get(1)).setEnabled(true);
		((JButton) this.getjButtons().get(2)).setEnabled(true);
		((JButton) this.getjButtons().get(3)).setEnabled(true);
		this.currentMsb = currentMsb;
		this.currentRow = currentRow;
		this.setStatus(STATUS_EDIT);
	}

	private MonitorStockBean currentMsb;
	private int currentRow;

	public int getCurrentRow() {
		if (STATUS_NEW.equals(this.getStatus())) {
			return -1;
		} else if (STATUS_EDIT.equals(this.getStatus())) {
			return currentRow;
		}
		return -1;
	}

	public void updateMonitoring(boolean onMonitoring) {
		currentMsb.setOnMonitoring(onMonitoring);
		try {
			new FileStockPoolAccessor().updateMonitorStock(currentMsb,
					this.currentRow);
		} catch (Exception ex) {
			logger.error("click save throw exception:", ex);
		}
	}

	private final static String STOCK_SETTING = "股票设置";
	private final static String STATUS_NEW = "new";
	private final static String STATUS_EDIT = "edit";

}
