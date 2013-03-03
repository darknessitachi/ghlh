package com.ghlh.ui;

import java.awt.Color;

import javax.swing.JLabel;

public class StatusField {
	private JLabel field = new JLabel();
	public JLabel getField() {
		return field;
	}

	public void setField(JLabel field) {
		this.field = field;
	}

	private static StatusField instance = new StatusField();

	public static StatusField getInstance() {
		return instance;
	}

	private StatusField() {

	}

	public void setWarningMessage(String message) {
		this.field.setForeground(Color.RED);
		this.field.setText(message);
	}

	public void setPromptMessage(String message) {
		this.field.setForeground(Color.BLUE);
		this.field.setText(message);
	}
	
	

}
