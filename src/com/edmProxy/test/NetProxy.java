package com.edmProxy.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Properties;

public class NetProxy {
	// 测试本地JVM的网络缺省配置
	public void setLocalProxy() {
		Properties prop = System.getProperties();
		// 设置http访问要使用的代理服务器的地址
//		prop.setProperty("http.proxyHost", "183.234.16.37");
//		// 设置http访问要使用的代理服务器的端口
//		prop.setProperty("http.proxyPort", "80");
		// 设置不需要通过代理服务器访问的主机，可以使用*通配符，多个地址用|分隔
		//prop.setProperty("http.nonProxyHosts", "localhost|10.10.*");

		// 设置安全访问使用的代理服务器地址与端口
		// 它没有https.nonProxyHosts属性，它按照http.nonProxyHosts 中设置的规则访问
//		prop.setProperty("https.proxyHost", "183.234.16.37");
//		prop.setProperty("https.proxyPort", "443");
		
		
//
//		// 使用ftp代理服务器的主机、端口以及不需要使用ftp代理服务器的主机
//		prop.setProperty("ftp.proxyHost", "183.234.16.37");
//		prop.setProperty("ftp.proxyPort", "2121");
//		prop.setProperty("ftp.nonProxyHosts", "localhost|10.10.*");
//
//		// socks代理服务器的地址与端口
		prop.setProperty("socksProxyHost", "61.19.42.244");
		prop.setProperty("socksProxyPort", "80");
	}

	// 清除proxy设置
	public void removeLocalProxy() {
		Properties prop = System.getProperties();
		prop.remove("http.proxyHost");
		prop.remove("http.proxyPort");
		prop.remove("http.nonProxyHosts");

		prop.remove("https.proxyHost");
		prop.remove("https.proxyPort");

		prop.remove("ftp.proxyHost");
		prop.remove("ftp.proxyPort");
		prop.remove("ftp.nonProxyHosts");

		prop.remove("socksProxyHost");
		prop.remove("socksProxyPort");
	}

	//   

	// 测试http
	public void showHttpProxy(Object... proxy) {
		URL url = null;
		try {
			url = new URL("http://blog.csdn.com/smallnest");
		} catch (MalformedURLException e) {
			return;
		}
		try {
			URLConnection conn = null;
			switch (proxy.length) {
			case 0:
				conn = url.openConnection();
				break;
			case 1:
				conn = url.openConnection((Proxy) proxy[0]);
				break;
			default:
				break;
			}

			if (conn == null)
				return;

			conn.setConnectTimeout(3000); // 设置连接超时时间
			InputStream in = conn.getInputStream();
			byte[] b = new byte[1024];
			try {
				while (in.read(b) > 0) {
					System.out.println(new String(b));
				}
			} catch (IOException e1) {
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	// 测试ftp
	public void showFtpProxy(Object... proxy) {
		URL url = null;
		try {
			url = new URL("ftp://ftp.tsinghua.edu.cn");
		} catch (MalformedURLException e) {
			return;
		}
		try {
			URLConnection conn = null;
			switch (proxy.length) {
			case 0:
				conn = url.openConnection();
				break;
			case 1:
				conn = url.openConnection((Proxy) proxy[0]);
				break;
			default:
				break;
			}

			if (conn == null)
				return;

			conn.setConnectTimeout(3000); // 设置连接超时时间
			InputStream in = conn.getInputStream();
			byte[] b = new byte[1024];
			try {
				while (in.read(b) > 0) {
					System.out.println(new String(b));
				}
			} catch (IOException e1) {
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	// 得到一个proxy
	public Proxy getProxy(Proxy.Type type, String host, int port) {
		SocketAddress addr = new InetSocketAddress(host, port);
		Proxy typeProxy = new Proxy(type, addr);
		return typeProxy;
	}

	public static void main(String[] args) {
		NetProxy proxy = new NetProxy();
		// 测试代理服务器
		proxy.setLocalProxy();
		proxy.showHttpProxy();

		// 下面两行是清除系统属性，而通过Proxy类指定代理服务器
		// proxy.removeLocalProxy
		// proxy.showHttpProxy(proxy.getProxy(Proxy.Type.SOCKS,"10.10.0.96",1080));

	}
}