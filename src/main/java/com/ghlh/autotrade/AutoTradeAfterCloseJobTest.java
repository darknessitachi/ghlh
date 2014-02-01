package com.ghlh.autotrade;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.ghlh.conf.ConfigurationAccessor;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.strategy.TradeConstants;

public class AutoTradeAfterCloseJobTest {
	static {
		ConfigurationAccessor.getInstance().setOpenSoftwareTrade(false);
	}
	
	
	@Test
	public void testExecute() {
		AutoTradeAfterCloseJob autoTradeAfterCloseJob = new AutoTradeAfterCloseJob();
		try {
			autoTradeAfterCloseJob.execute(null);
			List unfinishedTrades = StocktradeDAO.getUnfinishedTradeRecords();
			for (int i = 0; i < unfinishedTrades.size(); i++) {
				StocktradeVO stVO = (StocktradeVO) unfinishedTrades.get(i);
				if(stVO.getStatus() != TradeConstants.STATUS_HOLDING){
					fail("Something wrong");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
