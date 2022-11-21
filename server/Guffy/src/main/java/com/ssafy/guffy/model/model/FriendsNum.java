package com.ssafy.guffy.model.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendsNum {
    private Integer id;
    private Integer userid;
    private Integer count;
        
    
    public FriendsNum() {
    	super();
    }

	public FriendsNum(Integer userid) {
        super();
        this.userid = userid;
        this.count = 0; // default = 0
    }
	
    public FriendsNum(Integer userid, Integer count) {
        super();
        this.userid = userid;
        this.count = count;
    }
    
    public FriendsNum(Integer id, Integer userid, Integer count) {
    	this.id = id;
    	this.userid = userid;
    	this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

	@Override
	public String toString() {
		return "FriendsNum [id=" + id + ", userid=" + userid + ", count=" + count + "]";
	}
    
    
}
