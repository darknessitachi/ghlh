package com.ghlh.ui.gtable;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ComboRenderer extends JComboBox implements TableCellRenderer {

	public ComboRenderer(String[] items) {
		for (int i = 0; i < items.length; i++) {
			addItem(items[i]);
		}
	}

	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		setSelectedItem(value);
		return this;
	}
}
