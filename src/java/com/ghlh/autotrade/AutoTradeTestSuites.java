package com.ghlh.autotrade;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.ghlh.strategy.once.OnceBeforeOpenStrategyTest;
import com.ghlh.strategy.once.OnceIntradyFirstBuyStrategyTest;
import com.ghlh.strategy.stair.StairBeforeOpenStrategyTest;
import com.ghlh.strategy.stair.StairIntradayStrategyTest;
import com.ghlh.strategy.stair.StairIntradyFirstBuyStrategyTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AutoTradeAfterCloseJobTest.class,
		AutoTradeBeforeOpenJobTest.class })
public class AutoTradeTestSuites {
}
