package com.edmProxy.util.mail.send;

import java.util.ArrayList;

public class TestSendMailEntiy {
	
	SendEmailEntiy sendEmailEntiy1=new SendEmailEntiy();
	SendEmailEntiy sendEmailEntiy2=new SendEmailEntiy();
	SendEmailEntiy sendEmailEntiy3=new SendEmailEntiy();
	SendEmailEntiy sendEmailEntiy4=new SendEmailEntiy();
	SendEmailEntiy sendEmailEntiy5=new SendEmailEntiy();
	
	ArrayList<SendEmailEntiy> sendEmailEntiyList=new ArrayList();
	
	public void create(){
		sendEmailEntiy1.setHost("smtp.sina.com");
		sendEmailEntiy1.setPort("25");
		sendEmailEntiy1.setSendAccount("dtnilans@sina.com");
		sendEmailEntiy1.setPassword("sayler8390");
		
//		sendEmailEntiy2.setHost("smtp.126.com");
//		sendEmailEntiy2.setPort("25");
//		sendEmailEntiy2.setSendAccount("lookskystaremail@126.com");
//		sendEmailEntiy2.setPassword("8524127");
//		
//		sendEmailEntiy3.setHost("smtp.sina.com");
//		sendEmailEntiy3.setPort("25");
//		sendEmailEntiy3.setSendAccount("dtnilans@sina.com");
//		sendEmailEntiy3.setPassword("sayler8390");
//		
//		sendEmailEntiy4.setHost("smtp.sina.com");
//		sendEmailEntiy4.setPort("25");
//		sendEmailEntiy4.setSendAccount("zikowys13@sina.com");
//		sendEmailEntiy4.setPassword("kukows3842");
//		
//		sendEmailEntiy5.setHost("smtp.sina.com");
//		sendEmailEntiy5.setPort("25");
//		sendEmailEntiy5.setSendAccount("gladena18@sina.com");
//		sendEmailEntiy5.setPassword("mustia9661");
		for(int i=0;i<500;i++){
			sendEmailEntiyList.add(sendEmailEntiy1);
		}
		
//		sendEmailEntiyList.add(sendEmailEntiy2);
//		sendEmailEntiyList.add(sendEmailEntiy3);
//		sendEmailEntiyList.add(sendEmailEntiy4);
//		sendEmailEntiyList.add(sendEmailEntiy5);
		
	}
}
