package com.ghlh.strategy.stair;

public class AdditionalInfoBean {
	private double stairZDF;

	public double getStairZDF() {
		return stairZDF;
	}

	public void setStairZDF(double stairZDF) {
		this.stairZDF = stairZDF;
	}

	public int getTradeNumber() {
		return tradeNumber;
	}

	public void setTradeNumber(int tradeNumber) {
		this.tradeNumber = tradeNumber;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	private int tradeNumber;
	private int rank;

	private int maxRank;

	public int getMaxRank() {
		return maxRank;
	}

	public void setMaxRank(int maxRank) {
		this.maxRank = maxRank;
	}

	private double standardPrice;

	public double getStandardPrice() {
		return standardPrice;
	}

	public void setStandardPrice(double standardPrice) {
		this.standardPrice = standardPrice;
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

	private int canSellNumber;
	private int currentNumber;
}
