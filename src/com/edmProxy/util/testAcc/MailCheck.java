package com.edmProxy.util.testAcc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

//测试账号
public class MailCheck {
	public static void main(String[] args) {
		

	}
	//
	private Socket socket;
	public MailCheck(String server, int port) throws UnknownHostException,IOException {
	try {
		socket = new Socket(server, port);
	} catch (SocketException e)
		{
		} 
	catch (Exception e) 
		{
		} finally 
		{
		}
	}
	//注册到邮件服务器
	public void helo(String server, BufferedReader in, BufferedWriter out)throws IOException {
		int result;
		result = getResult(in);
		// 连接上邮件服务后,服务器给出220应答
		if (result != 220) {
			throw new IOException("连接服务器失败");
		}
		result = sendServer("HELO " + server, in, out);
		// HELO命令成功后返回250
		if (result != 250) {
			throw new IOException("注册邮件服务器失败！");
		}
	}
	private int sendServer(String str, BufferedReader in, BufferedWriter out)throws IOException {
		out.write(str);
		out.newLine();
		out.flush();
		return getResult(in);
	}
	public int getResult(BufferedReader in) {
		String line = "";
		try {
			String inRead=in.readLine();
			System.out.println(inRead);
			line = inRead;
			System.out.println(line);
		
			//服务器返回状态:454 Authentication failed, please open smtp flag first!
			if (null == line ||  line.indexOf("open smtp flag") != -1) {
				//serviceIsOpen = 1;
			}
			if (line.indexOf("No route to host") != -1 ) {
				//serviceIsOpen = 2;
			}
			if (line.indexOf("authentication failed") != -1|| line.indexOf("550") != -1) {
				//serviceIsOpen = 3;
			}
		} catch (Exception e) {
		}
		// 从服务器返回消息中读出状态码,将其转换成整数返回
		StringTokenizer st = new StringTokenizer(line, " ");
		return Integer.parseInt(st.nextToken());
	}
	public void authLogin(String emailuser,String password, BufferedReader in,BufferedWriter out) throws IOException {
		int result;
//		result = sendServer("AUTH LOGIN", in, out);
//		if (result != 334) {
//			throw new IOException("用户验证失败！");
//		}
//		result = sendServer(encode.encode(emailuser.getBytes()), in,out);
//		if (result != 334) {
//			throw new IOException("用户名错误！");
//		}
//		result = sendServer(encode.encode(password.getBytes()),in, out);
//		if (result != 235) {
//			throw new IOException("验证失败！");
//		}
	}
	// 退出
	public void quit(BufferedReader in, BufferedWriter out) throws IOException {
		int result;
		result = sendServer("QUIT", in, out);
		if (result != 221) {
			throw new IOException("未能正确退出");
		}
	}
	// 发送邮件主程序
	public boolean sendMail(String emailuser,String password, String server) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
			socket.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
			socket.getOutputStream()));
			helo(server, in, out);// HELO命令 连接服务器
			authLogin(emailuser,password, in, out);// AUTH LOGIN命令
			quit(in, out);// QUIT
		} catch (Exception e) {
			return false;
		}
			return true;
	}
}
