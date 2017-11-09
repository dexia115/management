package com.operate.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.operate.tools.JsonObj;
import com.operate.vo.account.UserVo;

@RestController
@RequestMapping("/api/v1/inner/variableConfig")
public class VariableConfigApi {
	
	@RequestMapping(value = "findVariableConfigs")
	@ResponseBody
	public JsonObj findVariableConfigs(String code){
		UserVo userVo = new UserVo();
		userVo.setUserName("lxd");
		userVo.setMobile("1234556");
		userVo.setIdcard("50011233");
		return JsonObj.newSuccessJsonObj("",userVo);
	}

}
