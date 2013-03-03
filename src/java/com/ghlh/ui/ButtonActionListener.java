package com.ghlh.ui;

import java.util.List;

import com.ghlh.ui.autotrade.ContentPanel;

public interface ButtonActionListener {
	public void setUICompoments(List compoments);

	public void setCompoments(List compoments);

	public void setStatus(String status);

	public void setContentPanel(ContentPanel contentPanel);

	public void setUIButtons(List uiButtons);

}
