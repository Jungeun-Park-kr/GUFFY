package com.ssafy.guffy.controller.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.guffy.model.model.ChatFriend;
import com.ssafy.guffy.model.model.Friend;
import com.ssafy.guffy.model.model.Nickname;
import com.ssafy.guffy.model.model.User;
import com.ssafy.guffy.model.service.UserService;
import com.ssafy.guffy.util.service.MailService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService service;
    
    @Autowired
    private PasswordEncoder pwEncoder;
    

    @GetMapping("/isUsed")
    @ApiOperation(value = "request parameter로 전달된 email가 이이미 사용중인지 반환한다.\n 사용중인경우 yes, 사용 가능한 email의 경우 no를 반환한다.")
    public String isUsedEmail(@RequestParam String email) {
        log.info("email: " + email);
        if (service.isUsed(email) == 1) {
            return "yes";
        }
        return "no";
    }

    // 구현 필요!!!!!???
    @GetMapping("/info")
    @ApiOperation(value = "해당 email을 가진 사용자의 정보와 채팅 정보를 함께 반환한다.")
    public User selectInfo(@RequestParam String email) {
        return service.select(email);
    }

    @GetMapping("")
    @ApiOperation(value = "해당 email을 가진 사용자 정보를 반환한다.")
    public User select(@RequestParam String email) {
        return service.select(email);
    }
    
    @PostMapping("/login")
    @ApiOperation(value = "사용자의 이메일, 비밀번호가 맞는지 확인 후 맞다면 사용자 정보를 반환한다.")
    public User login(@RequestBody User user) {
    	if(user.getEmail() == null || user.getPw() == null)
    		return null;
    	
    	//userInfo에서 가져온 비밀번호(암호화됨)와 지금 입력받은 비밀번호 match 확인
    	String encodePw = service.select(user.getEmail()).getPw(); // 암호화된 비밀번호
    	if(pwEncoder.matches(user.getPw(),encodePw)) {
    		//암호화 된 비밀번호로 pw 정보 변경 후 로그인
    		user.setPw(encodePw);
    		return service.login(user);
    	}else {
    		return null;
    	}
    }

    @PostMapping("")
    @ApiOperation(value = "전달받은 사용자를 DB에 추가한다.")
    public String create(@RequestBody User user) {
    	log.info("user: "+user);
        if (service.select(user.getEmail()) == null) { // DB에 없는 ID여야 함
            user.setNickname(makeNickName()); // 랜덤 닉네임 생성
        	user.setPw(pwEncoder.encode(user.getPw()));
        	log.info("비밀번호:"+user.getPw());
            if (service.create(user) == 1)
                return "success";
        }
        return "fail";
    }

    @DeleteMapping("")
    @ApiOperation(value = "전달받은 email을 가진 사용자를 DB에서 삭제한다.")
    public String delete(@RequestParam String email) {
        if (service.delete(email) == 1)
            return "success";
        return "fail";
    }

    @PutMapping("")
    @ApiOperation(value = "body로 전달받은 사용자를 DB에서 수정한다.")
    public String update(@RequestBody User user) {
        if (service.update(user) == 1)
            return "success";
        return "fail";
    }
    
    @PutMapping("/password")
    @ApiOperation(value = "body로 전달받은 사용자의 비밀번호를 암호화하여 변경한다.")
    public String updatePw(@RequestBody User user) {
    	//userInfo에서 가져온 비밀번호(암호화됨)와 지금 입력받은 비밀번호 match 확인
    	String encodePw = pwEncoder.encode(user.getPw()); // 새 비밀번호
    	user = service.select(user.getEmail()); // 기존 유저 정보
    	user.setPw(encodePw); // dto를 새 비밀번호로 갱신
    	if (service.update(user) == 1) // 암호화된 새 비밀번호를 DB에 갱신
    		return "success";
    	return null;
    }

    // 현재 사용자와 친구가 되어있는 모든 사람의 친구id, 채팅방id를 리턴
    @GetMapping("/friend/friendsIds")
    @ApiOperation(value = "넘어온 email의 친구 id, 그 친구와의 채팅방 id가 담긴 친구 리스트를 가져온다.")
    public List<ChatFriend> friends(@RequestParam String email) {
        return service.friends(email);
    }

    // friend_id를 가진 친구의 정보와 채팅 정보를 함께 가져온다.
    @GetMapping("/friend")
    @ApiOperation(value = "넘어온 id를 가진 친구의 정보, 그 친구와의 채팅방 정보를 가져온다.")
    public Friend friend(@RequestParam int friend_id) {
        return service.friend(friend_id);
    }
    
    @GetMapping("/name")
    public String makeNickName() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        String url = "https://nickname.hwanmoo.kr/?format=json";
        String jsonInString = "";
        String newName = "";
        //ResponseEntity<Nickname> response = restTemplate.getForEntity(fooResourceUrl + "/1", Nickname.class);
        //Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        
        try {
            RestTemplate restTemplate = new RestTemplate();
            
            
            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);
            
            ResponseEntity<Nickname> resultMap = restTemplate.exchange(url, HttpMethod.GET, entity, Nickname.class);
            
            result.put("statusCode", resultMap.getStatusCodeValue()); //http status code를 확인
            result.put("header", resultMap.getHeaders()); //헤더 정보 확인
            result.put("body", resultMap.getBody()); //실제 데이터 정보 확인
        
            //데이터를 제대로 전달 받았는지 확인 string형태로 파싱해줌
            ObjectMapper mapper = new ObjectMapper();
            
            
            jsonInString = mapper.writeValueAsString(resultMap.getBody());
            
            log.info("mapper:"+jsonInString);
            
            Nickname jsonToNickname = mapper.readValue(jsonInString, Nickname.class);
            
            log.info("jsonToNickname: "+jsonToNickname.getWords().get(0));
            newName = jsonToNickname.getWords().get(0);
            
            return newName;
            //jsonInString = mapper.writeValueAsString(mnList);
            
        }catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body"  , e.getStatusText());
            System.out.println("dfdfdfdf");
            System.out.println(e.toString());
 
        } catch (Exception e) {
            result.put("statusCode", "999");
            result.put("body"  , "excpetion오류");
            System.out.println(e.toString());
        }
         log.info("result: " +result);
         
        return "fail";
        
    }


    /*
     * @GetMapping("/friend/friendsNum")
     * 
     * @ApiOperation(value="넘어온 id를 가진 친구 수를 반환한다.")
     *//**
        * 어차피 friends()에서 리스트 개수가 친구 수라 필요가 없음
        * 단, ChattingRoomController에서 친구 매칭해줄때 필요할 듯...
        * 따라서 ChattingRoomController에서 관리해야 함
        * @param email
        * @return
        *//*
           * public Integer friendsNum(@RequestParam String email) {
           * 
           * }
           */
}
