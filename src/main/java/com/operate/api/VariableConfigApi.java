package com.operate.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.operate.enums.CheckState;
import com.operate.tools.JsonObj;
import com.operate.vo.StoreVo;
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
	
	@RequestMapping(value = "findVos")
	@ResponseBody
	public JsonObj findVos(@RequestBody StoreVo storeVo){
		System.out.println(storeVo);
		return JsonObj.newSuccessJsonObj("",storeVo);
	}
	
	@PostMapping("/person")
    public Object convertrStringToPerson(@RequestParam("checkState") CheckState checkState) {
        System.out.println(checkState);
		return checkState;
    }

}
