package com.operate.config.message;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import com.operate.vo.account.UserVo;

@EnableBinding(value={Sink.class})
public class SinkReceiver {
	
	@StreamListener(Sink.INPUT)
	public void receive(Message<UserVo> message){
		UserVo userVo = message.getPayload();
		System.out.println(userVo);
		System.out.println(userVo.getUserName()+"-"+userVo.getRealName());
	}

}
