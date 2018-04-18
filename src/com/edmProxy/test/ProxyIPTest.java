package com.edmProxy.test;
import java.io.BufferedInputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;

import java.io.BufferedInputStream;  
import java.io.IOException;  
import java.net.URL;  
import java.net.URLConnection;  
  
import org.apache.log4j.Logger;  
  
/**  
 * 58.20.53.243

 */  
public class ProxyIPTest  {  
    private static final Logger log = Logger.getLogger(ProxyIPTest.class);  
      
    public static void main(String[] args) throws IOException {
        System.getProperties().setProperty("http.maxRedirects", "50");  
        ////设置HTTP代理
//    	System.getProperties().setProperty("proxySet", "true");//如果不设置，只要代理IP和代理端口正确,此项不设置也可以  
//        System.getProperties().setProperty("http.proxyHost", "23.104.72.2");  
//        System.getProperties().setProperty("http.proxyPort", "808");  
        
        //设置socket5代理
        System.getProperties().setProperty("socksProxySet","true");  
        System.getProperties().setProperty("socksProxyHost","23.109.91.100");  
        System.getProperties().setProperty("socksProxyPort","1080");  
        
        //Authenticator.setDefault(new MyAuthenticator("user001","123456"));
        
        Authenticator.setDefault(new Authenticator(){
        	   protected PasswordAuthentication getPasswordAuthentication() {
        	       return new PasswordAuthentication("user001", new String("123456").toCharArray());
        	 }
        });
        
        
        //无代理连接
        //确定代理是否设置成功  
        log.info(getHtml("http://iframe.ip138.com/ic.asp"));
        System.out.println(getHtml("http://iframe.ip138.com/ic.asp"));
        //HttpURLConnection con = (HttpURLConnection)( new URL( ip ) ).openConnection();
  
    }  
    
    static class MyAuthenticator extends Authenticator{
    	private String username,password;

    	public MyAuthenticator(String username,String password){
    		this.username=username;
    		this.password=password;
    	}
    	protected PasswordAuthentication getPasswordAuthentication(){
    		System.out.println("Requesting Host     :"+getRequestingHost());
    		System.out.println("Requesting Port     :"+getRequestingPort());
    		System.out.println("Requesting Prompt   :"+getRequestingPrompt());
    		System.out.println("Requesting Protocol :"+getRequestingProtocol());
    		System.out.println("Requesting Scheme   :"+getRequestingScheme());
    		System.out.println("Requesting Site     :"+getRequestingSite());

    		return new PasswordAuthentication(username,password.toCharArray());
    	}
    }
//     
    //返回指定网页内容
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