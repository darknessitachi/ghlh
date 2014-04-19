package com.ghlh.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class KLineUtil {
	public KLineUtil() {
	}

	public static boolean saveUrlAs(String stockId, String specificPath) {
		return saveUrlAs(stockId, specificPath, null);
	}

	public static boolean saveUrlAs(String stockId, String specificPath,
			String klineName) {

		try {
			String photoUrl = null;
			if (stockId.startsWith("6")) {
				photoUrl = "http://hqpick.eastmoney.com/k/" + stockId
						+ "1kxld.png";
			} else {
				photoUrl = "http://hqpick.eastmoney.com/k/" + stockId
						+ "2kxld.png";
			}

			// 截取最后/后的字符串
			String fileName = null;
			if (klineName != null) {
				fileName = klineName + ".png";
			} else {
				fileName = photoUrl.substring(photoUrl.lastIndexOf("/") + 1);
			}

			// 图片保存路径
			String filePath = "C:\\Users\\Robin\\Documents\\GitHub\\ghlh\\kline\\"
					+ specificPath;
			File dir = new File(filePath);

			if (!dir.exists()) {
				dir.mkdirs();
			}

			String savePath = filePath + "\\" + fileName;
			File file = new File(savePath);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			/* 将网络资源地址传给,即赋值给url */
			URL url = new URL(photoUrl);

			/* 此为联系获得网络资源的固定格式用法，以便后面的in变量获得url截取网络资源的输入流 */
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			DataInputStream in = new DataInputStream(
					connection.getInputStream());

			/* 此处也可用BufferedInputStream与BufferedOutputStream 需要保存的路径 */
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					savePath));

			/* 将参数savePath，即将截取的图片的存储在本地地址赋值给out输出流所指定的地址 */
			byte[] buffer = new byte[4096];
			int count = 0;
			while ((count = in.read(buffer)) > 0)/* 将输入流以字节的形式读取并写入buffer中 */
			{
				out.write(buffer, 0, count);
			}
			out.close();/* 后面三行为关闭输入输出流以及网络资源的固定格式 */
			in.close();
			connection.disconnect();
			return true;/* 网络资源截取并存储本地成功返回true */

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		saveUrlAs("600036", "abc");
		saveUrlAs("300044", "abc");
	}
}