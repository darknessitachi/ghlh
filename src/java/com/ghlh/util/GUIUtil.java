package com.ghlh.util;

import java.awt.Dimension;
import java.awt.Toolkit;

public class GUIUtil {
	public static String getScreenSize() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		String result = width + "_" + height;
		return result;
	}

}
