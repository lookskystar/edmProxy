package httpClientMail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MailTest {
	/**
	 * 单发
	 */
	//@Test
	public void testSingleSend() {
		SimpleMail sm = new SimpleMail();
		sm.setSubject("第一封邮件");
		String str = ReadHtmlFile.getSource("http://www.baidu.com");
		// String str = ReadHtmlFile.readFile("这里填写你需要发送的本地文件路径");
		System.out.println(str);
		sm.setContent(str);
		SimpleMailSender sms = new SimpleMailSender("这里填写你的发件箱", "这里填写你的发件箱密码");
		try {
			sms.send("这里需要填写接收邮件的邮箱", sm);
			System.out.println("执行完成！！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 群发
	 * 
	 */
	//@Test
	public void testMassSend() {
		SimpleMail sm = new SimpleMail();
		sm.setSubject("第一封邮件");
		String str = ReadHtmlFile.getSource("http://www.baidu.com");
		// String str = ReadHtmlFile.readFile("这里填写需要发送的本地文件路径");
		System.out.println(str);
		sm.setContent("test");
		SimpleMailSender sms = new SimpleMailSender("lookskystar8@sohu.com", "123456");
		List<String> recipients = new ArrayList<String>();
		recipients.add("lookskystaremail@126.com"); //添加地址
		try {
			sms.send(recipients, sm);
			System.out.println("执行完成！！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		MailTest mailTest=new MailTest();
		mailTest.testMassSend();
	}
}
