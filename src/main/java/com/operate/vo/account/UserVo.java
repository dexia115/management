package com.operate.vo.account;

import java.util.List;

public class UserVo {
	
	private Long id;
	
	private String userName;
	
	private String password;
	
	private String mobile;
	
	private String idcard;
	
	private String realName;
	
	private Boolean enable = true;
	
	private List<Long> roles;
	
	private List<RoleVo> roleVoList;
	
	private String roleStr;
	
	private String createTime;
	
	public UserVo(){
		
	}
	
	public UserVo(Long id,String userName,String password,String mobile){
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.mobile = mobile;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public List<Long> getRoles() {
		return roles;
	}

	public void setRoles(List<Long> roles) {
		this.roles = roles;
	}

	public String getRoleStr() {
		return roleStr;
	}

	public void setRoleStr(String roleStr) {
		this.roleStr = roleStr;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<RoleVo> getRoleVoList() {
		return roleVoList;
	}

	public void setRoleVoList(List<RoleVo> roleVoList) {
		this.roleVoList = roleVoList;
	}

}
