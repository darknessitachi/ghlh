package com.ghlh.data;

import com.ghlh.DemoStockException;

public class StockPoolAccessorException extends DemoStockException {

	private static final long serialVersionUID = 1202194525031558648L;

	public StockPoolAccessorException(String message, Exception rootException) {
		super(message, rootException);
	}

	public StockPoolAccessorException(String message) {
		super(message);
	}

	public StockPoolAccessorException(Exception rootException) {
		super(rootException);
	}

}
