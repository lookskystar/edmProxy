package com.edmProxy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import com.edmProxy.dao.DBHelp;
/*
 * 读取配置文件信息
 */
public class ReadDBPrpperties {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ReadDBPrpperties d = new ReadDBPrpperties();
		System.out.println(d.DRIVER);

	}

	private static String DRIVER = ""; // 数据库驱动
	private static String URL = ""; // 数据库地址
	private static String USER = ""; // 数据库用户名
	private static String PASSWORD = ""; // 数据库密码
	public static String getDRIVER() {
		return DRIVER;
	}
	public static void setDRIVER(String driver) {
		DRIVER = driver;
	}
	public static String getURL() {
		return URL;
	}
	public static void setURL(String url) {
		URL = url;
	}
	public static String getUSER() {
		return USER;
	}
	public static void setUSER(String user) {
		USER = user;
	}
	public static String getPASSWORD() {
		return PASSWORD;
	}
	public static void setPASSWORD(String password) {
		PASSWORD = password;
	}




	public ReadDBPrpperties() {
		// 项目路径
		String pathString = DBHelp.class.getClassLoader().getResource("")
				.toString();
	    //pathString=pathString.substring(6,pathString.length());//打包发布读取配置文件信息
		pathString = pathString.substring(6, pathString.length() - 4);// 测试读取配置文件信息
		pathString = pathString + "resource/db.properties";
		Properties props = new Properties();

		// 从类路径下加载属性文件
		File file = new File(pathString);
		InputStreamReader in;
		try {
			in = new InputStreamReader(new FileInputStream(file), "gbk");
			props.load(in);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DRIVER = props.getProperty("driver");
		URL = props.getProperty("url");
		USER = props.getProperty("user");
		PASSWORD = props.getProperty("password");
	}

}
