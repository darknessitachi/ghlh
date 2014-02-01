package com.ghlh.strategy;

public class BuyStockBean {
	private String stockId;
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public double getTradeMoney() {
		return tradeMoney;
	}
	public void setTradeMoney(double tradeMoney) {
		this.tradeMoney = tradeMoney;
	}
	public double getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}
	public double getWinSellPrice() {
		return winSellPrice;
	}
	public void setWinSellPrice(double winSellPrice) {
		this.winSellPrice = winSellPrice;
	}
	public double getLostSellPrice() {
		return lostSellPrice;
	}
	public void setLostSellPrice(double lostSellPrice) {
		this.lostSellPrice = lostSellPrice;
	}
	public String getStrategy() {
		return strategy;
	}
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	private double tradeMoney;
	private double buyPrice;
	private double winSellPrice;
	private double lostSellPrice;
	private String strategy;
	private int number;
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public boolean isConfirm() {
		return isConfirm;
	}
	public void setConfirm(boolean isConfirm) {
		this.isConfirm = isConfirm;
	}
	public int getPreviousTradeId() {
		return previousTradeId;
	}
	public void setPreviousTradeId(int previousTradeId) {
		this.previousTradeId = previousTradeId;
	}
	private boolean isConfirm;
	private int previousTradeId;
	
}
