package com.ghlh.ui.autotrade;

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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.ghlh.ui.ButtonActionListener;
import com.ghlh.ui.bean.UIComponentMetadata;
import com.ghlh.ui.bean.ComponentsBean;
import com.ghlh.util.ReflectUtil;

public abstract class AbstractContentPanel implements ContentPanel {
	private static Logger logger = Logger.getLogger(AbstractContentPanel.class);
	private ButtonActionListener bal = null;

	protected ButtonActionListener getBal() {
		return bal;
	}

	private List jButtons = new ArrayList();
	private List uiComponents = new ArrayList();

	protected JPanel content = new JPanel();

	public List getUIcomponents() {
		return uiComponents;
	}

	public List getJButtons() {
		return jButtons;
	}

	public void setJButtons(List jButtons) {
		this.jButtons = jButtons;
	}

	protected void initButtonActionListener(ComponentsBean compomentBean,
			JPanel componentPanel) {
		try {
			bal = getBal(compomentBean);
			bal.setUICompoments(uiComponents);
			List components = new ArrayList();
			if (compomentBean.getComponents() != null) {
				components.addAll(compomentBean.getComponents());
			}
			if (compomentBean.getAdditionalCompoments() != null) {
				components.addAll(compomentBean.getAdditionalCompoments());
			}
			bal.setCompoments(components);
			bal.setContentPanel(this);
			bal.setUIButtons(this.jButtons);
		} catch (Throwable e) {
			logger.error("Loading ButtonActionListener throw exception:", e);
		}
	}

	protected ButtonActionListener getBal(ComponentsBean compomentBean) {
		return (ButtonActionListener) ReflectUtil.getClassInstance(
				"com.ghlh.ui.autotrade", compomentBean.getMenuCmd(),
				"ButtonActionListener");
	}

	protected JPanel getButtonPanel(List buttons) {
		FlowLayout buttonPanelLayout = new FlowLayout();
		JPanel result = new JPanel();
		result.setLayout(buttonPanelLayout);
		buttonPanelLayout.setAlignment(FlowLayout.LEFT);
		for (int i = 0; i < buttons.size(); i++) {
			JButton button = new JButton(buttons.get(i).toString());
			button.setName("button" + (i + 1));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					buttonActionPerformed(e);
				}
			});
			result.add(button);
			this.jButtons.add(button);
		}
		result.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		return result;
	}

	protected void buttonActionPerformed(ActionEvent e) {
		try {
			Class[] paraTypes = new Class[] { ActionEvent.class };
			String buttonName = ((JButton) e.getSource()).getName();
			ActionEvent[] events = new ActionEvent[] { e };

			ReflectUtil.excuteClassMethodNoReturn(bal, buttonName
					+ "ActionPerformed", paraTypes, events);
		} catch (Exception ex) {
			logger.error("perform Button Action throw exception:", ex);
		}
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
			JComponent component = ContentPanelUtil.getJComponent(com);
			result.add(component, c);
			uiComponents.add(component);
		}
		return result;
	}

	protected GridBagConstraints getConstraints(int gridx, int gridy) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 5, 10); // top padding
		c.gridx = gridx; // aligned with button 2
		c.gridy = gridy; // third row
		return c;
	}

	public JPanel getContent() {
		return content;
	}

}
