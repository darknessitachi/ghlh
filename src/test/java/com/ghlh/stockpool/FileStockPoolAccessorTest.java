package com.ghlh.stockpool;

import java.util.List;

import org.junit.Test;

import com.ghlh.stockpool.FileStockPoolAccessor;
import com.ghlh.stockpool.MonitorStockBean;

public class FileStockPoolAccessorTest {

	@Test
	public void test() {
		try {
			FileStockPoolAccessor fileStockPoolAccessorTest = new FileStockPoolAccessor();
			List<MonitorStockBean> stocks = fileStockPoolAccessorTest.getMonitorStocks();
			assert (stocks.size() >= 0);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
	}

}
