package com.ghlh.strategy.catchyzstair;

/**
 * ץ��ͣ�� ����С������
 */
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ghlh.analysis.QiangZTResultBean;
import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.util.DateUtil;
import com.ghlh.util.MathUtil;

public class CatchYZAnalyst {

	static double yinliPercentage = 0;
	static double kuiSunPercentage = 0;

	public QiangZTResultBean calculateResult() {
		pickup = 0;
		yinLi = 0;
		kuiSun = 0;

		double winPercentage = 0.04;
		double lostPercentage = 0.3;
		double openPercentage = 0.0;
		Date date = DateUtil.getDate(2014, 3, 1);
		Date now = new Date();
		Map alreadyAnaystedMap = new HashMap();

		while (date.before(now)) {
			String sDate = DateUtil.formatDay(date);
			// System.out.println("process date = " + sDate);
			String sql = "SELECT * FROM stockdailyinfo WHERE todayopenprice = highestprice AND highestprice = lowestprice AND todayopenprice > 0 AND DATE LIKE '"
					+ sDate + "%' ";

			List<StockdailyinfoVO> list = GhlhDAO.list(sql,
					"com.ghlh.data.db.StockdailyinfoVO");

			if (list == null) {
				date = DateUtil.getNextMarketOpenDay(date);
				continue;
			}

			for (int i = 0; i < list.size(); i++) {
				StockdailyinfoVO ztDailyinfoVO = list.get(i);
				String stockId = ztDailyinfoVO.getStockid();
				if (alreadyAnaystedMap.get(stockId) != null) {
					continue;
				}
				StockQuotesBean sqb = InternetStockQuotesInquirer.getInstance()
						.getStockQuotesBean(stockId);
				if (sqb.getName().indexOf("ST") >= 0) {
					continue;
				}
				Date nextDate = DateUtil.getNextMarketOpenDay(date);
				String sNextDay = DateUtil.formatDay(nextDate);
				String sql1 = "SELECT * FROM stockdailyinfo WHERE DATE > '"
						+ sNextDay + "' AND stockId = '" + stockId
						+ "' ORDER BY DATE";
				List dailyInfoList = GhlhDAO.list(sql1,
						"com.ghlh.data.db.StockdailyinfoVO");
				double buyPrice = 0;
				double inPos = 0;
				Date inDate = null;

				for (int j = 0; j < dailyInfoList.size(); j++) {
					StockdailyinfoVO sdiVO1 = (StockdailyinfoVO) dailyInfoList
							.get(j);
					if (buyPrice != 0) {
						if (sdiVO1.getCurrentprice() > 0) {
							double highPrice = sdiVO1.getHighestprice();
							double percentage = (highPrice - buyPrice)
									/ buyPrice;
							percentage = MathUtil
									.formatDoubleWith2(percentage * 100);
							if (percentage > 0) {
								System.out.println(sqb.getStockId() + " "
										+ sqb.getName() + "��"
										+ DateUtil.formatDay(inDate)
										+ "��ͼ�����, �ڵ� " + (j - inPos)
										+ " ���ӯ��**************�ɽ�, �����:"
										+ buyPrice + " ������߼� :" + highPrice
										+ " ӯ������:" + percentage);
								yinliPercentage += percentage;
								yinLi++;
								break;
							} else {
								System.out
										.println(sqb.getStockId()
												+ " "
												+ sqb.getName()
												+ "��"
												+ DateUtil.formatDay(inDate)
												+ "��ͼ�����, �ڵ� "
												+ (j - inPos)
												+ " ������-----------------------------�ɽ�, �����:"
												+ buyPrice + " ������߼� :"
												+ highPrice + " �������:"
												+ percentage);
								kuiSun++;
								kuiSunPercentage += percentage;

								break;
							}
						}
					}

					if (sdiVO1.getTodayopenprice() > 0
							&& sdiVO1.getTodayopenprice() == sdiVO1
									.getHighestprice()
							&& sdiVO1.getHighestprice() == sdiVO1
									.getLowestprice()) {
						continue;
					} else {
						if (sdiVO1.getTodayopenprice() > 0) {
							if (sdiVO1.getHighestprice() != sdiVO1
									.getLowestprice()) {
								double inPrice = sdiVO1.getLowestprice();
								inPrice = MathUtil
										.formatDoubleWith2QuanJin(inPrice);
								buyPrice = inPrice;
								inPos = j;
								inDate = sdiVO1.getDate();
								System.out.println(stockId + " ��  " + (j + 1)
										+ " ��һ�ֺ�, ��"
										+ DateUtil.formatDay(sdiVO1.getDate())
										+ "��ͣ�򿪣�������ͼ�:" + inPrice);
								pickup++;
								alreadyAnaystedMap.put(stockId, "already");
							}
						}
					}
				}

			}
			date = DateUtil.getNextMarketOpenDay(date);
		}
		QiangZTResultBean result = new QiangZTResultBean();
		result.setKuiSun(kuiSun);
		result.setYinLi(yinLi);
		result.setPickUp(pickup);
		return result;
	}

	private int kuiSun = 0;
	private int yinLi = 0;
	private int pickup = 0;

	public static void main(String[] args) {
		QiangZTResultBean result = new CatchYZAnalyst().calculateResult();
		System.out.println("Result = " + result + " ӯ���ܰٷֱ�  : "
				+ MathUtil.formatDoubleWith2(yinliPercentage) + " �����ܰٷֱ� : "
				+ MathUtil.formatDoubleWith2(kuiSunPercentage));
	}
}
