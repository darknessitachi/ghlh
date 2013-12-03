package com.ghlh.ui.autotrade.autotradetesting;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.ghlh.tradeway.SoftwareTrader;
import com.ghlh.tradeway.StockTrader;
import com.ghlh.ui.AbstractButtonActionListener;
import com.ghlh.ui.Launcher;
import com.ghlh.ui.StatusField;
import com.ghlh.util.MathUtil;
import com.ghlh.util.TimeUtil;

public class AutoTradeTestingButtonActionListener extends
		AbstractButtonActionListener {

	public AutoTradeTestingButtonActionListener() {
		this.setStatus("自动交易测试");
	}

	public void button1ActionPerformed(ActionEvent e) {
		if (validateData()) {
			int confirm = this.showSoftwareRunningConfirmDialog();
			if (confirm == 0) {
				String stockId = ((JTextField) this.getUIComponents().get(0))
						.getText();
				int number = Integer.parseInt(((JTextField) this
						.getUIComponents().get(2)).getText());
				StatusField.getInstance().setWarningMessage(
						"交易指令即将发出， 请不用动鼠标键盘");
				TimeUtil.pause(1000);

				SoftwareTrader.getInstance().buyStock(stockId, number);

				TimeUtil.pause(1000);

				StatusField.getInstance().setPromptMessage(
						"测试交易已完成， 请到交易软件-今天委托 确认是否成功");

			} else {
				StatusField.getInstance().setWarningMessage(
						"请启动交易软件并登陆，然后最小化窗口, 请按F1了解详细");
			}
		}
	}

	public void button2ActionPerformed(ActionEvent e) {
		int confirm = this.showSoftwareRunningConfirmDialog();
		if (confirm == 0) {
			String stockId = ((JTextField) this.getUIComponents().get(0))
					.getText();
			int number = Integer.parseInt(((JTextField) this.getUIComponents()
					.get(2)).getText());
			int confirmHasStock = showHasTestSellStock(stockId, number);
			if (confirmHasStock == 0) {

				StatusField.getInstance().setWarningMessage(
						"交易指令即将发出， 请不用动鼠标键盘");
				TimeUtil.pause(1000);

				SoftwareTrader.getInstance().sellStock(stockId, number);

				TimeUtil.pause(1000);

				StatusField.getInstance().setPromptMessage(
						"测试交易已完成， 请到交易软件-今天委托 确认是否成功");
			} else {
				StatusField.getInstance().setWarningMessage(
						"为了保证交易测试成功, 请用证券帐号里有的股票进行测试");

			}
		} else {
			StatusField.getInstance().setWarningMessage(
					"请启动交易软件并登陆，然后最小化窗口, 请按F1了解详细");

		}
	}

	private boolean validateData() {
		String stockId = ((JTextField) this.getUIComponents().get(0)).getText();
		String number = ((JTextField) this.getUIComponents().get(2)).getText();
		if (stockId == null || stockId.length() != 6) {
			StatusField.getInstance().setWarningMessage("不合理的股票代码， 请重输");
			return false;
		}
		if (!MathUtil.isInt(stockId)) {
			StatusField.getInstance().setWarningMessage("不合理的股票代码， 请重输");
			return false;
		}
		char first = stockId.charAt(0);
		if (first != '6' && first != '2' && first != '0' && first != '3') {
			StatusField.getInstance().setWarningMessage("不合理的股票代码， 请重输");
			return false;
		}

		if (number == null || !MathUtil.isInt(number)) {
			StatusField.getInstance().setWarningMessage("不合理的交易数量， 请重输");
			return false;
		}
		return true;
	}

	private int showSoftwareRunningConfirmDialog() {
		int result = JOptionPane.showOptionDialog(Launcher.get_frame(),
				"您确认交易软件已经启动了并且登陆后最小化窗口了吗?", "交易软件启动登陆确认",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null);

		return result;

	}

	private int showHasTestSellStock(String stockId, int number) {
		int result = JOptionPane.showOptionDialog(Launcher.get_frame(),
				"您确认您的帐号里有股票\"" + stockId + "\" " + number + "股吗?",
				"证券帐号有测试卖出股票确认", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);

		return result;

	}

}
