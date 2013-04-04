package com.ghlh;

public class MarketIsRestFlag {
	private static MarketIsRestFlag instance = new MarketIsRestFlag();

	public static MarketIsRestFlag getInstance() {
		return instance;
	}

	private boolean marketRest;

	public boolean isMarketRest() {
		 return marketRest;
		//return false;
	}

	public void setMarketRest(boolean marketRest) {
		this.marketRest = marketRest;
	}

}
