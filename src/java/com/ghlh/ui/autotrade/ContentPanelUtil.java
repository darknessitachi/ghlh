package com.ghlh.ui.autotrade;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import com.ghlh.ui.FloatOnlyDocument;
import com.ghlh.ui.IntOnlyDocument;
import com.ghlh.ui.bean.UIComponentMetadata;
import com.ghlh.ui.bean.UIComponentType;

public class ContentPanelUtil {
	public static JComponent getJComponent(UIComponentMetadata com) {
		JComponent result = null;
		if (com.getCompomentType() == UIComponentType.TEXT_FIELD) {
			result = new JTextField("test", 10);
			((JTextField) result).setText(com.getDefaultValue());
		}
		if (com.getCompomentType() == UIComponentType.INT_FIELD) {
			result = new JTextField(10);
			((JTextField) result).setDocument(new IntOnlyDocument());
			if (com.getDefaultValue() != null) {
				((JTextField) result).setText(com.getDefaultValue());
			}
		}
		if (com.getCompomentType() == UIComponentType.FLOAT_FIELD) {
			result = new JTextField(10);
			((JTextField) result).setDocument(new FloatOnlyDocument());
			if (com.getDefaultValue() != null) {
				((JTextField) result).setText(com.getDefaultValue());
			}
		}
		if (com.getCompomentType() == UIComponentType.COMBOX_FIELD) {
			result = new JComboBox(com.getSelectList().toArray());
			if (com.getDefaultValue() != null) {
				((JComboBox) result).setSelectedItem(com.getDefaultValue());
			}
		}
		return result;
	}
}
