package com.operate.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.operate.pojo.account.Authority;
import com.operate.pojo.account.Role;
import com.operate.pojo.account.User;
import com.operate.repository.account.AuthorityRepository;
import com.operate.repository.account.RoleRepository;
import com.operate.repository.account.UserRepository;
import com.operate.service.AccountService;
import com.operate.tools.Groups;
import com.operate.tools.PageObj;
import com.operate.vo.account.AuthorityVo;
import com.operate.vo.account.RoleVo;
import com.operate.vo.account.UserVo;

@Transactional(readOnly=true)
@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private AuthorityRepository authorityRepository;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public PageObj findUserPageByGroups(Groups groups, PageObj page) {
		userRepository.findEntityPageByGroups(groups, page);
		page.setItems(User.toVoList(page.getItems()));
		
		return page;
	}
	
	@Override
	public UserVo findUserByField(String propertyName, Object value){
		Groups groups = new Groups();
		groups.Add("enable",true);
		groups.Add(propertyName,value);
		List<User> users = userRepository.findEntityByGroups(groups);
		if(!users.isEmpty()){
			User user = users.get(0);
			return user.toVo();
		}
		return null;
	}
	
	@Cacheable(value="user")
	@Override
	public UserVo findUser(Long id){
		User user = userRepository.find(id);
		return user.toVo();
	}

	@Transactional
	@Override
	public void saveUser(UserVo userVo) throws Exception {
		User user = User.fromVo(userVo);
		userRepository.save(user);		
	}
	
	@Transactional
	@Override
	public void updateUser(UserVo userVo) throws Exception{
		User user = User.fromVo(userVo);
		User temp = userRepository.find(userVo.getId());
		temp.setMobile(user.getMobile());
		temp.setEnable(user.getEnable());
		temp.setRealName(user.getRealName());
		temp.setRoles(user.getRoles());
		userRepository.save(temp);
	}
	
	@Transactional
	@Override
	public void updatePassword(Long id, String password) throws Exception{
		User user = userRepository.find(id);
		user.setPassword(password);
		userRepository.save(user);
	}
	
	
	
	@Override
	public PageObj findRolePageByGroups(Groups groups, PageObj page) {
		roleRepository.findEntityPageByGroups(groups, page);
		page.setItems(Role.toVoList(page.getItems()));
//		String sql = "select id,code,name from account_role";
//		roleRepository.findPageByGroups(groups, page, sql, RoleVo.class);
		
		return page;
	}
	@Override
	public List<RoleVo> findRoleByGroups(Groups groups) {
		List<Role> roleList = roleRepository.findEntityByGroups(groups);
		return Role.toVoList(roleList);
	}
	
	@Override
	public RoleVo findRole(Long id) {
		Role role = roleRepository.find(id);
		return role.toVo();
	}

	@Transactional
	@Override
	public void saveRole(RoleVo roleVo) throws Exception {
		Role role = Role.fromVo(roleVo);
		roleRepository.save(role);
	}
	
	@Transactional
	@Override
	public void updateRole(RoleVo roleVo) throws Exception {
		Role role = roleRepository.find(roleVo.getId());
		role.setName(roleVo.getName());
		role.setCode(roleVo.getCode());
		role.setEnable(roleVo.getEnable());
		List<Long> authIds = roleVo.getAuthorityIds();
		if(authIds != null && !authIds.isEmpty()){
			List<Authority> authoritys = new ArrayList<>();
			for(Long authId : authIds){
				authoritys.add(new Authority(authId));
			}
			role.setAuthoritys(authoritys);
		}
		
		roleRepository.save(role);
	}
	
	

	@Override
	public PageObj findAuthorityPageByGroups(Groups groups, PageObj page) {
		authorityRepository.findEntityPageByGroups(groups, page);
		page.setItems(Authority.toVoList(page.getItems()));
		
		return page;
	}

	@Override
	public List<AuthorityVo> findAuthorityByGroups(Groups groups) {
		List<Authority> authoritys = authorityRepository.findEntityByGroups(groups);
		return Authority.toVoList(authoritys);
	}

	@Transactional
	@Override
	public void saveAuthority(AuthorityVo authorityVo) throws Exception {
		Authority authority = Authority.fromVo(authorityVo);
		authorityRepository.save(authority);
	}

	@Transactional
	@Override
	public void updateAuthority(AuthorityVo authorityVo) throws Exception {
		Authority authority = authorityRepository.find(authorityVo.getId());
		authority.setName(authorityVo.getName());
		authority.setCode(authorityVo.getCode());
		authority.setMethod(authorityVo.getMethod());
		authority.setSort(authorityVo.getSort());
		authority.setUrl(authorityVo.getUrl());
		authority.setIcons(authorityVo.getIcons());
		Long parentId = authorityVo.getParentId();
		if(parentId != null){
			authority.setParent(new Authority(parentId));
		}
		authorityRepository.save(authority);
	}

	@Transactional
	@Override
	public void deleteAuthority(Long id) throws Exception {
		authorityRepository.delete(id);
	}

	

	

}
