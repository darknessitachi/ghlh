package com.ghlh.util;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

public class ImageCaptureUtil {
	public static void captureImage(Rectangle rect, String fileName,
			String format) {
		try {
			Robot robot = new Robot();
			BufferedImage image = robot.createScreenCapture(rect);
			File f = new File(fileName);
			if (!f.exists()) {
				f.createNewFile();
			}
			ImageIO.write(image, format, f);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void catputeTradeImage() {
		File f = new File("tradelog");
		if (!f.exists()) {
			f.mkdirs();
		}

		Rectangle rect = new Rectangle(280, 95, 806, 538);
		String format = "jpg";
		Date now = Calendar.getInstance().getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS");
		String imageName = df.format(now);
		String fileName = "tradelog\\" + imageName + "." + format;
		captureImage(rect, fileName, format);
	}

	public static void catputeScreen() {
		File f = new File("tradelog");
		if (!f.exists()) {
			f.mkdirs();
		}

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle rect = new Rectangle(0, 0, (int) d.getWidth(),
				(int) d.getHeight());
		String format = "jpg";
		Date now = Calendar.getInstance().getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS");
		String imageName = df.format(now);
		String fileName = "tradelog\\" + imageName + "." + format;
		captureImage(rect, fileName, format);
	}

	public static void main(String[] args) {
		catputeScreen();
	}
}
