package com.ghlh.strategy.stair;

public class StairAnalysisBean {
	private int stockCount;

	public int getStockCount() {
		return stockCount;
	}

	public void setStockCount(int stockCount) {
		this.stockCount = stockCount;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}

	private double money;
	private double buyPrice;
	private double sellPrice;

	public String toString() {
		return " Money = " + money + " Count = " + stockCount + " buyPrice = "
				+ buyPrice + " sellPrice = " + sellPrice;
	}
}
