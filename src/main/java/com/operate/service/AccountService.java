package com.operate.service;

import java.util.List;
import com.operate.tools.Groups;
import com.operate.tools.PageObj;
import com.operate.vo.account.AuthorityVo;
import com.operate.vo.account.RoleVo;
import com.operate.vo.account.UserVo;

public interface AccountService {
	
	PageObj findUserPageByGroups(Groups groups, PageObj page);
	
	UserVo findUserByField(String propertyName, Object value);
	
	UserVo findUser(Long id);
	
	void saveUser(UserVo userVo) throws Exception;
	void updateUser(UserVo userVo) throws Exception;
	void updatePassword(Long id, String password) throws Exception;
	
	PageObj findRolePageByGroups(Groups groups, PageObj page);
	List<RoleVo> findRoleByGroups(Groups groups);
	RoleVo findRole(Long id);
	void saveRole(RoleVo roleVo) throws Exception;
	void updateRole(RoleVo roleVo) throws Exception;
	
	
	PageObj findAuthorityPageByGroups(Groups groups, PageObj page);
	List<AuthorityVo> findAuthorityByGroups(Groups groups);
	void saveAuthority(AuthorityVo authorityVo) throws Exception;
	void updateAuthority(AuthorityVo authorityVo) throws Exception;
	void deleteAuthority(Long id) throws Exception;

}
