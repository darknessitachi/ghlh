package com.ghlh.util;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JOptionPane;

import com.ghlh.ui.Launcher;

public class GUIUtil {
	public static String getScreenSize() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		String result = width + "_" + height;
		return result;
	}

	public static int showConfirmDialog(String message, String title) {
		int result = JOptionPane.showOptionDialog(Launcher.get_frame(),
				message, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		return result;
	
	}

}
