package com.ghlh.autotrade;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.strategy.TradeConstants;

public class AutoTradeAfterCloseJob implements Job {
	private static Logger logger = Logger
			.getLogger(AutoTradeAfterCloseJob.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String message = "��ʼ15:05�̺���";
		EventRecorder.recordEvent(this.getClass(), message);
		List unfinishedTrades = StocktradeDAO.getUnfinishedTradeRecords();
		for (int i = 0; i < unfinishedTrades.size(); i++) {
			StocktradeVO stVO = (StocktradeVO) unfinishedTrades.get(i);
			if (stVO.getStatus() == TradeConstants.STATUS_POSSIBLE_SELL
					|| stVO.getStatus() == TradeConstants.STATUS_T_0_BUY) {
				StocktradeDAO.updateStocktradeStatus(stVO.getId(),
						TradeConstants.STATUS_HOLDING);
			}
			if (stVO.getStatus() == TradeConstants.STATUS_PENDING_BUY) {
				StocktradeDAO.removeStocktrade(stVO.getId());
			}
		}

		message = "����15:05�̺���";
		EventRecorder.recordEvent(this.getClass(), message);
	}
}
