package com.ssafy.guffy.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.guffy.model.mapper.UserMapper;
import com.ssafy.guffy.model.model.User;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper mapper;

    @Override
    public User select(String email) {
        return mapper.select(email);
    }

    @Override
    public List<User> selectAll() {
        return mapper.selectAll();
    }

    @Override
    public int delete(String email) {
        return mapper.delete(email);
    }

    @Override
    public int update(User user) {
        return mapper.update(user);
    }

    @Override
    public int create(User user) {
        return mapper.create(user);
    }

    @Override
    public int isUsed(String email) {
        return mapper.isUsed(email);
    }

}
