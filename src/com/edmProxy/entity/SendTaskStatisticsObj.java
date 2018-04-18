package com.edmProxy.entity;

public class SendTaskStatisticsObj {
	private int id;
	private StatisticsEntity statisticsEntity;
	private SendTaskEntity sendTaskEntity;
	
	public SendTaskStatisticsObj() {
		super();
	}

	public SendTaskStatisticsObj(int id, StatisticsEntity statisticsEntity,
			SendTaskEntity sendTaskEntity) {
		super();
		this.id = id;
		this.statisticsEntity = statisticsEntity;
		this.sendTaskEntity = sendTaskEntity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public StatisticsEntity getStatisticsEntity() {
		return statisticsEntity;
	}

	public void setStatisticsEntity(StatisticsEntity statisticsEntity) {
		this.statisticsEntity = statisticsEntity;
	}
	public SendTaskEntity getSendTaskEntity() {
		return sendTaskEntity;
	}
	public void setSendTaskEntity(SendTaskEntity sendTaskEntity) {
		this.sendTaskEntity = sendTaskEntity;
	}
}
