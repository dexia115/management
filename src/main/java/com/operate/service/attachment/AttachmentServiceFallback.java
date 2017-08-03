package com.operate.service.attachment;

import org.springframework.stereotype.Component;
import com.operate.tools.JsonObj;
import com.operate.vo.AttachmentVo;

@Component
public class AttachmentServiceFallback implements AttachmentService {


	@Override
	public JsonObj uploadFile(AttachmentVo attachmentVo) {
		return JsonObj.newErrorJsonObj("附件服务忙，请稍后再试");
	}

}
