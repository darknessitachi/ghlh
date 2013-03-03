package com.ghlh.ui.autotrade;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class CommandTree implements TreeSelectionListener {
	private JTree cmds = null;
	private MainPanel mainPanel = null;

	public void setMainPanel(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public CommandTree() {
		DefaultMutableTreeNode autoTrade = new DefaultMutableTreeNode(
				Constants.LABEL_AUTO_TRADE);
		DefaultMutableTreeNode autoSoftwareSetting = new DefaultMutableTreeNode(
				Constants.LABEL_TRADE_SOFTWARE_SETTING);
		autoTrade.add(autoSoftwareSetting);
		DefaultMutableTreeNode autoTradeTesting = new DefaultMutableTreeNode(
				Constants.LABEL_AUTO_TRADE_TESTING);
		autoTrade.add(autoTradeTesting);
		DefaultMutableTreeNode stockSetting = new DefaultMutableTreeNode(
				Constants.LABEL_STOCK_SETTING);
		autoTrade.add(stockSetting);
		cmds = new JTree(autoTrade);
		cmds.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		cmds.addTreeSelectionListener(this);
	}

	public JTree getCmds() {
		return cmds;
	}

	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) cmds
				.getLastSelectedPathComponent();

		mainPanel.swithContent(node);
	}
}
