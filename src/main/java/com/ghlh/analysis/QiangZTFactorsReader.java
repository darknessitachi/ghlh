package com.ghlh.analysis;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.ghlh.tradeway.software.ControllScriptReader;
import com.ghlh.util.DateUtil;

public class QiangZTFactorsReader {
	private Logger logger = Logger.getLogger(QiangZTFactorsReader.class);

	private static QiangZTFactorsReader instance = new QiangZTFactorsReader();

	public static QiangZTFactorsReader getInstance() {
		return instance;
	}

	private Map factors = new HashMap();

	public Map getFactors() {
		return factors;
	}

	private QiangZTFactorsReader() {
		InputStream is = QiangZTFactorsReader.class
				.getResourceAsStream("qiangztfactors.properties");
		String line = null;
		if (is != null) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "UTF-8"));
				while ((line = br.readLine()) != null
						&& !line.trim().equals("")) {
					String name = line.substring(0, line.indexOf("=")).trim();
					String value = line.substring(line.indexOf("=") + 1).trim();
					String type = name.substring(name.indexOf("_") + 1).trim();
					int iType = Integer.parseInt(type);
					name = name.substring(0, name.indexOf("_"));
					factors.put(name, convertDateList(value, iType));
				}
				br.close();
				is.close();
			} catch (Exception e) {
				logger.error("Read stragegies file: strategies.properties,"
						+ " throw : ", e);
			}

		}
	}

	private List convertDateList(String value, int type) {
		Pattern pattern = Pattern.compile(",");
		String[] dataPieces = pattern.split(value);
		List result = new ArrayList();
		for (int i = 0; i < dataPieces.length; i++) {
			switch (type) {
			case 1:
				result.add(DateUtil.parseDay(dataPieces[i]));
				break;
			case 2:
				result.add(Integer.valueOf(dataPieces[i]));
				break;
			case 3:
				result.add(Double.valueOf(dataPieces[i]));
				break;
			case 4:
				result.add(Boolean.valueOf(dataPieces[i]));
				break;
			default:
				result.add(Double.valueOf(dataPieces[i]));
			}
		}
		return result;
	}

	public static void main(String[] args) {
		Map map = QiangZTFactorsReader.getInstance().getFactors();
		System.out.println("Map = " + map);
	}

}
