package com.ssafy.guffy.util.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ssafy.guffy.model.model.User;
import com.ssafy.guffy.model.service.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailService {
    
    @Autowired
    private JavaMailSender javaMailSender;
   
    
    public String sendAuthMail(String userEmail) {
        String authString = getRandStr();
        SimpleMailMessage simpleMessage = new SimpleMailMessage(); // 단순 텍스트 메일 메시지 생성시 사용
        simpleMessage.setTo(userEmail);
        
        simpleMessage.setSubject("GUFFY 이메일 인증");
        simpleMessage.setText("회원가입을 위해 다음의 인증번호를 입력해주세요.\n인증번호 : "+authString);
        
        try {
            javaMailSender.send(simpleMessage); // 메일 발송
            return authString; // 생성된 인증번호 반환
        }
        catch(Exception e) {
            return "fail";
        }
    }
    
    public String sendTempPw(String userEmail) {        
        String tempPw = getRandStr();
        SimpleMailMessage simpleMessage = new SimpleMailMessage(); // 단순 텍스트 메일 메시지 생성시 사용
        simpleMessage.setTo(userEmail);
        simpleMessage.setSubject("GUFFY 이메일 인증");
        simpleMessage.setText("임시 비밀번호 : "+getRandStr()+"\n임시 비밀번호로 로그인 후 비밀번호를 꼭 변경해주세요.");
        
        try {
            javaMailSender.send(simpleMessage); // 메일 발송
            
            return tempPw;
        }
        catch(Exception e) {
            return "fail";
        }
    }

    
    //랜덤함수로 임시비밀번호 구문 만들기
    public String getRandStr(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }
  
}
