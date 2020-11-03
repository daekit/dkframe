package com.dksys.biz.cmn.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {

	Map<String, String> selectTargetRoom(Map<String, String> param);

	int createChatRoom(Map<String, String> param);

	int createChatRoomJoin(Map<String, String> param);

	List<Map<String, String>> selectChatRoom(Map<String, String> param);

	int insertChatMsg(Map<String, String> message);

	List<Map<String, String>> selectChatMsg(Map<String, String> param);

	List<Map<String, String>> selectChatRoomList(Map<String, String> param);
	
}
