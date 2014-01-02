package com.ghlh.ui.autotradestart;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import org.apache.log4j.Logger;

import com.ghlh.autotrade.AutoTradeStockQuartzServer;
import com.ghlh.autotrade.EventRecorder;
import com.ghlh.autotrade.MonitoringJobForStartIntrady;
import com.ghlh.ui.AbstractButtonActionListener;
import com.ghlh.ui.StatusField;
import com.ghlh.util.GUIUtil;
import com.ghlh.util.StockMarketUtil;

public class AutoTradeStartButtonActionListener extends
		AbstractButtonActionListener {

	private static Logger logger = Logger
			.getLogger(AutoTradeStartButtonActionListener.class);

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
				AutoTradeStockQuartzServer.getInstance().startJob();
				if (!StockMarketUtil.isMarketRest()
						&& !StockMarketUtil.isMarketBreak()) {
					EventRecorder.recordEvent(this.getClass(), "�����������, ��ؽ�����");
					StatusField.getInstance().setPromptMessage("�Զ����׼���������� ���ڼ����");
					Thread t = new Thread(new MonitoringJobForStartIntrady());
					t.start();
				}
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
