package com.ssafy.guffy.controller.fcm;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.guffy.fcm.dto.FireStoreMessage;
import com.ssafy.guffy.fcm.service.FirebaseCloudMessageDataService;
import com.ssafy.guffy.fcm.service.FirebaseCloudMessageService;
import com.ssafy.guffy.model.model.Token;
import com.ssafy.guffy.model.model.User;
import com.ssafy.guffy.model.service.UserService;

@RestController
@CrossOrigin("*")
public class TokenController {
	
	private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    FirebaseCloudMessageService service;
    
    @Autowired
    FirebaseCloudMessageDataService dataService;
    
    @Autowired
    UserService userService;
    
    @PostMapping("/token")
    public Token registToken(Token token) {
    	logger.info("registToken : token:"+ token);
        service.addToken(token.getToken());
        //return "'"+token+"'" ;

        // 이 user_id 가진 사용자의 토큰 갱신
        User user = userService.selectById(Integer.parseInt(token.getUser_id()));
        if (user != null) {
        	user.setToken(token.getToken());
            userService.update(user);
            
        } else {
        	logger.warn("user ㅇ벗음...");
        }
        
        return token;
    }
    
    @PostMapping("/broadcast")
    public Integer broadCast(String title, String body) throws IOException {
    	logger.info("broadCast : title:{}, body:{}", title, body);
    	return service.broadCastMessage(title, body);
    }

    @PostMapping("/sendMessageTo")
    public void sendMessageTo(String token, String title, String body) throws IOException {
    	logger.info("sendMessageTo : token:{}, title:{}, body:{}", token, title, body);
        service.sendMessageTo(token, title, body);
    }
    
    @PostMapping("/fcm")
    public Boolean sendFCM(@RequestBody FireStoreMessage msg) throws IOException {
    	return dataService.broadCastDataMessage(msg);
    }
}

