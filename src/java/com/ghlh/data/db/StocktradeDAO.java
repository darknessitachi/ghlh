package com.ghlh.data.db;

import java.util.List;

import com.ghlh.strategy.TradeConstants;

public class StocktradeDAO {

	public static List getUnfinishedTradeRecords(String stockId, String strategy) {
		return getTradeRecords(stockId, strategy, 0, true);
	}

	public static List getPossibleSellTradeRecords(String stockId,
			String strategy) {
		return getTradeRecords(stockId, strategy,
				TradeConstants.STATUS_POSSIBLE_SELL, false);
	}

	public static List getPossiblePendingBuyTradeRecords(String stockId,
			String strategy) {
		return getTradeRecords(stockId, strategy,
				TradeConstants.STATUS_PENDING_BUY, false);
	}

	public static List getOneStockTradeRecords(String stockId, String strategy) {
		return getTradeRecords(stockId, strategy, 0, false);
	}

	private static List getTradeRecords(String stockId, String strategy,
			int status, boolean isUnfinished) {
		String sql = "SELECT * FROM stocktrade WHERE stockId = '" + stockId
				+ "' AND tradeAlgorithm = '" + strategy + "' ";
		if (status != 0) {
			sql += " AND status = " + status;
		}
		if (isUnfinished) {
			sql += " AND isNull(sellDate) ";
		}
		sql += " ORDER BY buyPrice desc ";
		List result = GhlhDAO.list(sql, "com.ghlh.data.db.StocktradeVO");
		return result;
	}

}
