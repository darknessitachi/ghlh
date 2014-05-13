package com.ghlh.strategy.ztCount;

import java.util.Date;
import java.util.List;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.util.DateUtil;
import com.ghlh.util.KLineUtil;

public class ZTOf123DaysAnalyst {

	public static void main(String[] args) {
		//Date now = DateUtil.getDate(2014, 2, 7);

		Date now = new Date();
		Date next = DateUtil.getNextMarketOpenDay(now);
		Date weeksAgo = DateUtil.getPreviousMarketOpenDay(now, 15);

		String sql = "SELECT stockid, ztgs FROM ( "
				+ " SELECT stockid, COUNT(zdf) ztgs FROM stockdailyinfo WHERE zdf > 8 AND highestprice != lowestprice AND todayopenprice != 0  AND DATE > '"
				+ DateUtil.formatDay(weeksAgo)
				+ "' and Date < '"
				+ DateUtil.formatDay(next)
				+ "' GROUP BY stockid) a WHERE a.ztgs >= 1 ORDER BY a.ztgs DESC ";
		System.out.println("Sql = " + sql);

		List stocks = GhlhDAO.list(sql, "com.ghlh.strategy.ztCount.ZTBean");
		for (int j = 0; j < stocks.size(); j++) {
			ZTBean bean = (ZTBean) stocks.get(j);
			KLineUtil.saveUrlAs(bean.getStockid(),
			"ztgs\\" + DateUtil.formatDay(now), bean.getZtgs() + "_"
							+ bean.getStockid());
		}

	}
}
