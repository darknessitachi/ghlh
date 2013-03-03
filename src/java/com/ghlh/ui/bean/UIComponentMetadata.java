package com.ghlh.ui.bean;

import java.util.List;

public class UIComponentMetadata {
	private String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getCompomentType() {
		return compomentType;
	}

	public void setCompomentType(int compomentType) {
		this.compomentType = compomentType;
	}

	private int compomentType;

	private List selectList;

	public List getSelectList() {
		return selectList;
	}

	public void setSelectList(List selectList) {
		this.selectList = selectList;
	}

	private String defaultValue;

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}


	private boolean notAllowNull;

	public boolean isNotAllowNull() {
		return notAllowNull;
	}

	public void setNotAllowNull(boolean notAllowNull) {
		this.notAllowNull = notAllowNull;
	}

}
