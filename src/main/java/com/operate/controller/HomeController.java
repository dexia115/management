package com.operate.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.operate.service.AccountService;
import com.operate.tools.AuthorityUtil;
import com.operate.tools.Constants;
import com.operate.vo.account.AuthorityVo;
import com.operate.vo.account.RoleVo;
import com.operate.vo.account.UserVo;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	private AccountService accountService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		String username = AuthorityUtil.getLoginUsername();
		UserVo userVo = accountService.findUserByField("userName", username);
		List<AuthorityVo> authorities = new ArrayList<AuthorityVo>();
		List<RoleVo> roles = userVo.getRoleVoList();
		Map<Long,AuthorityVo> authMap = new HashMap<>();
		roles.forEach(r -> {
	    	List<AuthorityVo> auths = r.getAuthorityVos();
	    	for(AuthorityVo auth : auths){
	    		if(auth.getMethod() == Constants.AUTHORITY_MENU){
	    			if(auth.getParentId() == null){
	    				authorities.add(auth);
	    				authMap.put(auth.getId(), auth);
	    			}else{
	    				Long pid = auth.getParentId();
	    				AuthorityVo authVo = authMap.get(pid);
	    				if(authVo != null){
	    					authVo.getChildren().add(auth);
	    				}
	    			}
	    		}
	    	}
	    });
		model.addAttribute("authorities", authorities);
		
		return "home";
	}

	@GetMapping("tab")
	public String tab() {
		return "tab";
	}

	@GetMapping("welcome")
	public String welcome() {
		return "index";
	}
	
	@RequestMapping(value = "login")
	public String login(){
		return "login";
	}

}
