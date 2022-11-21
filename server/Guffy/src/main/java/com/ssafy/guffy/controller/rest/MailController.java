package com.ssafy.guffy.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.guffy.model.model.User;
import com.ssafy.guffy.model.service.UserService;
import com.ssafy.guffy.util.service.MailService;

import io.swagger.annotations.ApiOperation;

@RestController
public class MailController {
    private static final Logger log = LoggerFactory.getLogger(MailController.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private MailService mailService;
    
    @Autowired
    private PasswordEncoder pwEncoder;
    
    public MailController(MailService service) {
        this.mailService = service;
    }
    
    
    @PostMapping("/mail/sendAuth")
    @ApiOperation(value = "해당 email을 가진 사용자에게 회원 가입 인증 번호가 담긴 메일을 발송하고, 생성된 인증번호를 반환한다.")
    public String sendAuthMail(@RequestParam String email) {
        String result = mailService.sendAuthMail(email);
        if(result != "false") {
            log.info("email 발송 성공");
            return result;            
        }
        return "false";
    }
    
    @PostMapping("/mail/sendTempPw")
    @ApiOperation(value = "해당 email을 가진 사용자에게 임시 비밀번호가 담긴 메일을 발송하고, DB의 비밀번호를 임시 비밀번호로 변경한다.")
    public String sendTempPwMailAndUpdatePw(@RequestParam String email) {
        String result = mailService.sendTempPw(email);
        if(result != "fail") {
            log.info("새 비번:"+result+", email 발송 성공");
            
            // 회원 비밀번호를 임시 비밀번호로 변경
            User user = userService.select(email);
            
            if (user == null) return "fail";
            String curPw = user.getPw();
            
            user.setPw(pwEncoder.encode(result)); // result 를 암호화해서 저장
            
            if(!curPw.equals(user.getPw())) {
            	log.info("새 비번 암호화:"+user.getPw());
            }
            if(userService.update(user) == 1) { // 암호화된 새 임시비번으로 DB 수정
            	return "success";
            }
            	            
        }
        return "fail";
    }

}
