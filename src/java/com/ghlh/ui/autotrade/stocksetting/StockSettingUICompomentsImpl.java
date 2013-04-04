package com.ghlh.ui.autotrade.stocksetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.ghlh.strategy.StrategyAddiComMetaReader;
import com.ghlh.ui.autotrade.UIComponentsI;
import com.ghlh.ui.bean.ComponentsBean;
import com.ghlh.ui.bean.UIComponentMetadata;
import com.ghlh.ui.bean.UIComponentType;

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
		Map map = StrategyAddiComMetaReader.getInstance()
				.getStrategyNameAsKey();
		Set strategyNames = map.keySet();
		List comboList = new ArrayList();
		comboList.addAll(strategyNames);
		component.setSelectList(comboList);
		component.setDefaultValue(StrategyAddiComMetaReader.getInstance()
				.getDefaultStrategyName());
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
		//component.setFieldLength(15);
		components.add(component);

		result.setComponents(components);
		result.setAdditionalCompoments(this
				.getAdditionalComponent(StrategyAddiComMetaReader.getInstance()
						.getDefaultStrategyName()));
		result.setMenuCmd("StockSetting");
		return result;
	}

	private int getFieldType(String fieldType) {
		int result = 0;
		if (fieldType.equals("text")) {
			result = UIComponentType.TEXT_FIELD;
		}
		if (fieldType.indexOf("combo") == 0) {
			result = UIComponentType.COMBOX_FIELD;
		}
		if (fieldType.equals("int")) {
			result = UIComponentType.INT_FIELD;
		}
		if (fieldType.equals("double")) {
			result = UIComponentType.DOUBLE_FIELD;
		}
		return result;
	}

	private UIComponentMetadata parseUIComponentMetadata(String line) {
		UIComponentMetadata result = new UIComponentMetadata();
		Pattern pattern = Pattern.compile(",");
		String[] metadataItems = pattern.split(line);
		result.setLabel(metadataItems[0].trim());
		result.setCompomentType(getFieldType(metadataItems[1].trim()));
		generateComboList(metadataItems[1].trim(), result);
		result.setNotAllowNull(Boolean.parseBoolean(metadataItems[2].trim()));
		if (metadataItems[3] != null && !metadataItems[3].trim().equals("")) {
			result.setDefaultValue(metadataItems[3].trim());
		}
		result.setFieldName(metadataItems[4].trim());
		result.setFieldType(metadataItems[5].trim());

		return result;
	}

	private void generateComboList(String line, UIComponentMetadata result) {
		if (result.getCompomentType() == UIComponentType.COMBOX_FIELD) {
			String itemString = line.trim().substring(
					line.trim().indexOf("(") + 1, line.trim().length() - 1);
			Pattern pattern = Pattern.compile("#");
			String[] items = pattern.split(itemString);
			List comboList = new ArrayList();
			for (int i = 0; i < items.length; i++) {
				comboList.add(items[i]);
			}
			result.setSelectList(comboList);
		}
	}

	public List getAdditionalComponent(String strategyName) {
		List result = new ArrayList();
		String strategy = getStrategy(strategyName);
		List additionalComs = (List) StrategyAddiComMetaReader.getInstance()
				.getStrategyCom().get(strategy);
		for (int i = 0; i < additionalComs.size(); i++) {
			String line = (String) additionalComs.get(i);
			UIComponentMetadata com = parseUIComponentMetadata(line);
			result.add(com);
		}
		return result;
	}

	public static String getStrategyName(String strategy) {
		Map map = StrategyAddiComMetaReader.getInstance().getStrategies();
		return map.get(strategy).toString();
	}

	public static String getStrategy(String strategyName) {
		Map map = StrategyAddiComMetaReader.getInstance()
				.getStrategyNameAsKey();
		return map.get(strategyName).toString();
	}
}
