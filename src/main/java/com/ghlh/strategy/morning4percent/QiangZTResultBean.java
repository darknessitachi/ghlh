package com.ghlh.strategy.morning4percent;

public class QiangZTResultBean {
	private int yinLi;

	public int getYinLi() {
		return yinLi;
	}

	public void setYinLi(int yinLi) {
		this.yinLi = yinLi;
	}

	public int getKuiSun() {
		return kuiSun;
	}

	public void setKuiSun(int kuiSun) {
		this.kuiSun = kuiSun;
	}

	public int getPickUp() {
		return pickUp;
	}

	public void setPickUp(int pickUp) {
		this.pickUp = pickUp;
	}

	private int kuiSun;
	private int pickUp;

	public String toString() {
		return "Ó¯Àû  " + yinLi + " ¿÷Ëð" + kuiSun + " Pickup" + pickUp;
	}
}
