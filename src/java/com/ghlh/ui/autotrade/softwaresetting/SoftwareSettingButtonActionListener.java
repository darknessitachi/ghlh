package com.ghlh.ui.autotrade.softwaresetting;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JComboBox;

import com.ghlh.conf.ConfigurationAccessor;
import com.ghlh.ui.AbstractButtonActionListener;
import com.ghlh.ui.ButtonActionListener;
import com.ghlh.ui.StatusField;

public class SoftwareSettingButtonActionListener extends
		AbstractButtonActionListener {

	public SoftwareSettingButtonActionListener() {
		this.setStatus("交易软件设置");
	}

	public void button1ActionPerformed(ActionEvent e) {
		JComboBox softwareList = (JComboBox) this.getUIComponents().get(0);
		String vendorSoftware = softwareList.getSelectedItem().toString();
		ConfigurationAccessor.getInstance().saveTradeVendor(vendorSoftware);
		StatusField.getInstance().setPromptMessage("保存软件成功");
	}

	public void setUICompoments(List components) {
		super.setCompoments(components);
		JComboBox softwareList = (JComboBox) components.get(0);
		softwareList.setSelectedItem(ConfigurationAccessor.getInstance()
				.getTradeVendorName());
	}
}
