package com.operate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private MyUserDetailsService myUserDetailsService;
	@Autowired
	private MyAuthenticationProvider authenticationProvider;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4);
	}

	@Configuration
	@Order(1)
	public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable().antMatcher("/api/**").authorizeRequests().antMatchers("/api/v1/inner/**").permitAll();
		}
	}

	@Configuration
	@Order(2)
	public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/js/**", "/css/**", "/images/**", "/**/favicon.ico");
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
			auth.eraseCredentials(false);
			// auth.authenticationProvider(authenticationProvider);
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().antMatchers("/login").permitAll().antMatchers("/bus/refresh").permitAll()
					// .antMatchers("/**").hasRole("ADMIN_USER")
					// .antMatchers("/**").hasAnyAuthority("ADMIN_USER")
					.anyRequest().authenticated().and().headers().frameOptions().sameOrigin().and().formLogin()
					.loginPage("/login").permitAll().defaultSuccessUrl("/", true).and().logout().permitAll();
		}
	}

}
