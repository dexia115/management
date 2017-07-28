package com.operate.controller.account;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.operate.pojo.account.Role;
import com.operate.service.AccountService;
import com.operate.tools.CommonUtil;
import com.operate.tools.Groups;
import com.operate.tools.JsonObj;
import com.operate.tools.PageObj;
import com.operate.tools.TableVo;
import com.operate.vo.account.RoleVo;

@RequestMapping("role")
@Controller
public class RoleController {
	
	@Autowired
	private AccountService accountService;
	
	private static Logger logger = LoggerFactory.getLogger(RoleController.class);
	
	@GetMapping("/")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_ROLE')")
	public String index() {
		
		return "account/role";
	}
	
	@RequestMapping(value = "loadRole")
	@ResponseBody
	public TableVo loadRole(@Valid TableVo tableVo, BindingResult result, HttpServletRequest request){
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
		groups.setOrderby(colname);
		groups.setOrder(dir);
		PageObj<Role> page = new PageObj<Role>(pageSize,currentPage);
		accountService.findRolePageByGroups(groups, page);
		long total = page.getTotalCount();
		tableVo.setAaData(page.getItems());
		tableVo.setiTotalDisplayRecords(total);
		tableVo.setiTotalRecords(total);
		return tableVo;
	}
	
	@GetMapping("/add")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_ROLE_ADD')")
	public String addForm() {
		return "account/role_add";
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_ROLE_ADD')")
	@ResponseBody
	public JsonObj addSubmit(@Valid RoleVo roleVo, BindingResult result){
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			return JsonObj.newErrorJsonObj(errorMsg.toString());
		}
		try {
			accountService.saveRole(roleVo);
		} catch (Exception e) {
			logger.error("添加角色失败",e);
			return JsonObj.newErrorJsonObj("保存失败："+e.getMessage());
		}
		return JsonObj.newSuccessJsonObj("保存成功");
	}
	
	
	@GetMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_ROLE_EDIT')")
	public String editForm(Long id,Model model) {
		RoleVo roleVo = accountService.findRole(id);
		model.addAttribute("roleVo", roleVo);
		
		return "account/role_edit";
	}
	
	@PostMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ACCOUNT_ROLE_EDIT')")
	@ResponseBody
	public JsonObj editSubmit(@Valid RoleVo roleVo, BindingResult result){
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			return JsonObj.newErrorJsonObj(errorMsg.toString());
		}
		
		try {
			accountService.updateRole(roleVo);
		} catch (Exception e) {
			logger.error("编辑角色失败",e);
			return JsonObj.newErrorJsonObj("保存失败："+e.getMessage());
		}
		
		return JsonObj.newSuccessJsonObj("保存成功");
	}

}
