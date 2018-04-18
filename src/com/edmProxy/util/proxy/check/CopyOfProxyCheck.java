package com.edmProxy.util.proxy.check;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import com.edmProxy.entity.*;
import com.edmProxy.util.proxy.*;



public class CopyOfProxyCheck {
	private int count=0;
	private ArrayList<ProxyEntity> list;
	private ExecutorService threadPool;     //线程池
	private BlockingQueue<String> queue;    //阻塞队列
	
	
	//Boolean flag=true;
	
	public synchronized void incCount(){   //可能有多个线程访问它，所以要同步。
		count++;
	}
	public synchronized void decCount(){
		count--;
	}
	public synchronized int getCount(){
		return count;
	}
	
	
	public void start() throws Exception{
		threadPool=Executors.newFixedThreadPool(10);   //线程池创建100个线程  
		queue=new LinkedBlockingDeque<String>(20);   //创建10000个阻塞队列
		
		TestEntiy testEntiy=new TestEntiy();
		
		testEntiy.create();
		
		list=new ArrayList<ProxyEntity>();
		list=testEntiy.list;
		int i;
		
		for (i = 0; i < list.size(); i++) {
			System.out.println(i);
			ProxyCheckUtil proxyCheckUtil=new ProxyCheckUtil();
			//while(flag){//永远等待 ，收到一个连接，就启动一个线程，再继续等待。
				Handler handler=new Handler(list.get(i),proxyCheckUtil);
				//handler.start();
				threadPool.execute(handler);//从线程池里面拿出一个线程，运行run方法
				incCount();
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//}
		}
		
		
	}
	
	
	//线程类-发送
	class Handler implements Runnable{      //用接口 Handler是一个普通的类，不过他实现了run方法
		private ProxyEntity proxyEntiy;
        private ProxyCheckUtil proxyCheckUtil;
		Handler(ProxyEntity proxyEntiy,ProxyCheckUtil proxyCheckUtil){
			this.proxyEntiy=proxyEntiy;
        	this.proxyCheckUtil=proxyCheckUtil;
		}
		public void run(){
			while(true){
				System.out.println(getCount()+":"+list.size()+":"+(getCount()==list.size()));
				if(getCount()==list.size())
					break;
				System.out.println("-->"+getCount());
				System.out.println("任务:-"+proxyEntiy.getProxyHost()+"-验证开始");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	proxyCheckUtil.check(proxyEntiy, "10", "http://iframe.ip138.com/ic.asp");
			}	
		}
	}
	
	
	
	public static void main(String[] args) {
		CopyOfProxyCheck proxyCheck=new CopyOfProxyCheck();
		try {
			proxyCheck.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
