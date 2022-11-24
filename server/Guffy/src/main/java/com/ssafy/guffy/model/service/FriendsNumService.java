package com.ssafy.guffy.model.service;

import java.util.List;

import com.ssafy.guffy.model.model.FriendsNum;

public interface FriendsNumService {
	/**
     * 친구수  조회
     * @param user_id
     * @return FriendsNum
     */
	public FriendsNum select(String user_id);
	
	/**
     * 새로 연결할 친구 조회
     * @param 
     * @return FriendsNum
     */
	public List<FriendsNum> findNewFriends();
	
	/**
     * 친구 수 업데이트
     * @param FriendsNum
     * @return 1 : 업데이트 완료
     */
	public int update(FriendsNum friendsNum);
	
	/**
     * 친구 수 생성
     * @param user_id
     * @return 1 : 생성 완료
     */
	public int create(String user_id);
	
	/**'
	 * 모든 사람의 친구 수 정보를 업데이트한다
	 * @return 성공시 "success", 실패시 "fail"
	 */
	public String updateAll();
	
	/**'
	 * friends 테이블 전체를 가지고 온다.
	 * @return 성공시 "테이블 전체", 실패시 "null"
	 */
	public List<FriendsNum> getAllUser();
	
	
}
