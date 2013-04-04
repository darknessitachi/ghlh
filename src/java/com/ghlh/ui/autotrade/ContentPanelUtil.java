package com.ghlh.ui.autotrade;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import com.ghlh.ui.DoubleOnlyDocument;
import com.ghlh.ui.IntOnlyDocument;
import com.ghlh.ui.bean.UIComponentMetadata;
import com.ghlh.ui.bean.UIComponentType;

public class ContentPanelUtil {
	public static JComponent getJComponent(UIComponentMetadata com) {
		JComponent result = null;
		if (com.getCompomentType() == UIComponentType.TEXT_FIELD) {
			int length = com.getFieldLength();
			if (length == 0) {
				length = 10;
			}
			result = new JTextField("test", length);
			((JTextField) result).setText(com.getDefaultValue());
		}
		if (com.getCompomentType() == UIComponentType.INT_FIELD) {
			result = new JTextField(10);
			((JTextField) result).setDocument(new IntOnlyDocument());
			if (com.getDefaultValue() != null) {
				((JTextField) result).setText(com.getDefaultValue());
			}
		}
		if (com.getCompomentType() == UIComponentType.DOUBLE_FIELD) {
			result = new JTextField(10);
			((JTextField) result).setDocument(new DoubleOnlyDocument());
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
