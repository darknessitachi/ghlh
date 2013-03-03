package com.ghlh.ui.autotrade.stocksetting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ghlh.ui.autotrade.UIComponentsI;
import com.ghlh.ui.bean.UIComponentMetadata;
import com.ghlh.ui.bean.UIComponentType;
import com.ghlh.ui.bean.ComponentsBean;

public class StockSettingUICompomentsImpl implements UIComponentsI {
	public ComponentsBean getComponentsBean() {
		List buttons = new ArrayList();
		buttons.add("�½�");
		buttons.add("�޸�");
		buttons.add("����");
		buttons.add("ȡ��");
		buttons.add("ɾ��");
		ComponentsBean result = new ComponentsBean();
		result.setButtons(buttons);
		List components = new ArrayList();

		UIComponentMetadata component = new UIComponentMetadata();
		component.setLabel("�Զ����ײ���");
		component.setCompomentType(UIComponentType.COMBOX_FIELD);
		List comboList = new ArrayList();
		comboList.add("����¥��");
		comboList.add("����¥��");
		comboList.add("������");
		component.setSelectList(comboList);
		components.add(component);

		component = new UIComponentMetadata();
		component.setLabel("��Ʊ����");
		component.setCompomentType(UIComponentType.INT_FIELD);
		component.setNotAllowNull(true);
		component.setDefaultValue("600036");
		components.add(component);

		component = new UIComponentMetadata();
		component.setLabel("��Ʊ����");
		component.setNotAllowNull(true);
		component.setCompomentType(UIComponentType.TEXT_FIELD);
		components.add(component);

		component = new UIComponentMetadata();
		component.setLabel("��׼�۸�");
		component.setCompomentType(UIComponentType.FLOAT_FIELD);
		component.setDefaultValue("0");
		components.add(component);

		component = new UIComponentMetadata();
		component.setLabel("��������");
		component.setCompomentType(UIComponentType.INT_FIELD);
		components.add(component);

		component = new UIComponentMetadata();
		component.setLabel("������");
		component.setCompomentType(UIComponentType.INT_FIELD);
		components.add(component);

		result.setComponents(components);
		result.setAdditionalCompoments(this.getAdditionalComponent("����¥��"));
		result.setMenuCmd("StockSetting");
		return result;
	}

	public List getAdditionalComponent(String strategy) {
		List result = new ArrayList();
		if (strategy.equals("����¥��")) {
			UIComponentMetadata component = new UIComponentMetadata();
			component.setLabel("̨���ǵ���");
			component.setCompomentType(UIComponentType.FLOAT_FIELD);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("һ�ν�������");
			component.setCompomentType(UIComponentType.INT_FIELD);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("��ǰ̨��");
			component.setCompomentType(UIComponentType.INT_FIELD);
			result.add(component);
		
			component = new UIComponentMetadata();
			component.setLabel("���̨��");
			component.setCompomentType(UIComponentType.INT_FIELD);
			result.add(component);
		}
		// String[] additionalLabels2 = { "����", "������", "��ʲô" };
		// String[] additionalLabels3 = { "�й�", "����", "���ô�", "̩��" };

		if (strategy.equals("����¥��")) {
			UIComponentMetadata component = new UIComponentMetadata();
			component.setLabel("����");
			component.setCompomentType(UIComponentType.FLOAT_FIELD);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("������");
			component.setCompomentType(UIComponentType.INT_FIELD);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("��ʲô");
			component.setCompomentType(UIComponentType.INT_FIELD);
			result.add(component);
		}

		if (strategy.equals("������")) {
			UIComponentMetadata component = new UIComponentMetadata();
			component.setLabel("�й�");
			component.setCompomentType(UIComponentType.FLOAT_FIELD);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("����");
			component.setCompomentType(UIComponentType.INT_FIELD);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("���ô�");
			component.setCompomentType(UIComponentType.INT_FIELD);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("̩��");
			component.setCompomentType(UIComponentType.INT_FIELD);

			result.add(component);
		}

		return result;

	}

	public static String getStrategyName(String strategy) {
		Map map = new HashMap();
		map.put("Stair", "����¥��");
		return map.get(strategy).toString();
	}
	
	public static String getStrategy(String strategyName) {
		Map map = new HashMap();
		map.put("����¥��","Stair");
		return map.get(strategyName).toString();
	}
}
