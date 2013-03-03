package com.ghlh.stockquotes;

public class StockQuotesBean {
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

	public double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public double getZdf() {
		return zdf;
	}

	public void setZdf(double zdf) {
		this.zdf = zdf;
	}

	public double getZde() {
		return zde;
	}

	public void setZde(double zde) {
		this.zde = zde;
	}

	private String name;
	private double currentPrice;
	private double zdf;
	private double zde;

	public String toString() {
		return "StockId = " + stockId + " Name = " + name + " CurrentPrice = "
				+ currentPrice + " Zdf = " + zdf + " Zde = " + zde;

	}
}