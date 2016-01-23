package com.ghlh.data.db;

import junit.framework.Assert;

import org.junit.Test;

public class MonitorstockDAO_FileTest {

	@Test
	public void testMethod() {
		MonitorstockDAO_I monitorstockDAO = new MonitorstockDAO_File();
		MonitorstockVO monitorstockVO = new MonitorstockVO();
		monitorstockVO.setAdditioninfo("50 50 100");
		monitorstockVO.setStockid("600036");
		monitorstockVO.setName("’–…Ã“¯––");
		monitorstockVO.setOnmonitoring("true");
		monitorstockVO.setTradealgorithm("Stairs");
		monitorstockDAO.save(monitorstockVO);
		MonitorstockVO monitorstockVOGet = monitorstockDAO.get("600036");
		Assert.assertEquals(monitorstockVO.getStockid(), monitorstockVOGet.getStockid());
		Assert.assertEquals(monitorstockVO.getAdditioninfo(), monitorstockVOGet.getAdditioninfo());
		Assert.assertEquals(monitorstockVO.getName(), monitorstockVOGet.getName());
		Assert.assertEquals(monitorstockVO.getOnmonitoring(), monitorstockVOGet.getOnmonitoring());
		Assert.assertEquals(monitorstockVO.getTradealgorithm(), monitorstockVOGet.getTradealgorithm());
	}

}
