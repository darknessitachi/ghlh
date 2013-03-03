package com.ghlh.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class FloatOnlyDocument extends PlainDocument {
	public void insertString(int offset, String s, AttributeSet attrSet)
			throws BadLocationException {
		try {
			Float.parseFloat(s);
		} catch (NumberFormatException ex) {
			return;
		}
		super.insertString(offset, s, attrSet);
	}
}