package com.ghlh.tradeway;

public interface StockTrader {
	void buyStock(String stockId, int number);
	void sellStock(String stockId, int number);
	void buyStock(String stockId, int number, double price);
	void sellStock(String stockId, int number, double price);
	void activateTradeSoft();
}
