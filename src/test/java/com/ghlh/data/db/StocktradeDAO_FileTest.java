package com.ghlh.data.db;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class StocktradeDAO_FileTest {

	@Test
	public void test() {
			StocktradeDAO_File dao = new StocktradeDAO_File();
			dao.removeStockTrade("600036");
			
			StocktradeVOFile vo = new StocktradeVOFile();
			vo.setBuyPrice(10);
			vo.setDate(new Date());
			vo.setNumber(1000);
			vo.setSellPrice(11);
			vo.setStockid("600036");
			dao.save(vo);
			StocktradeVOFile vo2 = new StocktradeVOFile();
			vo2.setBuyPrice(20);
			vo2.setDate(new Date());
			vo2.setNumber(2000);
			vo2.setSellPrice(21);
			vo2.setStockid("600036");
			dao.save(vo2);
			StocktradeVOFile vo3 = new StocktradeVOFile();
			vo3.setBuyPrice(30);
			vo3.setDate(new Date());
			vo3.setNumber(3000);
			vo3.setSellPrice(31);
			vo3.setStockid("600036");
			vo3.setStatus(1);
			dao.save(vo3);
			List listAll = dao.readStockTrade("600036");
			Assert.assertEquals(3, listAll.size());
			
			List list = dao.readCanSellStockTrade("600036");
			Assert.assertEquals(1, list.size());

			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StocktradeVOFile vo4 = new StocktradeVOFile();
			vo4.setBuyPrice(40);
			vo4.setDate(new Date());
			vo4.setNumber(4000);
			vo4.setSellPrice(41);
			vo4.setStockid("600036");
			vo4.setStatus(1);
			dao.save(vo4);
			list = dao.readCanSellStockTrade("600036");
			Assert.assertEquals(2, list.size());
			StocktradeVOFile vo5 = (StocktradeVOFile)list.get(1);
			dao.removeSoldStockTrade(vo5);
			list = dao.readCanSellStockTrade("600036");
			Assert.assertEquals(1, list.size());

			dao.saveTradeHistory(vo4, new Date());
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			dao.saveTradeHistory(vo3, new Date());
			
	
	}
}
