package com.operate.controller.account;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.operate.tools.AuthorityUtil;
import com.operate.tools.CommonUtil;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import com.operate.tools.Groups;
import com.operate.tools.JsonObj;
import com.operate.tools.PageObj;
import com.operate.tools.PropertyFilter.MatchType;
import com.operate.tools.TableVo;
import com.operate.vo.account.RoleVo;
import com.operate.vo.account.UserVo;
import com.operate.pojo.account.User;
import com.operate.service.AccountService;

@RequestMapping("user")
@Controller
public class UserController {
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/")
    @PreAuthorize("hasAnyAuthority('ACCOUNT_USER')")
    public String index(Model model) {
    	Groups groups = new Groups();
    	List<RoleVo> roleList = accountService.findRoleByGroups(groups);
    	model.addAttribute("roleList", roleList);
        return "account/user";
    }

    @RequestMapping("loadUser")
    @ResponseBody
    public TableVo loadUser(@Valid TableVo tableVo, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            tableVo.setAaData(new ArrayList<>());
            tableVo.setiTotalDisplayRecords(0);
            tableVo.setiTotalRecords(0);
            return tableVo;
        }
        int pageSize = tableVo.getiDisplayLength();
        int index = tableVo.getiDisplayStart();
        int currentPage = index / pageSize + 1;
        String params = tableVo.getsSearch();
        int col = tableVo.getiSortCol_0();
        String dir = tableVo.getsSortDir_0();
        String colname = request.getParameter("mDataProp_" + col);
        Groups groups = CommonUtil.filterGroup(params);
        if(CommonUtil.isNull(colname)){
        	colname = "id";
        	dir = "desc";
        }
//        List<Long> ids = new ArrayList<>();
//        ids.add(1L);
//        ids.add(2L);
//        groups.Add("id",ids,MatchType.NOTIN);
//        groups.Add("realName","",MatchType.NOTNULL);
        
//        List<Groups> childGroupsList = new ArrayList<Groups>();
//		Groups groups2 = new Groups();
//		groups2.Add("userName","luke",MatchType.LIKE,MatchType.OR);
//		groups2.Add("realName","test8",MatchType.EQ,MatchType.OR);
//		groups2.setParentRelation(MatchType.AND);
//		childGroupsList.add(groups2);
//		groups.setChildGroupsList(childGroupsList);
        
        groups.setOrderby(colname);
        groups.setOrder(dir);
        PageObj<User> page = new PageObj<User>(pageSize,currentPage);
        accountService.findUserPageByGroups(groups, page);
        Long total = page.getTotalCount();
        tableVo.setAaData(page.getItems());
        tableVo.setiTotalDisplayRecords(total);
        tableVo.setiTotalRecords(total);
        return tableVo;
    }
    
    @RequestMapping(value = "checkMobile")
	@ResponseBody
	public Boolean checkMobile(Long id, String mobile)
	{
		Boolean valid = true;
		UserVo user = accountService.findUserByField("mobile", mobile);
		if (user != null)
		{
			if (user.getId().equals(id)) {//修改时
				valid = true;
			}else{
				valid = false;
			}
		}

		return valid;
	}
    
    
    
    
    @GetMapping("/add")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_USER_ADD')")
	public String addForm(Model model) {
    	Groups groups = new Groups();
    	groups.Add("enable",true);
    	List<RoleVo> roleList = accountService.findRoleByGroups(groups);
    	model.addAttribute("roleList", roleList);
    	
		return "account/user_add";
	}

	@PostMapping("/add")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_USER_ADD')")
	@ResponseBody
	public JsonObj addSubmit(@Valid UserVo userVo, BindingResult result, Model model) {
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			return JsonObj.newErrorJsonObj(errorMsg.toString());
		}
		try {
			String newPassword = passwordEncoder.encode(userVo.getPassword());
			userVo.setPassword(newPassword);
			userVo.setId(null);
			accountService.saveUser(userVo);
		} catch (Exception e) {
			logger.error("新增用户失败",e);
			return JsonObj.newErrorJsonObj("保存失败："+e.getMessage());
		}
		return JsonObj.newSuccessJsonObj("保存成功");
	}
	
	@GetMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_USER_EDIT')")
	public String editForm(Long id,Model model) {
		UserVo userVo = accountService.findUser(id);
		Groups groups = new Groups();
		groups.Add("enable",true);
		List<RoleVo> roles = accountService.findRoleByGroups(groups);
		model.addAttribute("allRoles", roles);
		model.addAttribute("userVo", userVo);
		
		return "account/user_edit";
	}
	
	@PostMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_USER_EDIT')")
	@ResponseBody
	public JsonObj editSubmit(@Valid UserVo userVo, BindingResult result,Model model) {
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			return JsonObj.newErrorJsonObj(errorMsg.toString());
		}
		try {
			accountService.updateUser(userVo);
		} catch (Exception e) {
			logger.error("修改用户失败",e);
			return JsonObj.newErrorJsonObj("保存失败："+e.getMessage());
		}
		return JsonObj.newSuccessJsonObj("保存成功");
	}
	
	@RequestMapping(value = "updatePassword")
	@ResponseBody
	public JsonObj updatePassword(@Valid UserVo userVo, BindingResult result){
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			return JsonObj.newErrorJsonObj(errorMsg.toString());
		}
		try {
			Long userId = userVo.getId();
			if(userId == null){
				userVo = accountService.findUserByField("login", AuthorityUtil.getLoginUsername());
				userId = userVo.getId();
			}
			String newPassword = passwordEncoder.encode(userVo.getPassword());
			
			accountService.updatePassword(userId, newPassword);
			logger.info("修改用户密码："+userId);
			return JsonObj.newSuccessJsonObj("修改成功");
		} catch (Exception e) {
			logger.error("修改用户密码失败",e);
			return JsonObj.newSuccessJsonObj("修改失败");
		}
	}


}