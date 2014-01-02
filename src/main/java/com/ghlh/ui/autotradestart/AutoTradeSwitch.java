package com.ghlh.ui.autotradestart;


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

}
