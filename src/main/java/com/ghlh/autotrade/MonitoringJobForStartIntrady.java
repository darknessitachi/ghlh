package com.ghlh.autotrade;

public class MonitoringJobForStartIntrady implements Runnable {

	public void run() {
		new StockTradeIntradyMonitoringJob().monitoring();
	}

}
