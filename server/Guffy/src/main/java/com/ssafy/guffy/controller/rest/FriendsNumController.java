package com.ssafy.guffy.controller.rest;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.guffy.model.model.FriendsNum;
import com.ssafy.guffy.model.service.FriendsNumService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/friendsNum")
@CrossOrigin("*")
public class FriendsNumController {

	private static final Logger log = LoggerFactory.getLogger(FriendsNumController.class);

	@Autowired
	private FriendsNumService service;
	
	@GetMapping("/updateAll")
	@ApiOperation("모든 유저의 친구 수를 업데이트 한다. (참여중인 채팅방 개수 만큼)")
	public String getAllFriendsNum() {
		
		
		return "success";
	}

	@GetMapping("")
	@ApiOperation("parameter로 전달된 id를 가진 친구 수 정보를 반환한다.")
	public FriendsNum select(@RequestParam String user_id) {
		// 가져오기 전에 모든 사람 친구 수 업데이트
		// service.updateAll(); // 구현하기) FriendsNum.xml도 비어있음
		
		return service.select(user_id);
	}

	@GetMapping("/findNewFriends")
	@ApiOperation("연결된 친구 수가 가장 작은 순서대로 상위 1개 데이터만 출력")
	public List<FriendsNum>	 findNewFriends() {
		return service.findNewFriends();
	}

	@PutMapping("")
	@ApiOperation("requestbody에 전달된 대로 친구 수 정보 수정")
	public int update(@RequestBody FriendsNum friendsNum) {
		return service.update(friendsNum);
	}

	@PostMapping("")
	@ApiOperation("parameter로 전달된 id를 가진 친구 수 정보 생성")
	public int create(@RequestParam String user_id) {
		return service.create(user_id);
	}

}
