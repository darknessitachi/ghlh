package com.ghlh.data.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class FileUtil {
	private static Logger logger = Logger.getLogger(FileUtil.class);
	public final static String MONITOR_STOCK_DIRECTORY = "MonitorStocks";

	public static String[] getFilesList() {
		File file = new File(MONITOR_STOCK_DIRECTORY);
		if(!file.exists()){
			file.mkdirs();
		}
		if (file.isDirectory()) {
			String[] subNames = file.list();
			return subNames;
		} else {
			throw new RuntimeException("MonitorStocks isn't directory");
		}
	}

	public static boolean isHidenFile(String fileName) {
		File file = new File(MONITOR_STOCK_DIRECTORY + "/" + fileName);
		return file.isHidden();
	}

	public static void deleteFile(String fileName) {
		File file = new File(MONITOR_STOCK_DIRECTORY + "/" + fileName);
		file.delete();
	}

	public static void updatePropertiesToFile(String fileName, Properties props) {
		File monitorDir = new File(MONITOR_STOCK_DIRECTORY);
		if (!monitorDir.exists()) {
			monitorDir.mkdirs();
		}

		File monitorStockFile = new File(MONITOR_STOCK_DIRECTORY + "/"
				+ fileName);
		try {
			OutputStream out = new FileOutputStream(monitorStockFile);
			props.store(out, "Stock is created at : " + new Date());
			out.close();
		} catch (IOException ex) {
			logger.error("createStock", ex);
			throw new RuntimeException(ex);
		}
	}

	public static Properties loadPropertiesFromFile(String fileName) {
		Properties result = new Properties();
		InputStream is = null;
		try {
			File f = new File(MONITOR_STOCK_DIRECTORY + "/" + fileName);
			is = new FileInputStream(f);
			result.load(is);
			is.close();
		} catch (Exception ex) {
			logger.error("createStock", ex);
			throw new RuntimeException();
		}
		return result;
	}

	public static Properties convertObjectToProperties(Object o) {
		Properties result = new Properties();
		Class c = o.getClass();
		Method[] m = c.getMethods();
		for (int i = 0; i < m.length; i++) {
			String methodName = m[i].getName();
			if (methodName.indexOf("get") == 0
					&& !methodName.equals("getClass")) {
				Object value = null;
				try {
					value = m[i].invoke(o, null);
				} catch (Exception e) {
					logger.error("GetMethod invoke : ", e);
					throw new RuntimeException(e);
				}
				String columnName = methodName.replaceFirst("get", "");
				if (value != null) {
					result.put(columnName, value.toString());
				}
			}
		}
		return result;
	}

	public static Object convertPropertiesToObject(Properties props,
			String className) {
		Object result = null;
		try {
			Class<?> c = Class.forName(className);
			Constructor<?> cons = c.getConstructor(null);
			result = cons.newInstance(null);
			Method[] m = c.getMethods();
			for (int i = 0; i < m.length; i++) {
				String methodName = m[i].getName();
				if (methodName.indexOf("set") == 0) {
					Object value = props.get(methodName.substring(3));
					try {
						if (value != null) {
							if (m[i].getParameterTypes()[0]
									.equals(String.class)) {
								m[i].invoke(result, new Object[] { value });
							} else if (m[i].getParameterTypes()[0]
									.equals(Integer.class)) {
								m[i].invoke(result, new Object[] { Integer
										.valueOf(value.toString()) });
							} else if (m[i].getParameterTypes()[0]
									.equals(Double.class)) {
								m[i].invoke(result, new Object[] { Double
										.valueOf(value.toString()) });
							}

						}
					} catch (Exception e) {
						logger.error("SetMethod invoke : ", e);
						throw new RuntimeException(e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Invoke exception : ", e);
			throw new RuntimeException(e);
		}
		return result;
	}

}
