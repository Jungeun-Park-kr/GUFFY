package com.ssafy.guffy.controller.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.guffy.model.model.ChatFriend;
import com.ssafy.guffy.model.model.Friend;
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

    @PostMapping("")
    @ApiOperation(value = "전달받은 사용자를 DB에 추가한다.")
    public String create(@RequestBody User user) {
        if (service.select(user.getEmail()) == null) { // DB에 없는 ID여야 함
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
