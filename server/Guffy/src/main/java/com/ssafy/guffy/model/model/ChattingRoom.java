package com.ssafy.guffy.model.model;

import lombok.Getter;
import lombok.Setter;

public class ChattingRoom {
    private Integer id;
    private String user1Id;
    private String user2Id;
    private Long user1LastVisitedTime;
    private Long user2LastVisitedTime;
    private Long user1LastChattingTime;
    private Long user2LastChattingTime;
    
    public ChattingRoom() {
    	
    }
    

	public ChattingRoom(Integer id, String user1Id, String user2Id, Long user1LastVisitedTime,
			Long user1LastChattingTime) {
		super();
		this.id = id;
		this.user1Id = user1Id;
		this.user2Id = user2Id;
		this.user1LastVisitedTime = user1LastVisitedTime;
		this.user1LastChattingTime = user1LastChattingTime;
	}


	public ChattingRoom(String user1Id, String user2Id) {
		super();
		this.user1Id = user1Id;
		this.user2Id = user2Id;
	}

	public ChattingRoom(String user1Id, String user2Id, Long user1LastVisitedTime, Long user2LastVisitedTime,
			Long user1LastChattingTime, Long user2LastChattingTime) {
		super();
		this.user1Id = user1Id;
		this.user2Id = user2Id;
		this.user1LastVisitedTime = user1LastVisitedTime;
		this.user2LastVisitedTime = user2LastVisitedTime;
		this.user1LastChattingTime = user1LastChattingTime;
		this.user2LastChattingTime = user2LastChattingTime;
	}

	public ChattingRoom(Integer id, String user1Id, String user2Id, Long user1LastVisitedTime,
			Long user2LastVisitedTime, Long user1LastChattingTime, Long user2LastChattingTime) {
		super();
		this.id = id;
		this.user1Id = user1Id;
		this.user2Id = user2Id;
		this.user1LastVisitedTime = user1LastVisitedTime;
		this.user2LastVisitedTime = user2LastVisitedTime;
		this.user1LastChattingTime = user1LastChattingTime;
		this.user2LastChattingTime = user2LastChattingTime;
	}
	

	public String getUser1Id() {
		return user1Id;
	}

	public void setUser1Id(String user1Id) {
		this.user1Id = user1Id;
	}

	public String getUser2Id() {
		return user2Id;
	}

	public void setUser2Id(String user2Id) {
		this.user2Id = user2Id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getUser1LastVisitedTime() {
		return user1LastVisitedTime;
	}

	public void setUser1LastVisitedTime(Long user1LastVisitedTime) {
		this.user1LastVisitedTime = user1LastVisitedTime;
	}

	public Long getUser2LastVisitedTime() {
		return user2LastVisitedTime;
	}

	public void setUser2LastVisitedTime(Long user2LastVisitedTime) {
		this.user2LastVisitedTime = user2LastVisitedTime;
	}

	public Long getUser1LastChattingTime() {
		return user1LastChattingTime;
	}

	public void setUser1LastChattingTime(Long user1LastChattingTime) {
		this.user1LastChattingTime = user1LastChattingTime;
	}

	public Long getUser2LastChattingTime() {
		return user2LastChattingTime;
	}

	public void setUser2LastChattingTime(Long user2LastChattingTime) {
		this.user2LastChattingTime = user2LastChattingTime;
	}

	
     
    

}
