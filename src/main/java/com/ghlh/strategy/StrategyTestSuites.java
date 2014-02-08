package com.ghlh.strategy;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.ghlh.autotrade.AutoTradeTestSuites;
import com.ghlh.strategy.catchyzstair.CatchYZStairBeforeOpenStrategyTest;
import com.ghlh.strategy.catchyzstair.CatchYZStairIntradayStrategyTest;
import com.ghlh.strategy.once.OnceBeforeCloseStrategyTest;
import com.ghlh.strategy.once.OnceBeforeOpenStrategyTest;
import com.ghlh.strategy.once.OnceIntradyFirstBuyStrategyTest;
import com.ghlh.strategy.stair.StairBeforeOpenStrategyTest;
import com.ghlh.strategy.stair.StairIntradayStrategyTest;
import com.ghlh.strategy.stair.StairIntradyFirstBuyStrategyTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ OnceBeforeOpenStrategyTest.class,
		OnceIntradyFirstBuyStrategyTest.class,
		StairBeforeOpenStrategyTest.class, StairIntradayStrategyTest.class,
		StairIntradyFirstBuyStrategyTest.class,
		OnceBeforeCloseStrategyTest.class , CatchYZStairBeforeOpenStrategyTest.class,CatchYZStairIntradayStrategyTest.class})
public class StrategyTestSuites {
}
