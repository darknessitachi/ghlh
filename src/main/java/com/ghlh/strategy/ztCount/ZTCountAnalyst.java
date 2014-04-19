package com.ghlh.strategy.ztCount;

import java.util.Date;
import java.util.List;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.util.DateUtil;
import com.ghlh.util.KLineUtil;

public class ZTCountAnalyst {

	public static void main(String[] args) {
		Date now = DateUtil.getDate(2014,2, 28);

		Date next = DateUtil.getNextMarketOpenDay(now);
		Date weeksAgo = DateUtil.getPreviousMarketOpenDay(now, 10);

		String sql = "SELECT stockid FROM ( "
				+ " SELECT stockid, COUNT(zdf) ztgs FROM stockdailyinfo WHERE zdf > 8  AND DATE > '"
				+ DateUtil.formatDay(weeksAgo)
				+ "' and Date < '"
				+ DateUtil.formatDay(next)
				+ "' GROUP BY stockid) a WHERE a.ztgs > 1 ORDER BY a.ztgs DESC ";
		List stocks = GhlhDAO.list(sql);
		for (int j = 0; j < stocks.size(); j++) {
			String stockId = (String) stocks.get(j);
			KLineUtil.saveUrlAs(stockId, "ztgs\\" + DateUtil.formatDay(now));
		}

	}
}
