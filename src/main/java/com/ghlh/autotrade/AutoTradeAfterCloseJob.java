package com.ghlh.autotrade;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.TradeConstants;
import com.ghlh.strategy.TradeUtil;
import com.ghlh.util.DateUtil;
import com.ghlh.util.EastMoneyUtil;
import com.ghlh.util.StockMarketUtil;

public class AutoTradeAfterCloseJob implements Job {
	private static Logger logger = Logger
			.getLogger(AutoTradeAfterCloseJob.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String message = "开始15:05盘后处理";
		EventRecorder.recordEvent(this.getClass(), message);
		if (StockMarketUtil.isMarketRest()) {
			return;
		}
		processPredoneOrders();
		collectStockDailyInfo();
		message = "结束15:05盘后处理";
		EventRecorder.recordEvent(this.getClass(), message);
	}

	private void collectStockDailyInfo() {
		Date now = new Date();
		EventRecorder.recordEvent(this.getClass(), "开始收集收盘数据");
		String today = DateUtil.formatDay(now);
		for (int i = 0; i < 5; i++) {
			new DataCollector().collectDailyInfo(now, false);
			String sql = "SELECT COUNT(*) FROM stockdailyinfo WHERE "
					+ "(stockid = '000001' OR stockid = '600036' OR stockid = '000002') AND DATE LIKE '"
					+ today + "%'";
			String value = GhlhDAO.selectSingleValue(sql);
			int count = Integer.parseInt(value);
			if (count != 0) {
				EventRecorder.recordEvent(this.getClass(), "第 " + (i + 1)
						+ "收集成功");
				break;
			} else {
				if (i == 4) {
					EventRecorder.recordEvent(this.getClass(), "第 " + (i + 1)
							+ "收集 还是没有成功!");
				}
			}
		}
		EventRecorder.recordEvent(this.getClass(), "结束收集收盘数据");

	}

	public static void main(String[] args) {
		try {
			new AutoTradeAfterCloseJob().execute(null);
		} catch (Exception ex) {
			System.out.println();
		}

	}

	private void processPredoneOrders() {
		List unfinishedTrades = StocktradeDAO.getUnfinishedTradeRecords();
		for (int i = 0; i < unfinishedTrades.size(); i++) {
			StocktradeVO stVO = (StocktradeVO) unfinishedTrades.get(i);
			String eventMessage = null;
			if (stVO.getStatus() == TradeConstants.STATUS_POSSIBLE_SELL
					|| stVO.getStatus() == TradeConstants.STATUS_T_0_BUY) {
				eventMessage = TradeUtil.getAfterCloseEventMessage(stVO);
				StocktradeDAO.updateStocktradeStatus(stVO.getId(),
						TradeConstants.STATUS_HOLDING);
			}
			if (stVO.getStatus() == TradeConstants.STATUS_PENDING_BUY) {
				eventMessage = TradeUtil.getAfterCloseEventMessage(stVO);
				StocktradeDAO.removeStocktrade(stVO.getId());
			}
			if (eventMessage != null) {
				EventRecorder.recordEvent(this.getClass(), eventMessage);
			}
		}
	}
}
