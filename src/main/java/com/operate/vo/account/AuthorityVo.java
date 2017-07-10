package com.operate.vo.account;

import java.util.ArrayList;
import java.util.List;

public class AuthorityVo {
	
	private Long id;
	
	private String name;
	
	private String code;
	
	private String details;
	
	private String url;
	
	private Integer sort = 0;
	
	private Integer method;
	
	private String icons;
	
	private Long parentId;
	
	private String parentName;
	
	private List<AuthorityVo> children = new ArrayList<AuthorityVo>();
	
	
	public AuthorityVo(){
		
	}
	
	public AuthorityVo(Long id, String name, String code, String details, String url, Integer sort, Integer method,
			String icons, Long parentId, String parentName) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.details = details;
		this.url = url;
		this.sort = sort;
		this.method = method;
		this.icons = icons;
		this.parentId = parentId;
		this.parentName = parentName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getMethod() {
		return method;
	}

	public void setMethod(Integer method) {
		this.method = method;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getIcons() {
		return icons;
	}

	public void setIcons(String icons) {
		this.icons = icons;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public List<AuthorityVo> getChildren() {
		return children;
	}

	public void setChildren(List<AuthorityVo> children) {
		this.children = children;
	}

}
