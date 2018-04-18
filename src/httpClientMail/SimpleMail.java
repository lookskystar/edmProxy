package httpClientMail;

public class SimpleMail {
	//邮件中的主题 
	private String subject; 
	//邮件的内容（正文） 
	private String content; 
	//可根据需要增加其他邮件元素 

	public void setSubject(String subject) {
	this.subject = subject; 
	} 

	public void setContent(String content) {
	this.content = content; 
	} 

	public String getSubject() { 
	return subject; 
	} 

	public String getContent() { 
	return content; 
	} 
}
