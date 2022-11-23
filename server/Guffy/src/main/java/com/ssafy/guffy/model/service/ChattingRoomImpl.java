package com.ssafy.guffy.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.guffy.model.mapper.ChattingRoomMapper;
import com.ssafy.guffy.model.model.ChatFriend;
import com.ssafy.guffy.model.model.ChattingRoom;

@Service
public class ChattingRoomImpl implements ChattingRoomService{

	@Autowired
	private ChattingRoomMapper mapper;
	
	@Override
	public ChattingRoom select(String id) {
		return mapper.select(id);
	}

	@Override
	public int delete(String id) {
		return mapper.delete(id);
	}

	@Override
	public int create(ChattingRoom chattingRoom) {
		return mapper.create(chattingRoom);
	}

	@Override
	public int isUsed(String id) {
		return mapper.isUsed(id);
	}

	@Override
	public int update(ChattingRoom chattingRoom) {
		return mapper.update(chattingRoom);
	}

	@Override
	public List<ChatFriend> getMyFriends(String id) {
		return mapper.getMyFriends(id);
	}

}
