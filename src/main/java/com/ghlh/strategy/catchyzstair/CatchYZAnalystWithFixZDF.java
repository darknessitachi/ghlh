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

public class CatchYZAnalystWithFixZDF {

	public QiangZTResultBean calculateResult() {
		pickup = 0;
		yinLi = 0;
		kuiSun = 0;

		double winPercentage = 0.04;
		double lostPercentage = 0.3;
		double openPercentage = 0.0;
		Date date = DateUtil.getDate(2014, 1, 7);
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
					// System.out.println("sdiVO.getDate() = " +
					// sdiVO1.getDate());
					if (buyPrice != 0) {
						double winPrice = buyPrice * (1 + winPercentage);
						winPrice = MathUtil.formatDoubleWith2QuanShe(winPrice);

						double losePrice = buyPrice * (1 - lostPercentage);
						losePrice = MathUtil
								.formatDoubleWith2QuanJin(losePrice);
						double highPrice = sdiVO1.getHighestprice();
						double lowPrice = sdiVO1.getLowestprice();
						if (highPrice >= winPrice && lowPrice <= losePrice) {
							System.out.println("ͬʱ��һ��ﵽӯ�� �� ���� �۸�");
							break;
						} else if (highPrice >= winPrice) {
							System.out.println(sqb.getStockId() + " "
									+ sqb.getName() + "��"
									+ DateUtil.formatDay(inDate) + "����, �ڵ� "
									+ (j - inPos)
									+ " ���ӯ��**************�ɽ�, �����:" + buyPrice
									+ " ������ :" + winPrice + " ���������Ƿ�:"
									+ sdiVO1.getZdf());
							yinLi++;
							break;
						} else if (lowPrice <= losePrice) {
							System.out.println(sqb.getStockId() + " "
									+ sqb.getName() + "��"
									+ DateUtil.formatDay(inDate) + "����, �ڵ� "
									+ (j - inPos)
									+ " ������________________�ɽ�, �����:" + buyPrice
									+ " ������ :" + losePrice + " ���������Ƿ�:"
									+ sdiVO1.getZdf());
							kuiSun++;
							break;
						}
						if (j == dailyInfoList.size() - 1) {
							double currentPrice = sdiVO1.getCurrentprice();
							double yikui = MathUtil
									.formatDoubleWith2((currentPrice - buyPrice)
											/ buyPrice * 100);
							System.out.println(inDate + " " + sqb.getStockId()
									+ " " + sqb.getName() + "�ڵ� " + (j - inPos)
									+ " ��δ��Ŀ��, �����:" + buyPrice
									+ " ��ǰ-------------------------------ӯ��: "
									+ yikui);
						}
						continue;
					}
					if (sdiVO1.getTodayopenprice() > 0
							&& sdiVO1.getTodayopenprice() == sdiVO1
									.getHighestprice()
							&& sdiVO1.getHighestprice() == sdiVO1
									.getLowestprice()) {
						continue;
					} else {
						if (sdiVO1.getTodayopenprice() > 0) {
							double openZDF = (sdiVO1.getTodayopenprice() - sdiVO1
									.getYesterdaycloseprice())
									/ sdiVO1.getYesterdaycloseprice() * 100;
							if (openZDF >= 9.9) {
								double inPrice = sdiVO1.getTodayopenprice()
										* (1 - openPercentage);
								inPrice = MathUtil
										.formatDoubleWith2QuanJin(inPrice);
								if (sdiVO1.getLowestprice() <= inPrice) {
									buyPrice = inPrice;
									inPos = j;
									inDate = sdiVO1.getDate();
									System.out.println(stockId
											+ " ��  "
											+ (j + 1)
											+ " ��һ�ֺ�, ��"
											+ DateUtil.formatDay(sdiVO1
													.getDate())
											+ "��ͣ���̣� ��������������������:" + inPrice);
									pickup++;
									alreadyAnaystedMap.put(stockId, "already");
								} else {
									System.out.println(stockId
											+ " ��  "
											+ (j + 1)
											+ " ��һ�ֺ�, ��"
											+ DateUtil.formatDay(sdiVO1
													.getDate())
											+ "��ͣ���̣��򿪲������������");
									alreadyAnaystedMap.put(stockId, "already");
									break;
								}
							} else {
								// System.out.println("StockID " + stockId
								// + " �� " + sdiVO1.getDate()
								// + "δ��ͣ���̣��򿪲������������");
								alreadyAnaystedMap.put(stockId, "already");
								break;
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
		QiangZTResultBean result = new CatchYZAnalystWithFixZDF().calculateResult();
		System.out.println("Result = " + result);
	}
}
