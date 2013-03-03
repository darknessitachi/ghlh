package com.ghlh;

public class DemoStockException extends Exception {

	private static final long serialVersionUID = -4645937706720799768L;

	private Exception rootException;
	private String message;

	public DemoStockException(String message, Exception rootException) {
		this.message = message;
		this.rootException = rootException;
	}

	public DemoStockException(String message) {
		this.message = message;
	}

	public DemoStockException(Exception rootException) {
		this.rootException = rootException;
	}

	public void printStackTrace() {
		if (rootException != null) {
			rootException.printStackTrace();
		}
		if (message != null) {
			System.out.println("Exception Message = " + message);
		}

	}
}
