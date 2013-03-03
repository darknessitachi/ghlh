package com.ghlh.conf;

import org.junit.Test;

public class ConfigurationAccessorTest {

	@Test
	public void testGetTradeVendor() {
		String vendor = ConfigurationAccessor.getInstance().getTradeVendor();
		System.out.println("vendor = " + vendor);
	}
}
