package com.operate;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.operate.repository.BaseRepositoryFactoryBean;

@EnableJpaRepositories(basePackages = { "com.operate.repository" }, repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class// 指定自己的工厂类
)
@SpringBootApplication
@EnableCaching
@EnableEurekaClient
@EnableFeignClients(basePackages={"com.operate.service.attachment"})
@EnableCircuitBreaker
@EnableRabbit
public class ManagementApp extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ManagementApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ManagementApp.class, args);
//		new SpringApplicationBuilder(ManagementApp.class).web(true).run(args);
	}
}