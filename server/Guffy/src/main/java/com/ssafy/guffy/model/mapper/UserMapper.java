package com.ssafy.guffy.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.guffy.model.model.User;

public interface UserMapper {
    User select(String email);
    List<User> selectAll();
    int update(User user);
    int create(User user);
    int delete(String email);
    int isUsed(String email);
}
