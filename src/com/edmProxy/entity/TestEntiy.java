package com.edmProxy.entity;

import java.util.ArrayList;

import com.edmProxy.entity.ProxyEntity;

//≤‚ ‘¿‡
public class TestEntiy {
	public ProxyEntity proxyEntiy1=new ProxyEntity();
	public ProxyEntity proxyEntiy2=new ProxyEntity();
	public ProxyEntity proxyEntiy3=new ProxyEntity();
	public ProxyEntity proxyEntiy4=new ProxyEntity();
	public ProxyEntity proxyEntiy5=new ProxyEntity();
	public ProxyEntity proxyEntiy6=new ProxyEntity();
	
	public ArrayList<ProxyEntity> list=new ArrayList<ProxyEntity>();
	
	public void create(){
		proxyEntiy1.setProxyType(0);
		proxyEntiy1.setProxyHost("23.104.72.2");
		proxyEntiy1.setProxyPort("808");
		proxyEntiy1.setProxyAccount("user001");
		proxyEntiy1.setProxyPassword("123456");
		
		proxyEntiy2.setProxyType(0);
		proxyEntiy2.setProxyHost("23.104.72.3");
		proxyEntiy2.setProxyPort("808");
		proxyEntiy2.setProxyAccount("user001");
		proxyEntiy2.setProxyPassword("123456");
		
		proxyEntiy3.setProxyType(0);
		proxyEntiy3.setProxyHost("23.104.72.4");
		proxyEntiy3.setProxyPort("808");
		proxyEntiy3.setProxyAccount("user001");
		proxyEntiy3.setProxyPassword("123456");
		
		proxyEntiy4.setProxyType(1);
		proxyEntiy4.setProxyHost("173.234.50.210");
		proxyEntiy4.setProxyPort("1080");
		proxyEntiy4.setProxyAccount("user001");
		proxyEntiy4.setProxyPassword("123456");
		
		proxyEntiy5.setProxyType(1);
		proxyEntiy5.setProxyHost("173.234.50.211");
		proxyEntiy5.setProxyPort("1080");
		proxyEntiy5.setProxyAccount("user001");
		proxyEntiy5.setProxyPassword("123456");
		
		proxyEntiy6.setProxyType(1);
		proxyEntiy6.setProxyHost("173.234.50.212");
		proxyEntiy6.setProxyPort("1080");
		proxyEntiy6.setProxyAccount("user001");
		proxyEntiy6.setProxyPassword("123456");
		
		
//		list.add(proxyEntiy1);
//		list.add(proxyEntiy2);
//		list.add(proxyEntiy3);
		list.add(proxyEntiy4);
		list.add(proxyEntiy5);
		list.add(proxyEntiy6);
	}
}
