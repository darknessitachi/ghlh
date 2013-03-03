package com.ghlh.ui.autotrade;

import org.apache.log4j.Logger;

import com.ghlh.ui.bean.ComponentsBean;

public class UIComponentsFactory {
	private static Logger logger = Logger.getLogger(UIComponentsFactory.class);
	private static UIComponentsFactory instance = new UIComponentsFactory();

	public static UIComponentsFactory getInstance() {
		return instance;
	}

	private UIComponentsFactory() {

	}

	public ComponentsBean getComponentsBean(String menu) {
		UIComponentsI uiComponentsI = null;
		try {
			Class cls = Class.forName("com.ghlh.ui.autotrade."
					+ menu.toLowerCase() + "." + menu + "UICompomentsImpl");
			uiComponentsI = (UIComponentsI) cls.newInstance();
		} catch (Throwable e) {
			logger.error("Loading UICompomentsImpl throw exception:", e);
		}
		ComponentsBean result = uiComponentsI.getComponentsBean();
		return result;
	};
}
