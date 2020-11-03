package com.dksys.biz.cmn.service;

import java.util.List;
import java.util.Map;

public interface ChatService {

	Map<String, String> checkChatRoom(Map<String, String> param);

	List<Map<String, String>> selectChatRoom(Map<String, String> param);

	int insertChatMsg(Map<String, String> message);

	List<Map<String, String>> selectChatMsg(Map<String, String> param);

	List<Map<String, String>> selectChatRoomList(Map<String, String> param);

}