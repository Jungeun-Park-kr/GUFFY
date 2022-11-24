package com.ssafy.guffy.model.model;

public class Friend {
    private Integer friend_id; // 친구의 id
    private String friend; // 친구가 user1 인지 user2 인지 담고있음
    private String email;
    private String nickname;
    private String gender;
    private String mbti;
    private String interest1;
    private String interest2;
    private String interest3;
    private String interest4;
    private String interest5;
    private String token;
    private Integer chat_id; // 이 친구와 참여중인 채팅방의 id
    private Long user1_last_visited_time;
    private Long user2_last_visited_time;
    private Long user1_last_chatting_time;
    private Long user2_last_chatting_time;
    
   

    public Friend(Integer friend_id, String friend, String email, String nickname, String gender, String mbti,
            String interest1, String interest2, String interest3, String interest4, String interest5, String token
            , Integer chat_id) {
        super();
        this.friend_id = friend_id;
        this.friend = friend;
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.mbti = mbti;
        this.interest1 = interest1;
        this.interest2 = interest2;
        this.interest3 = interest3;
        this.interest4 = interest4;
        this.interest5 = interest5;
        this.chat_id = chat_id;
    }

    public Friend(Integer friend_id, String friend, String email, String nickname, String gender, String mbti,
            String interest1, String interest2, String interest3, String interest4, String interest5,String token,
            Integer chat_id, Long user1_last_visited_time, Long user2_last_visited_time, Long user1_last_chatting_time,
            Long user2_last_chatting_time) {
        super();
        this.friend_id = friend_id;
        this.friend = friend;
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.mbti = mbti;
        this.interest1 = interest1;
        this.interest2 = interest2;
        this.interest3 = interest3;
        this.interest4 = interest4;
        this.interest5 = interest5;
        this.token = token;
        this.chat_id = chat_id;
        this.user1_last_visited_time = user1_last_visited_time;
        this.user2_last_visited_time = user2_last_visited_time;
        this.user1_last_chatting_time = user1_last_chatting_time;
        this.user2_last_chatting_time = user2_last_chatting_time;
    }


    public Integer getFriend_id() {
        return friend_id;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getNickname() {
        return nickname;
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getGender() {
        return gender;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getMbti() {
        return mbti;
    }


    public void setMbti(String mbti) {
        this.mbti = mbti;
    }


    public String getInterest1() {
        return interest1;
    }


    public void setInterest1(String interest1) {
        this.interest1 = interest1;
    }


    public String getInterest2() {
        return interest2;
    }


    public void setInterest2(String interest2) {
        this.interest2 = interest2;
    }


    public String getInterest3() {
        return interest3;
    }


    public void setInterest3(String interest3) {
        this.interest3 = interest3;
    }


    public String getInterest4() {
        return interest4;
    }


    public void setInterest4(String interest4) {
        this.interest4 = interest4;
    }


    public String getInterest5() {
        return interest5;
    }


    public void setInterest5(String interest5) {
        this.interest5 = interest5;
    }
    

    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFriend() {
        return friend;
    }

    public Integer getChat_id() {
        return chat_id;
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
