package com.ghlh.strategy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ghlh.tradeway.software.ControllScriptReader;

public class StrategyAddiComMetaReader {
	private Logger logger = Logger.getLogger(StrategyAddiComMetaReader.class);

	private static StrategyAddiComMetaReader instance = new StrategyAddiComMetaReader();

	public static StrategyAddiComMetaReader getInstance() {
		return instance;
	}

	private Map strategies = new HashMap();

	private Map strategyNameAsKey = new HashMap();

	public Map getStrategyNameAsKey() {
		return strategyNameAsKey;
	}

	public Map getStrategies() {
		return strategies;
	}

	public Map getStrategyCom() {
		return strategyCom;
	}

	private Map strategyCom = new HashMap();

	private String defaultStrategyName = null;

	public String getDefaultStrategyName() {
		return defaultStrategyName;
	}

	private StrategyAddiComMetaReader() {
		InputStream is = StrategyAddiComMetaReader.class
				.getResourceAsStream("strategies.properties");
		String line = null;
		if (is != null) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "UTF-8"));
				List<String> comList = null;
				String strategy = null;
				String strategyName = null;
				while ((line = br.readLine()) != null
						&& !line.trim().equals("")) {
					if (line.indexOf("strategy=") == 0) {
						comList = new ArrayList<String>();
						strategy = line.substring(line.indexOf("=") + 1).trim();
						strategyCom.put(strategy, comList);
					} else if (line.indexOf("strategyName=") == 0) {
						strategyName = line
								.substring(line.indexOf("=") + 1);
						strategies.put(strategy, strategyName);
						strategyNameAsKey.put(strategyName, strategy);
					} else if (line.indexOf("defaultStrategy=true") == 0) {
						defaultStrategyName = strategyName;
					} else {
						comList.add(line.substring(line.indexOf("=") + 1));
					}
				}
				br.close();
				is.close();
			} catch (Exception e) {
				logger.error("Read stragegies file: strategies.properties,"
						+ " throw : ", e);
			}

		}
	}
}
