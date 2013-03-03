package com.ghlh.tradeway;

import org.junit.Test;

import com.ghlh.tradeway.SoftwareTrader;

public class SoftwareTraderTest extends SoftwareTrader {
	private SoftwareTrader softwareTrader = new SoftwareTrader();
	@Test
	public void testBuyStock() {
		softwareTrader.buyStock("600036", 100);
	}

	@Test
	public void testSellStock() {
		softwareTrader.sellStock("601002", 100);
	}

}
