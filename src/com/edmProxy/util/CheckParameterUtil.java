package com.edmProxy.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//验证电子邮件是否合法
public class CheckParameterUtil {
	public static boolean checkEmail(String email) { 
		String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"; 
		Pattern p = Pattern.compile(regex); 
		Matcher m = p.matcher(email);
		//System.out.println(m.find());
		return m.find(); 
	}
	
	public static boolean checkIP(String IP) { 
		String regex = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b"; 
		Pattern p = Pattern.compile(regex); 
		Matcher m = p.matcher(IP);
		//System.out.println(m.find());
		return m.find(); 
	}
	
	
	public static void main(String[] args) {
		CheckParameterUtil.checkIP("173.234.50.210");
	}
}
