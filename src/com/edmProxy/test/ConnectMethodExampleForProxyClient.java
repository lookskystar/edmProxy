package com.edmProxy.test;
import org.apache.commons.httpclient.ProxyClient;
import org.apache.commons.httpclient.ConnectMethod;
import org.apache.commons.httpclient.ProxyClient.ConnectResponse;

import java.net.Socket;

public class ConnectMethodExampleForProxyClient {

  public static void main(String args[]) {

		ProxyClient client = new ProxyClient();
		client.getParams().setParameter("http.useragent","Proxy Test Client");

		client.getHostConfiguration().setHost("www.baidu.com");
		client.getHostConfiguration().setProxy("23.109.91.98",1080);

		Socket socket = null;

		try{
			ConnectResponse response = client.connect();
			socket = response.getSocket();
			if(socket == null) {
				ConnectMethod method = response.getConnectMethod();
				System.err.println("Socket not created: " + method.getStatusLine());
			}
			// do something
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if(socket != null) 
			    try { 
			        socket.close(); 
			    } catch (Exception fe) {}
		}

  }
}