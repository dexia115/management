package com.operate.config;

import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.operate.enums.CustomEnumModule;
import com.operate.enums.Java8Mapper;
import com.operate.enums.UniversalEnumConverterFactory;


@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {
	
	private static final String ENUM_PROP_NAME = "value";
	
	
	@Override
	public void addFormatters(FormatterRegistry registry) {
		UniversalEnumConverterFactory enumConverterFactory = new UniversalEnumConverterFactory();
		registry.addConverterFactory(enumConverterFactory);
	}
	
	@Bean
    public ObjectMapper objectMapper() {
        Java8Mapper java8Mapper = new Java8Mapper();
        // 确保反序列化时自动加上TimeZone信息
        java8Mapper.setTimeZone(TimeZone.getDefault());
        java8Mapper.registerModule(new CustomEnumModule(ENUM_PROP_NAME));

        return java8Mapper;
    }
}