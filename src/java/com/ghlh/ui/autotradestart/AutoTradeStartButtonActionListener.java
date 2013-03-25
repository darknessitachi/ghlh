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
		StatusField.getInstance().setWarningMessage("�Զ����׼����������");
	}

	protected void initButtonStatus() {
		((JButton) this.getjButtons().get(0)).setEnabled(true);
		((JButton) this.getjButtons().get(1)).setEnabled(false);
	}

	public void button1ActionPerformed(ActionEvent e) {
		int confirm = GUIUtil.showConfirmDialog(
				"��ȷ���Ѳ��Թ�֤ȯ�������? ��û��,�뵽[�Զ���������/�Զ����ײ���]���в���,��ȷ����ļ�ؽ����ܳɹ�ִ��",
				"�����������ȷ��");
		if (confirm == 0) {
			confirm = GUIUtil.showConfirmDialog("�Զ��������������,��¼����С������? ",
					"�����������ȷ��");
			if (confirm == 0) {
				AutoTradeSwitch.getInstance().setStart(true);
				((JButton) this.getjButtons().get(0)).setEnabled(false);
				((JButton) this.getjButtons().get(1)).setEnabled(true);
				if (StockMarketUtil.isMarketOpenning()) {
					AutoTradeStockQuartzServer.getInstance().addRightNowJob();
					StatusField.getInstance().setPromptMessage(
							"�Զ����׼���������� ���ڼ�ؽ�����");
				} else {
					StatusField.getInstance().setPromptMessage(
							"�Զ����׼���������� ����������");
				}
				AutoTradeStockQuartzServer.getInstance().startJob();
			}
		}
	}

	public void button2ActionPerformed(ActionEvent e) {
		AutoTradeSwitch.getInstance().setStart(false);
		((JButton) this.getjButtons().get(0)).setEnabled(true);
		((JButton) this.getjButtons().get(1)).setEnabled(false);
		StatusField.getInstance().setPromptMessage("�Զ����׼����ֹͣ");
		AutoTradeStockQuartzServer.getInstance().stopJob();
	}
}
