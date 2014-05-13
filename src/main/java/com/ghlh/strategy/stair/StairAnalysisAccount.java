package com.ghlh.strategy.stair;

import java.util.ArrayList;
import java.util.List;

import com.ghlh.data.db.StockdailyinfoVO;

public class StairAnalysisAccount {
	private int currentStair;

	public int getCurrentStair() {
		return currentStair;
	}

	public void setCurrentStair(int currentStair) {
		this.currentStair = currentStair;
	}

	public double getCurrentStairPrice() {
		return currentStairPrice;
	}

	public void setCurrentStairPrice(double currentStairPrice) {
		this.currentStairPrice = currentStairPrice;
	}

	private double currentStairPrice;

	private StairAnalysisAccount() {

	}

	private static StairAnalysisAccount instance = new StairAnalysisAccount();

	public static StairAnalysisAccount getInstance() {
		return instance;
	}

	public List stairs = new ArrayList();

	public void buyStock(StairAnalysisBean sab) {
		stairs.add(sab);
	}

	public boolean sellStock(StockdailyinfoVO firstDay) {
		boolean result = false;
		for (int i = stairs.size() - 1; i >= 0; i--) {
			StairAnalysisBean bean = (StairAnalysisBean) stairs.get(i);
			if (firstDay.getHighestprice() >= bean.getSellPrice()) {
				System.out.println(firstDay.getDate()
						+ " sellPrice----------- " + bean.getSellPrice());
				StairAnalysis.sellTimes ++;
				this.currentStair--;
				if (this.currentStair != 0) {
					bean = (StairAnalysisBean) stairs.get(i - 1);
					this.setCurrentStairPrice(bean.getBuyPrice());
				} else {
					this.setCurrentStairPrice(0);
				}
				result = true;
				stairs.remove(i);
				i--;
			}
		}
		return result;
	}

	public String toString() {
		return "CurrentPrice = " + this.currentStairPrice + " CurrentStair = "
				+ this.currentStair + " currentTradeStock = " + stairs.size()
				+ stairs;
	}

}
