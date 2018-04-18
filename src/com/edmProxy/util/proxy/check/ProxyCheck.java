package com.edmProxy.util.proxy.check;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.util.WriteFile;
import com.edmProxy.util.proxy.check.ProxyCheckUtil;
import com.edmProxy.entity.TestEntiy;

public class ProxyCheck {	
	//代理实体，本机外网地址，验证的时间，返回本机外网地址的网址
	public String[] start(ProxyEntity proxyEntiy,String outNetAddress,String checkTime,String getProxyHost,String newFile[]) throws IOException{
		String[] arry =new String[2];
		ProxyCheckUtil proxyCheckUtil = new ProxyCheckUtil();
		String getHtml = proxyCheckUtil.check(proxyEntiy, checkTime,getProxyHost);
		boolean flag=ProxyCheck.checkHtml(getHtml,outNetAddress);
		String msg="";
		String proxy=proxyEntiy.getProxyType()+"-"+proxyEntiy.getProxyHost()+"-"+proxyEntiy.getProxyPort()+"-"+proxyEntiy.getProxyAccount()+"-"+proxyEntiy.getProxyPassword();
		if(newFile!=null){//批量检测写入文件
			if(flag){
				
				msg=proxyEntiy.getProxyHost()+"--代理测试有效！";
				//有效记录
				if(!"".equals(newFile[0])){//批量检测，写入文件
					
					WriteFile.getDataWriteFile(newFile[0],proxy);
				}
			}else{
				//WriteFile.getDataWriteFile(newFile[1],proxy);
				//msg=proxyEntiy.getProxyHost()+"--代理测试无效！";
				//无效记录
			}
		}
//		else{//单个检测显示
//			if(flag){
//				msg=proxyEntiy.getProxyHost()+"--代理测试有效！";
//			}else{
//				msg=proxyEntiy.getProxyHost()+"--代理测试无效！";
//			}
//		}
		arry[0]=flag+"";
		arry[1]=getHtml+"\n\n"+msg+"\n----------------------------";
		removeLocalProxy();
		return arry;
	}
	
	public static boolean checkHtml(String getHtml,String localhost){
		//localhost="58.20.53.243";
		if(getHtml==null){
			return false;
		}else if(getHtml.indexOf(localhost)>-1){
			return false; //无效代理IP
		}
		return true;
	}
	
	
	// 清除proxy设置   
	public void removeLocalProxy()   
	{   
		Properties prop = System.getProperties();   
		prop.remove("http.proxyHost");   
		prop.remove("http.proxyPort");   

		prop.remove("socksProxyHost");   
		prop.remove("socksProxyPort");   
	}   

	public static void main(String[] args) {
		TestEntiy testEntiy = new TestEntiy();
		testEntiy.create();

		ArrayList<ProxyEntity>  list = new ArrayList<ProxyEntity>();
		list = testEntiy.list;
		ProxyCheck proxyCheck = new ProxyCheck();
		for (int i = 0; i < list.size(); i++) {
			//proxyCheck.start(list.get(i),"58.20.53.243","10","http://iframe.ip138.com/ic.asp");
		}
	}
}
