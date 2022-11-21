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

import com.ssafy.guffy.model.model.ChattingRoom;
import com.ssafy.guffy.model.model.FriendsNum;
import com.ssafy.guffy.model.service.ChattingRoomService;
import com.ssafy.guffy.model.service.FriendsNumService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/chatroom")
@CrossOrigin("*")
public class ChattingRoomController {

	private static final Logger log = LoggerFactory.getLogger(ChattingRoomController.class);

	@Autowired
	private ChattingRoomService service;

	@Autowired
	private FriendsNumService friendsNumService;

	@GetMapping("")
	@ApiOperation(value = "채팅방 아이디로 row를 전체 조회한다.")
	public ChattingRoom select(@RequestParam String id) {
		return service.select(id);
	}

	@DeleteMapping("")
	@ApiOperation("채팅방 아이디에 해당하는 row를 삭제한다.")
	public int delete(@RequestParam String id) { // 채팅방 아이디
		// 채팅방 아이디로 전체 row 찾아오기
		ChattingRoom chattingRoom = service.select(id);

		// u1 friendsNum 테이블에서 count--
		FriendsNum u1FriendsNum = friendsNumService.select(chattingRoom.getUser1Id());
		u1FriendsNum.setCount(Math.max(u1FriendsNum.getCount() - 1, 0));
		friendsNumService.update(u1FriendsNum);

		// u2 friendsNum 테이블에서 count--
		FriendsNum u2FriendsNum = friendsNumService.select(chattingRoom.getUser2Id());
		u2FriendsNum.setCount(Math.max(u2FriendsNum.getCount() - 1, 0));
		friendsNumService.update(u2FriendsNum);

		// 채팅방 삭제
		return service.delete(id);
	}

	@PostMapping("")
	@ApiOperation("새로운 채팅방을 추가한다. 새로운 채팅방 생성시 1, 실패시 0, 더이상 연결할 수 있는 친구가 없을 때-1 ")
	public int create(@RequestParam String user1Id) { // 파라미터에 user1에만 들어있음
		
		String user2Id = "";
		// 새로운 친구 연결
		List<FriendsNum> newFriendsList = friendsNumService.findNewFriends();
		for (FriendsNum newOne : newFriendsList) {
			if (newOne.getCount() >= 3) {
				return -1;
			}
			if (newOne.getId().toString().equals(user1Id.trim()) == false) { // 자기 자신이 아니면
				user2Id = newOne.getId().toString();
				log.info("새로운 친구 : " + user2Id);
				break;
			}
		}
		 
		// friendsNum 의 테이블에 user1, user2 업데이트할것,
		FriendsNum user1 = friendsNumService.select(user1Id);
		user1.setCount(user1.getCount() + 1);
		friendsNumService.update(user1);
		
		FriendsNum user2 = friendsNumService.select(user2Id);
		user2.setCount(user2.getCount() + 1);
		friendsNumService.update(user2);
		

		return service.create(new ChattingRoom(user1Id, user2Id, 0L, 0L, 0L, 0L));
	}

	@GetMapping("/isUsed")
	@ApiOperation("현재 사용중인 채팅방 아이디인지 검사한다.\n 0이라면 사용가능한 채팅방 아이디이다.")
	public int isUsed(@RequestParam String id) {
		return service.isUsed(id);
	}

	@PutMapping("")
	@ApiOperation("request parameter로 전송된 채팅방 내용으로 채팅방을 업데이트한다.")
	public int update(@RequestBody ChattingRoom chattingRoom) {
		return service.update(chattingRoom);
	}

}
