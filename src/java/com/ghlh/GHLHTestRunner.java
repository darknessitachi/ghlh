package com.ghlh;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.ghlh.autotrade.AutoTradeTestSuites;
import com.ghlh.strategy.StrategyTestSuites;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AutoTradeTestSuites.class,
		StrategyTestSuites.class })
public class GHLHTestRunner {
}
