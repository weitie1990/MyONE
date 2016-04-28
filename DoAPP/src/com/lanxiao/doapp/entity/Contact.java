package com.lanxiao.doapp.entity;

/**
 * 联系人实体类
 * 
 *
 */
public class Contact {
	/**
	 * 联系人姓名
	 */
	private String name;
	private String number;
	/**
	 * 排序字母
	 */
	private String sortKey;

	public String getName() {
		return name;
	}
	public String getNumber(){
		return number;
	}
	public void setNumber(String number){
		this.number=number;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

}
