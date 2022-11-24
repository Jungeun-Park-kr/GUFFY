package com.ssafy.guffy.model.service;

import java.util.List;

import com.ssafy.guffy.model.model.ChatFriend;
import com.ssafy.guffy.model.model.ChattingRoom;

public interface ChattingRoomService{
	
	// 채팅방 전체 조회
	public ChattingRoom select(String id);
	
	// 채팅방 삭제
	public int delete(String id);
	
	// 채팅방 생성
	public int create(ChattingRoom chattingRoom);
	
	// 채팅방 이름 중복 검사
	public int isUsed(String id);
	
	// 채팅방 내용 업데이트
	public int update(ChattingRoom chattingRoom);
	
	// 나와 연결된 친구의 id와 그 채팅방의 id
	public List<ChatFriend> getMyFriends(String id);
}
