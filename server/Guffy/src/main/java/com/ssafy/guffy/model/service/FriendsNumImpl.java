package com.ssafy.guffy.model.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.guffy.model.mapper.FriendsNumMapper;
import com.ssafy.guffy.model.model.FriendsNum;

@Service
public class FriendsNumImpl implements FriendsNumService{
	
	@Autowired
	private FriendsNumMapper mapper;
	
	@Override
	public FriendsNum select(String user_id) {
		return mapper.select(user_id);
	}

	@Override
	public List<FriendsNum> findNewFriends() {
		return mapper.findNewFriends();
	}

	@Override
	public int update(FriendsNum friendsNum) {
		return mapper.update(friendsNum);
	}

	@Override
	public int create(String user_id) {
		return mapper.create(user_id);
	}

}
