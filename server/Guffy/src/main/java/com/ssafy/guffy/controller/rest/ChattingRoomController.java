package com.ssafy.guffy.controller.rest;

import java.io.IOException;
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

import com.ssafy.guffy.fcm.service.FirebaseCloudMessageDataService;
import com.ssafy.guffy.fcm.service.FirebaseCloudMessageService;
import com.ssafy.guffy.model.model.ChatFriend;
import com.ssafy.guffy.model.model.ChattingRoom;
import com.ssafy.guffy.model.model.FriendsNum;
import com.ssafy.guffy.model.model.User;
import com.ssafy.guffy.model.service.ChattingRoomService;
import com.ssafy.guffy.model.service.FriendsNumService;
import com.ssafy.guffy.model.service.UserService;

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
	
	@Autowired
	FirebaseCloudMessageDataService tokenService;
	
	@Autowired
	UserService userService;

	@GetMapping("")
	@ApiOperation(value = "채팅방 아이디로 row를 전체 조회한다.")
	public ChattingRoom select(@RequestParam String id) {
		log.info("message: " + service.select(id));
		return service.select(id);
	}

	@DeleteMapping("")
	@ApiOperation("채팅방 아이디에 해당하는 row를 삭제한다.")
	public int delete(@RequestParam String id) { // 채팅방 아이디
		// 채팅방 아이디로 전체 row 찾아오기
		ChattingRoom chattingRoom = service.select(id);

		// 채팅방 id에 맞도록 삭제후 결과 저장
		int result = service.delete(id);

		// 친구 수 테이블 업데이트
		List<FriendsNum> allUser = friendsNumService.getAllUser();

		for (FriendsNum friendsNum : allUser) {
			String user_id = friendsNum.getUserId().toString(); // 업데이트할 user_id

			// user_id와 연결된 친구 목록 조회
			List<ChatFriend> friendsList = service.getMyFriends(user_id);

			// user_id에 해당하는 friendsNum 객체에 친구수 업데이트
			// 친구수는 친구목록의 size로 알수있음
			friendsNum.setFriendsNum(friendsList.size());

			// 수정한 친구 수 저장
			friendsNumService.update(friendsNum);
		}

		// 채팅방 삭제
		return result;
	}

	@PostMapping("")
	@ApiOperation("새로운 채팅방을 추가한다. 새로운 채팅방 생성시 채팅방 id, 실패시 0, 더이상 연결할 수 있는 친구가 없을 때-1 ")
	public ChatFriend create(@RequestParam String user_id) { // 파라미터에 user1에만 들어있음
		String user2Id = "";
		// 새로운 친구 연결
		log.info("새로운 친구목록 가져왔어요 1 ");
		List<FriendsNum> newFriendsList = friendsNumService.findNewFriends();
		log.info("newFriendsList>> ");
		log.info(newFriendsList.toString() + "\n");

		log.info("나랑 연결된 친구목록 가져왔어요 2 ");
		List<ChatFriend> myFriendsList = service.getMyFriends(user_id);
		log.info("myFriendsList>> ");
		log.info(myFriendsList.toString() + "\n");

		boolean firstConnect = true;
		boolean dontConnectMyself = true;

		if (newFriendsList.get(0).getFriendsNum() >= 3) { // 오름차순 정렬했는데 0번째 친구가 꽉찬거면 다 꽉찬거임
			log.warn("여기니?");
			return new ChatFriend(-1, -1);
		}
		for (FriendsNum newOne : newFriendsList) { // 5개(그거보다 작다면 최대 크기만큼) 중에
			// log.info("새로운 친구 = " + newOne.getId().toString());

			firstConnect = true;
			dontConnectMyself = true;

			if (newOne.getUserId().toString().equals(user_id)) { // 자기자신과 연결하지말것
				dontConnectMyself = false;
				log.info("나랑 똑같아요");
			}
			for (ChatFriend friend : myFriendsList) { // 이미 연결된 친구와 새로 연결할 친구가 다르면
				if (newOne.getUserId().toString().equals(friend.getFriend_id().toString())) {
					firstConnect = false;
					break;
				}
			}

			if (firstConnect && dontConnectMyself) {
				user2Id = newOne.getUserId().toString();
				log.info("user2Id : " + user2Id);
				break;
			}
		}

		if (user2Id.equals("")) { // 추가 가능한 친구 없음 (모든 친구 꽉 참)
			log.warn("여기니?");
			return new ChatFriend(-1, -1);
		}

		// friendsNum 의 테이블에 user1, user2 업데이트할것,
		FriendsNum user1 = friendsNumService.select(user_id);
		user1.setFriendsNum(user1.getFriendsNum() + 1);
		friendsNumService.update(user1);

		FriendsNum user2 = friendsNumService.select(user2Id);
		user2.setFriendsNum(user2.getFriendsNum() + 1);
		friendsNumService.update(user2);

		ChattingRoom chattingRoom = new ChattingRoom(user_id, user2Id, 0L, 0L, 0L, 0L);
		service.create(chattingRoom);
		
		// 초대당한 user2에게 FCM 보내기
		try {
			User target = userService.selectById(Integer.parseInt(user_id)); // user2Id
			tokenService.sendDataMessageTo(target.getToken(), "새로운 친구가 나를 발견했어요!", "앱에 접속해서 대화를 시작해보세요 😊 ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ChatFriend newChatFriend = new ChatFriend(user2.getUserId(), chattingRoom.getId());
		return newChatFriend; // 생성된 채팅룸 id 리턴
	}

	@GetMapping("/isUsed")
	@ApiOperation("현재 사용중인 채팅방 아이디인지 검사한다.\n 0이라면 사용가능한 채팅방 아이디이다.")
	public int isUsed(@RequestParam String id) {
		return service.isUsed(id);
	}

	@PutMapping("")
	@ApiOperation("request parameter로 전송된 채팅방 내용으로 채팅방을 업데이트한다.")
	public int update(@RequestBody ChattingRoom chattingRoom) {
		
		// user1 접속시간 < user2 채팅 전송시간 : user2가 채팅 보냄 => user1한테 메시지
		if(chattingRoom.getuser1LastVisitedTime() < chattingRoom.getUser2LastChattingTime()) {
			try {
				log.info("user2가 user1한테 채팅보냄!");
				User sender = userService.selectById(Integer.parseInt(chattingRoom.getUser2Id()));
				User target = userService.selectById(Integer.parseInt(chattingRoom.getUser1Id()));

				tokenService.sendDataMessageTo(target.getToken(), sender.getNickname(), "새로운 메시지가 도착했어요! 확인하려면 앱에 접속하세요😉");;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// user2 접속시간 < user1 채팅 전송시간 : user1가 채팅 보냄
		else if (chattingRoom.getUser2LastVisitedTime() < chattingRoom.getUser1LastChattingTime()) {
			log.info("user1이 user2 한테 채팅보냄!");
			try {
				User sender = userService.selectById(Integer.parseInt(chattingRoom.getUser1Id()));
				User target = userService.selectById(Integer.parseInt(chattingRoom.getUser2Id()));
				// target으로 바꿔야 함!
				tokenService.sendDataMessageTo(sender.getToken(), sender.getNickname() , "새로운 메시지가 도착했어요. 확인하려면 앱에 접속하세요😉");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		
		// chattingRoom update
		return service.update(chattingRoom);
	}

	@GetMapping("/getMyFriends")
	@ApiOperation("id와 연결된 친구목록 전체를 리턴한다. 없다면 null 리턴")
	public List<ChatFriend> getMyFriends(@RequestParam String id) {
		return service.getMyFriends(id);
	}
//	
//	@GetMapping("/getMyFriendsCount")
//	@ApiOperation("id와 연결된 친구목록 전체를 리턴한다. 없다면 null 리턴")
//	public int getMyFriends(@RequestParam String id) {
//		return service.getMyFriends(id);
//	}

}
