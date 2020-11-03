package com.dksys.biz.cmn;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.service.ChatService;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/chat")
public class ChatController {

	@Autowired
	MessageUtils messageUtils;
	
	@Autowired
	ChatService chatService;

    @PostMapping("/checkChatRoom")
    public String checkChatRoom(@RequestBody Map<String, String> param, ModelMap model) {
    	Map<String, String> result = chatService.checkChatRoom(param);
    	model.addAttribute("result", result);
    	return "jsonView";
    }

    @PostMapping("/selectChatRoom")
    public String selectChatRoom(@RequestBody Map<String, String> param, ModelMap model) {
    	List<Map<String, String>> result = chatService.selectChatRoom(param);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
    @PostMapping("/selectChatMsg")
    public String selectChatMsg(@RequestBody Map<String, String> param, ModelMap model) {
    	List<Map<String, String>> result = chatService.selectChatMsg(param);
    	model.addAttribute("result", result);
    	return "jsonView";
    }

    @PostMapping("/selectChatRoomList")
    public String selectChatRoomList(@RequestBody Map<String, String> param, ModelMap model) {
    	List<Map<String, String>> result = chatService.selectChatRoomList(param);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
}