package com.operate.vo.account;

import java.util.List;

public class RoleVo {
	
	private Long id;
	
	private String name;
	
	private String code;
	
	private String details;
	
	private Boolean enable;
	
	private List<Long> authorityIds;
	
	private String authStr;
	
	private List<AuthorityVo> authorityVos;
	
	public RoleVo(){
		
	}
	
	public RoleVo(Long id,String name,String code,String details){
		this.id = id;
		this.name = name;
		this.code = code;
		this.details = details;
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

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public List<Long> getAuthorityIds() {
		return authorityIds;
	}

	public void setAuthorityIds(List<Long> authorityIds) {
		this.authorityIds = authorityIds;
	}

	public List<AuthorityVo> getAuthorityVos() {
		return authorityVos;
	}

	public void setAuthorityVos(List<AuthorityVo> authorityVos) {
		this.authorityVos = authorityVos;
	}

	public String getAuthStr() {
		return authStr;
	}

	public void setAuthStr(String authStr) {
		this.authStr = authStr;
	}

}
