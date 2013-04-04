package com.ghlh.ui.autotrade;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import com.ghlh.stockquotes.InternetStockQuotesInquirer;
import com.ghlh.stockquotes.StockQuotesBean;
import com.ghlh.ui.StatusField;

public class StockIdFieldFocusListener implements FocusListener {
	private JTextField stockNameField;

	public StockIdFieldFocusListener(JTextField stockNameField) {
		this.stockNameField = stockNameField;
	}

	public void focusGained(FocusEvent fe) {
	}

	public void focusLost(FocusEvent fe) {
		processStockIdFocusLost(fe);
	}

	private void processStockIdFocusLost(FocusEvent fe) {
		JTextField stockIdField = (JTextField) fe.getSource();
		String stockId = stockIdField.getText();
		StockQuotesBean sqb = InternetStockQuotesInquirer.queryStock(stockId);
		if (sqb == null) {
			StatusField.getInstance().setWarningMessage("股票代码不合理， 请重输");
		} else {
			stockNameField.setText(sqb.getName());
		}
	}
}
