package com.ssafy.guffy.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.guffy.model.model.ChatFriend;
import com.ssafy.guffy.model.model.Friend;
import com.ssafy.guffy.model.model.User;

public interface UserMapper {
    User select(String email);
    User login(User user);
    List<User> selectAll();
    int update(User user);
    int create(User user);
    int delete(String email);
    int isUsed(String email);
    List<ChatFriend> friends(String email);
    Friend friend(Integer friend_id);
}
