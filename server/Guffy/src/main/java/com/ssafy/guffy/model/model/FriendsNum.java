package com.ssafy.guffy.model.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendsNum {
    private Integer id;
    private Integer user_id;
    private Integer friends_num;
        
    
    public FriendsNum() {
    	super();
    }

	public FriendsNum(Integer userid) {
        super();
        this.user_id = userid;
        this.friends_num = 0; // default = 0
    }
	
    public FriendsNum(Integer userid, Integer count) {
        super();
        this.user_id = userid;
        this.friends_num = count;
    }
    
    public FriendsNum(Integer id, Integer userid, Integer count) {
    	this.id = id;
    	this.user_id = userid;
    	this.friends_num = count;
    }

   
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return user_id;
	}

	public void setUserId(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getFriendsNum() {
		return friends_num;
	}

	public void setFriendsNum(Integer friends_num) {
		this.friends_num = friends_num;
	}

	@Override
	public String toString() {
		return "FriendsNum [id=" + id + ", user_id=" + user_id + ", friends_num=" + friends_num + "]";
	}
    
    
}
