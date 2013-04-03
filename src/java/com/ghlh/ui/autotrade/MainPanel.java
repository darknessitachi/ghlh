package com.ghlh.ui.autotrade;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.tree.DefaultMutableTreeNode;

import com.ghlh.ui.autotrade.autotradetesting.AutoTradeTestingContentPanelImpl;
import com.ghlh.ui.autotrade.stocksetting.StockSettingContentPanel;
import com.ghlh.ui.bean.ComponentsBean;

public class MainPanel {
	JSplitPane mainPanel = null;
	private Map treeCmdMap = new HashMap();
	private JPanel contentPanel = new JPanel();

	private List test = null;

	private static MainPanel instance = new MainPanel();

	public static MainPanel getInstance() {
		return instance;
	}

	private ContentPanel stockSettingUI;

	private MainPanel() {
		mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		CommandTree ct = new CommandTree();
		ct.setMainPanel(this);
		mainPanel.add(new JScrollPane(ct.getCmds()));
		treeCmdMap.put("SoftwareSetting", this.getSoftwareSettingUI()
				.getContent());
		treeCmdMap.put("AutoTradeTesting", this.getAutoTradeTestingUI()
				.getContent());
		stockSettingUI = this.getStockSettingUI();
		treeCmdMap.put("StockSetting", stockSettingUI.getContent());

		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(this.getSoftwareSettingUI().getContent(),
				BorderLayout.CENTER);
		mainPanel.add(contentPanel);
	}

	private ContentPanel getSoftwareSettingUI() {
		ComponentsBean cb = UIComponentsFactory.getInstance()
				.getComponentsBean("SoftwareSetting");
		ContentPanel cp = new DefaultContentPanelImpl(cb);
		return cp;
	}

	private ContentPanel getAutoTradeTestingUI() {
		ComponentsBean cb = UIComponentsFactory.getInstance()
				.getComponentsBean("AutoTradeTesting");
		ContentPanel cp = new AutoTradeTestingContentPanelImpl(cb);
		return cp;
	}

	public ContentPanel getStockSettingUI() {
		ContentPanel cp = null;
		if (stockSettingUI == null) {
			ComponentsBean cb = UIComponentsFactory.getInstance()
					.getComponentsBean("StockSetting");
			cp = new StockSettingContentPanel(cb);
		} else {
			cp = this.stockSettingUI;
		}
		return cp;

		/*
		 * List buttons = new ArrayList(); for (int i = 0; i < 4; i++) {
		 * buttons.add("Button" + i); } ComponentsBean cb = new
		 * ComponentsBean(); cb.setButtons(buttons); List components = new
		 * ArrayList(); String[] stockSettingLabels = { "交易策略", "股票代码", "股票名称",
		 * "基准价格", "能卖数量", "总数量" }; for (int i = 0; i <
		 * stockSettingLabels.length; i++) { Component component = new
		 * Component(); component.setLabel(stockSettingLabels[i]);
		 * component.setCompomentType(ComponentType.TEXT_FIELD); if (i == 0) {
		 * component.setCompomentType(ComponentType.COMBOX_FIELD); List items =
		 * new ArrayList(); items.add("上楼梯"); items.add("下楼梯");
		 * items.add("爬格子"); component.setSelectList(items); }
		 * components.add(component); } cb.setComponents(components);
		 * cb.setMenuCmd("StockSetting");
		 * 
		 * String[] additionalLabels = {"台阶涨跌幅","交易数量","当前台阶"}; List
		 * additionalComponents = new ArrayList(); for (int i = 0; i <
		 * additionalLabels.length; i++) { Component component = new
		 * Component(); component.setLabel(additionalLabels[i]);
		 * component.setCompomentType(ComponentType.TEXT_FIELD);
		 * additionalComponents.add(component); }
		 * cb.setAdditionalCompoments(additionalComponents);
		 * 
		 * List headerList = new ArrayList(); for (int i = 0; i <
		 * stockSettingLabels.length; i++) {
		 * headerList.add(stockSettingLabels[i]); }
		 * cb.setTableHeaders(headerList); ContentPanel cp = new
		 * StockSettingContentPanel(cb); return cp;
		 */}

	public JSplitPane getMainPanel() {
		return mainPanel;
	}

	public void swithContent(DefaultMutableTreeNode node) {
		if (node != null && node.isLeaf()) {
			if (node.getUserObject().equals(
					Constants.LABEL_TRADE_SOFTWARE_SETTING)) {
				this.contentPanel.removeAll();
				this.contentPanel.add((JPanel) this.treeCmdMap
						.get("SoftwareSetting"));
				this.contentPanel.repaint();
				this.contentPanel.validate();
			}
			if (node.getUserObject().equals(Constants.LABEL_AUTO_TRADE_TESTING)) {
				this.contentPanel.removeAll();
				this.contentPanel.add((JPanel) this.treeCmdMap
						.get("AutoTradeTesting"));
				this.contentPanel.repaint();
				this.contentPanel.validate();
			}
			if (node.getUserObject().equals(Constants.LABEL_STOCK_SETTING)) {
				this.contentPanel.removeAll();
				this.contentPanel.add((JPanel) this.treeCmdMap
						.get("StockSetting"));
				this.contentPanel.repaint();
				this.contentPanel.validate();
			}

		}
	}

}
