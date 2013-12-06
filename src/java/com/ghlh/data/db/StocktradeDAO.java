package com.ghlh.data.db;

import java.util.Date;
import java.util.List;

import com.ghlh.strategy.TradeConstants;

public class StocktradeDAO {

	public static List getUnfinishedTradeRecords(String stockId, String strategy) {
		return getTradeRecords(stockId, strategy, 0, true);
	}

	public static List getUnfinishedTradeRecords() {
		return getTradeRecords(null, null, 0, true);
	}

	public static List getHoldingTradeRecords(String stockId, String strategy) {
		return getTradeRecords(stockId, strategy,
				TradeConstants.STATUS_HOLDING, true);
	}

	public static List getPossibleSellTradeRecords(String stockId,
			String strategy) {
		return getTradeRecords(stockId, strategy,
				TradeConstants.STATUS_POSSIBLE_SELL, false);
	}

	public static List getPendingBuyTradeRecords(String stockId, String strategy) {
		return getTradeRecords(stockId, strategy,
				TradeConstants.STATUS_PENDING_BUY, false);
	}

	public static List getOneStockTradeRecords(String stockId, String strategy) {
		return getTradeRecords(stockId, strategy, 0, false);
	}

	private static List getTradeRecords(String stockId, String strategy,
			int status, boolean isUnfinished) {
		String sql = "SELECT * FROM stocktrade";
		boolean isNeedWhere = true;

		if (stockId != null) {
			sql = appendConnectWork(sql, isNeedWhere);
			sql += " stockId = '" + stockId + "'";
			isNeedWhere = false;
		}
		if (strategy != null) {
			sql = appendConnectWork(sql, isNeedWhere);
			sql += " tradeAlgorithm = '" + strategy + "'";
			isNeedWhere = false;
		}

		if (status != 0) {
			sql = appendConnectWork(sql, isNeedWhere);
			sql += " status = " + status;
			isNeedWhere = false;
		}
		if (isUnfinished) {
			sql = appendConnectWork(sql, isNeedWhere);
			sql += " isNull(sellDate) ";
			isNeedWhere = false;
		}

		sql += " ORDER BY buyPrice desc ";
		List result = GhlhDAO.list(sql, "com.ghlh.data.db.StocktradeVO");
		return result;
	}

	private static String appendConnectWork(String sql, boolean isNeedWhere) {
		if (isNeedWhere) {
			sql += " Where ";
		} else {
			sql += " And ";
		}
		return sql;
	}

	public static void updateStocktradeStatus(int id, int status) {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(id);
		stocktradeVO1.setWhereId(true);
		stocktradeVO1.setStatus(status);
		stocktradeVO1.setSelldate(new Date());
		GhlhDAO.edit(stocktradeVO1);
	}

	public static void removeStocktrade(int id) {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(id);
		stocktradeVO1.setWhereId(true);
		GhlhDAO.remove(stocktradeVO1);
	}

}
