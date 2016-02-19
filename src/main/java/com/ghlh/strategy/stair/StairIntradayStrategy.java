package com.ghlh.strategy.stair;

import java.util.Date;
import java.util.List;

import com.ghlh.autotrade.EventRecorder;
import com.ghlh.autotrade.StockTradeIntradyMonitor;
import com.ghlh.autotrade.StockTradeIntradyUtil;
import com.ghlh.data.db.MonitorstockDAO;
import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.data.db.StocktradeDAO;
import com.ghlh.data.db.StocktradeVO;
import com.ghlh.data.db.StocktradeVOFile;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.strategy.AdditionInfoUtil;
import com.ghlh.strategy.MonitoringStrategy;
import com.ghlh.strategy.TradeConstants;
import com.ghlh.strategy.TradeUtil;
import com.ghlh.tradeway.SoftwareTrader;
import com.ghlh.util.MathUtil;

public class StairIntradayStrategy implements MonitoringStrategy {
	public void processSell(StockTradeIntradyMonitor monitor,
			StockQuotesBean sqb) {
		MonitorstockVO monitorstockVO = monitor.getMonitorstockVO();
		String additionInfo = monitorstockVO.getAdditioninfo();
		AdditionalInfoBean additionalBean = (AdditionalInfoBean) AdditionInfoUtil
				.parseAdditionalInfoBean(additionInfo,
						monitorstockVO.getTradealgorithm());
		List canSellList = monitor.getCanSellList();
		for (int i = 0; i < canSellList.size(); i++) {
			StocktradeVOFile stocktradeVO = (StocktradeVOFile) canSellList
					.get(i);
			if (sqb.getHighestPrice() >= stocktradeVO.getSellPrice()) {
				String message = TradeUtil.getConfirmedSellMessage(
						stocktradeVO.getStockid(), stocktradeVO.getNumber(),
						stocktradeVO.getSellPrice());
				EventRecorder.recordEvent(StockTradeIntradyUtil.class, message);
				SoftwareTrader.getInstance().sellStock(
						stocktradeVO.getStockid(), stocktradeVO.getNumber());
				canSellList.remove(i);
				i--;
				StocktradeDAO.removeSoldStockTrade(stocktradeVO);
				StocktradeDAO.saveTradeHistory(stocktradeVO, new Date());
				additionalBean
						.setCurrentStair(additionalBean.getCurrentStair() - 1);
				String additionalInfo = AdditionInfoUtil
						.parseAdditionalInfoBeanBack(additionalBean,
								monitorstockVO.getTradealgorithm());
				monitorstockVO.setAdditioninfo(additionalInfo);
				MonitorstockDAO.save(monitorstockVO);

			}
		}
	}

	public void processBuy(StockTradeIntradyMonitor monitor, StockQuotesBean sqb) {
		MonitorstockVO monitorstockVO = monitor.getMonitorstockVO();
		String additionInfo = monitorstockVO.getAdditioninfo();
		AdditionalInfoBean additionalBean = (AdditionalInfoBean) AdditionInfoUtil
				.parseAdditionalInfoBean(additionInfo,
						monitorstockVO.getTradealgorithm());
		if (additionalBean.getStairNumber() == additionalBean.getCurrentStair()) {
			return;
		}

		double buyPrice = 0;
		buyPrice = additionalBean.getFirstBuyPrice();

		if (additionalBean.getCurrentStair() == 0 && buyPrice == 0
				&& !additionalBean.getFirstBuyPriceStrategy().equals("…Ë∂®º€")) {
			buyPrice = sqb.getCurrentPrice();
			additionalBean.setFirstBuyPrice(buyPrice);
		}

		for (int i = 0; i < additionalBean.getCurrentStair(); i++) {
			buyPrice = MathUtil.formatDoubleWith2(buyPrice
					* (1 - additionalBean.getStairZDF()));
		}

		if (TradeUtil.isStopTrade(sqb)) {
			return;
		}
		if (sqb.getLowestPrice() <= buyPrice) {
			int number = TradeUtil.getTradeNumber(
					additionalBean.getStairMoney(), buyPrice);
			String message = TradeUtil.getConfirmedBuyMessage(monitor
					.getMonitorstockVO().getStockid(), number, buyPrice);

			EventRecorder.recordEvent(StockTradeIntradyUtil.class, message);
			SoftwareTrader.getInstance().buyStock(sqb.getStockId(), number);
			StocktradeVOFile stVO = new StocktradeVOFile();
			stVO.setStockid(monitor.getMonitorstockVO().getStockid());
			stVO.setBuyPrice(buyPrice);
			stVO.setSellPrice(MathUtil.formatDoubleWith2(buyPrice
					* (1 + additionalBean.getStairZDF())));
			stVO.setDate(new Date());
			stVO.setNumber(number);
			stVO.setStatus(0);
			StocktradeDAO.save(stVO);

			additionalBean
					.setCurrentStair(additionalBean.getCurrentStair() + 1);
			String additionalInfo = AdditionInfoUtil
					.parseAdditionalInfoBeanBack(additionalBean,
							monitorstockVO.getTradealgorithm());
			monitorstockVO.setAdditioninfo(additionalInfo);
			MonitorstockDAO.save(monitorstockVO);
		}
	}
}
