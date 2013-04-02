package com.ghlh.strategy;

import com.ghlh.Constants;

public class TradeResult {
	private int cmd;

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	private String stockId;
	private int number;

	private double tradePrice;

	public double getTradePrice() {
		return tradePrice;
	}

	public void setTradePrice(double tradePrice) {
		this.tradePrice = tradePrice;
	}

	public String toString() {
		String stockInfo = "";
		if (cmd == Constants.SELL) {
			stockInfo = "卖出";
		} else if (cmd == Constants.BUY) {
			stockInfo = "买入";
		}
		return stockInfo + "股票:" + stockId + " 价格:" + tradePrice + " 数量:"
				+ number;
	}
}
