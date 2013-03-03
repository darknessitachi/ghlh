package com.ghlh.ui.autotrade.stocksetting;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.ghlh.stockpool.FileStockPoolAccessor;
import com.ghlh.stockpool.MonitorStockBean;
import com.ghlh.ui.AbstractButtonActionListener;
import com.ghlh.ui.autotrade.AbstractContentPanel;
import com.ghlh.ui.autotrade.ContentPanelUtil;
import com.ghlh.ui.bean.UIComponentMetadata;
import com.ghlh.ui.bean.UIComponentType;
import com.ghlh.ui.bean.ComponentsBean;
import com.ghlh.util.MiscUtil;

public class StockSettingContentPanel extends AbstractContentPanel {
	private static Logger logger = Logger
			.getLogger(StockSettingContentPanel.class);

	private JTable stockTable = null;

	public StockSettingContentPanel(ComponentsBean compomentBean) {
		this.content.setLayout(new BorderLayout());
		JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.content.add(splitPanel, BorderLayout.CENTER);
		JPanel panel1 = new JPanel();
		this.addOneComponentEditor(compomentBean, panel1);
		splitPanel.add(panel1);
		initTable();
		JScrollPane scrollPane = new JScrollPane(stockTable);
		splitPanel.add(scrollPane);
	}

	private void initTable() {
		stockTable = new JTable();
		stockTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		stockTable.setFillsViewportHeight(true);
		stockTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		stockTable.setRowSelectionAllowed(true);
		stockTable.setColumnSelectionAllowed(false);
		stockTable.setModel(new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}
		});

		initTableData();

		stockTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						tableRowSelectionActionPerformed(event);
					}
				});
	}

	private void initTableData() {
		String[] columnNames = { "交易策略", "股票代码", "股票名称", "基准价格", "能卖数量", "总数量",
				"是否监控" };
		((DefaultTableModel) this.stockTable.getModel())
				.setColumnIdentifiers(columnNames);

		try {
			FileStockPoolAccessor accessor = new FileStockPoolAccessor();
			List<MonitorStockBean> list = accessor.getMonitorStocks();
			for (int i = 0; i < list.size(); i++) {
				MonitorStockBean msb = (MonitorStockBean) list.get(i);
				if (MiscUtil.isComment(msb.getStockId())) {
					continue;
				}
				Vector rowV = convertMonitorStockBeanToVector(msb);
				((DefaultTableModel) this.stockTable.getModel()).addRow(rowV);
			}
		} catch (Exception ex) {
			logger.error("Read stock list throw", ex);
		}
	}

	public void addStockInTable(MonitorStockBean msb) {
		Vector rowV = convertMonitorStockBeanToVector(msb);
		((DefaultTableModel) this.stockTable.getModel()).addRow(rowV);
		((DefaultTableModel) this.stockTable.getModel()).fireTableDataChanged();
	}

	private Vector convertMonitorStockBeanToVector(MonitorStockBean msb) {
		Vector rowV = new Vector();
		rowV.add(StockSettingUICompomentsImpl.getStrategyName(msb
				.getTradeAlgorithm()));
		rowV.add(msb.getStockId());
		rowV.add(msb.getName());
		rowV.add(msb.getStandardPrice());
		rowV.add(msb.getCanSellNumber());
		rowV.add(msb.getCurrentNumber());
		rowV.add(new Boolean(true));
		return rowV;
	}

	public void tableRowSelectionActionPerformed(ListSelectionEvent event) {
		int selectRow = this.stockTable.getSelectedRow();
		if (selectRow >= 0) {
			String strategy = this.stockTable.getValueAt(selectRow, 0)
					.toString();
			this.switchTradeStrategy(strategy);
		}
	}

	public StockSettingContentPanel() {
	}

	private JPanel componentPanel = new JPanel();

	public JPanel getComponentPanel() {
		return componentPanel;
	}

	private void addOneComponentEditor(ComponentsBean compomentBean,
			JPanel content) {
		content.setLayout(new BorderLayout());
		content.add(getButtonPanel(compomentBean.getButtons()),
				BorderLayout.NORTH);

		FlowLayout contentLayout = new FlowLayout();

		JPanel flowLayoutPanel = new JPanel();
		flowLayoutPanel.setLayout(contentLayout);
		contentLayout.setAlignment(FlowLayout.LEFT);

		this.componentPanel = getComponentsPanel(compomentBean);

		flowLayoutPanel.add(this.componentPanel);
		content.add(flowLayoutPanel, BorderLayout.CENTER);

		initButtonActionListener(compomentBean, flowLayoutPanel);

	}

	public JPanel getComponentsPanel(ComponentsBean componentBean) {

		JPanel result = new JPanel();
		result.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		result.setLayout(new GridBagLayout());

		List additionalComponents = componentBean.getAdditionalCompoments();

		List components = componentBean.getComponents();

		for (int i = 0; i < components.size(); i++) {
			GridBagConstraints c = getConstraints((i % 3) * 2, i / 3);
			c.weightx = 0.5;
			c.anchor = GridBagConstraints.NORTHEAST;
			UIComponentMetadata com = (UIComponentMetadata) components.get(i);
			JLabel label = new JLabel(com.getLabel());
			result.add(label, c);
			c = getConstraints((i % 3) * 2 + 1, i / 3);
			JComponent component = ContentPanelUtil.getJComponent(com);
			result.add(component, c);
			this.getUIcomponents().add(component);
			if (com.getCompomentType() == UIComponentType.COMBOX_FIELD) {
				((JComboBox) component).addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						comboBoxActionPerformed(e);
					}
				});
			}
		}
		basicEndPos = components.size();
		addAdditionAWTComponents(result, additionalComponents, basicEndPos);
		return result;
	}

	private int basicEndPos = 0;
	private List additionalComponents;

	private void addAdditionAWTComponents(JPanel result,
			List additionalComponents, int basicEndPos) {
		if (additionalComponents != null) {
			this.additionalComponents = additionalComponents;
			for (int i = 0; i < additionalComponents.size(); i++) {
				int additionalPos = basicEndPos + i;
				GridBagConstraints c = getConstraints((additionalPos % 3) * 2,
						additionalPos / 3);
				c.weightx = 0.5;
				c.anchor = GridBagConstraints.NORTHEAST;
				UIComponentMetadata com = (UIComponentMetadata) additionalComponents.get(i);
				JLabel label = new JLabel(com.getLabel());
				result.add(label, c);
				c = getConstraints((additionalPos % 3) * 2 + 1,
						additionalPos / 3);
				JComponent component = ContentPanelUtil.getJComponent(com);
				result.add(component, c);
				this.getUIcomponents().add(component);

				additionalAWTComponents.add(label);
				additionalAWTComponents.add(component);
			}
		}
	}

	private List additionalAWTComponents = new ArrayList();

	public void comboBoxActionPerformed(ActionEvent e) {
		try {
			String strategy = ((JComboBox) e.getSource()).getSelectedItem()
					.toString();
			switchTradeStrategy(strategy);
		} catch (Exception ex) {
			logger.error("perform ComboBox Action throw exception:", ex);
		}
	}

	private void switchTradeStrategy(String strategy) {
		for (int i = 0; i < this.additionalAWTComponents.size(); i++) {
			java.awt.Component component = (java.awt.Component) additionalAWTComponents
					.get(i);
			this.getComponentPanel().remove(component);
			this.getUIcomponents().remove(component);
			this.additionalAWTComponents.remove(i);
			i--;
		}
		for (int i = 0; i < this.additionalComponents.size(); i++) {
			UIComponentMetadata com = (UIComponentMetadata) additionalComponents.get(i);
			((AbstractButtonActionListener) this.getBal()).getComponents()
					.remove(com);
		}

		List additionComponent = (new StockSettingUICompomentsImpl())
				.getAdditionalComponent(strategy);
		((AbstractButtonActionListener) this.getBal()).getComponents().addAll(
				additionComponent);

		this.addAdditionAWTComponents(this.getComponentPanel(),
				additionComponent, basicEndPos);
		((AbstractButtonActionListener) this.getBal()).setUICompoments(this
				.getUIcomponents());

		this.getComponentPanel().repaint();
		this.getComponentPanel().validate();
	}
}
