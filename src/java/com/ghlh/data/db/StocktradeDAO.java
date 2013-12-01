package com.ghlh.data.db;

import java.util.List;

public class StocktradeDAO {

	public static List getHoldStocks(String stockId, String strategy) {
		String sql = "SELECT * FROM stocktrade WHERE stockId = '" + stockId
				+ "' AND tradeAlgorithm = '" + strategy
				+ "' AND isNull(sellDate) ORDER BY buyPrice desc ";
		List result = GhlhDAO.list(sql, "com.ghlh.data.db.StocktradeVO");
		return result;
	}

	public static List getOneStockTradeRecords(String stockId, String strategy) {
		String sql = "SELECT * FROM stocktrade WHERE stockId = '" + stockId
				+ "' AND tradeAlgorithm = '" + strategy
				+ "' ORDER BY buyPrice desc ";
		List result = GhlhDAO.list(sql, "com.ghlh.data.db.StocktradeVO");
		return result;
	}

}
