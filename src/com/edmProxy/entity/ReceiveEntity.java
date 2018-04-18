package com.edmProxy.entity;

import java.util.Date;

//接收地址
public class ReceiveEntity {
	private int id;
	private String receive;//接收地址
	private String post;//邮局
	private int sendCount;//发送次数
	private Date createDate;//创建时间
	private Date lastSendDate;//最后一次发送时间
	private String remark;

	public ReceiveEntity() {
		super();
	}

	public ReceiveEntity(int id, String receive, String post, int sendCount,
			Date createDate, Date lastSendDate, String remark) {
		super();
		this.id = id;
		this.receive = receive;
		this.post = post;
		this.sendCount = sendCount;
		this.createDate = createDate;
		this.lastSendDate = lastSendDate;
		this.remark = remark;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public int getSendCount() {
		return sendCount;
	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
