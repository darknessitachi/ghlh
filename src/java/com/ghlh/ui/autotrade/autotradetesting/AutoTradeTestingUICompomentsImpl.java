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
		buttons.add("买入");
		buttons.add("卖 出");
		ComponentsBean result = new ComponentsBean();
		result.setButtons(buttons);
		List components = new ArrayList();

		UIComponentMetadata component1 = new UIComponentMetadata();
		component1.setLabel("股票代码");
		component1.setCompomentType(UIComponentType.INT_FIELD);
		UIComponentMetadata component2 = new UIComponentMetadata();
		component2.setLabel("数量");
		component2.setCompomentType(UIComponentType.INT_FIELD);

		components.add(component1);
		components.add(component2);

		result.setComponents(components);
		result.setMenuCmd("AutoTradeTesting");
		return result;
	}
}
