package com.dksys.biz.cmn;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import com.dksys.biz.util.MessageUtils;

@Controller
public class ChatController {

	@Autowired
	MessageUtils messageUtils;
    
    // 1:1채팅 보내기 서버
    @MessageMapping("/chat/send/{roomId}")
    @SendTo("/topic/receive/{roomId}")
    public Map<String, String> sendMessage(@RequestParam Map<String, String> message) {
    	return message;
    }
    
}