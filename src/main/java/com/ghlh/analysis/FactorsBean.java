package com.ghlh.analysis;

import java.util.Date;

import com.ghlh.util.DateUtil;

public class FactorsBean {
	private Date date = DateUtil.getDate(2014, 2, 7);

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getZdts() {
		return zdts;
	}

	public void setZdts(int zdts) {
		this.zdts = zdts;
	}

	public double getMinZdf() {
		return minZdf;
	}

	public void setMinZdf(double minZdf) {
		this.minZdf = minZdf;
	}

	public double getMaxZdf() {
		return maxZdf;
	}

	public void setMaxZdf(double maxZdf) {
		this.maxZdf = maxZdf;
	}

	public double getMaxAvg() {
		return maxAvg;
	}

	public void setMaxAvg(double maxAvg) {
		this.maxAvg = maxAvg;
	}

	public double getMinAvg() {
		return minAvg;
	}

	public void setMinAvg(double minAvg) {
		this.minAvg = minAvg;
	}

	public double getCloseZT() {
		return closeZT;
	}

	public void setCloseZT(double closeZT) {
		this.closeZT = closeZT;
	}

	public double getWinPercentage() {
		return winPercentage;
	}

	public void setWinPercentage(double winPercentage) {
		this.winPercentage = winPercentage;
	}

	public double getLostPercentage() {
		return lostPercentage;
	}

	public void setLostPercentage(double lostPercentage) {
		this.lostPercentage = lostPercentage;
	}

	private int zdts = 20;
	private double minZdf = -5;
	private double maxZdf = 5;
	private double maxAvg = 0.3;
	private double minAvg = -0.3;
	private double closeZT = 9;
	private double winPercentage = 0.08;
	private double lostPercentage = 0.1;

	public String toString() {
		return " lowOpen=" + lowOpen + " basedonYesterdayClosePercentage="
				+ basedonYesterdayClosePercentage
				+ " basedonTodayOpenPercentage=" + basedonTodayOpenPercentage
				+ " Date=" + DateUtil.formatDay(date) + " zdts=" + zdts
				+ " minZdf=" + minZdf + " maxZdf=" + maxZdf + " maxAvg="
				+ maxAvg + " minAvg=" + minAvg + " closeZT=" + closeZT
				+ " winPercentage=" + winPercentage + " lostPercentage="
				+ lostPercentage;
	}

	private boolean lowOpen;

	public boolean isLowOpen() {
		return lowOpen;
	}

	public void setLowOpen(boolean lowOpen) {
		this.lowOpen = lowOpen;
	}

	public double getBasedonYesterdayClosePercentage() {
		return basedonYesterdayClosePercentage;
	}

	public void setBasedonYesterdayClosePercentage(
			double basedonYesterdayClosePercentage) {
		this.basedonYesterdayClosePercentage = basedonYesterdayClosePercentage;
	}

	public double getBasedonTodayOpenPercentage() {
		return basedonTodayOpenPercentage;
	}

	public void setBasedonTodayOpenPercentage(double basedonTodayOpenPercentage) {
		this.basedonTodayOpenPercentage = basedonTodayOpenPercentage;
	}

	private double basedonYesterdayClosePercentage;
	private double basedonTodayOpenPercentage;

}
