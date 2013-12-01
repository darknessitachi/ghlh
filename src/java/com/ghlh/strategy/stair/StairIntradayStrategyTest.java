package com.ghlh.strategy.stair;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.TradeConstants;

public class StairIntradayStrategyTest {

	@Test
	public void testProcessStockTrade() {
		StairBeforeOpenStrategyTest stairRestStrategyTest = new StairBeforeOpenStrategyTest();
		stairRestStrategyTest.test2StairsWith2PossibleBuyAnd2PossibleSell();
		MonitorstockVO monitorstockVO = StairTestDataGenerator
				.prepareMonitorstockVO(0.4, 4);
		List stocktradeList = StocktradeDAO
				.getHoldStocks(monitorstockVO.getStockid(),
						monitorstockVO.getTradealgorithm());
		StockQuotesBean sqb = StairTestDataGenerator.getMockStockQuotestBean();
		sqb.setHighestPrice(9.3);
		InternetStockQuotesInquirer.getInstance()
				.setTestingInjectStockQuotesBean(sqb);

		new StairIntradayStrategy().processStockTrade(monitorstockVO,
				stocktradeList);
		if (stocktradeList.size() != 6) {
			fail("something wrong");
		}

		List stocktradeAfter = StocktradeDAO
				.getOneStockTradeRecords(monitorstockVO.getStockid(),
						monitorstockVO.getTradealgorithm());
		if (stocktradeAfter.size() != 6) {
			fail("something wrong");
		}
		for (int i = 0; i < stocktradeAfter.size(); i++) {
			StocktradeVO stocktradeVO = (StocktradeVO) stocktradeAfter.get(i);
			if (stocktradeVO.getSelldate() != null
					&& stocktradeVO.getStatus() != TradeConstants.STATUS_FINISH) {
				fail("something wrong");
			}
		}

	}

}