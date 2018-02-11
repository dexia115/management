package com.operate.controller.account;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
//import com.operate.config.message.SinkSender;
import com.operate.pojo.account.Authority;
import com.operate.service.AccountService;
import com.operate.tools.CommonUtil;
import com.operate.tools.Constants;
import com.operate.tools.Groups;
import com.operate.tools.JsonObj;
import com.operate.tools.PageObj;
import com.operate.tools.PageVo;
import com.operate.tools.PropertyFilter.MatchType;
import com.operate.tools.ZtreeVo;
import com.operate.vo.account.AuthorityVo;
import com.operate.vo.account.UserVo;

@RequestMapping("authority")
@Controller
//@EnableBinding({ SinkSender.class })
public class AuthorityController {
	
	@Autowired
	private AccountService accountService;
	
//	@Autowired
//	private SinkSender sinkSender;
	
	private static Logger logger = LoggerFactory.getLogger(AuthorityController.class);
	
	@GetMapping("/")
	public String index() {
		UserVo userVo = new UserVo();
		userVo.setRealName("lxdd");
		userVo.setUserName("12345");
		
//		Message<UserVo> message = MessageBuilder.withPayload(userVo).build();
//		sinkSender.output().send(message);
		
		return "account/authority";
	}
	
	
	@RequestMapping(value = "loadAuthority")
	@ResponseBody
	public PageVo loadAuthority(@Valid PageVo pageVo,Long id, String params){
		Groups groups = new Groups();
		PageObj<Authority> page = new PageObj<Authority>(pageVo.getPageSize(),pageVo.getPageNo());
		if(!CommonUtil.isNull(params)){
			groups = CommonUtil.filterGroup(params);
		}
		if(id != null){
			groups.Add("parent.id",id);
		}else{
			groups.Add("parent.id","",MatchType.NULL);
		}
//		groups.Add("method",Constants.AUTHORITY_MENU);
		if(!CommonUtil.isNull(pageVo.getSortname())){
			groups.setOrder(pageVo.getOrder());
			groups.setOrderby(pageVo.getSortname());
		}else{
			groups.setOrder("asc");
			groups.setOrderby("sort");
		}
		accountService.findAuthorityPageByGroups(groups, page);
		pageVo.setItems(page.getItems());
		pageVo.setTotalCount(page.getTotalCount());
		pageVo.setTotalPageCount(page.getTotalPageCount());
		
		return pageVo;
	}
	
	@RequestMapping(value = "findAuthorityForTree")
	@ResponseBody
	public List<ZtreeVo> findAuthorityForTree(Long id)
	{
		List<ZtreeVo> ztreeVos = new ArrayList<ZtreeVo>();
		Groups groups = new Groups();
		groups.Add("enable",true);
		if(id == null || id == 0)
		{
			groups.Add("parent","",MatchType.NULL);
		}
		else
		{
			groups.Add("parent.id", id);
		}
		groups.setOrder("asc");
		groups.setOrderby("sort");
		List<AuthorityVo> authorityVos = accountService.findAuthorityByGroups(groups);
		for(AuthorityVo authorityVo : authorityVos)
		{
			ZtreeVo ztree = new ZtreeVo();
			ztree.setId(authorityVo.getId());
			ztree.setName(authorityVo.getName());
			if(authorityVo.getParentId() == null){
				ztree.setIsParent(true);
			}else{
				ztree.setIsParent(false);
			}
			ztreeVos.add(ztree);
		}
		
		return ztreeVos;
	}
	
	
	@RequestMapping(value = "findAuthorityByTree")
	@ResponseBody
	public List<ZtreeVo> findAuthorityByTree(Long id)
	{
		List<ZtreeVo> ztreeVos = new ArrayList<ZtreeVo>();
		Groups groups = new Groups();
		groups.Add("enable",true);
		if(id == null || id == 0)
		{
			groups.Add("parent","",MatchType.NULL);
		}
		else
		{
			groups.Add("parent.id", id);
		}
		groups.setOrder("asc");
		groups.setOrderby("sort");
		List<AuthorityVo> authorityVos = accountService.findAuthorityByGroups(groups);
		for(AuthorityVo authorityVo : authorityVos)
		{
			ZtreeVo ztree = new ZtreeVo();
			ztree.setId(authorityVo.getId());
			ztree.setName(authorityVo.getName());
			if(authorityVo.getMethod() == Constants.AUTHORITY_MENU){
				ztree.setIsParent(true);
			}else{
				ztree.setIsParent(false);
			}
			ztreeVos.add(ztree);
		}
		
		return ztreeVos;
	}
	
	@RequestMapping(value = "findAuthorityAllTree")
	@ResponseBody
	public List<ZtreeVo> findAuthorityAllTree(Long id)
	{
		List<ZtreeVo> ztreeVos = new ArrayList<ZtreeVo>();
		Groups groups = new Groups();
		groups.Add("enable",true);
		if(id == null || id == 0)
		{
			groups.Add("parent","",MatchType.NULL);
		}
		else
		{
			groups.Add("parent.id", id);
		}
		groups.setOrder("asc");
		groups.setOrderby("sort");
		List<AuthorityVo> authorityVos = accountService.findAuthorityByGroups(groups);
		for(AuthorityVo authorityVo : authorityVos)
		{
			ZtreeVo ztree = new ZtreeVo();
			ztree.setId(authorityVo.getId());
			ztree.setName(authorityVo.getName());
			if(authorityVo.getMethod() == Constants.AUTHORITY_MENU){
				ztree.setIsParent(true);
			}else{
				ztree.setIsParent(false);
			}
			List<ZtreeVo> children = findAuthorityAllTree(ztree.getId());
			ztree.setChildren(children);
			ztreeVos.add(ztree);
		}
		
		return ztreeVos;
	}
	
	
	
	@PostMapping("add")
	@ResponseBody
	public JsonObj addSubmit(@Valid AuthorityVo authorityVo, BindingResult result){
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			return JsonObj.newErrorJsonObj(errorMsg.toString());
		}
		
		try {
			authorityVo.setId(null);
			accountService.saveAuthority(authorityVo);
		} catch (Exception e) {
			logger.error("添加权限失败",e);
			return JsonObj.newErrorJsonObj("保存失败："+e.getMessage());
		}
		return JsonObj.newSuccessJsonObj("保存成功");
	}
	
	@PostMapping("edit")
	@ResponseBody
	public JsonObj editSubmit(@Valid AuthorityVo authorityVo, BindingResult result){
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			return JsonObj.newErrorJsonObj(errorMsg.toString());
		}
		
		try {
			accountService.updateAuthority(authorityVo);
		} catch (Exception e) {
			logger.error("修改权限失败",e);
			return JsonObj.newErrorJsonObj("保存失败："+e.getMessage());
		}
		return JsonObj.newSuccessJsonObj("保存成功");
	}
	
	@PostMapping("delete")
	@ResponseBody
	public JsonObj delete(Long id){
		try {
			accountService.deleteAuthority(id);
		} catch (Exception e) {
			logger.error("删除权限失败",e);
			return JsonObj.newErrorJsonObj("删除失败："+e.getMessage());
		}
		return JsonObj.newSuccessJsonObj("删除成功");
	}
	
	

}
