package com.ssafy.guffy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.guffy.model.model.User;
import com.ssafy.guffy.model.service.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController{
    
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService service;
    
    @GetMapping("/isUsed")
    @ApiOperation(value = "request parameter로 전달된 email가 이이미 사용중인지 반환한다.\n 사용중인경우 yes, 사용 가능한 email의 경우 no를 반환한다.")
    public String isUsedEmail(@RequestParam String email) {
        log.info("email: "+email);
        if(service.isUsed(email) == 1) {
            return "yes";
        }
        return "no";
    }

    // 구현 필요!!!!!
    @GetMapping("/info")
    @ApiOperation(value = "해당 email을 가진 사용자의 정보와 채팅 정보를 함께 반환한다.")
    public User selectInfo(@RequestParam  String email) {
        return service.select(email);
    }
    
    @GetMapping("")
    @ApiOperation(value = "해당 email을 가진 사용자 정보를 반환한다.")
    public User select(@RequestParam  String email) {
        return service.select(email);
    }
    
    
    @PostMapping("")
    @ApiOperation(value = "전달받은 사용자를 DB에 추가한다.")
    public String create(@RequestBody User user) {
        if (service.select(user.getEmail()) == null) { // DB에 없는 ID여야 함
            if(service.create(user) == 1) return "success"; 
        }
        return "fail";
    }
    
    
    @DeleteMapping("")
    @ApiOperation(value = "전달받은 email을 가진 사용자를 DB에서 삭제한다.")
    public String delete(@RequestParam String email) {
        if(service.delete(email) == 1) return "success";
        return "fail";
    }
    
    @PutMapping("")
    @ApiOperation(value = "body로 전달받은 사용자를 DB에서 수정한다.")
    public String update(@RequestBody User user) {
        if(service.update(user) == 1) return "success";
        return "fail";
    }
    
}
