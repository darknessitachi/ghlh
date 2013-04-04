package com.ghlh.ui.autotradestart;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

		for (int i = 0; i < components.size(); i++) {
			GridBagConstraints c = getConstraints((i % 3) * 2, i / 3);
			c.weightx = 0.5;
			c.anchor = GridBagConstraints.NORTHEAST;
			UIComponentMetadata com = (UIComponentMetadata) components.get(i);
			result.add(new JLabel(com.getLabel()), c);
			c = getConstraints((i % 3) * 2 + 1, i / 3);
			JTextField component = (JTextField) ContentPanelUtil
					.getJComponent(com);
			AutoTradeSwitch.getInstance().setMonitorField(component);
			component.setBackground(Color.black);
			component.setForeground(Color.WHITE);
			component.setEnabled(false);
			result.add(component, c);
			this.getUIcomponents().add(component);
		}
		return result;
	}
}
