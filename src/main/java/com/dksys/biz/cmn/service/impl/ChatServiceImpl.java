package com.dksys.biz.cmn.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.cmn.mapper.ChatMapper;
import com.dksys.biz.cmn.service.ChatService;

@Service
public class ChatServiceImpl implements ChatService {

	@Autowired
	ChatMapper chatMapper;
	
	@Override
	public Map<String, String> checkChatRoom(Map<String, String> param) {
		Map<String, String> result = chatMapper.selectTargetRoom(param);
		//연결된 채팅방이 없을 경우
		if(result == null) {
			param.put("roomId", UUID.randomUUID().toString().toUpperCase());
			chatMapper.createChatRoom(param);
			param.put("joinId", UUID.randomUUID().toString().toUpperCase());
			chatMapper.createChatRoomJoin(param);
			param.put("joinId", UUID.randomUUID().toString().toUpperCase());
			param.put("userId", param.get("targetId"));
			chatMapper.createChatRoomJoin(param);
			return param;
		}
		return result;
	}

	@Override
	public List<Map<String, String>> selectChatRoom(Map<String, String> param) {
		return chatMapper.selectChatRoom(param);
	}

	@Override
	public int insertChatMsg(Map<String, String> message) {
		message.put("messageId", UUID.randomUUID().toString().toUpperCase());
		return chatMapper.insertChatMsg(message);
	}

	@Override
	public List<Map<String, String>> selectChatMsg(Map<String, String> param) {
		return chatMapper.selectChatMsg(param);
	}

	@Override
	public List<Map<String, String>> selectChatRoomList(Map<String, String> param) {
		return chatMapper.selectChatRoomList(param);
	}

}