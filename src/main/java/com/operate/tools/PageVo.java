package com.operate.tools;

import java.util.List;

import javax.validation.constraints.Max;

public class PageVo {
	
	public PageVo(){
		
	}
	

	public PageVo(Integer pageSize, Integer pageNo){
		this.pageSize = pageSize;
		this.pageNo = pageNo;
	}
	
	private List items;

	private Integer totalCount;// 总记录数

	private Integer totalPageCount;// 总页数

	// 每页显示数量
	@Max(value = 200, message = "每页显示最大不能查过200")
	private Integer pageSize = 10;// 每页记录个数

	private Integer pageNo = 1;// 当前页数
	
	//查询索引位置
	private Integer fromIndex = -1;
	
	private String sortname;
	
	private String order;

	public List getItems() {
		return items;
	}


	public void setItems(List items) {
		this.items = items;
	}


	public Integer getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}


	public Integer getTotalPageCount() {
		return totalPageCount;
	}


	public void setTotalPageCount(Integer totalPageCount) {
		this.totalPageCount = totalPageCount;
	}


	public Integer getPageSize() {
		return pageSize;
	}


	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


	public Integer getPageNo() {
		return pageNo;
	}


	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}


	public Integer getFromIndex() {
		return fromIndex;
	}


	public void setFromIndex(Integer fromIndex) {
		this.fromIndex = fromIndex;
	}


	public String getSortname() {
		return sortname;
	}


	public void setSortname(String sortname) {
		this.sortname = sortname;
	}


	public String getOrder() {
		return order;
	}


	public void setOrder(String order) {
		this.order = order;
	}

}
