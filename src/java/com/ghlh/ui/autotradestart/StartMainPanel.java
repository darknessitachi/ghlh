package com.ghlh.ui.autotradestart;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.ghlh.ui.autotrade.CommandTree;
import com.ghlh.ui.autotrade.ContentPanel;
import com.ghlh.ui.autotrade.DefaultContentPanelImpl;
import com.ghlh.ui.autotrade.UIComponentsFactory;
import com.ghlh.ui.autotrade.stocksetting.StockSettingContentPanel;
import com.ghlh.ui.bean.ComponentsBean;

public class StartMainPanel {

	private JPanel contentPanel = new JPanel();

	public JPanel getContentPanel() {
		return contentPanel;
	}

	private List test = null;

	public StartMainPanel() {
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(this.getAutoTradeStartUI().getContent(),
				BorderLayout.CENTER);
	}

	private ContentPanel getAutoTradeStartUI() {
		ComponentsBean cb = new AutoTradeStartingUICompomentsImpl()
				.getComponentsBean();
		ContentPanel cp = new AutoTradeStartContentPanelImpl(cb);
		return cp;
	}

}
