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

			// ��ȡ���/����ַ���
			String fileName = null;
			if (klineName != null) {
				fileName = klineName + ".png";
			} else {
				fileName = photoUrl.substring(photoUrl.lastIndexOf("/") + 1);
			}

			// ͼƬ����·��
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

			/* ��������Դ��ַ����,����ֵ��url */
			URL url = new URL(photoUrl);

			/* ��Ϊ��ϵ���������Դ�Ĺ̶���ʽ�÷����Ա�����in�������url��ȡ������Դ�������� */
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			DataInputStream in = new DataInputStream(
					connection.getInputStream());

			/* �˴�Ҳ����BufferedInputStream��BufferedOutputStream ��Ҫ�����·�� */
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					savePath));

			/* ������savePath��������ȡ��ͼƬ�Ĵ洢�ڱ��ص�ַ��ֵ��out�������ָ���ĵ�ַ */
			byte[] buffer = new byte[4096];
			int count = 0;
			while ((count = in.read(buffer)) > 0)/* �����������ֽڵ���ʽ��ȡ��д��buffer�� */
			{
				out.write(buffer, 0, count);
			}
			out.close();/* ��������Ϊ�ر�����������Լ�������Դ�Ĺ̶���ʽ */
			in.close();
			connection.disconnect();
			return true;/* ������Դ��ȡ���洢���سɹ�����true */

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