package com.ghlh.stockpool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.ghlh.util.MiscUtil;

public class FileStockPoolAccessor implements StockPoolAccessor {

	public final static String MONITOR_STOCK_LIST_FILE = "MonitorStock.txt";

	File getMonitorStocksFile() throws StockPoolAccessorException {
		String filePath = MONITOR_STOCK_LIST_FILE;
		File result = new File(filePath);
		if (!result.exists()) {
			throw new StockPoolAccessorException(
					"Stock Pool of file doesn't exist");
		}
		return result;
	}

	public List<MonitorStockBean> getMonitorStocks()
			throws StockPoolAccessorException {
		List<MonitorStockBean> result = new ArrayList<MonitorStockBean>();
		File monitorStockFile = getMonitorStocksFile();
		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(monitorStockFile)));
			String line = bufferedReader.readLine();
			while (line != null && !line.trim().equals("")) {
				MonitorStockBean msb = new MonitorStockBean();
				if (MiscUtil.isComment(line)) {
					msb.setStockId(line);
				} else {
					msb = parseMonitorStock(line);
				}
				result.add(msb);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();

		} catch (Exception ex) {
			throw new StockPoolAccessorException(
					"there is errow while reading monitor stocks from file", ex);
		}
		return result;
	}

	MonitorStockBean parseMonitorStock(String line) {
		String additionalInfo = line.substring(line.indexOf("AI") + 2);
		line = line.substring(0, line.indexOf("AI"));
		Pattern pattern = Pattern.compile(",");
		String[] monitorStockInfo = pattern.split(line);
		MonitorStockBean monitorStockBean = new MonitorStockBean();
		monitorStockBean.setStockId(monitorStockInfo[0].trim());
		monitorStockBean.setName(monitorStockInfo[1].trim());
		monitorStockBean.setStandardPrice(Double
				.parseDouble(monitorStockInfo[2].trim()));
		monitorStockBean.setCurrentNumber(Integer.parseInt(monitorStockInfo[3]
				.trim()));
		monitorStockBean.setCanSellNumber(Integer.parseInt(monitorStockInfo[4]
				.trim()));
		monitorStockBean.setTradeAlgorithm(monitorStockInfo[5].trim());
		monitorStockBean.setAdditionInfo(additionalInfo);
		return monitorStockBean;
	}

	public void updateMonitorStock(MonitorStockBean monitorStockBean)
			throws StockPoolAccessorException {

	}

	public void addMonitorStock(MonitorStockBean monitorStockBean)
			throws StockPoolAccessorException {
		List<MonitorStockBean> list = getMonitorStocks();
		list.add(monitorStockBean);
		writeMonitorStocks(list);
	}

	public void writeMonitorStocks(List<MonitorStockBean> monitorStocks)
			throws StockPoolAccessorException {
		File monitorStockFile = getMonitorStocksFile();
		try {
			FileWriter fileWriter = new FileWriter(monitorStockFile);
			fileWriter.write("#Latest update at:" + new Date() + "\r\n");

			for (int i = 0; i < monitorStocks.size(); i++) {
				MonitorStockBean monitorStockBean = (MonitorStockBean) monitorStocks
						.get(i);
				String line = null;
				if (MiscUtil.isComment(monitorStockBean.getStockId())) {
					line = monitorStockBean.getStockId();
				} else {
					line = assessbleStockPoolRecord(monitorStockBean);
				}
				fileWriter.write(line + "\r\n");
			}
			fileWriter.close();
		} catch (Exception ex) {
			throw new StockPoolAccessorException(
					"there is errow while writing monitor stock pool to file",
					ex);
		}
	}

	String assessbleStockPoolRecord(MonitorStockBean msb) {
		String line = " " + msb.getStockId() + ", " + msb.getName() + ", "
				+ msb.getStandardPrice() + ",        " + msb.getCurrentNumber()
				+ ",            " + msb.getCanSellNumber() + ",             "
				+ msb.getTradeAlgorithm() + "   			 AI  "
				+ msb.getAdditionInfo();
		return line;
	}

}
