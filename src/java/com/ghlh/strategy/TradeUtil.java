package com.ghlh.strategy;

import java.util.Date;

import com.common.util.IDGenerator;
import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.tradeway.SoftwareTrader;

public class TradeUtil {

	public static void dealBuyStock(String stockId, double tradeMoney,
			double basePrice, double sellPrice, String strategy) {
		int number = getTradeNumber(tradeMoney, basePrice);
		dealBuyStock(stockId, basePrice, sellPrice, strategy, number);
	}

//	public static void dealConfirmedBuyStock(String stockId, double tradeMoney,
//			double basePrice, double sellPrice, String strategy) {
//		int number = getTradeNumber(tradeMoney, basePrice);
//		dealConfirmedBuyStock(stockId, basePrice, sellPrice, strategy, number);
//	}

	private static int getTradeNumber(double tradeMoney, double basePrice) {
		int number = (int) (tradeMoney / basePrice);
		number = ((int) (number / 100)) * 100;
		return number;
	}

	public static void dealBuyStock(String stockId, double basePrice,
			double sellPrice, String strategy, int number) {
		dealBuyStockWith2Status(stockId, basePrice, sellPrice, strategy,
				number, false);
	}

//	public static void dealConfirmedBuyStock(String stockId, double basePrice,
//			double sellPrice, String strategy, int number) {
//		dealBuyStockWith2Status(stockId, basePrice, sellPrice, strategy,
//				number, true);
//	}

	private static void dealBuyStockWith2Status(String stockId,
			double basePrice, double sellPrice, String strategy, int number,
			boolean isConfirm) {
		SoftwareTrader.getInstance().buyStock(stockId, number, basePrice);
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(IDGenerator.generateId("stocktrade"));
		stocktradeVO1.setStockid(stockId);
		stocktradeVO1.setTradealgorithm(strategy);
		stocktradeVO1.setBuydate(new Date());
		stocktradeVO1.setBuybaseprice(basePrice);
		stocktradeVO1.setBuyprice(basePrice);
		stocktradeVO1.setNumber(number);
		stocktradeVO1.setSellprice(sellPrice);
		stocktradeVO1.setCreatedtimestamp(new Date());
		stocktradeVO1.setLastmodifiedtimestamp(new Date());
		if (isConfirm) {
			stocktradeVO1.setStatus(TradeConstants.STATUS_HOLDING);
		} else {
			stocktradeVO1.setStatus(TradeConstants.STATUS_PENDING_BUY);
		}
		GhlhDAO.create(stocktradeVO1);
	}

	public static void dealSell(StocktradeVO stocktradeVO) {
		SoftwareTrader.getInstance().sellStock(stocktradeVO.getStockid(),
				stocktradeVO.getNumber(), stocktradeVO.getSellprice());
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(stocktradeVO.getId());
		stocktradeVO1.setWhereId(true);
		stocktradeVO1.setStatus(TradeConstants.STATUS_POSSIBLE_SELL);
		GhlhDAO.edit(stocktradeVO1);
	}

}
