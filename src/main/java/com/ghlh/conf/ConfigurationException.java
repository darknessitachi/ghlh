package com.ghlh.conf;

import com.ghlh.autotrade.DemoStockException;

public class ConfigurationException extends DemoStockException {
	private static final long serialVersionUID = 1202194525031558648L;

	public ConfigurationException(String message, Exception rootException) {
		super(message, rootException);
	}

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(Exception rootException) {
		super(rootException);
	}
}
