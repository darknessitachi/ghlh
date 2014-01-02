package com.ghlh.data;

import java.util.List;

import org.junit.Test;

import com.ghlh.data.FileStockPoolAccessor;
import com.ghlh.data.MonitorStockBean;

public class FileStockPoolAccessorTest {

	@Test
	public void test() {
		try {
			StockPoolAccessor fileStockPoolAccessorTest = new FileStockPoolAccessor();
			List<MonitorStockBean> stocks = fileStockPoolAccessorTest.getMonitorStocks();
			assert (stocks.size() >= 0);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
	}

}
