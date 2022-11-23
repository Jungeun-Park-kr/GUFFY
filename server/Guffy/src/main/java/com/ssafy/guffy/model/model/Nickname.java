package com.ssafy.guffy.model.model;

import java.util.List;

import lombok.Data;

@Data
public class Nickname {
    private List<String> words;
    private String seed;
    public Nickname(List<String> words, String seed) {
        super();
        this.words = words;
        this.seed = seed;
    }
    
    
    public Nickname() {
        super();
    }


    public List<String> getWords() {
        return words;
    }
    public void setWords(List<String> words) {
        this.words = words;
    }
    public String getSeed() {
        return seed;
    }
    public void setSeed(String seed) {
        this.seed = seed;
    }
    
    
}
