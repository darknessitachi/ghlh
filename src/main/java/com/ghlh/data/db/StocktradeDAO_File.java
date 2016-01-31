package com.ghlh.data.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.ghlh.strategy.TradeConstants;
import com.ghlh.util.DateUtil;
import com.ghlh.util.MiscUtil;

public class StocktradeDAO_File implements StocktradeDAO_I {

	public List getUnfinishedTradeRecords(String stockId, String strategy) {
		return getTradeRecords(stockId, strategy, 0, true, false);
	}

	public List getUnfinishedTradeRecords() {
		return getTradeRecords(null, null, 0, true, false);
	}

	public List getHoldingTradeRecords(String stockId, String strategy) {
		return getTradeRecords(stockId, strategy,
				TradeConstants.STATUS_HOLDING, true, false);
	}

	public List getIntradyHoldingTradeRecords(String stockId, String strategy) {
		String sql = "SELECT * FROM stocktrade where  stockId = '" + stockId
				+ "' and tradeAlgorithm = '" + strategy + "' and (status = "
				+ TradeConstants.STATUS_HOLDING + " or status = "
				+ TradeConstants.STATUS_POSSIBLE_SELL + " or status = "
				+ TradeConstants.STATUS_T_0_BUY;
		sql += ") ORDER BY buyPrice desc ";
		List result = GhlhDAO.list(sql, "com.ghlh.data.db.StocktradeVO");
		return result;
	}

	public List getT_0_TradeRecords(String stockId, String strategy) {
		return getTradeRecords(stockId, strategy,
				TradeConstants.STATUS_T_0_BUY, false, false);
	}

	public List getSuccessfulTradeRecords(String stockId, String strategy) {
		return getTradeRecords(stockId, strategy,
				TradeConstants.STATUS_SUCCESS, false, false);
	}

	public List getFailedTradeRecords(String stockId, String strategy) {
		return getTradeRecords(stockId, strategy,
				TradeConstants.STATUS_FAILURE, false, false);
	}

	public List getPossibleSellTradeRecords(String stockId, String strategy) {
		return getTradeRecords(stockId, strategy,
				TradeConstants.STATUS_POSSIBLE_SELL, false, false);
	}

	public List getPendingBuyTradeRecords(String stockId, String strategy) {
		return getTradeRecords(stockId, strategy,
				TradeConstants.STATUS_PENDING_BUY, false, false);
	}

	public List getPendingRebuyTradeRecords(String stockId, String strategy) {
		return getTradeRecords(stockId, strategy,
				TradeConstants.STATUS_PENDING_BUY, false, true);
	}

	public List getOneStockTradeRecords(String stockId, String strategy) {
		return getTradeRecords(stockId, strategy, 0, false, false);
	}

	private List getTradeRecords(String stockId, String strategy, int status,
			boolean isUnfinished, boolean isRebuy) {
		return null;
	}

	private String appendConnectWork(String sql, boolean isNeedWhere) {
		if (isNeedWhere) {
			sql += " Where ";
		} else {
			sql += " And ";
		}
		return sql;
	}

	public void updateStocktradeStatus(int id, int status) {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(id);
		stocktradeVO1.setWhereId(true);
		stocktradeVO1.setStatus(status);
		GhlhDAO.edit(stocktradeVO1);
	}

	public void updateStocktradeFinished(int id) {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(id);
		stocktradeVO1.setWhereId(true);
		stocktradeVO1.setStatus(TradeConstants.STATUS_SUCCESS);
		stocktradeVO1.setSelldate(new Date());
		GhlhDAO.edit(stocktradeVO1);
	}

	public void updateStocktradeFailure(int id) {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(id);
		stocktradeVO1.setWhereId(true);
		stocktradeVO1.setStatus(TradeConstants.STATUS_FAILURE);
		stocktradeVO1.setSelldate(new Date());
		GhlhDAO.edit(stocktradeVO1);
	}

	public void removeStocktrade(int id) {
		StocktradeVO stocktradeVO1 = new StocktradeVO();
		stocktradeVO1.setId(id);
		stocktradeVO1.setWhereId(true);
		GhlhDAO.remove(stocktradeVO1);
	}

	public void save(StocktradeVOFile stocktradeVO) {
		String stockId = stocktradeVO.getStockid();
		List result = readTrackFromFile(stockId, false);
		result.add(stocktradeVO);
		saveStocktradeToFile(stockId, result);
	}

	public void removeStockTrade(String stockId) {
		String filePath = "MonitorStocks" + "/" + stockId + "_trade";
		File monitorStockFile = new File(filePath);
		monitorStockFile.delete();
	}

	private void saveStocktradeToFile(String stockId, List result) {
		String filePath = "MonitorStocks" + "/" + stockId + "_trade";
		File monitorStockFile = new File(filePath);
		try {
			FileWriter fileWriter = new FileWriter(monitorStockFile);
			fileWriter.write("#Latest update at:" + new Date() + "\r\n");

			for (int i = 0; i < result.size(); i++) {
				StocktradeVOFile stocktradeVOFile = (StocktradeVOFile) result
						.get(i);
				String line = formatStocktradeVOFile(stocktradeVOFile);
				fileWriter.write(line + "\r\n");
			}
			fileWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public List readCanSellStockTrade(String stockId) {
		return readTrackFromFile(stockId, true);
	}

	public List readStockTrade(String stockId) {
		return readTrackFromFile(stockId, false);
	}

	private List readTrackFromFile(String stockId, boolean isCanSell) {
		String filePath = "MonitorStocks" + "/" + stockId + "_trade";
		File file = new File(filePath);
		List result = new ArrayList();
		if (file.exists()) {
			try {
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(new FileInputStream(file)));
				String line = bufferedReader.readLine();
				while (line != null && !line.trim().equals("")) {
					if (!MiscUtil.isComment(line)) {
						StocktradeVOFile stockTrade = parseStocktradeVOFile(line);
						if (isCanSell) {
							if (stockTrade.getStatus() == 1) {
								result.add(stockTrade);
							}
						} else {
							result.add(stockTrade);
						}
					}
					line = bufferedReader.readLine();
				}
				bufferedReader.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	private StocktradeVOFile parseStocktradeVOFile(String line) {
		Pattern pattern = Pattern.compile("#");
		String[] line_StocktradeVOFile = pattern.split(line);
		StocktradeVOFile stockTrade = new StocktradeVOFile();
		stockTrade.setDate(DateUtil.parseDate(line_StocktradeVOFile[0]));
		stockTrade.setStockid(line_StocktradeVOFile[1]);
		stockTrade.setBuyPrice(Double.parseDouble(line_StocktradeVOFile[2]));
		stockTrade.setNumber(Integer.parseInt(line_StocktradeVOFile[3]));
		stockTrade.setSellPrice(Double.parseDouble(line_StocktradeVOFile[4]));
		stockTrade.setStatus(Integer.parseInt(line_StocktradeVOFile[5]));
		return stockTrade;
	}

	private String formatStocktradeVOFile(StocktradeVOFile stocktradeVO) {
		String result = "";
		result += DateUtil.formatDate(stocktradeVO.getDate());
		result += "#";
		result += stocktradeVO.getStockid();
		result += "#";
		result += stocktradeVO.getBuyPrice();
		result += "#";
		result += stocktradeVO.getNumber();
		result += "#";
		result += stocktradeVO.getSellPrice();
		result += "#";
		result += stocktradeVO.getStatus();
		return result;
	}

	public void removeSoldStockTrade(StocktradeVOFile stocktradeVO) {
		String stockId = stocktradeVO.getStockid();
		List result = readTrackFromFile(stockId, false);
		for (int i = 0; i < result.size(); i++) {
			StocktradeVOFile stVOFile = (StocktradeVOFile) result.get(i);
			if (stocktradeVO.getDate().equals(stVOFile.getDate())) {
				result.remove(i);
				break;
			}
		}

		saveStocktradeToFile(stockId, result);
	}

	public void saveTradeHistory(StocktradeVOFile stocktradeVO, Date soldDate) {
		String filePath = "MonitorStocks" + "/" + "tradeHistory";
		File monitorStockFile = new File(filePath);
		try {
			FileWriter fileWriter = new FileWriter(monitorStockFile, true);
			String line = formatStocktradeVOFile(stocktradeVO);
			fileWriter.write(line + "#" + DateUtil.formatDate(soldDate)
					+ "\r\n");
			fileWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
