package com.ghlh.strategy.morning4percent;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class QiangZTAnalyst {
	private String[] factorNames = { "date", "zdts", "minzdf", "maxzdf",
			"maxavg", "minavg", "closezt", "winpercentage", "lostpercentage",
			"lowopen", "basedonyesterdayclosepercentage",
			"basedontodayopenpercentage" };

	private int[] lastPos = null;
	private Map factors;

	public QiangZTAnalyst(Map factors) {
		this.factors = factors;
		lastPos = new int[factorNames.length];
		for (int i = 0; i < factorNames.length; i++) {
			lastPos[i] = ((List) factors.get(factorNames[i])).size() - 1;
		}
	}

	private int times = 0;

	public void checkBean() {
		FactorsBean result = null;
		QiangZTOpitimizator opitimizator = new QiangZTOpitimizator();
		for (Object o0 : (List) factors.get(factorNames[0])) {
			result = new FactorsBean();
			result.setDate((Date) o0);
			for (Object o1 : (List) factors.get(factorNames[1])) {
				result.setZdts(((Integer) o1).intValue());
				for (Object o2 : (List) factors.get(factorNames[2])) {
					result.setMinZdf(((Double) o2).doubleValue());
					for (Object o3 : (List) factors.get(factorNames[3])) {
						result.setMaxZdf(((Double) o3).doubleValue());

						for (Object o4 : (List) factors.get(factorNames[4])) {
							huigu1(result, opitimizator, o4);
						}
					}
				}

			}
		}

	}

	private void huigu1(FactorsBean result, QiangZTOpitimizator opitimizator,
			Object o4) {
		result.setMaxAvg(((Double) o4).doubleValue());

		for (Object o5 : (List) factors.get(factorNames[5])) {
			result.setMinAvg(((Double) o5).doubleValue());

			for (Object o6 : (List) factors.get(factorNames[6])) {
				result.setCloseZT(((Double) o6).doubleValue());

				for (Object o7 : (List) factors.get(factorNames[7])) {
					huigui2(result, opitimizator, o7);
				}
			}
		}
	}

	private void huigui2(FactorsBean result, QiangZTOpitimizator opitimizator,
			Object o7) {
		result.setWinPercentage(((Double) o7).doubleValue());

		for (Object o8 : (List) factors.get(factorNames[8])) {
			result.setLostPercentage(((Double) o8).doubleValue());
			for (Object o9 : (List) factors.get(factorNames[9])) {
				result.setLowOpen(((Boolean) o9).booleanValue());
				for (Object o10 : (List) factors.get(factorNames[10])) {
					result.setBasedonYesterdayClosePercentage(((Double) o10)
							.doubleValue());
					for (Object o11 : (List) factors.get(factorNames[11])) {
						result.setBasedonTodayOpenPercentage(((Double) o11)
								.doubleValue());

						times++;
						QiangZTResultBean resultBean = opitimizator
								.calculateResult(result);
						System.out.println("times = " + times + " Result ="
								+ resultBean + " Factors = " + result);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		QiangZTAnalyst lib = new QiangZTAnalyst(QiangZTFactorsReader.getInstance()
				.getFactors());
		lib.checkBean();
	}
}
