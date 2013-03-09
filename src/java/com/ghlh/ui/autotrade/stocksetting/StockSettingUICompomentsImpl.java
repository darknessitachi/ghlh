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
		comboList.add("一次交易");
		component.setSelectList(comboList);
		component.setDefaultValue("下上楼梯");
		components.add(component);

		component = new UIComponentMetadata();
		component.setLabel("股票代码");
		component.setCompomentType(UIComponentType.INT_FIELD);
		component.setNotAllowNull(true);
		components.add(component);

		component = new UIComponentMetadata();
		component.setLabel("股票名称");
		component.setNotAllowNull(true);
		component.setCompomentType(UIComponentType.TEXT_FIELD);
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
			component.setLabel("基准价格");
			component.setCompomentType(UIComponentType.FLOAT_FIELD);
			component.setNotAllowNull(true);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("能卖数量");
			component.setCompomentType(UIComponentType.INT_FIELD);
			component.setNotAllowNull(true);
			component.setDefaultValue("0");
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("总数量");
			component.setCompomentType(UIComponentType.INT_FIELD);
			component.setNotAllowNull(true);
			component.setDefaultValue("0");
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("台阶涨跌幅");
			component.setCompomentType(UIComponentType.FLOAT_FIELD);
			component.setNotAllowNull(true);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("一次交易数量");
			component.setCompomentType(UIComponentType.INT_FIELD);
			component.setNotAllowNull(true);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("当前台阶");
			component.setCompomentType(UIComponentType.INT_FIELD);
			component.setNotAllowNull(true);
			component.setDefaultValue("0");
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("最大台阶");
			component.setCompomentType(UIComponentType.INT_FIELD);
			component.setNotAllowNull(true);
			result.add(component);
		}

		if (strategy.equals("一次交易")) {
			UIComponentMetadata component = new UIComponentMetadata();
			component.setLabel("交易命令");
			component.setCompomentType(UIComponentType.COMBOX_FIELD);
			List comboList = new ArrayList();
			comboList.add("买入");
			comboList.add("卖出");
			component.setSelectList(comboList);
			component.setDefaultValue("买入");
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("目标价格");
			component.setCompomentType(UIComponentType.FLOAT_FIELD);
			component.setNotAllowNull(true);
			result.add(component);
			

			component = new UIComponentMetadata();
			component.setLabel("交易数量");
			component.setCompomentType(UIComponentType.INT_FIELD);
			component.setNotAllowNull(true);
			result.add(component);

			component = new UIComponentMetadata();
			component.setLabel("当前状态");
			component.setCompomentType(UIComponentType.COMBOX_FIELD);
			comboList = new ArrayList();
			comboList.add("未交易");
			comboList.add("成功");
			component.setDefaultValue("未交易");
			component.setSelectList(comboList);
			result.add(component);
		}
		return result;

	}

	public static String getStrategyName(String strategy) {
		Map map = new HashMap();
		map.put("Stair", "下上楼梯");
		map.put("Once", "一次交易");
		return map.get(strategy).toString();
	}

	public static String getStrategy(String strategyName) {
		Map map = new HashMap();
		map.put("下上楼梯", "Stair");
		map.put("一次交易", "Once");
		return map.get(strategyName).toString();
	}
}
