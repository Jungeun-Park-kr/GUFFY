package com.ssafy.guffy.model.model;

public class MeAndFriend {
	private Integer user_id;
	private Integer friend_id;
	public MeAndFriend(Integer user_id, Integer friend_id) {
		super();
		this.user_id = user_id;
		this.friend_id = friend_id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Integer getFriend_id() {
		return friend_id;
	}
	public void setFriend_id(Integer friend_id) {
		this.friend_id = friend_id;
	}
	
}
