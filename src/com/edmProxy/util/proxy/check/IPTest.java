package com.edmProxy.util.proxy.check;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

public class IPTest {
	public static void main ( String[] args ) throws IOException 
	  {    
		URL url = new URL("http://iframe.ip138.com/ic.asp");
		  URLConnection conn = url.openConnection();
		  conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2.15) Gecko/20110303 Firefox/3.6.15");
		  conn.setRequestProperty("Content-Type", "text/html");
		  conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		  InputStream is = conn.getInputStream();
		  BufferedReader br = new BufferedReader(new InputStreamReader(is, "GB2312"));
		  String line = null;
		  while ((line = br.readLine()) != null) {
			  System.out.println(line);
		   if (line.contains("����IP��")) {
		    
		    int start = line.indexOf('[') + 1;
		    int end = line.indexOf(']');
		    System.out.println(line.substring(start, end));
		   }
		  }
		  br.close();
	  }
}
