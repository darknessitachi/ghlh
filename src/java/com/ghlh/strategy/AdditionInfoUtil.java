package com.ghlh.strategy;

import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.ghlh.data.db.MonitorstockVO;
import com.ghlh.ui.autotrade.stocksetting.StockSettingUICompomentsImpl;
import com.ghlh.ui.bean.UIComponentMetadata;
import com.ghlh.util.ClassUtil;
import com.ghlh.util.ReflectUtil;
import com.ghlh.util.StringUtil;

public class AdditionInfoUtil {
	public static Object parseAdditionalInfoBean(String additionalInfo,
			String strategy) {
		if (additionalInfo == null) {
			return null;
		}
		List uiComponentMeta = getUIComponentMeta(strategy);
		Object additionalObject = ReflectUtil.getClassInstance(
				"com.ghlh.strategy." + strategy.toLowerCase(),
				"AdditionalInfoBean");

		Pattern pattern = Pattern.compile(",");
		String[] additionalInfoSegs = pattern.split(additionalInfo);

		for (int i = 0; i < uiComponentMeta.size(); i++) {
			UIComponentMetadata com = (UIComponentMetadata) uiComponentMeta
					.get(i);
			String method = "set"
					+ StringUtil.makeFirstLetterToUpper(com.getFieldName());
			ReflectUtil.excuteClassMethodNoReturn(additionalObject, method,
					new Class[] { ClassUtil.getClass(com.getFieldType()) },
					new Object[] { ClassUtil.getObject(com.getFieldType(),
							additionalInfoSegs[i].trim()) });
		}
		return additionalObject;
	}

	private static List getUIComponentMeta(String strategy) {
		String strategyName = StockSettingUICompomentsImpl
				.getStrategyName(strategy);
		List uiComponentMeta = new StockSettingUICompomentsImpl()
				.getAdditionalComponent(strategyName);
		return uiComponentMeta;
	}

	public static void setAdditionalInfoToUIComponents(List uiComponents,
			String additionInfo, String strategy) {
		Object additionalObject = parseAdditionalInfoBean(additionInfo,
				strategy);
		List uiComponentMeta = getUIComponentMeta(strategy);
		for (int i = 0; i < uiComponentMeta.size(); i++) {
			UIComponentMetadata com = (UIComponentMetadata) uiComponentMeta
					.get(i);
			String method = "get"
					+ StringUtil.makeFirstLetterToUpper(com.getFieldName());

			Object fieldValue = ReflectUtil.excuteClassMethod(additionalObject,
					method, null, null);
			Object uiField = uiComponents.get(i + 3);

			if (uiField != null) {
				if (uiField instanceof JTextField) {
					((JTextField) uiField).setText(fieldValue.toString());
				}
				if (uiField instanceof JComboBox) {
					((JComboBox) uiField)
							.setSelectedItem(fieldValue.toString());
				}
			}

		}
	}

	public static boolean hasChangedValueInAdditionalUIComponents(
			List uiComponents, MonitorstockVO currentMsb) {
		String additionalInfo = currentMsb.getAdditioninfo();
		Pattern pattern = Pattern.compile(",");
		String[] additionalInfoSegs = pattern.split(additionalInfo);
		for (int i = 3; i < uiComponents.size(); i++) {
			String text = null;
			if (uiComponents.get(i) instanceof JTextField) {
				text = ((JTextField) uiComponents.get(i)).getText();
			}
			if (uiComponents.get(i) instanceof JComboBox) {
				text = ((JComboBox) uiComponents.get(i)).getSelectedItem()
						.toString();
			}

			if (!additionalInfoSegs[i - 3].trim().equals(text)) {
				return true;
			}
		}
		return false;
	}

	public static String collectAdditionalInfoFromUIComponents(
			List uiComponents, String strategy) {
		Object additionalObject = ReflectUtil.getClassInstance(
				"com.ghlh.strategy." + strategy.toLowerCase(),
				"AdditionalInfoBean");

		List uiComponentMeta = getUIComponentMeta(strategy);
		for (int i = 0; i < uiComponentMeta.size(); i++) {
			UIComponentMetadata com = (UIComponentMetadata) uiComponentMeta
					.get(i);
			String method = "set"
					+ StringUtil.makeFirstLetterToUpper(com.getFieldName());
			Object uiField = uiComponents.get(i + 3);
			String fieldValue = null;
			if (uiField instanceof JTextField) {
				fieldValue = ((JTextField) uiField).getText();
			}
			if (uiField instanceof JComboBox) {
				fieldValue = (String) ((JComboBox) uiField).getSelectedItem();
			}
			fieldValue = ClassUtil.getDefaultValue(com.getFieldType(),
					fieldValue);

			ReflectUtil.excuteClassMethodNoReturn(additionalObject, method,
					new Class[] { ClassUtil.getClass(com.getFieldType()) },
					new Object[] { ClassUtil.getObject(com.getFieldType(),
							fieldValue.trim()) });
		}
		return parseAdditionalInfoBeanBack(additionalObject, strategy);
	}

	public static String parseAdditionalInfoBeanBack(Object additionalInfoBean,
			String strategy) {
		List uiComponentMeta = getUIComponentMeta(strategy);
		String line = "";
		for (int i = 0; i < uiComponentMeta.size(); i++) {
			UIComponentMetadata com = (UIComponentMetadata) uiComponentMeta
					.get(i);
			String method = "get"
					+ StringUtil.makeFirstLetterToUpper(com.getFieldName());
			if (i == uiComponentMeta.size() - 1) {
				line += ReflectUtil.excuteClassMethod(additionalInfoBean,
						method, null, null);
			} else {
				line += ReflectUtil.excuteClassMethod(additionalInfoBean,
						method, null, null) + ",";
			}
		}
		return line;
	}

}
