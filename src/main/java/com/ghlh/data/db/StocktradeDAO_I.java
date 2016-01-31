package com.ghlh.data.db;

import java.util.Date;
import java.util.List;

import com.ghlh.strategy.TradeConstants;

public interface StocktradeDAO_I {

	List getUnfinishedTradeRecords(String stockId, String strategy);

	List getUnfinishedTradeRecords();

	List getHoldingTradeRecords(String stockId, String strategy);

	List getIntradyHoldingTradeRecords(String stockId, String strategy);

	List getT_0_TradeRecords(String stockId, String strategy);

	List getSuccessfulTradeRecords(String stockId, String strategy);

	List getFailedTradeRecords(String stockId, String strategy);

	List getPossibleSellTradeRecords(String stockId, String strategy);

	List getPendingBuyTradeRecords(String stockId, String strategy);

	List getPendingRebuyTradeRecords(String stockId, String strategy);

	List getOneStockTradeRecords(String stockId, String strategy);

	void updateStocktradeStatus(int id, int status);

	void updateStocktradeFinished(int id);

	void updateStocktradeFailure(int id);

	void removeStocktrade(int id);

	void save(StocktradeVOFile stocktradeVO);

	List readCanSellStockTrade(String stockId);

	List readStockTrade(String stockId);

	void removeSoldStockTrade(StocktradeVOFile stocktradeVO);

	void saveTradeHistory(StocktradeVOFile stocktradeVO, Date soldDate);

}
