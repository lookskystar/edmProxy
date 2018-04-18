package com.edmProxy.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class SMTPDemo {
	String mailServer;
	String from;
	String pass;
	String to;
	String content;
	String lineFeet = "\r\n";
	private int port = 25;

	Socket client;
	BufferedReader in;
	DataOutputStream os;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMailServer() {
		return mailServer;
	}

	public void setMailServer(String mailServer) {
		this.mailServer = mailServer;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
	
	public String getPass() {
		return pass;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	private boolean init() {
		boolean boo = true;
		if (mailServer == null || "".equals(mailServer)) {
			return false;
		}
		try {
			client = new Socket(mailServer, port);
			System.out.println("client------>"+client);
			in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			os = new DataOutputStream(client.getOutputStream());
			
			String isConnect = response();
			if (isConnect.startsWith("220")) {

			} else {
				System.out.println("建立连接失败：" + isConnect);
				boo = false;
			}
		} catch (UnknownHostException e) {
			System.out.println("建立连接失败！");
			e.printStackTrace();
			boo = false;
		} catch (IOException e) {
			System.out.println("读取流失败！");
			e.printStackTrace();
			boo = false;
		}
		return boo;
	}

	private String sendCommand(String msg) {
		String result = null;
		try {
			os.writeBytes(msg);
			os.flush();
			result = response();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private String response() {
		String result = null;
		try {
			result = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void close() {
		try {
			os.close();
			in.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean sendMail() {
		
		if (client == null) {
			if (init()) {

			} else {
				return false;
			}
		}
	
		if (from == null || from.isEmpty() || to == null || to.isEmpty()) {
			return false;
		}

		String result = sendCommand("HELO " + mailServer + lineFeet);
		if (isStartWith(result, "250")) {
		} else {
			System.out.println("握手失败：" + result);
			return false;
		}

		String auth = sendCommand("AUTH LOGIN" + lineFeet);
		if (isStartWith(auth, "334")) {
		} else {
			return false;
		}
		String user = sendCommand(new String(Base64.encode(from.getBytes()))
				+ lineFeet);
		if (isStartWith(user, "334")) {
		} else {
			return false;
		}
		String p = sendCommand(new String(Base64.encode(pass.getBytes()))
				+ lineFeet);
		if (isStartWith(p, "235")) {
		} else {
			return false;
		}

		String f = sendCommand("Mail From:<" + from + ">" + lineFeet);
		if (isStartWith(f, "250")) {
		} else {
			return false;
		}
		String toStr = sendCommand("RCPT TO:<" + to + ">" + lineFeet);
		if (isStartWith(toStr, "250")) {
		} else {
			return false;
		}

		String data = sendCommand("DATA" + lineFeet);
		if (isStartWith(data, "354")) {
		} else {
			return false;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("From:<" + from + ">" + lineFeet);
		sb.append("To:<" + to + ">" + lineFeet);
		sb.append("Subject:test" + lineFeet);
		sb.append("Date:2010/10/27 17:30" + lineFeet);
		sb.append("Content-Type:text/plain;charset=\"GB2312\"" + lineFeet);
		sb.append(lineFeet);
		sb.append(content);
		sb.append(lineFeet + "." + lineFeet);

		String conStr = sendCommand(sb.toString());
		
		System.out.println("---->"+sb);
		
		if (isStartWith(conStr, "250")) {
		} else {
			return false;
		}

		String quit = sendCommand("QUIT" + lineFeet);
		if (isStartWith(quit, "221")) {
		} else {
			return false;
		}
		close();
		return true;
	}

	private boolean isStartWith(String res, String with) {
		return res.startsWith(with);
	}

	public static void main(String[] args) {
		SMTPDemo mail = new SMTPDemo();
		mail.setMailServer("smtp.126.com");
		mail.setFrom("lookskystaremail@126.com");
		mail.setPass("8524127");
		mail.setTo("lookskystaremail@126.com");
		mail.setContent("hello,my name is zhujiahao.");
		boolean boo = mail.sendMail();
		if (boo)
			System.out.println("邮件发送成功！");
		else {
			System.out.println("邮件发送失败！");
		}
	}
}