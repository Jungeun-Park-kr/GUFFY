package com.ssafy.guffy.model.model;

public class Mail {
    private String email;
    private String title;
    private String message;
    
    public Mail(String email, String title, String message) {
        super();
        this.email = email;
        this.title = title;
        this.message = message;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
