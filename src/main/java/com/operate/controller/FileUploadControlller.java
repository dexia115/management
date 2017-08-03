package com.operate.controller;

import java.io.IOException;
import java.util.Iterator;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.google.gson.Gson;
import com.operate.service.attachment.AttachmentService;
import com.operate.tools.CommonUtil;
import com.operate.tools.JsonObj;
import com.operate.vo.AttachmentVo;
import com.operate.vo.KindeditorVo;

@RequestMapping("/files")
@Controller
public class FileUploadControlller {
	
	@Resource
	private AttachmentService attachmentService;
	
	@PostMapping("/uploadFile")
	@ResponseBody
	public JsonObj uploadFile(HttpServletRequest request) throws Exception{
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		JsonObj jsonObj = null;
		
		Iterator<String> it = multipartRequest.getFileNames();
		while (it.hasNext())
		{
			String fileName = it.next();
			MultipartFile uploadify = multipartRequest.getFile(fileName);
			long size = uploadify.getSize();
			if (size > 2 * 1024 * 1024)// 只能上传2M以内的文件
			{
				return JsonObj.newErrorJsonObj("只能上传2M以内的文件！");
			}
			String originalFilename = uploadify.getOriginalFilename();
			byte[] data = uploadify.getBytes();
			String filePath = "/"+username + "/";
			AttachmentVo attachmentVo = new AttachmentVo();
			attachmentVo.setFileName(originalFilename);
			attachmentVo.setFilePath(filePath);
			attachmentVo.setData(data);
			jsonObj = attachmentService.uploadFile(attachmentVo);
		}
		
		return jsonObj;
	}
	
	@PostMapping("/uploadBase64File")
	@ResponseBody
	public JsonObj uploadBase64File(String base64Url,String fileName){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if(base64Url == null){
			return JsonObj.newErrorJsonObj("上传失败：上传图片文件偏大，请更换小一点的图片！");
		}
		byte[] data = CommonUtil.writeToFile(base64Url);
		String filePath = "/"+username + "/";
		AttachmentVo attachmentVo = new AttachmentVo();
		attachmentVo.setFileName(fileName);
		attachmentVo.setFilePath(filePath);
		attachmentVo.setData(data);
		JsonObj jsonObj = attachmentService.uploadFile(attachmentVo);
		
		return jsonObj;
	}
	
	@RequestMapping(value = "uploadKindeditor")
	@ResponseBody
	public void uploadKindeditor(HttpServletRequest request, HttpServletResponse response){
		KindeditorVo kindeditorVo = new KindeditorVo();
		
		Gson gson = new Gson();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Iterator<String> it = multipartRequest.getFileNames();
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		try {
			while (it.hasNext())
			{
				String fileName = it.next();
				MultipartFile uploadFile = multipartRequest.getFile(fileName);
				long size = uploadFile.getSize();
				if (size > 2 * 1024 * 1024)// 只能上传2M以内的文件
				{
					kindeditorVo.setError(1);
					kindeditorVo.setMessage("只能上传2M以内的文件！");
					response.getWriter().write(gson.toJson(kindeditorVo));
					return;
				}
				String filename = uploadFile.getOriginalFilename();
				// 检查扩展名
				String fileExt = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
				if (!"gif".equals(fileExt) && !"jpg".equals(fileExt) && !"jpeg".equals(fileExt)
				        && !"png".equals(fileExt) && !"bmp".equals(fileExt)) {
					kindeditorVo.setError(1);
					kindeditorVo.setMessage("图片格式不正确！");
					response.getWriter().write(gson.toJson(kindeditorVo));
					return;
				}
				MultipartFile uploadify = multipartRequest.getFile(fileName);
				
				String originalFilename = uploadify.getOriginalFilename();
				byte[] data = uploadify.getBytes();
				String filePath = "/"+username+"/editor/";
				AttachmentVo attachmentVo = new AttachmentVo();
				attachmentVo.setFileName(originalFilename);
				attachmentVo.setFilePath(filePath);
				attachmentVo.setData(data);
				JsonObj jsonObj = attachmentService.uploadFile(attachmentVo);
				if(jsonObj.isSuccess()){
					String url = "";
					kindeditorVo.setUrl(url+jsonObj.getResultObject());
					kindeditorVo.setError(0);
				}else{
					kindeditorVo.setError(1);
					kindeditorVo.setMessage("上传出错：" + jsonObj.getMessage());
				}
				response.getWriter().write(gson.toJson(kindeditorVo));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
