package com.ghlh.tradeway;

import com.ghlh.autotrade.DemoStockException;

public class StockTradeException extends DemoStockException {
	private static final long serialVersionUID = 349813282542703507L;

	public StockTradeException(String message, Exception rootException) {
		super(message, rootException);
	}

	public StockTradeException(String message) {
		super(message);
	}

	public StockTradeException(Exception rootException) {
		super(rootException);
	}
}
