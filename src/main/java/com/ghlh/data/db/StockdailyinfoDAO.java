package com.ghlh.data.db;

import java.util.List;

public class StockdailyinfoDAO {
	public static List getPrevious9DaysInfo(String stockId) {
		String sql = "select * from stockdailyinfo where stockid = '" + stockId
				+ "' order by date desc";
		String className = "com.ghlh.data.db.StockdailyinfoVO";
		return GhlhDAO.list(sql, className, 0, 9);
	}
}
