package com.ssafy.guffy.model.mapper;


import java.util.List;

import com.ssafy.guffy.model.model.FriendsNum;

public interface FriendsNumMapper {
	FriendsNum select(String user_id);
	List<FriendsNum> findNewFriends();
	int update(FriendsNum friendsNum);
	int create(String user_id);
	String updateAll();
	List<FriendsNum> getAllUser();
}
