package httpClientMail;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage.RecipientType;

//简单邮件发送器，可单发，群发。 
public class SimpleMailSender {
	// 发送邮件的props文件 
	private final transient Properties props = System.getProperties();
	// 邮件服务器登录验证 
	private transient MailAuthenticator authenticator;
	// 邮箱session 
	private transient Session session;
	
	/** 
	* 初始化邮件发送器 
	* @param smtpHostName SMTP邮件服务器地址 
	* @param username 发送邮件的用户名(地址) 
	* @param password 发送邮件的密码 
	*/ 
	public SimpleMailSender(final String smtpHostName,final String username,final String password) { 
		init(username, password, smtpHostName); 
	} 
	/** 
	* 初始化邮件发送器 
	* @param username 发送邮件的用户名(地址)，并以此解析SMTP服务器地址 
	* @param password 发送邮件的密码 
	*/ 
	public SimpleMailSender(final String username,final String password) {
		// 通过邮箱地址解析出smtp服务器，对大多数邮箱都管用 
		final String smtpHostName = "smtp." + username.split("@")[1]; 
		init(username, password, smtpHostName); 
	} 
	
	/** 
	* 初始化 
	* @param username 发送邮件的用户名(地址) 
	* @param password 密码 
	* @param smtpHostName SMTP主机地址 
	*/ 
	private void init(String username, String password, String smtpHostName) {
		
		//代理
		props.put("proxySet", "true");//如果不设置，只要代理IP和代理端口正确,此项不设置也可以  
		props.put("http.proxyHost", "23.104.72.3");  
		props.put("http.proxyPort", "808"); 
		Authenticator.setDefault(new Authenticator(){
     	   protected PasswordAuthentication getPasswordAuthentication() {
     	       return new PasswordAuthentication("user001", new String("123456").toCharArray());
     	 }
		});
		// 初始化props 
		props.put("mail.smtp.auth", "true"); 
		props.put("mail.smtp.host", smtpHostName); 
		// 验证 
		authenticator = new MailAuthenticator(username, password);
		// 创建session 
		session = Session.getInstance(props, authenticator); 
	} 
	/** 
	* 发送邮件 
	* @param recipient收件人邮箱地址 
	* @param subject邮件主题 
	* @param content邮件内容 
	* @throws AddressException 
	* @throws MessagingException 
	*/ 
	public void send(String recipient, String subject, String content)throws AddressException, MessagingException { 
		// 创建mime类型邮件 
		final MimeMessage message = new MimeMessage(session); 
		// 设置发信人 
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// 设置收件人 
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		// 设置主题 
		message.setSubject(subject); 
		// 设置邮件内容 
		Multipart mp = new MimeMultipart("related");
		MimeBodyPart mbp = new MimeBodyPart(); 
		mbp.setContent(content.toString(),"text/html;charset=utf-8");
		mp.addBodyPart(mbp); 
		message.setContent(mp); 


		// 设置邮件内容 
		// message.setContent(content.toString(), "text/html;charset=utf-8");
		// 发送 
		Transport.send(message); 
	}
	/** 
	* 群发邮件 
	* @param recipients收件人们 
	* @param subject 主题 
	* @param content 内容 
	* @throws AddressException 
	* @throws MessagingException 
	*/ 
	public void send(List<String> recipients, SimpleMail simpleMail)throws AddressException, MessagingException { 
		// 创建mime类型邮件 
		final MimeMessage message = new MimeMessage(session); 
		// 设置发信人 
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// 设置收件人们 
		final int num = recipients.size();
		InternetAddress[] addresses = new InternetAddress[num];
		for (int i = 0; i < num; i++) {
			addresses[i] = new InternetAddress(recipients.get(i));
		} 
		message.setRecipients(RecipientType.TO, addresses); 
		// 设置主题 
		message.setSubject(simpleMail.getSubject()); 
		// 设置邮件内容 
		message.setContent(simpleMail.getContent(), "text/html;charset=utf-8");
		// 发送 
		Transport.send(message); 
	} 
	
	/** 
	* 发送邮件 
	* @param recipient收件人邮箱地址 
	* @param mail邮件对象 
	* @throws AddressException 
	* @throws MessagingException 
	* 　 
	*/ 
	public void send(String recipient, SimpleMail mail)throws AddressException, MessagingException { 
		send(recipient, mail.getSubject(), mail.getContent()); 
	} 
	

}
