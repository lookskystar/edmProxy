package com.edmProxy.util.proxy.check;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.test.ProxyIPTest;

public class ProxyCheckUtil {
	private static final Logger log = Logger.getLogger(ProxyCheckUtil.class);
	public static String getHtml;
	
	public static void main(String[] args) {
		ProxyEntity proxyEntiy=new ProxyEntity();
		proxyEntiy.setProxyHost("107.149.216.10");
		proxyEntiy.setProxyPort("1080");
		
		proxyEntiy.setProxyAccount("User-001");
		proxyEntiy.setProxyPassword("123456");
		proxyEntiy.setProxyType(1);
		
		ProxyCheckUtil.check(proxyEntiy, 1+"", "http://baidu.com");
		
	}

	public static String check(ProxyEntity proxyEntiy, String hcheckTimeeckTime,String getProxyHost) {
		if(proxyEntiy.getProxyType()==0){
			System.out.println("验证http："+proxyEntiy.getProxyHost());
			System.getProperties().setProperty("http.maxRedirects", hcheckTimeeckTime);
			//设置HTTP代理
			System.getProperties().setProperty("proxySet", "true");// 如果不设置，只要代理IP和代理端口正确,此项不设置也可以
			System.getProperties().setProperty("http.proxyHost",proxyEntiy.getProxyHost());
			System.getProperties().setProperty("http.proxyPort",proxyEntiy.getProxyPort());
		}else if(proxyEntiy.getProxyType()==1){
			System.out.println("验证scoket："+proxyEntiy.getProxyHost());
		// 设置socket5代理
			System.getProperties().setProperty("checkTimemaxRedirects", hcheckTimeeckTime);
			System.getProperties().setProperty("socksProxySet", "true");
			System.getProperties().setProperty("socksProxyHost",proxyEntiy.getProxyHost());
			System.getProperties().setProperty("socksProxyPort",proxyEntiy.getProxyPort());
		}
		Authenticator.setDefault(new MyAuthenticator(proxyEntiy.getProxyAccount(), proxyEntiy.getProxyPassword()));
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(
					null, "联网验证错误！",
					"系统提示", JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}
		getHtml=GetHtmlUtil.getHtml(getProxyHost);
		System.out.println(getHtml);
		return getHtml;
	}

	// http代理 认证（如果有）
	static class MyAuthenticator extends Authenticator {
		private String username, password;

		public MyAuthenticator(String username, String password) {
			this.username = username;
			this.password = password;
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			System.out.println("Requesting Host     :" + getRequestingHost());
			System.out.println("Requesting Port     :" + getRequestingPort());
			System.out.println("Requesting Prompt   :" + getRequestingPrompt());
			System.out.println("Requesting Protocol :"
					+ getRequestingProtocol());
			System.out.println("Requesting Scheme   :" + getRequestingScheme());
			System.out.println("Requesting Site     :" + getRequestingSite());
			System.out.println("Requesting Type    :" + getRequestorType());

			return new PasswordAuthentication(username, password.toCharArray());
		}
	}


}
