package com.ghlh.ui.gtable;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * @version 1.0 08/22/99
 */
public class EditableHeaderTableExample2 extends JFrame {

	public EditableHeaderTableExample2() {
		super("EditableHeader Example");

		JTable table = new JTable(7, 5);
		TableColumnModel columnModel = table.getColumnModel();
		table.setTableHeader(new EditableHeader(columnModel));

		String[] items = { "Dog", "Cat" };
		JComboBox combo = new JComboBox();
		for (int i = 0; i < items.length; i++) {
			combo.addItem(items[i]);
		}

		combo.addItemListener(new ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent ie) {
				System.out.println("item selected"
						+ ((JComboBox) ie.getSource()).getSelectedItem());
			}
		});
		ComboRenderer renderer = new ComboRenderer(items);

		EditableHeaderTableColumn col;
		// column 1
		col = (EditableHeaderTableColumn) table.getColumnModel().getColumn(1);
		col.setHeaderValue(combo.getItemAt(0));
		col.setHeaderRenderer(renderer);
		col.setHeaderEditor(new DefaultCellEditor(combo));

		// column 3
		col = (EditableHeaderTableColumn) table.getColumnModel().getColumn(3);
		col.setHeaderValue(combo.getItemAt(0));
		// col.setHeaderRenderer(renderer);
		col.setHeaderEditor(new DefaultCellEditor(combo));

		JScrollPane pane = new JScrollPane(table);
		getContentPane().add(pane);
	}

	public static void main(String[] args) {
		EditableHeaderTableExample2 frame = new EditableHeaderTableExample2();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setSize(300, 100);
		frame.setVisible(true);
	}
}
