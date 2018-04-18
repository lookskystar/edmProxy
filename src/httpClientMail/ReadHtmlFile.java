package httpClientMail;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class ReadHtmlFile {
	public static HttpClient client = new HttpClient();

	// 读取普通文件
	public static String readFile(String filename) {
		File file = new File(filename);
		Reader reader = null;
		StringBuffer stb = new StringBuffer();
		int charread = 0;
		char[] temps = new char[100];
		try {
			reader = new InputStreamReader(new FileInputStream(file));
			while ((charread = reader.read(temps)) != -1) {
				if (charread == temps.length) {
					stb.append(temps);
					temps = new char[100];
					charread = 0;
				}
			}
			stb.append(temps);
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String str = stb.toString();
		return str;
	}

	/**
	 * 
	 * 所要发送的网页中,不要使用外联样式，脚本等， 因为各大邮箱后台的过滤算法会过滤掉css,script等 样式用标签style属性写
	 * 图片、链接必须使用绝对地址 （http://.....） 形式才能接收显示
	 */
	public static String getSource(String url) {

		GetMethod method = new GetMethod(url);

		try {
			client.executeMethod(method);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream in = null;
		try {
			in = method.getResponseBodyAsStream();
		} catch (IOException e1) {

			// e1.printStackTrace();
		}
		in = new BufferedInputStream(in);
		Reader r = new InputStreamReader(in);
		int c;
		StringBuffer buffer = new StringBuffer();

		try {
			while ((c = r.read()) != -1)
				buffer.append((char) c);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		method.releaseConnection();

		return buffer.toString();
	}
}
