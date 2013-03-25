package com.ghlh.ui.autotrade.softwaresetting;
        

import java.util.ArrayList;
import java.util.List;

import com.ghlh.ui.autotrade.UIComponentsI;
import com.ghlh.ui.bean.ComponentsBean;
import com.ghlh.ui.bean.UIComponentMetadata;
import com.ghlh.ui.bean.UIComponentType;

public class SoftwareSettingUICompomentsImpl implements UIComponentsI {
	public ComponentsBean getComponentsBean() {
		
		List buttons = new ArrayList();
		buttons.add("����");
		ComponentsBean result = new ComponentsBean();
		result.setButtons(buttons);
		List components = new ArrayList();

		UIComponentMetadata component = new UIComponentMetadata();
		component.setLabel("֤ȯ�������");
		component.setCompomentType(UIComponentType.COMBOX_FIELD);
		List comboList = new ArrayList();
		comboList.add("����֤ȯ");
		comboList.add("����֤ȯ");
		component.setSelectList(comboList);
		components.add(component);

		result.setComponents(components);
		result.setMenuCmd("SoftwareSetting");
		return result;
	}
}
