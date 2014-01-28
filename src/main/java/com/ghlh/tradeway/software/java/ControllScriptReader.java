package com.ghlh.tradeway.software.java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ghlh.conf.ConfigurationAccessor;

public class ControllScriptReader {
	private Logger logger = Logger.getLogger(ControllScriptReader.class);

	private Map<String, List<String>> scriptMap = new HashMap<String, List<String>>();

	private static ControllScriptReader instance = new ControllScriptReader();

	public static ControllScriptReader getInstance() {
		return instance;
	}

	private String getScriptPath() {
		String tradeVendor = ConfigurationAccessor.getInstance()
				.getTradeVendor();
		String result = tradeVendor + ".properties";
		return result;
	}

	private ControllScriptReader() {
		String scriptPath = this.getScriptPath();
		InputStream is = ControllScriptReader.class
				.getResourceAsStream(getScriptPath());
		String line = null;
		if (is != null) {
			try {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				List<String> scriptList = null;
				while ((line = br.readLine()) != null
						&& !line.trim().equals("")) {
					if (line.indexOf("###") == 0) {
						scriptList = new ArrayList<String>();
						String cmdName = line.substring(3).trim();
						scriptMap.put(cmdName, scriptList);
					} else {
						scriptList.add(line);
					}
				}
				br.close();
				is.close();
			} catch (Exception e) {
				logger.error("Read software script: " + scriptPath
						+ " throw : ", e);
			}

		} else {
			logger.error("The software script " + scriptPath
					+ " could not be found");
		}
	}

	public List<String> getCMDScripts(String cmdName) {
		return (List<String>) scriptMap.get(cmdName);
	}
}
