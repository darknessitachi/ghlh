package com.ghlh.strategy.ztCount;

import java.util.Date;
import java.util.List;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.util.DateUtil;
import com.ghlh.util.KLineUtil;

public class ZTCountAnalyst {

	public static void main(String[] args) {

		Date now = new Date();
		int previousDays = 4;
		Date latest = DateUtil.getLatestMarketOpenDay(now);
		Date previous = DateUtil.getPreviousMarketOpenDay(latest, previousDays);

		String sql = "SELECT * FROM stockdailyinfo WHERE zdf > 9.9 AND DATE >  '"
				+ DateUtil.formatDay(previous)
				+ "' AND DATE < '"
				+ DateUtil.formatDay(latest) + "'";
		System.out.println("Sql = " + sql);

		List stocks = GhlhDAO.list(sql, "com.ghlh.data.db.StockdailyinfoVO");
		for (int j = 0; j < stocks.size(); j++) {
			StockdailyinfoVO bean = (StockdailyinfoVO) stocks.get(j);
			KLineUtil
					.saveUrlAs(bean.getStockid(),
							"previousZT\\" + DateUtil.formatDay(now),
							bean.getStockid());
		}
	}
}
