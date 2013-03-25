package com.ghlh.ui.autotradestart;

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

	private JTextField monitorField;

	public void setMonitorField(JTextField monitorField) {
		this.monitorField = monitorField;
	}

	public void setMonitorInfo(String monitoringInfo) {
		this.monitorField.setText(monitoringInfo);
	}
	
	public void showStopSuccessful() {
		this.monitorField.setText("自动交易监控已成功停止");
	}
}
