package com.ghlh.analysis;

public class QiangZSBean {
	private String stockid;
	public String getStockid() {
		return stockid;
	}
	public void setStockid(String stockid) {
		this.stockid = stockid;
	}
	public int getTradedays() {
		return tradedays;
	}
	public void setTradedays(int tradedays) {
		this.tradedays = tradedays;
	}
	public double getMaxzdf() {
		return maxzdf;
	}
	public void setMaxzdf(double maxzdf) {
		this.maxzdf = maxzdf;
	}
	public double getMinzdf() {
		return minzdf;
	}
	public void setMinzdf(double minzdf) {
		this.minzdf = minzdf;
	}
	public double getAvgzdf() {
		return avgzdf;
	}
	public void setAvgzdf(double avgzdf) {
		this.avgzdf = avgzdf;
	}
	public double getCurrentprice() {
		return currentprice;
	}
	public void setCurrentprice(double currentprice) {
		this.currentprice = currentprice;
	}
	private int tradedays;
	private double maxzdf;
	private double minzdf;
	private double avgzdf;
	private double currentprice;
}
