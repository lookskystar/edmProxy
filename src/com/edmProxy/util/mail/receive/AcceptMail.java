package com.edmProxy.util.mail.receive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.AndTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

import com.edmProxy.entity.AccountEntity;
import com.sun.mail.pop3.POP3Folder;

public class AcceptMail {
	 public static void main(String[] args) throws Exception{
//	        Properties ps = new Properties();
//	        ps.put("mail.smtp.host","server");
//	        ps.put("mail.smtp.auth","true");
//	        Session mySession = Session.getInstance(ps,null);
//	        Store myStore = mySession.getStore("pop3");//协议
//	        myStore.connect("pop3.126.com","lookskystaremail@126.com","8524127");
//	        Folder myFolder = myStore.getFolder("INBOX");//文件夹
//	        myFolder.open(Folder.READ_ONLY);//打开文件夹
//
//	        Message[] messages = myFolder.getMessages();//获得所有邮件
//
//	        for(int i = 0; i<messages.length ; i++){
//	            System.out.println(messages[i].getSubject());//主题
//	            //System.out.println(messages[i].getContent());//内容
//	            messages[i].setFlag(Flags.Flag.SEEN, true);
//	            messages[i].setFlag(Flags.Flag.DELETED, true);
//	            myFolder.close(true);
//	            System.out.println("删除成功");
//	        }
//	        
//	       // messages[1].writeTo(System.out);
//	        //System.out.println(messages[1].getContentType());
		 
		 
		 //AcceptMail.mailReceive("looktestandy@163.com", "andy123456", "imap.163.com", "33018953@qq.com", "test");
	    }  

	 //imap接收邮件           //收取的账号，密码，邮局，查询该账号邮件的发件人、主题
	 public static String mailReceive(String reveiceEmail,String password,String post,AccountEntity accountEntity,String searchSubject){
		 	String msg="";
		 // 连接pop3服务器的主机名、协议、用户名、密码  
//	        String pop3Server = "pop3.qq.com";  
//	        String protocol = "pop3";  
	        String server = "imap."+post;//"imap.163.com"; 
	        //System.out.println(server);
	        String temp=server.substring(0, server.indexOf("."));
	        
	        String protocol = temp;//"imap";  
	        String user = reveiceEmail;//"looktestandy@163.com";  
	        String pwd = password;//"andy123456";  
	         
	        
	       
	        // 创建一个有具体连接信息的Properties对象  
	        Properties props = new Properties();  
	        props.setProperty("mail.store.protocol", protocol);  
	        //props.setProperty("mail.pop3.host", pop3Server);
	        props.setProperty("mail."+protocol+".host", server);  
	          
	        // 使用Properties对象获得Session对象  
	        Session session = Session.getInstance(props);  
	        session.setDebug(true);  
	        try {
	        	// 利用Session对象获得Store对象，并连接pop3服务器  
		        Store store = session.getStore();  
		        store.connect(server, user, pwd);  
		          
		        // 获得邮箱内的邮件夹Folder对象，以"读-写"打开  
		        Folder folder = store.getFolder("inbox");  
		        folder.open(Folder.READ_WRITE);  
		          
		          
		        // 搜索发件人为 test_hao@sina.cn 和主题为"测试1"的邮件  
		        SearchTerm st = new AndTerm(  
		                new FromStringTerm(accountEntity.getAccount()),//"33018953@qq.com"),  
		                new SubjectTerm(searchSubject));//"test"));  
		          
//		      // 获得邮件夹Folder内的所有邮件Message对象  
//		      Message [] messages = folder.getMessages();  
		          
		        // 不是像上面那样直接返回所有邮件，而是使用Folder.search()方法  
		        Message [] messages = folder.search(st);  
		        int mailCounts = messages.length;  
//		        System.out.println("搜索过滤到" + mailCounts + " 封符合条件的邮件！");  
		        
		        for(int i = 0; i < mailCounts; i++) {  
		            String subject = messages[i].getSubject();  
		            String from = (messages[i].getFrom()[0]).toString();  
		              
//		            System.out.println("第 " + (i+1) + "封邮件的主题：" + subject);  
//		            System.out.println("第 " + (i+1) + "封邮件的发件人地址：" + from);  
		              
//		            System.out.println("是否删除该邮件(yes/no)?：");  
//		            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  
//		            String input = br.readLine();  
//		            if("yes".equalsIgnoreCase(input)) {  
		                // 直接输出到控制台中  
//		              messages[i].writeTo(System.out);  
		                // 设置删除标记，一定要记得调用saveChanges()方法 
		            if(from.equals(accountEntity.getAccount())&&searchSubject.equals(subject)){
		            	msg+="true-"+accountEntity.getAccount()+"-"+accountEntity.getPassword()+"-账号,发送测试信成功\n"; 
		            	
		            }else{
		            	//msg+="false-测试邮件:账号 "+searchFromEmail+" 发送测试信失败\n"; 
		            }
		            messages[i].setFlag(Flags.Flag.DELETED, true);
	            	messages[i].saveChanges(); 
//		            }             
		        }  
		        // 关闭连接时设置了删除标记的邮件才会被真正删除，相当于"QUIT"命令  
		        
		        folder.close(false);  
		        store.close(); 
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				return msg;
			}

	        //return msg;
	 }
	 
//	public static void receive(String server, int port, String username, String password, boolean isDelete)throws MessagingException {
//		Properties props =System.getProperties();
//		PopupAuthenticator auth =new PopupAuthenticator();
//		auth.performCheck(username,password);
//		
//		Session session = Session.getInstance(props,auth);
//		session.setDebug(false);
//		Store store = session.getStore("POP3");
//		store.connect(server,port,username,password);
//		//获得收件箱
//		POP3Folder folder =(POP3Folder)store.getFolder("INBOX");
//		try{
//		//读写方式打开
//			folder.open(Folder.READ_WRITE);
//		} catch(MessagingException ex) {
//		//制度方式打开
//			folder.open(Folder.READ_ONLY);
//		}
//		// int totalMessages = folder.getMessageCount（）；
//		Message m_message = null;
//		Message[] msgs = folder.getMessages();
//		for(int i = 0; i < msgs.length; i++){
//			m_message = msgs[i];
//			String UID = folder.getUID(m_message);
//			
//			
//			m_message.setFlag(Flags.Flag.SEEN, true);
//			m_message.setFlag(Flags.Flag.DELETED, isDelete);
//	//		if(haveReceived(UID){
//	//			// 插入数据库
//	//			// mailList.add（new RecvMailTask（m_message,
//	//			// p_st_attachmentParentDir, UID））；
//	//			// 设置为已读，IMAP协议支持，POP3协议不支持该功能
//	//			m_message.setFlag(Flags.Flag.SEEN, true);
//	//			// POP3协议可以删除
//	//			m_message.setFlag(Flags.Flag.DELETED, isDelete);
//	//		　　}
//		 }
//	}

}
