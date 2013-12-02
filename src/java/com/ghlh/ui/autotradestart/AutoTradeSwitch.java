package com.ghlh.ui.autotradestart;

import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AutoTradeSwitch {
	private static AutoTradeSwitch instance = new AutoTradeSwitch();

	public static AutoTradeSwitch getInstance() {
		return instance;
	}

	private boolean start;

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	private AutoTradeSwitch() {
	}

	private JTextArea monitorField;

	public void setMonitorField(JTextArea monitorField) {
		this.monitorField = monitorField;
	}

	public void setMonitorInfo(String monitoringInfo) {
		this.monitorField.setText(monitoringInfo);
	}
	
	
	public void appendMonitorInfo(String monitoringInfo) {
		this.monitorField.append(monitoringInfo + "\n");
	}
	
	public void showStopSuccessful() {
		this.monitorField.setText("自动交易监控已成功停止");
	}
}
