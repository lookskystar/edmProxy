package com.edmProxy.entity;

import java.util.Map;

//树菜单类
public class TreeUserObj {
	private int id;
	private String key;
	private String value;
	public TreeUserObj() {
		super();
	}

	public TreeUserObj(int id, String key, String value) {
		super();
		this.id = id;
		this.key = key;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	// 重点在toString，节点的显示文本就是toString
    public String toString() {
        return value;
    }
}
