package com.ghlh.ui.autotrade.autotradetesting;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.ghlh.ui.autotrade.AbstractContentPanel;
import com.ghlh.ui.autotrade.StockIdFieldFocusListener;
import com.ghlh.ui.bean.ComponentsBean;

public class AutoTradeTestingContentPanelImpl extends AbstractContentPanel {
	private static Logger logger = Logger
			.getLogger(AutoTradeTestingContentPanelImpl.class);

	public AutoTradeTestingContentPanelImpl(ComponentsBean compomentBean) {
		content.setLayout(new BorderLayout());
		content.add(getButtonPanel(compomentBean.getButtons()),
				BorderLayout.NORTH);

		FlowLayout contentLayout = new FlowLayout();
		contentLayout.setAlignment(FlowLayout.LEFT);
		JPanel flowLayoutPanel = new JPanel();
		flowLayoutPanel.setLayout(contentLayout);
		flowLayoutPanel.add(getComponentsPanel(compomentBean.getComponents()));
		content.add(flowLayoutPanel, BorderLayout.CENTER);
		initButtonActionListener(compomentBean, flowLayoutPanel);
		JTextField stockIdField = (JTextField) this.getUIcomponents().get(0);
		JTextField stockNameField = (JTextField) this.getUIcomponents().get(1);
		stockIdField.addFocusListener(new StockIdFieldFocusListener(
				stockNameField));

	}
}
