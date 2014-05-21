package com.ghlh.strategy.ztCount;

import java.util.Date;
import java.util.List;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.util.DateUtil;
import com.ghlh.util.KLineUtil;

public class SpecialTermSpecialZTCountAnalyst {

	public static void main(String[] args) {

		Date now = new Date();
		int previousDays = 5;
		Date latest = DateUtil.getNextMarketOpenDay(now);
		Date previous = DateUtil
				.getPreviousMarketOpenDay(now, previousDays - 1);

		String sql = "SELECT stockid, ztgs FROM ( "
				+ " SELECT stockid, COUNT(zdf) ztgs FROM stockdailyinfo WHERE zdf > 9.9 AND DATE > '"
				+ DateUtil.formatDay(previous) + "' AND DATE < '"
				+ DateUtil.formatDay(latest) + "'"
				+ " GROUP BY stockid) a WHERE a.ztgs >= 1 ORDER BY a.ztgs DESC ";
		System.out.println("Sql = " + sql);

		List stocks = GhlhDAO.list(sql, "com.ghlh.strategy.ztCount.ZTBean");
		for (int j = 0; j < stocks.size(); j++) {
			ZTBean bean = (ZTBean) stocks.get(j);
			KLineUtil.saveUrlAs(bean.getStockid(),
					"specialtermspecialztcount\\" + DateUtil.formatDay(now),
					bean.getStockid());
		}
	}
}
