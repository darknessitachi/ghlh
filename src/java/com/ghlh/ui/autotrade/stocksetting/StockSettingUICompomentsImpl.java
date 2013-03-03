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
		buttons.add("新建");
		buttons.add("修改");
		buttons.add("保存");
		buttons.add("取消");
		buttons.add("删除");
		ComponentsBean result = new ComponentsBean();
		result.setButtons(buttons);
		List components = new ArrayList();

		UIComponentMetadata component = new UIComponentMetadata();
		component.setLabel("自动交易策略");
		component.setCompomentType(UIComponentType.COMBOX_FIELD);
		List comboList = new ArrayList();
		comboList.add("下上楼梯");
		comboList.add("上下楼梯");
		comboList.add("爬格子");
		component.setSelectList(comboList);
		components.add(component);

		component = new UIComponentMetadata();
		component.setLabel("股票代码");
		component.setCompomentType(UIComponentType.INT_FIELD);
		component.setNotAllowNull(true);
		component.setDefaultValue("600036");
		components.add(component);

		component = new UIComponentMetadata();
		component.setLabel("股票名称");
		component.setNotAllowNull(true);
		component.setCompomentType(UIComponentType.TEXT_FIELD);
		components.add(component);

		component = new UIComponentMetadata();
		component.setLabel("基准价格");
		component.setCompomentType(UIComponentType.FLOAT_FIELD);
		component.setDefaultValue("0");
		components.add(component);

		component = new UIComponentMetadata();
		component.setLabel("能卖数量");
		component.setCompomentType(UIComponentType.INT_FIELD);
		components.add(component);

		component = new UIComponentMetadata();
		component.setLabel("总数量");
		component.setCompomentType(UIComponentType.INT_FIELD);
		components.add(component);

		result.setComponents(components);
		result.setAdditionalCompoments(this.getAdditionalComponent("下上楼梯"));
		result.setMenuCmd("StockSetting");
		return result;
	}

	public List getAdditionalComponent(String strategy) {
		List result = new ArrayList();
		if (strategy.equals("下上楼梯")) {
			UIComponentMetadata component = new UIComponentMetadata();
			component.setLabel("台阶涨跌幅");
			component.setCompomentType(UIComponentType.FLOAT_FIELD);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("一次交易数量");
			component.setCompomentType(UIComponentType.INT_FIELD);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("当前台阶");
			component.setCompomentType(UIComponentType.INT_FIELD);
			result.add(component);
		
			component = new UIComponentMetadata();
			component.setLabel("最大台阶");
			component.setCompomentType(UIComponentType.INT_FIELD);
			result.add(component);
		}
		// String[] additionalLabels2 = { "世界", "美丽的", "干什么" };
		// String[] additionalLabels3 = { "中国", "美国", "加拿大", "泰国" };

		if (strategy.equals("上下楼梯")) {
			UIComponentMetadata component = new UIComponentMetadata();
			component.setLabel("世界");
			component.setCompomentType(UIComponentType.FLOAT_FIELD);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("美丽的");
			component.setCompomentType(UIComponentType.INT_FIELD);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("干什么");
			component.setCompomentType(UIComponentType.INT_FIELD);
			result.add(component);
		}

		if (strategy.equals("爬格子")) {
			UIComponentMetadata component = new UIComponentMetadata();
			component.setLabel("中国");
			component.setCompomentType(UIComponentType.FLOAT_FIELD);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("美国");
			component.setCompomentType(UIComponentType.INT_FIELD);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("加拿大");
			component.setCompomentType(UIComponentType.INT_FIELD);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("泰国");
			component.setCompomentType(UIComponentType.INT_FIELD);

			result.add(component);
		}

		return result;

	}

	public static String getStrategyName(String strategy) {
		Map map = new HashMap();
		map.put("Stair", "下上楼梯");
		return map.get(strategy).toString();
	}
	
	public static String getStrategy(String strategyName) {
		Map map = new HashMap();
		map.put("下上楼梯","Stair");
		return map.get(strategyName).toString();
	}
}
