package com.ssafy.guffy.model.model;

public class Token {
	private String user_id;
	private String token;
	
	public Token(String user_id, String token) {
		super();
		this.user_id = user_id;
		this.token = token;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Override
	public String toString() {
		return "Token [user_id=" + user_id + ", token=" + token + "]";
	}
}
