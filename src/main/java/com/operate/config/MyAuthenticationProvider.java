package com.operate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider{
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		System.out.println("getAuthorities: "+authentication.getAuthorities());
		
		UserDetails userDetails = myUserDetailsService.loadUserByUsername(authentication.getName());
		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                userDetails, authentication.getCredentials(),userDetails.getAuthorities());
		
        return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
