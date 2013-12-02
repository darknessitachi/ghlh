package com.ghlh.ui.autotradestart;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.ghlh.ui.ButtonActionListener;
import com.ghlh.ui.autotrade.ContentPanelUtil;
import com.ghlh.ui.autotrade.DefaultContentPanelImpl;
import com.ghlh.ui.bean.ComponentsBean;
import com.ghlh.ui.bean.UIComponentMetadata;
import com.ghlh.util.ReflectUtil;

public class AutoTradeStartContentPanelImpl extends DefaultContentPanelImpl {
	private static Logger logger = Logger
			.getLogger(AutoTradeStartContentPanelImpl.class);

	public AutoTradeStartContentPanelImpl(ComponentsBean compomentBean) {
		super(compomentBean);
	}

	protected ButtonActionListener getBal(ComponentsBean compomentBean) {
		return (ButtonActionListener) ReflectUtil.getClassInstance(
				"com.ghlh.ui", "AutoTradeStart", "ButtonActionListener");
	}

	public JPanel getComponentsPanel(List components) {
		JPanel result = new JPanel();
		result.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		result.setLayout(new GridBagLayout());

		UIComponentMetadata com = (UIComponentMetadata) components.get(0);
		GridBagConstraints c = getConstraints(0, 0);
		c.weightx = 0.5;
		c.anchor = GridBagConstraints.NORTHEAST;
		result.add(new JLabel(com.getLabel()), c);

		c = getConstraints(1, 0);
		JComponent component = (JComponent) ContentPanelUtil.getJComponent(com);
		component.setBackground(Color.black);
		component.setForeground(Color.WHITE);
		component.setEnabled(false);
		AutoTradeMonitor.getInstance().setMonitorStockField(
				(JTextField) component);

		result.add(component, c);
		this.getUIcomponents().add(component);

		com = (UIComponentMetadata) components.get(1);
		c = getConstraints(0, 1);
		c.weightx = 0.5;
		c.anchor = GridBagConstraints.NORTHEAST;
		result.add(new JLabel(com.getLabel()), c);

		c = getConstraints(1, 1);
		component = (JComponent) ContentPanelUtil.getJComponent(com);
		component.setBackground(Color.black);
		component.setForeground(Color.WHITE);
		component.setEnabled(false);
		AutoTradeMonitor.getInstance().setMonitorArea((JTextArea) component);
		JScrollPane jsp = new JScrollPane(component);
		result.add(jsp, c);
		this.getUIcomponents().add(component);

		return result;
	}
}
