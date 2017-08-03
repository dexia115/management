package com.operate.service.attachment;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.operate.tools.JsonObj;
import com.operate.vo.AttachmentVo;


@FeignClient(name="attachment",fallback = AttachmentServiceFallback.class)
public interface AttachmentService {
	
	@RequestMapping(value ="/api/v1/inner/file/upload",method = RequestMethod.POST,produces =  MediaType.APPLICATION_JSON_UTF8_VALUE,
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	JsonObj uploadFile(AttachmentVo attachmentVo);

}
