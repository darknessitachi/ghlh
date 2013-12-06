package com.ghlh.strategy;

import java.util.Date;

import com.common.util.IDGenerator;
import com.ghlh.autotrade.EventRecorder;
import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.tradeway.SoftwareTrader;

public class TradeUtil {

	public static void dealBuyStock(String stockId, double tradeMoney,
			double basePrice, double sellPrice, String strategy) {
		int number = getTradeNumber(tradeMoney, basePrice);
		dealBuyStock(stockId, basePrice, sellPrice, strategy, number);
	}

	public static void dealBuyStockSuccessfully(String stockId,
			double tradeMoney, double basePrice, double sellPrice,
			String strategy) {
		int number = getTradeNumber(tradeMoney, basePrice);
		dealBuyStockSuccessfully(stockId, basePrice, sellPrice, strategy,
				number);
	}

	public static int getTradeNumber(double tradeMoney, double basePrice) {
		int number = (int) (tradeMoney / basePrice);
		number = ((int) (number / 100)) * 100;
		return number;
	}

	public static void dealBuyStock(String stockId, double basePrice,
			double sellPrice, String strategy, int number) {
		dealBuyStockWith2Status(stockId, basePrice, sellPrice, strategy,
				number, false);
	}

	public static void dealBuyStockSuccessfully(String stockId,
			double basePrice, double sellPrice, String strategy, int number) {
		dealBuyStockWith2Status(stockId, basePrice, sellPrice, strategy,
				number, true);

	}

	private static void dealBuyStockWith2Status(String stockId,
			double basePrice, double sellPrice, String strategy, int number,
			boolean isConfirm) {

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
			stocktradeVO1.setStatus(TradeConstants.STATUS_T_0_BUY);
			SoftwareTrader.getInstance().buyStock(stockId, number);

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

	public static String getOpenPriceBuyMessage(String stockId, int number,
			double price) {
		return getEventMessage(stockId, number, price, "buy", false, PRICE_OPEN);
	}

	public static String getIntradyPriceBuyMessage(String stockId, int number,
			double price, int priceType) {
		return getEventMessage(stockId, number, price, "buy", false, priceType);
	}

	public static String getPendingSellMessage(String stockId, int number,
			double price) {
		return getEventMessage(stockId, number, price, "sell", true, 0);
	}

	public static String getConfirmedSellMessage(String stockId, int number,
			double price) {
		return getEventMessage(stockId, number, price, "sell", false, 0);
	}

	public static String getPendingBuyMessage(String stockId, int number,
			double price) {
		return getEventMessage(stockId, number, price, "buy", true, 0);
	}

	public static String getConfirmedBuyMessage(String stockId, int number,
			double price) {
		return getEventMessage(stockId, number, price, "buy", false, 0);
	}

	public static final int PRICE_OPEN = 1;
	public static final int PRICE_CLOSE = 2;
	public static final int PRICE_NOON = 3;

	private static String getEventMessage(String stockId, int number,
			double price, String cmd, boolean isPending, int priceType) {
		String message = "";
		if (cmd.equals("buy")) {
			message += "买入";
		} else {
			message += "卖出";
		}
		message += "股票:" + stockId;
		if (isPending) {
			message += " 预下单";
		} else {
			message += " 成交";
		}
		message += " 数量:" + number;
		message += " 价格:" + price;
		switch (priceType) {
		case PRICE_OPEN:
			message += "(开盘价)";
			break;
		case PRICE_CLOSE:
			message += "(收盘价)";
			break;
		case PRICE_NOON:
			message += "(午盘价)";
			break;
		default:
		}
		return message;
	}

	public static String getAfterCloseEventMessage(StocktradeVO stVO) {
		String result = null;
		if (stVO.getStatus() == TradeConstants.STATUS_POSSIBLE_SELL) {
			result = "预下单卖出股票:" + stVO.getStockid() + "未成功，将状态置回持有";
		}
		if (stVO.getStatus() == TradeConstants.STATUS_T_0_BUY) {
			result = "下单买入股票:" + stVO.getStockid() + "成功，将状态置回持有";
		}
		if (stVO.getStatus() == TradeConstants.STATUS_PENDING_BUY) {
			result = "预下单买入股票:" + stVO.getStockid() + "未成功，将交易记录删除";
		}

		return result;
	}

	public static int getPriceType(String buyPriceStrategy) {
		if (buyPriceStrategy.equals("开盘价")) {
			return PRICE_OPEN;
		}
		if (buyPriceStrategy.equals("午盘价")) {
			return PRICE_NOON;
		}
		return 0;
	}
}
