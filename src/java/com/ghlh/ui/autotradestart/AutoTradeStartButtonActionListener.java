package com.ghlh.ui.autotradestart;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import com.ghlh.AutoTradeStockQuartzServer;
import com.ghlh.StockTradeMonitoringJob;
import com.ghlh.ui.AbstractButtonActionListener;
import com.ghlh.ui.StatusField;
import com.ghlh.util.GUIUtil;
import com.ghlh.util.StockMarketUtil;

public class AutoTradeStartButtonActionListener extends
		AbstractButtonActionListener {

	public AutoTradeStartButtonActionListener() {
		StatusField.getInstance().setWarningMessage("自动交易监控启动窗口");
	}

	protected void initButtonStatus() {
		((JButton) this.getjButtons().get(0)).setEnabled(true);
		((JButton) this.getjButtons().get(1)).setEnabled(false);
	}

	public void button1ActionPerformed(ActionEvent e) {
		int confirm = GUIUtil.showConfirmDialog(
				"你确认已测试过证券交易软件? 如没有,请到[自动交易设置/自动交易测试]进行测试,以确保你的监控交易能成功执行",
				"交易软件测试确认");
		if (confirm == 0) {
			confirm = GUIUtil.showConfirmDialog("自动交易软件已启动,登录并最小化窗口? ",
					"交易软件启动确认");
			if (confirm == 0) {
				AutoTradeSwitch.getInstance().setStart(true);
				((JButton) this.getjButtons().get(0)).setEnabled(false);
				((JButton) this.getjButtons().get(1)).setEnabled(true);
				if (StockMarketUtil.isMarketOpenning()) {
					AutoTradeStockQuartzServer.getInstance().addRightNowJob();
					StatusField.getInstance().setPromptMessage(
							"自动交易监控已启动， 正在监控交易中");
				} else {
					StatusField.getInstance().setPromptMessage(
							"自动交易监控已启动， 现在休市中");
				}
				AutoTradeStockQuartzServer.getInstance().startJob();
			}
		}
	}

	public void button2ActionPerformed(ActionEvent e) {
		AutoTradeSwitch.getInstance().setStart(false);
		((JButton) this.getjButtons().get(0)).setEnabled(true);
		((JButton) this.getjButtons().get(1)).setEnabled(false);
		StatusField.getInstance().setPromptMessage("自动交易监控已停止");
		AutoTradeStockQuartzServer.getInstance().stopJob();
	}
}
