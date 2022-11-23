package com.ssafy.guffy.model.model;


public class ChattingRoom {
    private Integer id;
    private String user1_id;
    private String user2_id;
    private Long user1_last_visited_time;
    private Long user2_last_visited_time;
    private Long user1_last_chatting_time;
    private Long user2_last_chatting_time;
    private Integer deleted;
    
    public ChattingRoom() {
    	
    }
    

	public ChattingRoom(Integer id, String user1Id, String user2Id, Long user1LastVisitedTime,
			Long user1LastChattingTime, Integer deleted) {
		super();
		this.id = id;
		this.user1_id = user1Id;
		this.user2_id = user2Id;
		this.user1_last_visited_time = user1LastVisitedTime;
		this.user1_last_chatting_time = user1LastChattingTime;
		this.deleted = deleted;
	}


	public ChattingRoom(String user1Id, String user2Id) {
		super();
		this.user1_id = user1Id;
		this.user2_id = user2Id;
		this.deleted = 0;
	}

	public ChattingRoom(String user1Id, String user2Id, Long user1LastVisitedTime, Long user2LastVisitedTime,
			Long user1LastChattingTime, Long user2LastChattingTime) {
		super();
		this.user1_id = user1Id;
		this.user2_id = user2Id;
		this.user1_last_visited_time = user1LastVisitedTime;
		this.user2_last_visited_time = user2LastVisitedTime;
		this.user1_last_chatting_time = user1LastChattingTime;
		this.user2_last_chatting_time = user2LastChattingTime;
	}

	public ChattingRoom(Integer id, String user1Id, String user2Id, Long user1LastVisitedTime,
			Long user2LastVisitedTime, Long user1LastChattingTime, Long user2LastChattingTime, Integer deleted) {
		super();
		this.id = id;
		this.user1_id = user1Id;
		this.user2_id = user2Id;
		this.user1_last_visited_time = user1LastVisitedTime;
		this.user2_last_visited_time = user2LastVisitedTime;
		this.user1_last_chatting_time = user1LastChattingTime;
		this.user2_last_chatting_time = user2LastChattingTime;
		this.deleted = deleted;
	}
	

	public String getUser1Id() {
		return user1_id;
	}

	public void setUser1Id(String user1Id) {
		this.user1_id = user1Id;
	}

	public String getUser2Id() {
		return user2_id;
	}

	public void setUser2Id(String user2Id) {
		this.user2_id = user2Id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getuser1LastVisitedTime() {
		return user1_last_visited_time;
	}

	public void setuser1LastVisitedTime(Long user1_last_visited_time) {
		this.user1_last_visited_time = user1_last_visited_time;
	}

	public Long getUser2LastVisitedTime() {
		return user2_last_visited_time;
	}

	public void setUser2LastVisitedTime(Long user2LastVisitedTime) {
		this.user2_last_visited_time = user2LastVisitedTime;
	}

	public Long getUser1LastChattingTime() {
		return user1_last_chatting_time;
	}

	public void setUser1LastChattingTime(Long user1LastChattingTime) {
		this.user1_last_chatting_time = user1LastChattingTime;
	}

	public Long getUser2LastChattingTime() {
		return user2_last_chatting_time;
	}

	public void setUser2LastChattingTime(Long user2LastChattingTime) {
		this.user2_last_chatting_time = user2LastChattingTime;
	}


	public Integer getDeleted() {
		return deleted;
	}


	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	
     
    

}
