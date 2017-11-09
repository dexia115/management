package com.operate.config.message;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	
	public void sender(){
		String message = "hhhh world";
		amqpTemplate.convertAndSend("elastic.message",message);
	}

}
