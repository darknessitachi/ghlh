package com.ghlh.ui.autotradestart;

import java.util.ArrayList;
import java.util.List;

import com.ghlh.ui.autotrade.UIComponentsI;
import com.ghlh.ui.bean.UIComponentMetadata;
import com.ghlh.ui.bean.UIComponentType;
import com.ghlh.ui.bean.ComponentsBean;

public class AutoTradeStartingUICompomentsImpl implements UIComponentsI {
	public ComponentsBean getComponentsBean() {
		List buttons = new ArrayList();
		buttons.add("����");
		buttons.add("ֹͣ");
		ComponentsBean result = new ComponentsBean();
		result.setButtons(buttons);
		List components = new ArrayList();

		UIComponentMetadata component1 = new UIComponentMetadata();
		component1.setLabel("��ǰϵͳ��ع�Ʊ:");
		component1.setCompomentType(UIComponentType.TEXT_FIELD);
		component1.setFieldLength(50);
		components.add(component1);
		UIComponentMetadata component2 = new UIComponentMetadata();
		component2.setLabel("��ǰϵͳ����¼�:");
		component2.setCompomentType(UIComponentType.TEXT_AREA);
		components.add(component2);
		
		result.setComponents(components);
		return result;
	}
}
