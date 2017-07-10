package com.operate.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.operate.pojo.account.Authority;
import com.operate.pojo.account.Role;
import com.operate.service.AccountService;
import com.operate.vo.account.AuthorityVo;
import com.operate.vo.account.RoleVo;
import com.operate.vo.account.UserVo;

@Component
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AccountService accountService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<GrantedAuthority> authorities = null;
		UserVo userVo = accountService.findUserByField("userName", username);
		String password = "";
		if(userVo != null){
			password = userVo.getPassword();
			List<RoleVo> roles = userVo.getRoleVoList();
			authorities = getGrantedAuthorities(roles);
		}else{
			throw new UsernameNotFoundException("Username not found: " + username);
		}
		return new User(username, password, true, true, true, true, authorities);
	}
	
	
	public static List<GrantedAuthority> getGrantedAuthorities(List<RoleVo> roles) {
	    List<GrantedAuthority> authorities = new ArrayList<>();
	    roles.forEach(r -> {
	    	List<AuthorityVo> auths = r.getAuthorityVos();
	    	for(AuthorityVo auth : auths){
	    		if(auth.getParentId() != null){
	    			authorities.add(new SimpleGrantedAuthority(auth.getCode()));
	    		}
	    	}
//	    	authorities.add(new SimpleGrantedAuthority(r.getName()));
	    });
	    return authorities;
	}

}
