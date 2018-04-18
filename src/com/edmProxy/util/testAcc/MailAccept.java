package com.edmProxy.util.testAcc;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

public class MailAccept {
	 public static void main(String[] args) throws Exception{
	        Properties ps = new Properties();
	        ps.put("mail.smtp.host","server");
	        ps.put("mail.smtp.auth","true");
	        Session mySession = Session.getInstance(ps,null);
	        Store myStore = mySession.getStore("pop3");//协议
	        myStore.connect("pop3.126.com","lookskystaremail@126.com","8524127");
	        Folder myFolder = myStore.getFolder("INBOX");//文件夹
	        myFolder.open(Folder.READ_ONLY);//打开文件夹

	        Message[] messages = myFolder.getMessages();//获得所有邮件

	        for(int i = 0; i<messages.length ; i++){
	            System.out.println(messages[i].getSubject());//主题
	            //System.out.println(messages[i].getContent());//内容
	        }
	       // messages[1].writeTo(System.out);
	        System.out.println(messages[1].getContentType());

	    }
}
