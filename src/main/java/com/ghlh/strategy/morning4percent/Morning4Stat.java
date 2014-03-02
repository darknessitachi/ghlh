package com.ghlh.strategy.morning4percent;

import java.util.Date;
import java.util.List;

import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.MathUtil;

public class Morning4Stat {

	public static void main(String[] args) {
		Date date = DateUtil.getDate(2014, 1, 7);
		double lowPercentage = 3.5;
		double highPercentage = 5.5;
		double lostPercentage = 0.08;
		double winPercentage = 0.12;
		int stockCount = 1;

		Date now = new Date();

		while (date.before(now)) {
			String sDate = DateUtil.formatDay(date);
			String sql = "SELECT * FROM stockdailyinfo10 WHERE DATE LIKE '"
					+ sDate + "%' AND zdf >= " + lowPercentage + " AND zdf <= "
					+ highPercentage + " ORDER BY hsl DESC";

			List list = GhlhDAO.list(sql, "com.ghlh.data.db.StockdailyinfoVO",
					0, stockCount + 2);
			int no = 0;

			for (int i = 0; i < list.size(); i++) {
				StockdailyinfoVO stockdailyinfoVO = (StockdailyinfoVO) list
						.get(i);
				StockQuotesBean sqb = InternetStockQuotesInquirer.getInstance()
						.getStockQuotesBean(stockdailyinfoVO.getStockid());
				if (sqb.getName().indexOf("ST") >= 0) {
					continue;
				}
				no++;
				processBuyStockResult(stockdailyinfoVO, sqb, date,
						winPercentage, lostPercentage);
				if (no >= stockCount) {
					break;
				}
			}
			date = DateUtil.getNextDay(date);

		}

	}

	public static void processBuyStockResult(StockdailyinfoVO stockdailyinfoVO,
			StockQuotesBean sqb, Date date, double winPercentage,
			double lostPercentage) {
		double buyPrice = stockdailyinfoVO.getCurrentprice();
		double winPrice = MathUtil.formatDoubleWith2QuanShe(buyPrice
				* (1 + winPercentage));
		double lostPrice = MathUtil.formatDoubleWith2QuanJin(buyPrice
				* (1 - lostPercentage));

		Date next = DateUtil.getNextDay(date);
		String sNext = DateUtil.formatDay(next);
		String sql1 = "SELECT * FROM stockdailyinfo WHERE stockid = '"
				+ stockdailyinfoVO.getStockid() + "' AND DATE > '" + sNext
				+ "' ORDER BY Date";

		List dailyInfoList = GhlhDAO.list(sql1,
				"com.ghlh.data.db.StockdailyinfoVO");
		for (int j = 0; j < dailyInfoList.size(); j++) {
			StockdailyinfoVO stockdailyinfoVO1 = (StockdailyinfoVO) dailyInfoList
					.get(j);
			if (stockdailyinfoVO1.getCurrentprice() == 0) {
				continue;
			}
			double highPrice = stockdailyinfoVO1.getHighestprice();
			double lowPrice = stockdailyinfoVO1.getLowestprice();
			if (highPrice >= winPrice && lowPrice <= lostPrice) {
				System.out.println("ͬʱ��һ��ﵽӯ�� �� ���� �۸�");
				break;
			}
			if (highPrice >= winPrice) {
				System.out.println(date + " " + sqb.getStockId() + " "
						+ sqb.getName() + "�ڵ� " + (j + 1) + " ��ӯ���ɽ�, �����:"
						+ buyPrice + " ������ :" + winPrice + " ���������Ƿ�:"
						+ stockdailyinfoVO1.getZdf());
				break;
			}
			if (lowPrice <= lostPrice) {
				System.out.println(date + " " + sqb.getStockId() + " "
						+ sqb.getName() + "�ڵ� " + (j + 1) + " �����ɽ�, �����:"
						+ buyPrice + " ������ :" + lostPrice + " ���������Ƿ�:"
						+ stockdailyinfoVO1.getZdf());
				break;
			}
		}
	}

}