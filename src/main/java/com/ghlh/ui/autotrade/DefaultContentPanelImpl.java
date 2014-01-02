package com.ghlh.ui.autotrade;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.ghlh.ui.ButtonActionListener;
import com.ghlh.ui.IntOnlyDocument;
import com.ghlh.ui.bean.UIComponentMetadata;
import com.ghlh.ui.bean.UIComponentType;
import com.ghlh.ui.bean.ComponentsBean;

public class DefaultContentPanelImpl extends AbstractContentPanel {
	private static Logger logger = Logger
			.getLogger(DefaultContentPanelImpl.class);

	public DefaultContentPanelImpl(ComponentsBean compomentBean) {
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
	}
}
