package com.edmProxy.util;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

public class GetHtmlUtil {
	//返回指定网页内容
    public static String getHtml(String address){  
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
