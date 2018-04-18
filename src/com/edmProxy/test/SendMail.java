package com.edmProxy.test;

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




public class SendMail {
	//发送邮件2 实体正式
	public static void SendMailTrue(SendEmailEntiy sendEmailEntiy,ArrayList<String> receiveMailInfoList){
        
//		ArrayList<String> receiveMailList=new ArrayList();
//		receiveMailList=receiveMailInfoList;
		
		
		String templateMail = MailUtil.readTemplateMail("F:\\mobang.html");//mail.getTemplateMail());// 读取发送邮件模板
		String titleMail = "邮件主题";
		// 身份验证发件人地址和密码
		PopupAuthenticator popupAuthenticator = new PopupAuthenticator();// 电子邮件发送服务器身份验证
		popupAuthenticator.performCheck(sendEmailEntiy.getSendAccount(),sendEmailEntiy.getPassword()); // 公司自己架设的邮件服务器用户名和密码,输入发件人地址和发件人密码（必须是真实存在的，发件服务器上的）

		Properties properties = System.getProperties();//封装服务器参数,Properties表示一个持久的属性集。Properties可保存在流中加载。属性列表中每个键及其对应值都是一个字符串,创建Properties对象（用户读取Properties文件）
		
		
//		System.getProperties().setProperty("proxySet", "true");//如果不设置，只要代理IP和代理端口正确,此项不设置也可以  
//		System.getProperties().setProperty("http.proxyHost", "23.104.72.2");  
//		System.getProperties().setProperty("http.proxyPort", "808"); 
		
		
		properties.setProperty("proxySet", "true");
		properties.setProperty("socksProxyHost", "23.104.72.2");
		properties.setProperty("socksProxyPort", "808");
		Authenticator.setDefault(new Authenticator(){
      	   protected PasswordAuthentication getPasswordAuthentication() {
      	       return new PasswordAuthentication("user001", new String("123456").toCharArray());
      	 }
		});
	        
		
		
		properties.setProperty("mail.transport.protocol","smtp");    //设置smtp协议和值
		properties.setProperty("mail.smtp.host", sendEmailEntiy.getHost());// 设置服务器Ip或域名和值
		properties.setProperty("mail.smtp.port", sendEmailEntiy.getPort());// 设置服务器Ip端口号和值
		properties.setProperty("mail.smtp.auth", "true"); //设置是否通过验证和值
		
		Session session = Session.getDefaultInstance(properties,
				popupAuthenticator);// 得到默认的对话对象（里面有系统参数和电子邮件发送服务器身份验证内容）

		try {
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
			// this.from=new String(from.getBytes("GBK"),"ISO-8859-1");
			InternetAddress internetAddress = new InternetAddress(sendEmailEntiy.getSendAccount());
			// 把发件人的地址设置到消息中
			mimeMessage.setFrom(internetAddress);
			
		    //to:收件人地址,一个收件人地址
			int receiveMailCount=receiveMailInfoList.size();
			 if (receiveMailCount > 0) {
				//新建一个地址数组，长度等于receiveMailList的长度
				InternetAddress[] address = new InternetAddress[receiveMailCount];
				//把receiveMailList数组中的数据拷到address
				 for (int i = 0; i < receiveMailCount; i++) {
				   String receiveMailStr =receiveMailInfoList.get(i);
				   System.out.println("receiveMailStr--"+receiveMailStr);
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
			 //设置邮件主题
			 mimeMessage.setSubject(titleMail);
			 //MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象（邮件主体）和邮件附件
			 Multipart multipartMain = new MimeMultipart(); 
			//创建一个包含HTML内容的MimeBodyPart（html邮件文字内容）
			 MimeBodyPart mimeBodyPartHtml = new MimeBodyPart();
			//设置html邮件文字内容和页面的编码格式
			mimeBodyPartHtml.setContent("F:\\mobang.html", "text/html ;charset=gbk");
			//把html邮件文字内容添加到multipartMain容器中
			multipartMain.addBodyPart(mimeBodyPartHtml);
			
			//可以添加发送多个附件
			ArrayList accessoryMailList=new ArrayList();
			accessoryMailList.add("F:\\mobang.html");
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
			//设置信件头的发送日期
		    mimeMessage.setSentDate(new Date());
		    //保存发送消息的
		    mimeMessage.saveChanges();
		    //发送信件
		    Transport transport = session.getTransport("smtp");
		    
		    transport.connect(sendEmailEntiy.getHost(), sendEmailEntiy.getSendAccount(),sendEmailEntiy.getPassword());
		    transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.TO));
		    transport.close();
		    
		    Thread.sleep(3000);

		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
