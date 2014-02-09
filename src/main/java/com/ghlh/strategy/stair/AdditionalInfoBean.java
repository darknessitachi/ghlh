package com.ghlh.strategy.stair;

import java.util.regex.Pattern;

public class AdditionalInfoBean {
	public AdditionalInfoBean() {

	}

	public AdditionalInfoBean(String additionalInfo) {
		Pattern pattern = Pattern.compile(",");
		String[] additionalInfoSegs = pattern.split(additionalInfo);
		stairZDF = Double.parseDouble(additionalInfoSegs[0]);
		stairMoney = Double.parseDouble(additionalInfoSegs[1]);
		stairNumber = Integer.parseInt(additionalInfoSegs[2]);
	}

	private double stairZDF;

	public double getStairZDF() {
		return stairZDF;
	}

	public void setStairZDF(double stairZDF) {
		this.stairZDF = stairZDF;
	}

	public double getStairMoney() {
		return stairMoney;
	}

	public void setStairMoney(double stairMoney) {
		this.stairMoney = stairMoney;
	}

	private double stairMoney;
	
	
	private int stairNumber;

	public int getStairNumber() {
		return stairNumber;
	}

	public void setStairNumber(int stairNumber) {
		this.stairNumber = stairNumber;
	}
	
	private String firstBuyPriceStrategy;
	public String getFirstBuyPriceStrategy() {
		return firstBuyPriceStrategy;
	}

	public void setFirstBuyPriceStrategy(String firstBuyPriceStrategy) {
		this.firstBuyPriceStrategy = firstBuyPriceStrategy;
	}

	public double getFirstBuyPrice() {
		return firstBuyPrice;
	}

	public void setFirstBuyPrice(double firstBuyPrice) {
		this.firstBuyPrice = firstBuyPrice;
	}

	private double firstBuyPrice;
	
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
