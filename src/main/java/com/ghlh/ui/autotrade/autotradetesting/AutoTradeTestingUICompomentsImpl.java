package com.ghlh.ui.autotrade.autotradetesting;

import java.util.ArrayList;
import java.util.List;

import com.ghlh.ui.autotrade.UIComponentsI;
import com.ghlh.ui.bean.UIComponentMetadata;
import com.ghlh.ui.bean.UIComponentType;
import com.ghlh.ui.bean.ComponentsBean;

public class AutoTradeTestingUICompomentsImpl implements UIComponentsI {
	public ComponentsBean getComponentsBean() {
		List buttons = new ArrayList();
		buttons.add("����");
		buttons.add("�� ��");
		ComponentsBean result = new ComponentsBean();
		result.setButtons(buttons);
		List components = new ArrayList();

		UIComponentMetadata component1 = new UIComponentMetadata();
		component1.setLabel("��Ʊ����");
		component1.setCompomentType(UIComponentType.INT_FIELD);
		UIComponentMetadata component2 = new UIComponentMetadata();
		component2.setLabel("��Ʊ����");
		component2.setCompomentType(UIComponentType.TEXT_FIELD);

		UIComponentMetadata component3 = new UIComponentMetadata();
		component3.setLabel("����");
		component3.setCompomentType(UIComponentType.INT_FIELD);

		components.add(component1);
		components.add(component2);
		components.add(component3);

		result.setComponents(components);
		result.setMenuCmd("AutoTradeTesting");
		return result;
	}
}
