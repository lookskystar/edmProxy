package httpClientMail;

import javax.mail.Authenticator; 
import javax.mail.PasswordAuthentication; 

//服务器邮箱登录验证 
public class MailAuthenticator extends Authenticator {
	private String username; // 用户名（登录邮箱） 
	private String password; // 密码 
	//初始化邮箱和密码 
	public MailAuthenticator(String username, String password){
		this.username = username; 
		this.password = password; 
	} 
	String getPassword() { 
		return password; 
	} 
	String getUsername() { 
		return username; 
	} 
	public void setPassword(String password) {
		this.password = password; 
	} 

	public void setUsername(String username) {
		this.username = username; 
	} 
	
	@Override 
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	} 
	
	
}
