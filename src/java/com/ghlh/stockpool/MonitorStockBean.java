package com.ghlh.stockpool;

import com.ghlh.strategy.TradeStrategy;
import com.ghlh.util.ReflectUtil;

public class MonitorStockBean {
	private String stockId;

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

	private String additionInfo;

	public String getAdditionInfo() {
		return additionInfo;
	}

	public void setAdditionInfo(String additionInfo) {
		this.additionInfo = additionInfo;
	}

	private String tradeAlgorithm;

	public String getTradeAlgorithm() {
		return tradeAlgorithm;
	}

	public void setTradeAlgorithm(String tradeAlgorithm) {
		this.tradeAlgorithm = tradeAlgorithm;
	}

	public void writeBackCurrentNumber(int cmd, int tradeNumber) {
		TradeStrategy ts = (TradeStrategy) ReflectUtil.getClassInstance(
				"com.ghlh.strategy", tradeAlgorithm, "TradeStrategy");
		String additionalInfo = (String) ReflectUtil.excuteClassMethod(ts,
				"updateCurrentNumber", new Class[] { String.class,
						Integer.class, Integer.class }, new Object[] {
						this.additionInfo, new Integer(cmd),
						new Integer(tradeNumber) });
		this.additionInfo = additionalInfo;
	}
	
	private boolean onMonitoring;

	public boolean isOnMonitoring() {
		return onMonitoring;
	}

	public void setOnMonitoring(boolean onMonitoring) {
		this.onMonitoring = onMonitoring;
	}
}
