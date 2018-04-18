package com.edmProxy.entity;

import java.util.Date;

//统计实体
public class StatisticsEntity {
	private int id;
	private int sendTaskId;//发送任务id
	private int openCount;//打开次数
	private int clickeCount;//点击次数
	private String receiveIds;//打开者Id（id1，id2）
	private Date createDate;//代理创建日期
	private String  remark;
	
	public StatisticsEntity() {
		super();
	}

	public StatisticsEntity(int id, int sendTaskId, int openCount,
			int clickeCount, String receiveIds, Date createDate, String remark) {
		super();
		this.id = id;
		this.sendTaskId = sendTaskId;
		this.openCount = openCount;
		this.clickeCount = clickeCount;
		this.receiveIds = receiveIds;
		this.createDate = createDate;
		this.remark = remark;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSendTaskId() {
		return sendTaskId;
	}

	public void setSendTaskId(int sendTaskId) {
		this.sendTaskId = sendTaskId;
	}

	public int getOpenCount() {
		return openCount;
	}

	public void setOpenCount(int openCount) {
		this.openCount = openCount;
	}

	public int getClickeCount() {
		return clickeCount;
	}

	public void setClickeCount(int clickeCount) {
		this.clickeCount = clickeCount;
	}

	public String getReceiveIds() {
		return receiveIds;
	}

	public void setReceiveIds(String receiveIds) {
		this.receiveIds = receiveIds;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
