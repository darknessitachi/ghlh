package com.ghlh.strategy.morning4percent;

import java.util.Date;
import java.util.List;

import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.common.util.IDGenerator;
import com.ghlh.autotrade.EventRecorder;
import com.ghlh.data.db.GhlhDAO;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StockdailyinfoVO;
import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.AdditionInfoUtil;
import com.ghlh.strategy.BuyStockBean;
import com.ghlh.strategy.TradeUtil;
import com.ghlh.util.DateUtil;
import com.ghlh.util.MathUtil;

public class Buyer {
	public static Logger logger = Logger.getLogger(Buyer.class);

	public void buy(Date date) {
		String sDate = DateUtil.formatDate(date);
		//String sDate = "2014-02-07 10:09:31";
		String sql = "SELECT * FROM stockdailyinfo10 WHERE DATE = '"
				+ sDate
				+ "' AND zdf >= 3.5 AND zdf <= 5.5"
				+ " AND (todayopenprice-yesterdaycloseprice)/yesterdaycloseprice <0.02 "
				+ " ORDER BY hsl DESC";
		List list = GhlhDAO
				.list(sql, "com.ghlh.data.db.StockdailyinfoVO", 0, 1);
		if (list.size() > 0) {
			StockdailyinfoVO stockdailyinfoVO = (StockdailyinfoVO) list.get(0);
			String stockId = stockdailyinfoVO.getStockid();
			MonitorstockVO monitorstockVO = new MonitorstockVO();
			monitorstockVO.setTradealgorithm("Morning4Percent");
			monitorstockVO.setStockid(stockId);
			StockQuotesBean sqb = InternetStockQuotesInquirer.getInstance()
					.getStockQuotesBean(stockId);
			monitorstockVO.setName(sqb.getName());
			int id = IDGenerator.generateId(MonitorstockVO.TABLE_NAME);
			monitorstockVO.setId(id);
			AdditionalInfoBean aib = new AdditionalInfoBean();
			aib.setLostDf(0);
			aib.setTargetZf(0);
			aib.setTradeMoney(20000);
			String additionalInfo = AdditionInfoUtil
					.parseAdditionalInfoBeanBack(aib, "Morning4Percent");
			monitorstockVO.setAdditioninfo(additionalInfo);
			monitorstockVO.setOnmonitoring("false");
			monitorstockVO.setCreatedtimestamp(date);
			monitorstockVO.setCreatedtimestamp(date);
			GhlhDAO.create(monitorstockVO);
			double winSellPrice = 0;
			winSellPrice = MathUtil.formatDoubleWith2QuanShe(winSellPrice);
			String message = TradeUtil.getIntradyPriceBuyMessage(
					monitorstockVO.getStockid(),
					TradeUtil.getTradeNumber(aib.getTradeMoney(),
							sqb.getCurrentPrice()), sqb.getCurrentPrice(),
					TradeUtil.PRICE_10);
			EventRecorder.recordEvent(Buyer.class, message);
			BuyStockBean buyStockBean = new BuyStockBean();
			buyStockBean.setStockId(monitorstockVO.getStockid());
			buyStockBean.setTradeMoney(aib.getTradeMoney());
			buyStockBean.setBuyPrice(sqb.getCurrentPrice());
			buyStockBean.setWinSellPrice(winSellPrice);
			buyStockBean.setLostSellPrice(0);
			buyStockBean.setStrategy(monitorstockVO.getTradealgorithm());
			TradeUtil.dealBuyStockSuccessfully(buyStockBean);
		}
	}

	public static void main(String[] args) {
		new Buyer().buy(new Date());
	}
}