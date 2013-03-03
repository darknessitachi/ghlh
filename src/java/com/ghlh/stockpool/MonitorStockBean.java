package com.ghlh.stockpool;

public class MonitorStockBean {
	private String stockId;
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public double getStandardPrice() {
		return standardPrice;
	}
	public void setStandardPrice(double standardPrice) {
		this.standardPrice = standardPrice;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCanSellNumber() {
		return canSellNumber;
	}
	public void setCanSellNumber(int canSellNumber) {
		this.canSellNumber = canSellNumber;
	}
	public int getCurrentNumber() {
		return currentNumber;
	}
	public void setCurrentNumber(int currentNumber) {
		this.currentNumber = currentNumber;
	}
	private double standardPrice;
	private String name;
	private int canSellNumber;
	private int currentNumber;
	
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
}
