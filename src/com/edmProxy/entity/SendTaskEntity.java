package com.edmProxy.entity;
import java.util.Date;


//发送任务-每个任务对应一个邮局,对应一个代理IP，开启多个任务就是对应多个邮局，多个任务下多个账号链接
public class SendTaskEntity {
	private int id;
	private String sendTask;//发送任务名
	private String post;//邮局
	private String title;//发送邮件标题
	private int proxyStart;//是否启用代理0不启用，1启用
	private String contentPath;//发送内容路径
	private String accessoryPath;//附件路径（附件1，附件2）
	private int sendTaskCount;//发送次数
	private Date lastsendTaskDate;//最后发送时间
	private String sendTaskAccounts;//发送账号(id-账号1-密码，)
	private int accountSendNum;//每个账号发送数
	private int accountStartLinks;//账号启动数，该任务（IP下，账号最大链接数）
	private String sendTaskReceivesPath;//接收地址(id-接收地址1，)
	private int sendIntervalTime;//发送间隔时间（秒）
	private String remark;
	
	public SendTaskEntity() {
		super();
	}

	public SendTaskEntity(int id, String sendTask, String post, String title,
			int proxyStart, String contentPath, String accessoryPath,
			int sendTaskCount, Date lastsendTaskDate, String sendTaskAccounts,
			int accountSendNum, int accountStartLinks,
			String sendTaskReceivesPath, int sendIntervalTime, String remark) {
		super();
		this.id = id;
		this.sendTask = sendTask;
		this.post = post;
		this.title = title;
		this.proxyStart = proxyStart;
		this.contentPath = contentPath;
		this.accessoryPath = accessoryPath;
		this.sendTaskCount = sendTaskCount;
		this.lastsendTaskDate = lastsendTaskDate;
		this.sendTaskAccounts = sendTaskAccounts;
		this.accountSendNum = accountSendNum;
		this.accountStartLinks = accountStartLinks;
		this.sendTaskReceivesPath = sendTaskReceivesPath;
		this.sendIntervalTime = sendIntervalTime;
		this.remark = remark;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSendTask() {
		return sendTask;
	}

	public void setSendTask(String sendTask) {
		this.sendTask = sendTask;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getProxyStart() {
		return proxyStart;
	}

	public void setProxyStart(int proxyStart) {
		this.proxyStart = proxyStart;
	}

	public String getContentPath() {
		return contentPath;
	}

	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
	}

	public String getAccessoryPath() {
		return accessoryPath;
	}

	public void setAccessoryPath(String accessoryPath) {
		this.accessoryPath = accessoryPath;
	}

	public int getSendTaskCount() {
		return sendTaskCount;
	}

	public void setSendTaskCount(int sendTaskCount) {
		this.sendTaskCount = sendTaskCount;
	}

	public Date getLastsendTaskDate() {
		return lastsendTaskDate;
	}

	public void setLastsendTaskDate(Date lastsendTaskDate) {
		this.lastsendTaskDate = lastsendTaskDate;
	}

	public String getSendTaskAccounts() {
		return sendTaskAccounts;
	}

	public void setSendTaskAccounts(String sendTaskAccounts) {
		this.sendTaskAccounts = sendTaskAccounts;
	}

	public int getAccountSendNum() {
		return accountSendNum;
	}

	public void setAccountSendNum(int accountSendNum) {
		this.accountSendNum = accountSendNum;
	}

	public int getAccountStartLinks() {
		return accountStartLinks;
	}

	public void setAccountStartLinks(int accountStartLinks) {
		this.accountStartLinks = accountStartLinks;
	}

	public String getSendTaskReceivesPath() {
		return sendTaskReceivesPath;
	}

	public void setSendTaskReceivesPath(String sendTaskReceivesPath) {
		this.sendTaskReceivesPath = sendTaskReceivesPath;
	}

	public int getSendIntervalTime() {
		return sendIntervalTime;
	}

	public void setSendIntervalTime(int sendIntervalTime) {
		this.sendIntervalTime = sendIntervalTime;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
