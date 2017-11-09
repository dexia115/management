package com.operate.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.operate.config.message.MessageSender;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MessageTest.class)
public class MessageTest {
	
	@Autowired
	private MessageSender messageSender;
	
	@Test
	public void test(){
		messageSender.sender();
	}

}
