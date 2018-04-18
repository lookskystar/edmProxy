package com.edmProxy.test;
import java.util.Date;
import java.util.Properties;
import java.security.Security;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailTest {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        // final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        //设置代理服务器
        Properties props = System.getProperties();
//        props.setProperty("proxySet", "true");
        
        props.put("mail.smtp.auth", "true");
       
        props.setProperty("mail.smtp.host", "smtp.sohu.com");
        

        
        
//        props.setProperty("socksProxyHost", "46.211.113.98");
//        props.setProperty("socksProxyPort", "1080");
        
        
        // props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        // props.setProperty("mail.smtp.socketFactory.fallback", "false");
        // props.setProperty("mail.smtp.port", "465");
        // props.setProperty("mail.smtp.socketFactory.port", "465");
        //props.put("mail.debug", "true");
        // props.put("mail.store.protocol", "pop3");
        // props.put("mail.transport.protocol", "smtp");
        final String username = "lookskystar8@sohu.com";
        final String password = "123456";
        
        //使用验证
        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(username,
                                password);
                    }
                });
        MimeMessage message = new MimeMessage(session);
        Address address = new InternetAddress("lookskystar8@sohu.com");
        Address toAaddress = new InternetAddress("lookskystaremail@126.com");
        
        message.setFrom(address);
        message.setRecipient(MimeMessage.RecipientType.TO, toAaddress);
        message.setSubject("测试");
        message.setText("test");
        message.setSentDate(new Date());
        
        Transport.send(message);
        System.out.println("邮件发送！");

    }

}