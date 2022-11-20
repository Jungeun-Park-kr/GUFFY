package com.ssafy.guffy.model.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FreidnsNum {
    private Integer id;
    private Integer user_id;
    private Integer friends_num;
    
    
    public FreidnsNum(Integer user_id) {
        super();
        this.user_id = user_id;
        this.friends_num = 0; // default = 0
    }

    public FreidnsNum(Integer user_id, Integer friends_num) {
        super();
        this.user_id = user_id;
        this.friends_num = friends_num;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getFriends_num() {
        return friends_num;
    }

    public void setFriends_num(Integer friends_num) {
        this.friends_num = friends_num;
    }
    
    
}
