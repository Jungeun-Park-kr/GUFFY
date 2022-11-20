package com.ssafy.guffy.model.model;


/**
 * 현재 사용자와 친구인 사람의 id, 그 사람과의 채팅 id를 담는 dto
 * @author jungeun
 *
 */
public class ChatFriend {
    private String friend_id;
    private String chat_id;
    public ChatFriend(String friend_id, String chat_id) {
        super();
        this.friend_id = friend_id;
        this.chat_id = chat_id;
    }
    public String getFriend_id() {
        return friend_id;
    }
    public String getChat_id() {
        return chat_id;
    }
}
