package com.ghlh;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.ghlh.autotrade.AutoTradeTestSuites;
import com.ghlh.conf.ConfigurationAccessor;
import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.strategy.StrategyTestSuites;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AutoTradeTestSuites.class, StrategyTestSuites.class })
public class GHLHTestRunner {
	public void setup() {
		try {
			ConfigurationAccessor.getInstance().setOpenSoftwareTrade(false);
			GhlhDAO.remove(new MonitorstockVO());
			GhlhDAO.remove(new StocktradeVO());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
