package com.operate.tools;

public class OrderObj {
	private String field;
	private String sortType;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	
	public enum SortType{
		ASC,DESC
	}
}


