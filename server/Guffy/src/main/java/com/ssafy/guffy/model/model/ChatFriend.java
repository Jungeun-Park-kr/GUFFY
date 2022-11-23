package com.ssafy.guffy.model.model;


/**
 * 현재 사용자와 친구인 사람의 id, 그 사람과의 채팅 id를 담는 dto
 * @author jungeun
 *
 */
public class ChatFriend {
    private Integer friend_id;
    private Integer chat_id;
    private Integer deleted;
    
    public ChatFriend(Integer friend_id, Integer chat_id) {
        super();
        this.friend_id = friend_id;
        this.chat_id = chat_id;
        this.deleted = 0;
    }
    
    public ChatFriend(Integer friend_id, Integer chat_id, Integer deleted) {
        super();
        this.friend_id = friend_id;
        this.chat_id = chat_id;
        this.deleted = deleted;
    }
    public Integer getFriend_id() {
        return friend_id;
    }
    public Integer getChat_id() {
        return chat_id;
    }
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
    
}
