package com.ghlh.tradeway;

import java.util.HashMap;
import java.util.Map;

import com.ghlh.tradeway.software.TradeSoftwareController;

public class SoftwareTrader implements StockTrader {

	private final static String BUY_CMD = "BuyStock";
	private final static String SELL_CMD = "SellStock";

	public void buyStock(String stockId, int number) {
		tradeStock(stockId, number, BUY_CMD);
	}

	public void sellStock(String stockId, int number) {
		tradeStock(stockId, number, SELL_CMD);
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
	
	public void activateTradeSoft(){
		TradeSoftwareController.getInstance().activateTradeSoft();
	}

}
