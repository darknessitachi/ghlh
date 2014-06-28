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
			tradeStock(stockId, number, 0, BUY_CMD);
		}
	}

	public void sellStock(String stockId, int number) {
		if (ConfigurationAccessor.getInstance().isOpenSoftwareTrade()) {
			tradeStock(stockId, number, 0, SELL_CMD);
		}
	}

	public void buyStock(String stockId, int number, double price) {
		if (ConfigurationAccessor.getInstance().isOpenSoftwareTrade()) {
			tradeStock(stockId, number, price, BUY_CMD);
		}
	}

	public void sellStock(String stockId, int number, double price) {
		if (ConfigurationAccessor.getInstance().isOpenSoftwareTrade()) {
			tradeStock(stockId, number, price, SELL_CMD);
		}
	}

	private void tradeStock(String stockId, int number, double price,
			String tradeCmd) {
		Map<String, Object> cmdParameters = prepareParameters(stockId, number,
				price);
		TradeSoftwareController.getInstance().executeTradeCMD(tradeCmd,
				cmdParameters);

	}

	private Map<String, Object> prepareParameters(String stockID, int count,
			double price) {
		Map<String, Object> cmdParameters = new HashMap<String, Object>();
		cmdParameters.put("stockID", stockID);
		cmdParameters.put("count", new Integer(count));
		if (price > 0) {
			cmdParameters.put("price", new Double(price));
		}
		return cmdParameters;
	}

	public void activateTradeSoft() {
		if (ConfigurationAccessor.getInstance().isOpenSoftwareTrade()) {
			TradeSoftwareController.getInstance().activateTradeSoft();
		}
	}

	public static void main(String[] args) {
//		SoftwareTrader.getInstance().buyStock("600036",100);	
//		SoftwareTrader.getInstance().sellStock("300056",100);	
//		SoftwareTrader.getInstance().activateTradeSoft();
	}

}

