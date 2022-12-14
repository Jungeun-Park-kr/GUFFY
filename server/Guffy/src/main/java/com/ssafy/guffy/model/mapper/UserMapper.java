package com.ssafy.guffy.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.guffy.model.model.ChatFriend;
import com.ssafy.guffy.model.model.Friend;
import com.ssafy.guffy.model.model.MeAndFriend;
import com.ssafy.guffy.model.model.User;

public interface UserMapper {
    User select(String email);
    User selectById(int id);
    User login(User user);
    List<User> selectAll();
    int update(User user);
    int create(User user);
    int delete(String email);
    int isUsed(String email);
    int isUsedName(String name);
    List<ChatFriend> friends(String email);
    Friend friend(MeAndFriend meAndFriend);
}
