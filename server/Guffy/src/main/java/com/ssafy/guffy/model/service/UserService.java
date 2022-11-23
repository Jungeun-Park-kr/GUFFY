package com.ssafy.guffy.model.service;

import java.util.List;

import com.ssafy.guffy.model.model.ChatFriend;
import com.ssafy.guffy.model.model.Friend;
import com.ssafy.guffy.model.model.MeAndFriend;
import com.ssafy.guffy.model.model.User;

public interface UserService {
    /**
     * 사용자 정보 조회
     * @param email
     * @return
     */
    public User select(String email);
    
    /**
     * 이메일, 비밀번호 입력받아 로그인 
     * @param user
     * @return 사용자 정보 반환
     */
    public User login(User user);
    
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
    
    /**
     * 나와 친구인 사람의 id, 그 사람과의 채팅방 id를 가져옴
     * @param email
     * @return
     */
    List<ChatFriend> friends(String email);
    
    /**
     * friend_id인 친구 정보와 그 친구와의 채팅 정보를 가져온다.
     * @param friend_id
     * @return
     */
    Friend friend(MeAndFriend meAndFriend);
    
    /**
     * 이미 사용중인 닉네임인지 확인한다.
     * @param name
     * @return
     */
    int isUsedName(String name);
}
