package com.ssafy.guffy.model.mapper;

import java.util.List;

import com.ssafy.guffy.model.model.ChatFriend;
import com.ssafy.guffy.model.model.ChattingRoom;

public interface ChattingRoomMapper {
	ChattingRoom select(String id); 
	int delete(String id); 
	int create(ChattingRoom chattingRoom); 
	int isUsed(String id); 
	int update(ChattingRoom chattingRoom);
	List<ChatFriend> getMyFriends(String id);
	
}
