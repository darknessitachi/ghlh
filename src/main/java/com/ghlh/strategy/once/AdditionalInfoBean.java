package com.ghlh.strategy.once;

public class AdditionalInfoBean {
	private String buyPriceStrategy;
	
	public String getBuyPriceStrategy() {
		return buyPriceStrategy;
	}
	public void setBuyPriceStrategy(String buyPriceStrategy) {
		this.buyPriceStrategy = buyPriceStrategy;
	}
	public double getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}
	public double getTargetZf() {
		return targetZf;
	}
	public void setTargetZf(double targetZf) {
		this.targetZf = targetZf;
	}
	public double getTradeMoney() {
		return tradeMoney;
	}
	public void setTradeMoney(double tradeMoney) {
		this.tradeMoney = tradeMoney;
	}
	private double buyPrice;
	private double targetZf;
	private double tradeMoney;
	private double lostDf;

	public double getLostDf() {
		return lostDf;
	}
	public void setLostDf(double lostDf) {
		this.lostDf = lostDf;
	}
}
