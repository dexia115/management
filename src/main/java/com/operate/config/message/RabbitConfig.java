package com.operate.config.message;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	
	@Bean
	public Queue lasticQueue(){
		return new Queue("elastic.message");
	}

}
