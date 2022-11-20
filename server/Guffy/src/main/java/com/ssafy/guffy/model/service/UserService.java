package com.ssafy.guffy.model.service;

import java.util.List;

import com.ssafy.guffy.model.model.User;

public interface UserService {
    /**
     * 사용자 정보 조회
     * @param email
     * @return
     */
    public User select(String email);
    
    /**
     * 모든 사용자 정보 조회
     * @return
     */
    public List<User> selectAll();
    
    /**
     * 사용자 정보 삭제
     * @param email
     * @return
     */
    public int delete(String email);
    
    /**
     * 사용자 정보를 DB에서 업데이트 한다
     * @param user
     * @return
     */
    public int update(User user);
    
    /**
     * 새로운 유저를 DB에 추가
     * @param user
     * @return
     */
    public int create(User user);
    
    /**
     * 사용중인 email인지 확인
     * @param email
     * @return 1: 사용중 , 0 : 사용 X
     */
    public int isUsed(String email);
}
