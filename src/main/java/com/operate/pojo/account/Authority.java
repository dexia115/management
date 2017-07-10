package com.operate.pojo.account;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import com.operate.pojo.BaseEntity;
import com.operate.vo.account.AuthorityVo;

@Entity
@Table(name = "account_authority")
public class Authority extends BaseEntity{
	
	private static final long serialVersionUID = -2824880752071083208L;

	//权限名称
	@Column(length=50)
	private String name;
	
	//权限代码
	@Column(length=50)
	private String code;
	
	//描述
	@Column(length=200)
	private String details;
	
	@Column(length=50)
	private String url;
	
	//1:菜单  2：权限
	private Integer method;
	
	@Column(length=20)
	private String icons;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent_id")
	private Authority parent;
	
	@OrderBy(value = "sort ASC")
	@OneToMany(fetch=FetchType.LAZY,mappedBy="parent")
	private List<Authority> children;
	
	private Integer sort;
	
	public Authority(){
		
	}
	
	public Authority(Long id){
		this.id = id;
	}
	
	public Authority(Long id, String name, String code, String details,String url,Integer sort,Integer method,String icons, Authority parent){
		this.id = id;
		this.name = name;
		this.code = code;
		this.details = details;
		this.url = url;
		this.sort = sort;
		this.method = method;
		this.icons = icons;
		this.parent = parent;
	}
	
	public static Authority fromVo(AuthorityVo authorityVo) {
		Authority parent = null;
		Long parentId = authorityVo.getParentId();
		if (parentId != null) {
			parent = new Authority(parentId);
		}
		return new Authority(authorityVo.getId(), authorityVo.getName(), authorityVo.getCode(),
				authorityVo.getDetails(), authorityVo.getUrl(), authorityVo.getSort(), authorityVo.getMethod(),
				authorityVo.getIcons(), parent);
	}

	public AuthorityVo toVo(){
		Long parentId = null;
		String parentName = null;
		if(parent != null){
			parentId = parent.getId();
			parentName = parent.getName();
		}
		return new AuthorityVo(id, name, code, details, url, sort, method,icons, parentId, parentName);
	}
	
	public static List<Authority> fromVoList(List<AuthorityVo> voList){
		List<Authority> authList = new ArrayList<>();
		if(voList != null){
			voList.stream().forEach(d -> authList.add(fromVo(d)));
		}
		
		return authList;
	}
	
	public static List<AuthorityVo> toVoList(List<Authority> authList) {
	    List<AuthorityVo> detailsList = new ArrayList<>();
	    if (authList != null) {
	    	authList.stream().forEach(auth -> detailsList.add(auth.toVo()));
	    }
	    return detailsList;
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

	public Authority getParent() {
		return parent;
	}

	public void setParent(Authority parent) {
		this.parent = parent;
	}

	public List<Authority> getChildren() {
		return children;
	}

	public void setChildren(List<Authority> children) {
		this.children = children;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getMethod() {
		return method;
	}

	public void setMethod(Integer method) {
		this.method = method;
	}

	public String getIcons() {
		return icons;
	}

	public void setIcons(String icons) {
		this.icons = icons;
	}

}
