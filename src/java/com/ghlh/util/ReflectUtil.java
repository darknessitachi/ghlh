package com.ghlh.util;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.ghlh.ui.autotrade.AbstractContentPanel;

public class ReflectUtil {
	private static Logger logger = Logger.getLogger(AbstractContentPanel.class);

	public static Object getClassInstance(String packageName,
			String moduleName, String classBasicName) {
		String fullClassName = packageName + "." + moduleName.toLowerCase()
				+ "." + moduleName + classBasicName;
		try {
			Class cls = Class.forName(fullClassName);
			Object result = cls.newInstance();
			return result;
		} catch (Exception ex) {
			logger.error("getClassInstance through error", ex);
			throw new RuntimeException(
					"getClassInstance through error, className = "
							+ fullClassName);
		}
	}

	public static void excuteClassMethodNoReturn(Object object,
			String methodName, Class[] paraTypes, Object[] parameters) {
		try {
			Method method = object.getClass().getMethod(methodName, paraTypes);
			method.invoke(object, parameters);

		} catch (Exception ex) {
			logger.error("excuteClassMethod throw exception:", ex);
			throw new RuntimeException(
					"excuteClassMethod throw exception, method = " + methodName);
		}
	}

	public static Object excuteClassMethod(Object object, String methodName,
			Class[] paraTypes, Object[] parameters) {
		try {
			Method method = object.getClass().getMethod(methodName, paraTypes);
			Object result = method.invoke(object, parameters);
			return result;
		} catch (Exception ex) {
			logger.error("excuteClassMethod throw exception:", ex);
			throw new RuntimeException(
					"excuteClassMethod throw exception, method = " + methodName);
		}
	}

}
