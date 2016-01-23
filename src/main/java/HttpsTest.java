import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpsTest {

	public static void main(String[] args) {
		// String abc =
		// HttpUtil.accessInternet("https://etrade.cs.ecitic.com/ymtrade/login/login.jsp?ssl=false&ftype=pro");
		// System.out.println("abc = " + abc);
		HttpClient httpclient = new HttpClient();
		GetMethod httpget = new GetMethod(
				"https://etrade.cs.ecitic.com/ymtrade/login/login.jsp?ssl=false&ftype=pro");
		try {
			try {
				httpclient.executeMethod(httpget);
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(httpget.getStatusLine());
			InputStream in;
			try {
				in = httpget.getResponseBodyAsStream();
				System.out.println("length = " + httpget.getResponseContentLength());
				
				BufferedReader br = new BufferedReader(new InputStreamReader(
						in, "utf-8"));
				String line = br.readLine();
				System.out.println("line = " + line);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} finally {
			httpget.releaseConnection();
		}

	}
}
