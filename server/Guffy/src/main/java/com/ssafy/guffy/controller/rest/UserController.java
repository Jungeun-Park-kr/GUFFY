package com.ssafy.guffy.controller.rest;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
import com.ssafy.guffy.model.model.MeAndFriend;
import com.ssafy.guffy.model.model.Nickname;
import com.ssafy.guffy.model.model.User;
import com.ssafy.guffy.model.service.FriendsNumService;
import com.ssafy.guffy.model.service.UserService;

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
    
    @Autowired
    private FriendsNumService friendsNumService;
    

    @GetMapping("/isUsed")
    @ApiOperation(value = "request parameterė” ģ ė¬ė emailź° ģ“ģ“ėÆø ģ¬ģ©ģ¤ģøģ§ ė°ķķė¤.\n ģ¬ģ©ģ¤ģøź²½ģ° yes, ģ¬ģ© ź°ė„ķ emailģ ź²½ģ° noė„¼ ė°ķķė¤.")
    public String isUsedEmail(@RequestParam String email) {
        log.info("email: " + email);
        if (service.isUsed(email) == 1) {
            return "yes";
        }
        return "no";
    }
    
    @GetMapping("/isUsedName")
    @ApiOperation(value = "ģ“ėÆø ģ“ģ©ė ėė¤ģģøģ§ ķģøķģ¬ ģ¬ģ©ģ¤ģø ź²½ģ° yes, ģ¬ģ© ź°ė„ķ ź²½ģ° noė„¼ ė°ķķė¤.")
    public String isUsedNickname(@RequestParam String nickname) {
    	if(service.isUsedName(nickname) >= 1) {
    		return "yes";
    	}
    	return "no";
    }

    @GetMapping("")
    @ApiOperation(value = "ķ“ė¹ emailģ ź°ģ§ ģ¬ģ©ģ ģ ė³“ė„¼ ė°ķķė¤.")
    public User select(@RequestParam String email) {
        return service.select(email);
    }
    
    @GetMapping("/id")
    @ApiOperation(value = "ķ“ė¹ idė„¼ ź°ģ§ ģ¬ģ©ģ ģ ė³“ė„¼ ė°ķķė¤.")
    public User select(@RequestParam int id) {
        return service.selectById(id);
    }
    
    @PostMapping("/login")
    @ApiOperation(value = "ģ¬ģ©ģģ ģ“ė©ģ¼, ė¹ė°ė²ķøź° ė§ėģ§ ķģø ķ ė§ė¤ė©“ ģ¬ģ©ģ ģ ė³“ė„¼ ė°ķķė¤. ķė¦¬ė¤ė©“ nullģ ė°ķķė¤.")
    public User login(@RequestBody User user) {
    	if(user.getEmail() == null || user.getPw() == null)
    		return null;
    	
    	//userInfoģģ ź°ģ øģØ ė¹ė°ė²ķø(ģķøķėØ)ģ ģ§źø ģė „ė°ģ ė¹ė°ė²ķø match ķģø
    	String encodePw = service.select(user.getEmail()).getPw(); // ģķøķė ė¹ė°ė²ķø
    	if(pwEncoder.matches(user.getPw(),encodePw)) {
    		//ģķøķ ė ė¹ė°ė²ķøė” pw ģ ė³“ ė³ź²½ ķ ė”ź·øģø
    		user.setPw(encodePw);
    		User loginedUser = service.login(user);
    		// ģ“ė ķ ķ°ģ ģė°ģ“ķø ķźø°
    		//service.update(loginedUser); // ģ±ģģ ķ ķ° ź°ģ“ ė³“ė“źø°
    		return loginedUser;
    	}else {
    		return null;
    	}
    }

    @PostMapping("")
    @ApiOperation(value = "ģ ė¬ė°ģ ģ¬ģ©ģė„¼ DBģ ģ¶ź°ķė¤. ģ±ź³µģ success, ģ¤ķØģ failģ ė°ķķė¤.")
    public String create(@RequestBody User user) {
        if (service.select(user.getEmail()) == null) { // DBģ ģė IDģ¬ģ¼ ķØ
        	user.setPw(pwEncoder.encode(user.getPw()));
        	user.setNickname(makeNickName()); // ėė¤ ėė¤ģ ģģ±
            if (service.create(user) == 1) {
            	user = service.select(user.getEmail());
            	friendsNumService.create(user.getId().toString());
            	return "success";
            }
                
        }
        return "fail";
    }

    @DeleteMapping("")
    @ApiOperation(value = "ģ ė¬ė°ģ emailģ ź°ģ§ ģ¬ģ©ģė„¼ DBģģ ģ­ģ ķė¤.ģ±ź³µģ success, ģ¤ķØģ failģ ė°ķķė¤.")
    public String delete(@RequestParam String email) {
        if (service.delete(email) == 1) {
        	return "success";
        }   
        return "fail";
    }

    @PutMapping("")
    @ApiOperation(value = "bodyė” ģ ė¬ė°ģ ģ¬ģ©ģė„¼ DBģģ ģģ ķė¤. ģ±ź³µģ success, ģ¤ķØģ failģ ė°ķķė¤.")
    public String update(@RequestBody User user) {
        if (service.update(user) == 1)
            return "success";
        return "fail";
    }
    
    @PutMapping("/password")
    @ApiOperation(value = "bodyė” ģ ė¬ė°ģ ģ¬ģ©ģģ ė¹ė°ė²ķøė„¼ ģķøķķģ¬ ė³ź²½ķė¤. ģ±ź³µģ success, ģ¤ķØģ nullģ ė°ķķė¤.")
    public String updatePw(@RequestBody User user) {
    	//userInfoģģ ź°ģ øģØ ė¹ė°ė²ķø(ģķøķėØ)ģ ģ§źø ģė „ė°ģ ė¹ė°ė²ķø match ķģø
    	String encodePw = pwEncoder.encode(user.getPw()); // ģ ė¹ė°ė²ķø
    	user = service.select(user.getEmail()); // źø°ģ”“ ģ ģ  ģ ė³“
    	user.setPw(encodePw); // dtoė„¼ ģ ė¹ė°ė²ķøė” ź°±ģ 
    	if (service.update(user) == 1) // ģķøķė ģ ė¹ė°ė²ķøė„¼ DBģ ź°±ģ 
    		return "success";
    	return null;
    }

    // ķģ¬ ģ¬ģ©ģģ ģ¹źµ¬ź° ėģ“ģė ėŖØė  ģ¬ėģ ģ¹źµ¬id, ģ±ķė°©idė„¼ ė¦¬ķ“
    @GetMapping("/friend/friendsIds")
    @ApiOperation(value = "ėģ“ģØ emailģ ģ¹źµ¬ id, ź·ø ģ¹źµ¬ģģ ģ±ķė°© idź° ė“źø“ ģ¹źµ¬ ė¦¬ģ¤ķøė„¼ ź°ģ øģØė¤.")
    public List<ChatFriend> friends(@RequestParam String email) {
        return service.friends(email);
    }

    // friend_idė„¼ ź°ģ§ ģ¹źµ¬ģ ģ ė³“ģ ģ±ķ ģ ė³“ė„¼ ķØź» ź°ģ øģØė¤.
    @GetMapping("/friend")
    @ApiOperation(value = "ėģ“ģØ idė„¼ ź°ģ§ ģ¹źµ¬ģ ģ ė³“, ź·ø ģ¹źµ¬ģģ ģ±ķė°© ģ ė³“ė„¼ ź°ģ øģØė¤.")
    public Friend friend(@RequestParam int user_id, @RequestParam int friend_id) {
        return service.friend(new MeAndFriend(user_id, friend_id));
    }
    
    @GetMapping("/name")
    @ApiOperation(value = "ėė¤ģ ģģ± ģøė¶ APIė„¼ ģ“ģ©ķ“ģ ėė¤ģ¼ė” ėė¤ģģ ģģ±ķė¤. ģ±ź³µģ ėė¤ģ, ģ¤ķØģ failģ ė°ķķė¤.")
    public String makeNickName() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        String url = "https://nickname.hwanmoo.kr/?format=json";
        String jsonInString = "";
        String newName = "";

        try {
        	
            RestTemplate restTemplate = new RestTemplate();
            
            
            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);
            
            while(true) {
            	ResponseEntity<Nickname> resultMap = restTemplate.exchange(url, HttpMethod.GET, entity, Nickname.class);
                
                result.put("statusCode", resultMap.getStatusCodeValue()); //http status codeė„¼ ķģø
                result.put("header", resultMap.getHeaders()); //ķ¤ė ģ ė³“ ķģø
                result.put("body", resultMap.getBody()); //ģ¤ģ  ė°ģ“ķ° ģ ė³“ ķģø
            
                //ė°ģ“ķ°ė„¼ ģ ėė” ģ ė¬ ė°ģėģ§ ķģø stringķķė” ķģ±ķ“ģ¤
                ObjectMapper mapper = new ObjectMapper();
                
                
                jsonInString = mapper.writeValueAsString(resultMap.getBody());
                
                log.info("mapper:"+jsonInString);
                
                Nickname jsonToNickname = mapper.readValue(jsonInString, Nickname.class);
                
                log.info("jsonToNickname: "+jsonToNickname.getWords().get(0));
                newName = jsonToNickname.getWords().get(0);
                
                // ģ¬ģ© ģ ķ ėė¤ģģ¼ ź²½ģ°ģė§ ė¦¬ķ“ģģ¼ģ£¼źø°
                log.info("ģ¬ģ©ķė ėė¤ģģøź°? :"+service.isUsedName(newName));
                if(service.isUsedName(newName) == 0) return newName;
            }
            
        }catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body"  , e.getStatusText());
            System.out.println("dfdfdfdf");
            System.out.println(e.toString());
 
        } catch (Exception e) {
            result.put("statusCode", "999");
            result.put("body"  , "excpetionģ¤ė„");
            System.out.println(e.toString());
        }
         log.info("result: " +result);
         
        return "fail";
    }
}
