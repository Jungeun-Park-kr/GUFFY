package com.ssafy.guffy.model.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String email;
    private String pw;
    private String nickname;
    private String gender;
    private String mbti;
    private String interest1;
    private String interest2;
    private String interest3;
    private String interest4;
    private String interest5;
    
    
    public User() {
        super();
    }

    public User(Integer id, String email, String pw, String nickname, String gender, String mbti, String interest1,
            String interest2, String interest3, String interest4, String interest5) {
        super();
        this.id = id;
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.gender = gender;
        this.mbti = mbti;
        this.interest1 = interest1;
        this.interest2 = interest2;
        this.interest3 = interest3;
        this.interest4 = interest4;
        this.interest5 = interest5;
    }

    public User(String email, String pw, String nickname, String gender, String mbti, String interest1,
            String interest2, String interest3) {
        super();
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.gender = gender;
        this.mbti = mbti;
        this.interest1 = interest1;
        this.interest2 = interest2;
        this.interest3 = interest3;
    }
    
    public User(String email, String pw, String nickname, String gender, String mbti, String interest1,
            String interest2, String interest3, String interest4) {
        super();
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.gender = gender;
        this.mbti = mbti;
        this.interest1 = interest1;
        this.interest2 = interest2;
        this.interest3 = interest3;
        this.interest4 = interest4;
    }


    public User(String email, String pw, String nickname, String gender, String mbti, String interest1,
            String interest2, String interest3, String interest4, String interest5) {
        super();
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.gender = gender;
        this.mbti = mbti;
        this.interest1 = interest1;
        this.interest2 = interest2;
        this.interest3 = interest3;
        this.interest4 = interest4;
        this.interest5 = interest5;
    }

    public String getEmail() {
        return email;
    }

    public Integer getId() {
        return id;
    }
    
    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
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

    public void setEmail(String email) {
        this.email = email;
    }

    
}
