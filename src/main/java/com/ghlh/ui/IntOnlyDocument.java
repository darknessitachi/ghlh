package com.ghlh.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.ghlh.util.MathUtil;

public class IntOnlyDocument extends PlainDocument {
	public void insertString(int offset, String s, AttributeSet attrSet)
			throws BadLocationException {
		if (MathUtil.isInt(s)) {
			super.insertString(offset, s, attrSet);
		}
	}
}