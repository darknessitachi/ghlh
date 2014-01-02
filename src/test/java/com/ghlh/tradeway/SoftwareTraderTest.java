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
		softwareTrader.sellStock("601118", 100);
	}

	@Test
	public void testBuyStockWithPrice() {
		softwareTrader.buyStock("600036", 100,12.51);
	}

	@Test
	public void testSellStockWithPrice() {
		softwareTrader.sellStock("601118", 100,9.24);
	}

	
	@Test
	public void testActivateTradeSoft() {
		softwareTrader.activateTradeSoft();
	}

}
