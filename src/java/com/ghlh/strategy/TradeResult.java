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
			stockInfo = "����";
		} else if (cmd == Constants.BUY) {
			stockInfo = "����";
		}
		return stockInfo + "��Ʊ:" + stockId + " �۸�:" + tradePrice + " ����:"
				+ number;
	}
}
