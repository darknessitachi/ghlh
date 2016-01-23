package com.ghlh.tradeway.software.java;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ghlh.conf.ConfigurationAccessor;
import com.ghlh.util.ImageCaptureUtil;
import com.ghlh.util.TimeUtil;

public class TradeSoftwareController extends
		com.ghlh.tradeway.software.TradeSoftwareController {

	private static Logger logger = Logger
			.getLogger(TradeSoftwareController.class);

	private static TradeSoftwareController instance = new TradeSoftwareController();

	public static TradeSoftwareController getInstance() {
		return instance;
	}

	Robot robot = null;

	public TradeSoftwareController() {
		try {
			robot = new Robot();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void activateTradeSoft() {
		String cmdName = "ActivateTradeSoftwareWindow";
		executeTradeSoftwareCMD(cmdName, new HashMap<String, Object>());
	}

	public void executeTradeCMD(String cmdName,
			Map<String, Object> cmdParameters) {
		executeTradeSoftwareCMD(cmdName, cmdParameters);
	}

	private void executeActiveWindow() {
		List<String> scripts = (List<String>) ControllScriptReader
				.getInstance().getActiveWindowsScript();
		for (int i = 0; i < scripts.size(); i++) {
			String cmd = scripts.get(i);
			if (cmd.indexOf("MouseClick") == 0) {
				if (ConfigurationAccessor.getInstance().getPosition() != null) {
					String pos = ConfigurationAccessor.getInstance()
							.getPosition();
					String xS = pos.substring(0, pos.indexOf("-"));
					String yS = pos.substring(pos.indexOf("-") + 1);
					this.mouseClick(Integer.parseInt(xS), Integer.parseInt(yS));
				} else {
					String pos = cmd.substring(cmd.indexOf(" ") + 1);
					String xS = pos.substring(0, pos.indexOf(" "));
					String yS = pos.substring(pos.indexOf(" ") + 1);
					this.mouseClick(Integer.parseInt(xS), Integer.parseInt(yS));
				}
			}
			if (cmd.indexOf("Sleep") == 0) {
				String timeS = cmd.substring(cmd.indexOf(" ") + 1);
				TimeUtil.pause(Integer.parseInt(timeS));
			}
			if (ConfigurationAccessor.getInstance().isTradeLog()) {
				if (cmd.indexOf("CaptureImage") == 0) {
					ImageCaptureUtil.catputeTradeImage();
				}
				if (cmd.indexOf("CaptureScreen") == 0) {
					ImageCaptureUtil.catputeScreen();
				}
			}

		}
	}

	private synchronized void executeTradeSoftwareCMD(String cmdName,
			Map<String, Object> cmdParameters) {
		this.executeActiveWindow();
		List<String> scripts = (List<String>) ControllScriptReader
				.getInstance().getCMDScripts(cmdName);
		for (int i = 0; i < scripts.size(); i++) {
			String cmd = (String) scripts.get(i);
			if (cmd.indexOf("price") > 0 && cmdParameters.get("price") == null) {
				continue;
			}
			if (cmd.indexOf("%") > 0) {
				String parameterName = cmd.substring(cmd.indexOf("%") + 1);
				parameterName = parameterName.substring(0,
						parameterName.indexOf("%"));
				String parameter = cmdParameters.get(parameterName).toString();
				cmd = cmd.replace("%" + parameterName + "%", parameter);
			}
			executeCall(cmd);
		}
	}

	private void executeCall(String cmd) {
		if (cmd.indexOf("MouseClick") == 0) {
			String pos = cmd.substring(cmd.indexOf(" ") + 1);
			String xS = pos.substring(0, pos.indexOf(" "));
			String yS = pos.substring(pos.indexOf(" ") + 1);
			this.mouseClick(Integer.parseInt(xS), Integer.parseInt(yS));
		}
		if (cmd.indexOf("Sleep") == 0) {
			String timeS = cmd.substring(cmd.indexOf(" ") + 1);
			TimeUtil.pause(Integer.parseInt(timeS));
		}
		if (cmd.indexOf("SendInput") == 0) {
			String data = cmd.substring(cmd.indexOf(" ") + 1);
			if ("enter".equals(data)) {
				this.inputEnter();
			} else if ("tab".equals(data)) {
				this.inputTab();
			} else {
				this.inputString(data);
			}
		}
		if (ConfigurationAccessor.getInstance().isTradeLog()) {
			if (cmd.indexOf("CaptureImage") == 0) {
				ImageCaptureUtil.catputeTradeImage();
			}
			if (cmd.indexOf("CaptureScreen") == 0) {
				ImageCaptureUtil.catputeScreen();
			}
		}
	}

	private void inputString(String stockId) {
		char[] chars = stockId.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '.') {
				robot.keyPress(KeyEvent.VK_PERIOD);
				robot.keyRelease(KeyEvent.VK_PERIOD);
			} else {
				inputKey((int) chars[i]);
			}
		}
	}

	private void inputEnter() {
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}

	private void inputTab() {
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
	}

	private void inputKey(int key) {
		robot.keyPress(key + 48);
		robot.keyRelease(key + 48);
	}

	private void mouseClick(int x, int y) {
		robot.mouseMove(x, y);
		robot.mousePress(MouseEvent.BUTTON1_MASK);
		robot.mouseRelease(MouseEvent.BUTTON1_MASK);
	}

	public static void main(String[] args) {
		new TradeSoftwareController().activateTradeSoft();
	}

}
