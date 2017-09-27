package com.operate.common;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.operate.ManagementApp;
import com.operate.service.AccountService;
import com.operate.vo.account.UserVo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ManagementApp.class)
@WebAppConfiguration
public class AccountTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test
	@Rollback(false)
	public void testUser(){
		UserVo user = accountService.findUser(1L);
		System.out.println(user.getUserName());
		
		UserVo userVo = new UserVo();
		userVo.setUserName("wang123");
		userVo.setPassword("123456");
		userVo.setMobile("13545677789");
		List<Long> roles = new ArrayList<>();
		roles.add(1L);
		userVo.setRoles(roles);
		String newPassword = passwordEncoder.encode(userVo.getPassword());
		userVo.setPassword(newPassword);
		userVo.setId(null);
		try {
			accountService.saveUser(userVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
