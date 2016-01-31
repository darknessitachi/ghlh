package com.ghlh.data.db;

import java.util.Date;
import java.util.List;

public class StocktradeDAO {

	private static StocktradeDAO_I stocktradeDao = new StocktradeDAO_File();

	public static List getUnfinishedTradeRecords(String stockId, String strategy) {
		return stocktradeDao.getUnfinishedTradeRecords(stockId, strategy);
	}

	public static List getUnfinishedTradeRecords() {
		return stocktradeDao.getUnfinishedTradeRecords();
	}

	public static List getHoldingTradeRecords(String stockId, String strategy) {
		return stocktradeDao.getHoldingTradeRecords(stockId, strategy);
	}

	public static List getIntradyHoldingTradeRecords(String stockId,
			String strategy) {
		return stocktradeDao.getIntradyHoldingTradeRecords(stockId, strategy);
	}

	public static List getT_0_TradeRecords(String stockId, String strategy) {
		return stocktradeDao.getT_0_TradeRecords(stockId, strategy);
	}

	public static List getSuccessfulTradeRecords(String stockId, String strategy) {
		return stocktradeDao.getSuccessfulTradeRecords(stockId, strategy);
	}

	public static List getFailedTradeRecords(String stockId, String strategy) {
		return stocktradeDao.getFailedTradeRecords(stockId, strategy);
	}

	public static List getPossibleSellTradeRecords(String stockId,
			String strategy) {
		return stocktradeDao.getPossibleSellTradeRecords(stockId, strategy);
	}

	public static List getPendingBuyTradeRecords(String stockId, String strategy) {
		return stocktradeDao.getPendingBuyTradeRecords(stockId, strategy);
	}

	public static List getPendingRebuyTradeRecords(String stockId,
			String strategy) {
		return stocktradeDao.getPendingRebuyTradeRecords(stockId, strategy);
	}

	public static List getOneStockTradeRecords(String stockId, String strategy) {
		return stocktradeDao.getOneStockTradeRecords(stockId, strategy);
	}

	public static void save(StocktradeVOFile stocktradeVO) {
		stocktradeDao.save(stocktradeVO);
	}

	public static List readCanSellStockTrade(String stockId) {
		return stocktradeDao.readCanSellStockTrade(stockId);
	}

	public static void removeSoldStockTrade(StocktradeVOFile stocktradeVO) {
		stocktradeDao.removeSoldStockTrade(stocktradeVO);
	}

	public static List readStockTrade(String stockId) {
		return stocktradeDao.readStockTrade(stockId);
	}

	public static void updateStocktradeStatus(int id, int status) {
		stocktradeDao.updateStocktradeStatus(id, status);
	}

	public static void updateStocktradeFinished(int id) {
		stocktradeDao.updateStocktradeFinished(id);
	}

	public static void updateStocktradeFailure(int id) {
		stocktradeDao.updateStocktradeFailure(id);
	}

	public static void removeStocktrade(int id) {
		stocktradeDao.removeStocktrade(id);
	}

	public static void saveTradeHistory(StocktradeVOFile stocktradeVO, Date soldDate) {
		stocktradeDao.saveTradeHistory(stocktradeVO, soldDate);
	}

}
