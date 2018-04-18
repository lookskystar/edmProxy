package com.edmProxy.entity;

import java.util.Date;

//发送账号
public class AccountEntity {
	private int id;
	private String account;//发送账号
	private String password;//密码
	private String post;//邮局
	private int valid;//是否有效（0无效）
	private int start;//是否启用（0无效）
	private Date createDate;//创建时间
	private int sendCount;//发送次数
	private Date lastSendDate;//最后发送时间
	private String remark;
	
	public AccountEntity() {
		super();
	}

	public AccountEntity(int id, String account, String password, String post,
			int valid, int start, Date createDate, int sendCount,
			Date lastSendDate, String remark) {
		super();
		this.id = id;
		this.account = account;
		this.password = password;
		this.post = post;
		this.valid = valid;
		this.start = start;
		this.createDate = createDate;
		this.sendCount = sendCount;
		this.lastSendDate = lastSendDate;
		this.remark = remark;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getSendCount() {
		return sendCount;
	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}

	public Date getLastSendDate() {
		return lastSendDate;
	}

	public void setLastSendDate(Date lastSendDate) {
		this.lastSendDate = lastSendDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}