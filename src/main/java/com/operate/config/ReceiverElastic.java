package com.operate.config;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;

@Component
@RabbitListener(queues = "elastic.message")
public class ReceiverElastic {
	
	@RabbitHandler
    public void process(String message) {
//		message = StringEscapeUtils.unescapeJson(message);  //反转义
//		Gson gson = new Gson();
//		MessageBody messageBody = gson.fromJson(message, MessageBody.class);
//        System.out.println("Receiver  : " + messageBody);
		System.out.println("Receiver  : " + message);
    }

}
