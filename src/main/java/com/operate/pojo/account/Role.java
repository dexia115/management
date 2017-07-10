package com.operate.pojo.account;

import javax.persistence.*;
import com.operate.pojo.BaseEntity;
import com.operate.vo.account.AuthorityVo;
import com.operate.vo.account.RoleVo;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account_role")
public class Role extends BaseEntity {

	private static final long serialVersionUID = -9196730643894509307L;

	@Column(name = "name", length = 20)
	private String name;
	
	@Column(length = 20)
	private String code;

	// 描述
	@Column(length = 20)
	private String details;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles", cascade = CascadeType.DETACH)
	private List<User> users;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "role_authority")
	@OrderBy(value = "parent_id ASC,sort ASC")
	private List<Authority> authoritys;

	public Role() {
		// 保留缺省构造方法
	}

	public Role(Long id) {
		this.id = id;
	}

	public Role(String name) {
		this.name = name;
	}

	public Role(Long id, String name, String code, String details) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.details = details;
	}

	public RoleVo toVo() {
		RoleVo roleVo = new RoleVo(id, name, code, details);
		roleVo.setEnable(enable);
		List<Long> authorityIds = new ArrayList<>();
		List<AuthorityVo> authorityVos = new ArrayList<>();
		StringBuffer sb = new StringBuffer();
		if(!authoritys.isEmpty()){
			for(Authority auth : authoritys){
				authorityIds.add(auth.getId());
				authorityVos.add(auth.toVo());
				if(auth.getMethod()==1 && auth.getParent() != null){
					sb.append(auth.getName()+",");
				}
			}
			if(sb.length() > 1){
				sb.deleteCharAt(sb.length()-1);
			}
			roleVo.setAuthStr(sb.toString());
		}
		roleVo.setAuthorityIds(authorityIds);
		roleVo.setAuthorityVos(authorityVos);
		return roleVo;
	}
	
	public static Role fromVo(RoleVo vo){
		return new Role(vo.getId(),vo.getName(),vo.getCode(),vo.getDetails());
	}

	public static List<RoleVo> toVoList(List<Role> roleList) {
		List<RoleVo> detailsList = new ArrayList<>();
		if (roleList != null) {
			roleList.stream().forEach(role -> detailsList.add(role.toVo()));
		}
		return detailsList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public List<Authority> getAuthoritys() {
		return authoritys;
	}

	public void setAuthoritys(List<Authority> authoritys) {
		this.authoritys = authoritys;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
