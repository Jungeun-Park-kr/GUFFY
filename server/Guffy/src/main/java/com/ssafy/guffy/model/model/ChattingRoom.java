package com.ssafy.guffy.model.model;

import lombok.Getter;
import lombok.Setter;

public class ChattingRoom {
    private Integer id;
    private String user1_id;
    private String user2_id;
    private Long user1_last_visited_time;
    private Long user2_last_visited_time;
    private Long user1_last_chatting_time;
    private Long user2_last_chatting_time;
    
     
    public ChattingRoom(String user1_id, String user2_id) {
        super();
        this.user1_id = user1_id;
        this.user2_id = user2_id;
    }
    

    public ChattingRoom(String user1_id, String user2_id, Long user1_last_visited_time) {
        super();
        this.user1_id = user1_id;
        this.user2_id = user2_id;
        this.user1_last_visited_time = user1_last_visited_time;
    }

    public ChattingRoom(String user1_id, String user2_id, Long user1_last_visited_time,
            Long user1_last_chatting_time) {
        super();
        this.user1_id = user1_id;
        this.user2_id = user2_id;
        this.user1_last_visited_time = user1_last_visited_time;
        this.user1_last_chatting_time = user1_last_chatting_time;
    }

    public ChattingRoom(String user1_id, String user2_id, Long user1_last_visited_time,
            Long user2_last_visited_time, Long user1_last_chatting_time, Long user2_last_chatting_time) {
        super();
        this.user1_id = user1_id;
        this.user2_id = user2_id;
        this.user1_last_visited_time = user1_last_visited_time;
        this.user2_last_visited_time = user2_last_visited_time;
        this.user1_last_chatting_time = user1_last_chatting_time;
        this.user2_last_chatting_time = user2_last_chatting_time;
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getUser1_id() {
        return user1_id;
    }


    public void setUser1_id(String user1_id) {
        this.user1_id = user1_id;
    }


    public String getUser2_id() {
        return user2_id;
    }


    public void setUser2_id(String user2_id) {
        this.user2_id = user2_id;
    }


    public Long getUser1_last_visited_time() {
        return user1_last_visited_time;
    }


    public void setUser1_last_visited_time(Long user1_last_visited_time) {
        this.user1_last_visited_time = user1_last_visited_time;
    }


    public Long getUser2_last_visited_time() {
        return user2_last_visited_time;
    }


    public void setUser2_last_visited_time(Long user2_last_visited_time) {
        this.user2_last_visited_time = user2_last_visited_time;
    }


    public Long getUser1_last_chatting_time() {
        return user1_last_chatting_time;
    }


    public void setUser1_last_chatting_time(Long user1_last_chatting_time) {
        this.user1_last_chatting_time = user1_last_chatting_time;
    }


    public Long getUser2_last_chatting_time() {
        return user2_last_chatting_time;
    }


    public void setUser2_last_chatting_time(Long user2_last_chatting_time) {
        this.user2_last_chatting_time = user2_last_chatting_time;
    }

}
