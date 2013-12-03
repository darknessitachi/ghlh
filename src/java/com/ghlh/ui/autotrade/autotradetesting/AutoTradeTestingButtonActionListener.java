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
		this.setStatus("�Զ����ײ���");
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
						"����ָ��������� �벻�ö�������");
				TimeUtil.pause(1000);

				SoftwareTrader.getInstance().buyStock(stockId, number);

				TimeUtil.pause(1000);

				StatusField.getInstance().setPromptMessage(
						"���Խ�������ɣ� �뵽�������-����ί�� ȷ���Ƿ�ɹ�");

			} else {
				StatusField.getInstance().setWarningMessage(
						"�����������������½��Ȼ����С������, �밴F1�˽���ϸ");
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
						"����ָ��������� �벻�ö�������");
				TimeUtil.pause(1000);

				SoftwareTrader.getInstance().sellStock(stockId, number);

				TimeUtil.pause(1000);

				StatusField.getInstance().setPromptMessage(
						"���Խ�������ɣ� �뵽�������-����ί�� ȷ���Ƿ�ɹ�");
			} else {
				StatusField.getInstance().setWarningMessage(
						"Ϊ�˱�֤���ײ��Գɹ�, ����֤ȯ�ʺ����еĹ�Ʊ���в���");

			}
		} else {
			StatusField.getInstance().setWarningMessage(
					"�����������������½��Ȼ����С������, �밴F1�˽���ϸ");

		}
	}

	private boolean validateData() {
		String stockId = ((JTextField) this.getUIComponents().get(0)).getText();
		String number = ((JTextField) this.getUIComponents().get(2)).getText();
		if (stockId == null || stockId.length() != 6) {
			StatusField.getInstance().setWarningMessage("������Ĺ�Ʊ���룬 ������");
			return false;
		}
		if (!MathUtil.isInt(stockId)) {
			StatusField.getInstance().setWarningMessage("������Ĺ�Ʊ���룬 ������");
			return false;
		}
		char first = stockId.charAt(0);
		if (first != '6' && first != '2' && first != '0' && first != '3') {
			StatusField.getInstance().setWarningMessage("������Ĺ�Ʊ���룬 ������");
			return false;
		}

		if (number == null || !MathUtil.isInt(number)) {
			StatusField.getInstance().setWarningMessage("������Ľ��������� ������");
			return false;
		}
		return true;
	}

	private int showSoftwareRunningConfirmDialog() {
		int result = JOptionPane.showOptionDialog(Launcher.get_frame(),
				"��ȷ�Ͻ�������Ѿ������˲��ҵ�½����С����������?", "�������������½ȷ��",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null);

		return result;

	}

	private int showHasTestSellStock(String stockId, int number) {
		int result = JOptionPane.showOptionDialog(Launcher.get_frame(),
				"��ȷ�������ʺ����й�Ʊ\"" + stockId + "\" " + number + "����?",
				"֤ȯ�ʺ��в���������Ʊȷ��", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);

		return result;

	}

}
