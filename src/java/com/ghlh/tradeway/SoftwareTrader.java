package com.ghlh.tradeway;

import java.util.HashMap;
import java.util.Map;

import com.ghlh.conf.ConfigurationAccessor;
import com.ghlh.tradeway.software.TradeSoftwareController;

public class SoftwareTrader implements StockTrader {

	private final static String BUY_CMD = "BuyStock";
	private final static String SELL_CMD = "SellStock";

	private static StockTrader instance = new SoftwareTrader();

	public static StockTrader getInstance() {
		return instance;
	}

	public void buyStock(String stockId, int number) {
		if (ConfigurationAccessor.getInstance().isOpenSoftwareTrade()) {
			tradeStock(stockId, number, BUY_CMD);
		}
	}

	public void sellStock(String stockId, int number) {
		if (ConfigurationAccessor.getInstance().isOpenSoftwareTrade()) {
			tradeStock(stockId, number, SELL_CMD);
		}
	}
	
	public void buyStock(String stockId, int number, double price){
		//@to-do
	}
	public void sellStock(String stockId, int number, double price){
		//@to-do
	}

	private void tradeStock(String stockId, int number, String tradeCmd) {
		Map<String, Object> cmdParameters = prepareParameters(stockId, number);
		TradeSoftwareController.getInstance().executeTradeCMD(tradeCmd,
				cmdParameters);

	}

	private Map<String, Object> prepareParameters(String stockID, int count) {
		Map<String, Object> cmdParameters = new HashMap<String, Object>();
		cmdParameters.put("stockID", stockID);
		cmdParameters.put("count", new Integer(count));
		return cmdParameters;
	}

	public void activateTradeSoft() {
		if (ConfigurationAccessor.getInstance().isOpenSoftwareTrade()) {
			TradeSoftwareController.getInstance().activateTradeSoft();
		}
	}

}
