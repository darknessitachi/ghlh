package com.ghlh.autotrade;

public class MonitoringJobForStartIntrady implements Runnable {

	@Override
	public void run() {
		new StockTradeIntradyMonitoringJob().monitoring();
	}

}
