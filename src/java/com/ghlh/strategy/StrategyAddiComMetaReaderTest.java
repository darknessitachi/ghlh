package com.ghlh.strategy;

import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;

public class StrategyAddiComMetaReaderTest {

	@Test
	public void testGetStrategyNameAsKey() {
		Map map = StrategyAddiComMetaReader.getInstance().getStrategies();
		
		//fail("Not yet implemented");
	}

}
