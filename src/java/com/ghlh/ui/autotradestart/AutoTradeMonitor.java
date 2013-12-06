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
		String stopMessage = "自动交易监控已成功停止";
		this.monitorStockField.setText(stopMessage);
		appendMonitorInfo(stopMessage);
	}

	public void setMonitorStock(String stockId, String name) {
		if (this.monitorStockField != null) {
			if (stockId.equals("0")) {
				this.monitorStockField.setText("自动监控休市停止");
			} else {
				this.monitorStockField
						.setText("正在监控股票:" + stockId + " " + name);
			}
		}
	}
}
