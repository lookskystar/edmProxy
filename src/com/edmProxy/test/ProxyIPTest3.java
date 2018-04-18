package com.edmProxy.test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ProxyIPTest3 {
    private static final Logger log = Logger.getLogger(ProxyIPTest.class);
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		 Properties prop = System.getProperties();
	        // 设置http访问要使用的代理服务器的地址
//	        prop.setProperty("http.proxyHost", "23.109.91.98");
//	        // 设置http访问要使用的代理服务器的端口
//	        prop.setProperty("http.proxyPort", "1080");
	        // 设置不需要通过代理服务器访问的主机，可以使用*通配符，多个地址用|分隔
//	        prop.setProperty("http.nonProxyHosts", "localhost|192.168.0.*");
//	        // 设置安全访问使用的代理服务器地址与端口
//	        // 它没有https.nonProxyHosts属性，它按照http.nonProxyHosts 中设置的规则访问
//	        prop.setProperty("https.proxyHost", "192.168.0.254");
//	        prop.setProperty("https.proxyPort", "443");
//	        // 使用ftp代理服务器的主机、端口以及不需要使用ftp代理服务器的主机
//	        prop.setProperty("ftp.proxyHost", "192.168.0.254");
//	        prop.setProperty("ftp.proxyPort", "2121");
//	        prop.setProperty("ftp.nonProxyHosts", "localhost|192.168.0.*");
//	        // socks代理服务器的地址与端口
	        prop.setProperty("socksProxyHost", "23.109.91.99");
	        prop.setProperty("socksProxyPort", "808");
//	        // 设置登陆到代理服务器的用户名和密码
//	        Authenticator.setDefault(new MyAuthenticator("userName", "Password"));
	        
	        log.info(getHtml("http://iframe.ip138.com/ic.asp"));
	        System.out.println(getHtml("http://iframe.ip138.com/ic.asp"));
	        
	      

	}
	 static class MyAuthenticator extends Authenticator {
	        private String user = "user001";
	        private String password = "123456";
	        public MyAuthenticator(String user, String password) {
	            this.user = user;
	            this.password = password;
	        }
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(user, password.toCharArray());
	        }
	 }
	 
	 
	 
	 private static String getHtml(String address){  
	        StringBuffer html = new StringBuffer();  
	        String result = null;  
	        try{  
	            URL url = new URL(address);  
	            URLConnection conn = url.openConnection();  
	            conn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 7.0; NT 5.1; GTB5; .NET CLR 2.0.50727; CIBA)");  
	            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());  
	              
	            try{  
	                String inputLine;  
	                byte[] buf = new byte[4096];  
	                int bytesRead = 0;  
	                while (bytesRead >= 0) {  
	                    inputLine = new String(buf, 0, bytesRead, "ISO-8859-1");  
	                    html.append(inputLine);  
	                    bytesRead = in.read(buf);  
	                    inputLine = null;  
	                }  
	                buf = null;  
	            }finally{  
	                in.close();  
	                conn = null;  
	                url = null;  
	            }  
	            result = new String(html.toString().trim().getBytes("ISO-8859-1"), "gb2312").toLowerCase();  
	              
	        }catch (Exception e) {  
	            e.printStackTrace();  
	            return null;  
	        }finally{  
	            html = null;              
	        }  
	        return result;  
	    }  
}
