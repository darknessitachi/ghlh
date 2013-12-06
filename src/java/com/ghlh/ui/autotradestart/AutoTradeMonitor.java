package com.ghlh.ui.autotradestart;

import java.util.Date;

import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AutoTradeMonitor {
	private static AutoTradeMonitor instance = new AutoTradeMonitor();

	public static AutoTradeMonitor getInstance() {
		return instance;
	}

	private AutoTradeMonitor() {
	}

	private JTextArea monitorArea;

	public void setMonitorArea(JTextArea monitorArea) {
		this.monitorArea = monitorArea;
	}

	private JTextField monitorStockField;

	public void setMonitorStockField(JTextField monitorStockField) {
		this.monitorStockField = monitorStockField;
	}

	public void appendMonitorInfo(String monitoringInfo) {
		Date timestamp = new Date();
		if (this.monitorArea != null) {
			this.monitorArea.append(timestamp + " " + monitoringInfo + "\n");
		}
	}

	public void showStopSuccessful() {
		String stopMessage = "�Զ����׼���ѳɹ�ֹͣ";
		this.monitorStockField.setText(stopMessage);
		appendMonitorInfo(stopMessage);
	}

	public void setMonitorStock(String stockId, String name) {
		if (this.monitorStockField != null) {
			if (stockId.equals("0")) {
				this.monitorStockField.setText("�Զ��������ֹͣ");
			} else {
				this.monitorStockField
						.setText("���ڼ�ع�Ʊ:" + stockId + " " + name);
			}
		}
	}
}
