package com.edmProxy.util.thread;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.mail.MessagingException;

public class MyStar {
	public static void main(String[] args) {
		//SendMail sendMail=new SendMail();
		TestSendMailEntiy tsme=new TestSendMailEntiy();
		tsme.create();
		ArrayList<SendEmailEntiy> sendMailEntiyList=new ArrayList();
		sendMailEntiyList=tsme.sendEmailEntiyList;
		
		
		ArrayList<String> receiveMailInfoList=new ArrayList();
		for (int i = 0; i < 1; i++) {
			receiveMailInfoList.add("lookskystaremail@126.com");
		}
		
//		SendMail sendMail=new SendMail();
//		SendEmailEntiy sendEmailEntiy=new SendEmailEntiy();
//		Runnable r=new MyThread(sendMail,sendMailEntiyList.get(1),receiveMailInfoList);
//		Thread t=new Thread(r);
//		t.start();
		
		SendMail sendMail=new SendMail();
		for (int i = 0; i < 2; i++) {
			Runnable r=new MyThread(i,sendMail,sendMailEntiyList.get(i),receiveMailInfoList);
			Thread t=new Thread(r);
			t.start();
		}
		
		
//		Send send1=new Send("账号2"+"开始发送....");
//		Runnable r1=new MyThread(send1);
//		Thread t1=new Thread(r1);
//		
//		t.start();
//		t1.start();
	}	
}
//线程类
class MyThread implements Runnable{
	private int threadCount;
	private SendMail sendMail;
	private SendEmailEntiy sendEmailEntiy;
	private ArrayList<String> receiveMailInfoList;
	public MyThread(int threadCount,SendMail sendMail,SendEmailEntiy sendEmailEntiy,ArrayList<String> receiveMailInfoList){
		this.threadCount=threadCount;
		this.sendMail=sendMail;
		this.sendEmailEntiy=sendEmailEntiy;
		this.receiveMailInfoList=receiveMailInfoList;
	}
	public void run() {
		
		//调用具体的类方法
		try {
			sendMail.SendMailTrue(sendEmailEntiy, receiveMailInfoList);
		} catch (UnsupportedEncodingException e1) {
			System.out.println("异常1");
			e1.printStackTrace();
		} catch (MessagingException e1) {
			System.out.println("MessagingException异常，链接超时，说明链接最大数是到这里"+threadCount);
			e1.printStackTrace();
		}
		
		try {
			Thread.sleep((long)Math.random()*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

//具体的类方法
// class  Send{
//	private String name;
//	public Send(String name){
//		this.name=name;
//	}
//	//为这个对象枷锁
//	public synchronized void starSend(){
//		System.out.println("同时执行->"+name);
//	}
//}

