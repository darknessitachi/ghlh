package com.ghlh.strategy.once;

public class AdditionalInfoBean {
	private String cmd;

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private double targetPrice;
	public double getTargetPrice() {
		return targetPrice;
	}

	public void setTargetPrice(double targetPrice) {
		this.targetPrice = targetPrice;
	}

	private int number;
	private String status;
	
	private String relationShipWithTargetPrice;

	public String getRelationShipWithTargetPrice() {
		return relationShipWithTargetPrice;
	}

	public void setRelationShipWithTargetPrice(String relationShipWithTargetPrice) {
		this.relationShipWithTargetPrice = relationShipWithTargetPrice;
	}
}
