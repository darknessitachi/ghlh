package com.ghlh.ui;

import java.util.List;

import javax.swing.JPanel;

import com.ghlh.ui.autotrade.ContentPanel;

public abstract class AbstractButtonActionListener implements
		ButtonActionListener {
	private List uicomponents;
	
	private List components;
	
	public List getComponents() {
		return components;
	}

	public void setComponents(List components) {
		this.components = components;
	}

	public List getUIComponents() {
		return uicomponents;
	}

	public void setUICompoments(List uicomponents) {
		this.uicomponents = uicomponents;
	}

	public String getStatus() {
		return status;
	}

	private String status;
	
	private List jButtons;

	public List getjButtons() {
		return jButtons;
	}

	public void setUIButtons(List jButtons) {
		this.jButtons = jButtons;
		this.initButtonStatus();
	}

	protected void initButtonStatus(){
		
	}
	
	public void setCompoments(List components) {
		this.components = components;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	private ContentPanel contentPanel;
	public ContentPanel getContentPanel() {
		return contentPanel;
	}

	public void setContentPanel(ContentPanel contentPanel){
		this.contentPanel = contentPanel;
	}

}
