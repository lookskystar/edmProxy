package com.edmProxy.util.mail.send;

import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;


public class CopyOfSendMail {
	
	public static void main(String[] args) throws UnsupportedEncodingException, MessagingException {
		CopyOfSendMail s=new CopyOfSendMail();
		
		AccountEntity accountEntity=new AccountEntity();
		accountEntity.setAccount("duhuai8416@126.com");
		accountEntity.setPassword("ercui534");
		accountEntity.setPost("126.com");
		
		
		
		ArrayList<String> receiveList=new ArrayList<String>();
		receiveList.add("lookskystaremail@126.com");
		
		String contentStr=MailUtil.readTemplateMail("F:\\mobang.html");
		
		s.sendMailTrue(accountEntity, receiveList, null, "test", contentStr, 0);
	}
	
	
	
	
	
	
	private static ArrayList<String> accessoryMailList;
	
	//设置代理主机,javamail只支持scoket
	public static void setProxyHost(Properties properties){//ProxyEntiy proxyEntiy
//		System.getProperties().setProperty("proxySet", "true");//如果不设置，只要代理IP和代理端口正确,此项不设置也可以  
//		System.getProperties().setProperty("http.proxyHost", "23.104.72.2");  
//		System.getProperties().setProperty("http.proxyPort", "808"); 

		properties.setProperty("proxySet", "true");
		properties.setProperty("socksProxyHost", "23.109.91.98"); //proxyEntiy.getProxyHost()
		properties.setProperty("socksProxyPort", "1080"); //proxyEntiy.getProxyPort();
		Authenticator.setDefault(new Authenticator(){
      	   protected PasswordAuthentication getPasswordAuthentication() {
      	       return new PasswordAuthentication("user001", new String("123456").toCharArray());
      	 }
		});
	}
	
	//设置邮件服务器信息
	public static void setMialHost(Properties properties,AccountEntity accountEntity){
		properties.setProperty("mail.transport.protocol","smtp");    //设置smtp协议和值
		properties.setProperty("mail.smtp.host", "smtp."+accountEntity.getPost());// 设置服务器Ip或域名和值
		properties.setProperty("mail.smtp.port", "25");// 设置服务器Ip端口号和值
		properties.setProperty("mail.smtp.auth", "true"); //设置是否通过验证和值
	}
	
	//设置收件人
	public static void setReveice(ArrayList<String> receiveMailInfoList,MimeMessage mimeMessage) throws MessagingException{
		//to:收件人地址,一个收件人地址
		int receiveMailCount=receiveMailInfoList.size();
		 if (receiveMailCount > 0) {
			//新建一个地址数组，长度等于receiveMailList的长度
			InternetAddress[] address = new InternetAddress[receiveMailCount];
			//把receiveMailList数组中的数据拷到address
			 for (int i = 0; i < receiveMailCount; i++) {
			   String receiveMailStr =receiveMailInfoList.get(i);
			   //System.out.println("receiveMailStr--"+receiveMailStr);
			   if(receiveMailStr.trim().length() > 0){
				   address[i] = new InternetAddress(receiveMailStr);  
			   }else{
				   System.out.println("地址为空!");
			   }
      	     }
			 //设置收件人的地址和发送方式//TO：主收件人，CC：抄送收件人，BCC：暗抄送人
			 mimeMessage.addRecipients(Message.RecipientType.TO, address);
		 }else{
			 System.out.println("没有收件人邮件地址名! 程序将退出!");
		 }
	}
	
	//设置附件
	public static void setAccessorys(Multipart multipartMain,MimeMessage mimeMessage,ArrayList<String> list) throws MessagingException, UnsupportedEncodingException{
		accessoryMailList=new ArrayList<String>();
		accessoryMailList=list;
		//accessoryMailList.add("F:\\mobang.html");
		int accessoryMailCount=accessoryMailList.size();
		if(accessoryMailCount>-1){
			for (int i = 0; i < accessoryMailCount; i++) {
				//创建邮件附件MimeBodyPart对象 
				MimeBodyPart mimeBodyPartMccessory = new MimeBodyPart(); 
				//选择出附件名
				String accessoryPath =(String)accessoryMailList.get(i);
				//得到数据源（把附件放到数据源中）
				FileDataSource fileDataSource = new FileDataSource(accessoryPath);
				 /*
     	        * javax.activation.DataHandler类(包含在JAF中)JavaMail API不限制信息只为文本,
     	        * 任何形式的信息都可能作茧自缚MimeMessage的一部分.除了文本信息,作为文件附件包含在电子邮件信息的一部分是很普遍的.
     	        * JavaMail API通过使用DataHandler对象,提供一个允许我们包含非文本BodyPart对象的简便方法. 
     	        */
				DataHandler dataHandler=new DataHandler(fileDataSource);
				//得到附件本身并至入mccessoryMimeBodyPart
				mimeBodyPartMccessory.setDataHandler(dataHandler);
				//得到文件名同样至入mccessoryMimeBodyPart
     	        mimeBodyPartMccessory.setFileName(fileDataSource.getName());
     	        
     	       //解决附件中文乱码问题
     	       String fileName=accessoryPath.substring(accessoryPath.lastIndexOf("\\")+1,accessoryPath.length());
     	       //System.out.println("你所发送的附件为--》:"+fileName);
     	       mimeBodyPartMccessory.setFileName(MimeUtility.encodeText("附件"));
     	       //System.out.println("------------------===>:"+mimeBodyPartMccessory.getFileName());
     	       //加载附件的中的图片<img src="cid:top_bg.jpg" /> 为附件中的图片设置content-id,这样html格式邮件中就可以显示附件中的图片了
    	       mimeBodyPartMccessory.addHeader("content-id", mimeBodyPartMccessory.getFileName());
    	       //把邮件附件加入邮件主体multipartMain
    	       multipartMain.addBodyPart(mimeBodyPartMccessory);
			}
			//Multipart加入到信件(把邮件的主体加到发送消息中)
		    mimeMessage.setContent(multipartMain);
		    //移走集合中的所有元素（清除List表中的附件）
		    accessoryMailList.removeAll(accessoryMailList);
		}
	}
	//设置主题和内容
	public static void setTitleContent(MimeMessage mimeMessage,Multipart multipartMain,String titleMail,String contentMail,int flag) throws MessagingException{
		//设置邮件主题
		 mimeMessage.setSubject(titleMail);
		 //MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象（邮件主体）和邮件附件
		//设置内容，分html和文本
//		 if(flag==0){
			//创建一个包含HTML内容的MimeBodyPart（html邮件文字内容）
			 MimeBodyPart mimeBodyPartHtml = new MimeBodyPart();
			//设置html邮件文字内容和页面的编码格式
			mimeBodyPartHtml.setContent(contentMail, "text/html ;charset=gbk");
			//把html邮件文字内容添加到multipartMain容器中
			multipartMain.addBodyPart(mimeBodyPartHtml);
			mimeMessage.setContent(multipartMain);
//		 }else if(flag==1){
//			 mimeMessage.setText(contentMail);  
//		 }
	}
	
	
	//发送邮件                                                                                       //附件                                //html模板                
	public void sendMailTrue(AccountEntity accountEntity,ArrayList<String> receiveList,ArrayList<String> accessoryMailList,String titleMail,String contentMail,int state) throws MessagingException, UnsupportedEncodingException{
		//String templateMail = MailUtil.readTemplateMail("F:\\mobang.html");//mail.getTemplateMail());// 读取发送邮件模板
		//String titleMail = "test";
		//System.out.println(accountEntity.getAccount()+":"+accountEntity.getPassword()+":"+accountEntity.getPost());
		// 身份验证发件人地址和密码
		PopupAuthenticator popupAuthenticator = new PopupAuthenticator();// 电子邮件发送服务器身份验证
		popupAuthenticator.performCheck(accountEntity.getAccount(),accountEntity.getPassword()); // 公司自己架设的邮件服务器用户名和密码,输入发件人地址和发件人密码（必须是真实存在的，发件服务器上的）

		//封装服务器参数,Properties表示一个持久的属性集。Properties可保存在流中加载。属性列表中每个键及其对应值都是一个字符串,创建Properties对象（用户读取Properties文件）
		//Properties properties = System.getProperties();
		Properties properties = new Properties();
//		if(state==1){//==1使用代理
//			setProxyHost(properties);//设置代理主机
//		}
		setMialHost(properties,accountEntity);//设置邮件服务器信息
		Session session = Session.getDefaultInstance(properties,popupAuthenticator);// 得到默认的对话对象（里面有系统参数和电子邮件发送服务器身份验证内容）

//		try {
			// 开始创建邮件主体(需要进行异常处理)
			/*
			 * javax.mail.internet,MinmeMessage类：这个类是实际消息的模型板。当它首次创建时包含的关于该消息的信息和数值很是有限；
			 * 跟着后续的方法从该消息中找到更多数值，这个类便用来存储这些数值。
			 * Message对象将存储我们实际发送的电子邮件信息，Message对象被作为一个MimeMessage对象来创建并且需要知道应当选择哪一个JavaMail
			 * session。
			 */
			// 创建一个消息，并初始化该消息的各项元素
			MimeMessage mimeMessage = new MimeMessage(session);
			/*
			 * 一旦您创建了 Session 和 Message，并将内容填入消息后，就可以用Address确定信件地址了。 和 Message
			 * 一样，Address 也是个抽象类。您用的是Javax.mail.internet.InternetAddress 类.
			 * this.from：发件人的地址
			 */
			String from=new String(accountEntity.getAccount().getBytes("GBK"),"ISO-8859-1");
			//System.out.println("from-->"+from);
			InternetAddress internetAddress = new InternetAddress(from);
			// 把发件人的地址设置到消息中
			mimeMessage.setFrom(internetAddress);
			Multipart multipartMain = new MimeMultipart(); 
			
			setReveice(receiveList,mimeMessage);//设置收件人
			//System.out.println("receiveMailInfoList--->"+receiveList.get(0));
			//setAccessorys(multipartMain,mimeMessage,accessoryMailList);//设置附件
			setTitleContent(mimeMessage,multipartMain,titleMail,contentMail,0);//设置主题和内容
			 
			//设置信件头的发送日期
		    mimeMessage.setSentDate(new Date());
		    //保存发送消息的
		    mimeMessage.saveChanges();
		    //发送信件
		    Transport transport = session.getTransport("smtp");
		    
		    //System.out.println("smtp."+accountEntity.getPost()+"-"+accountEntity.getAccount()+"-"+accountEntity.getPassword());
		    transport.connect("smtp."+accountEntity.getPost(), accountEntity.getAccount(),accountEntity.getPassword());
		    transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.TO));
		    transport.close();
		    
		    removeLocalSendMailInfo();
		    
		    try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//		} catch (AddressException e) {
//			System.out.println("异常1");
//			e.printStackTrace();
//		} catch (MessagingException e) {
//			System.out.println("MessagingException异常，链接超时，说明链接最大数是到这里");
//			e.printStackTrace();
//		} 
//		catch (UnsupportedEncodingException e) {
//			System.out.println("异常3");
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			System.out.println("异常4");
//			e.printStackTrace();
//		}

	}
	
	
	public void removeLocalSendMailInfo()   
	{   
		Properties prop = new Properties();   
		prop.remove("proxySet");   
		prop.remove("socksProxyHost");   

		prop.remove("mail.transport.protocol");   
		prop.remove("mail.smtp.host");   
		prop.remove("mail.smtp.port"); 
		prop.remove("mail.smtp.auth"); 
	}   
	
	
	
}
