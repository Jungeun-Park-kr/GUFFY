package com.ssafy.guffy.mainfragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ssafy.guffy.Adapter.FriendAdapter
import com.ssafy.guffy.ApplicationClass
import com.ssafy.guffy.ApplicationClass.Companion.retrofitChatroomService
import com.ssafy.guffy.ApplicationClass.Companion.retrofitUserService
import com.ssafy.guffy.ApplicationClass.Companion.wRetrofit
import com.ssafy.guffy.R
import com.ssafy.guffy.Service.RetrofitChatroomService
import com.ssafy.guffy.activity.ChattingActivity
import com.ssafy.guffy.activity.MainActivity
import com.ssafy.guffy.databinding.FragmentMainBinding
import com.ssafy.guffy.dialog.ConfirmDialog
import com.ssafy.guffy.dialog.ConfirmDialogInterface
import com.ssafy.guffy.dto.FriendItemDto
import com.ssafy.guffy.models.ChattingRoom
import com.ssafy.guffy.models.FriendListItem
import com.ssafy.guffy.models.User
import com.ssafy.guffy.util.Common.Companion.helloList
import com.ssafy.guffy.util.Common.Companion.showAlertWithMessageDialog
import kotlinx.coroutines.*
import retrofit2.awaitResponse
import kotlin.random.Random

private const val TAG = "MainFragment_구피"

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var mainActivity: MainActivity

    private lateinit var adapter: FriendAdapter
    private var friendList = mutableListOf<FriendItemDto>()
    private var friendsIdList = mutableListOf<FriendListItem>()
    private var myInterestList = mutableListOf<String>()

    private var userName: String = ""
    private var userId: Int = -1
    private var userEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity

        // 알림 권한 확인
        userName = ApplicationClass.sharedPreferences.getString("nickname", "").toString()
        userId = ApplicationClass.sharedPreferences.getString("id", "").toString().toInt()
        userEmail = ApplicationClass.sharedPreferences.getString("email", "").toString()

        Log.d(TAG, "onCreate: 이름: $userName, id: $userId, email: $userEmail")
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUserData()
        initFriendsData()
        binding.swipeRefreshLayout.setOnRefreshListener {
            CoroutineScope(Dispatchers.Main).launch {
                //mainActivity.recreate()
                withContext(Dispatchers.Main) {
                    initFriendsData()
                    delay(600)
                }
                if(friendList.size > 0)  adapter.notifyDataSetChanged()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        // 내 닉네임 보여주기
        binding.mainHelloTv.text = helloList[Random.nextInt(6)]
        binding.mainUserNameTv.text = userName
        binding.mainHelloTv.visibility = View.VISIBLE // 안녕하세요! 글씨 보이기

        // 설정 버튼 추가
        binding.mainSettingBtn.setOnClickListener {
            moveFragment(1, "", 0)
        }

        // 친구 추가 버튼 클릭
        binding.mainFriendAddBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val result = retrofitChatroomService.getFriendsNum(userId)
//                Log.d(TAG, "onViewCreated: 나의 친구 수: $result")

                if (result.friendsNum < 3) { // 친구 3명 미만
                    mainActivity.showFindingFriendDialog()
                    // 여기서 친구 추가 작업 해주기
                    val retrofit = wRetrofit.create(RetrofitChatroomService::class.java)
                    val newFriendChat = retrofit.createChattingRoom(userId)
//                    Log.d(TAG, "onViewCreated: 새로생긴 채팅룸id: ${newFriendChat.chat_id}")
                    if (newFriendChat.chat_id > 0) {
                        // 리스트에 새 친구 담기
                        launch(Dispatchers.Main) { // 뷰 관련된 것만 메인에서
                            addFriendInfo(newFriendChat.friend_id, newFriendChat.chat_id.toString())
                            delay(350) // DB에서 친구 정보 조회해서 추가 될 때까지 뷰 딜레이
                            Log.d(TAG, "onViewCreated: 추가됐는가????")
                            // 새 친구 데이터 불러와서 리스트에 추가하기
                            if(friendList.size > 0) adapter.notifyItemInserted(friendList.size) // 어댑터 갱신
                            showAlertWithMessageDialog(
                                mainActivity,
                                "새로운 친구를 추가했습니다.",
                                "친구와 새로운 채팅을 시작해보세요!",
                                "FriendsAddSucceeded"
                            )
                        }
                    } else {
                        launch(Dispatchers.Main) {
                            showAlertWithMessageDialog(
                                mainActivity,
                                "친구 추가를 실패했습니다.",
                                "다시 시도해주세요.",
                                "FriendsAddFailed"
                            )
                        }
                    }
                } else { // 친구 3명 꽉 찬 경우
                    showAlertWithMessageDialog(
                        mainActivity,
                        "더이상 추가할 수 없습니다",
                        "친구는 최대 3명까지 추가할 수 있습니다.",
                        "FriendsFull"
                    )
                }
            }
        }
    }

    private fun moveFragment(index: Int, key: String, value: Int) {
        val transaction = parentFragmentManager.beginTransaction()
        when (index) {
            //설정 화면 이동
            1 -> transaction.replace(R.id.frame_layout_main, SettingsFragment())
                .addToBackStack(null)
            //로그아웃 화면
            2 -> mainActivity.logout()
            // 채팅창 이동...
            3 -> {
                startActivity(Intent(mainActivity, ChattingActivity::class.java))
            }
        }
        transaction.commit()
    }

    private fun initUserData() {
        // 필요한 데이터 전부 가져와서 세팅하기

        // 1. 내 관심사 가져오기
        CoroutineScope(Dispatchers.Main).launch {
            myInterestList = mutableListOf()
            binding.mainChipGroup.removeAllViews()
            val user =
                retrofitUserService.getUser(userEmail).awaitResponse().body() as User

//            Log.d(TAG, "이메일로 검색한 user 정보: ${user}")
            myInterestList.add(user.interest1)
            myInterestList.add(user.interest2)
            myInterestList.add(user.interest3)
            if (user.interest4.isNotEmpty()) {
                myInterestList.add(user.interest4)
            }
            if (user.interest5.isNotEmpty()) {
                myInterestList.add(user.interest5)
            }
            // 내 관심사 보여주기
            for (i in myInterestList.indices) {
                // Chip 인스턴스 생성
                var chip = Chip(mainActivity)
                chip.isCheckable = false
                // 칩 고유 아이디 생성
                chip.id = i
                // Chip 의 텍스트 지정
                chip.text = myInterestList.get(i)
                chip.setChipBackgroundColorResource(R.color.none)
                chip.setChipStrokeColor(getColorStateList(mainActivity, R.color.orange))
                chip.setChipStrokeWidth(2.5F)

                // chip group 에 해당 chip 추가
                binding.mainChipGroup.addView(chip)
            }
//            Log.d(TAG, "내 관심사 받아오기: ${myInterestList}")

            // 내 mbti 보여주기
            binding.mainMbtiTv.text = user.mbti
        }
    }

    // 최근 연락 목록
    private fun initFriendsData() {
        // 서버에서 필요한 데이터 가져오기
        CoroutineScope(Dispatchers.IO).launch {
            friendList = mutableListOf()

            val result = retrofitUserService.getFriendIdList2(userEmail)
            if (result.isNotEmpty()) {
                friendsIdList = result as MutableList<FriendListItem>
                Log.d(TAG, "friendsIdList: $${friendsIdList}")

                for (i in friendsIdList) {
                    if (i.deleted == 1) { // 친구가 날 삭제했음
                        friendList.add(
                            FriendItemDto(
                                i.friend_id,
                                i.chat_id.toInt(),
                                "대화 불가능한 사용자",
                                "",
                                "",
                                "",
                                3,
                                ""
                            )
                        )
                    } else {
                        Log.d(TAG, "initData: FriendId: ${i.friend_id}, chatId: ${i.chat_id}")
                        addFriendInfo(i.friend_id, i.chat_id)
                    }
                }
                launch(Dispatchers.Main) {
                    Log.d(TAG, "onViewCreated: friendlist: $friendList")
                    adapter = FriendAdapter(friendList, userName)
                    binding.contactListRecyclerView.adapter = adapter
                    binding.contactListRecyclerView.layoutManager =
                        LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false)
                    adapter.setItemClickListener(object : FriendAdapter.OnItemClickListener {
                        override fun onClick(view: View, position: Int) {
                            // 이미 삭제된 친구의 경우
                            if (friendList[position].state == 3) { // 삭제 알리기
                                showAlertWithMessageDialog(
                                    mainActivity,
                                    "이미 삭제된 친구입니다.",
                                    "꾹 눌러서 채팅방을 나가주세요.",
                                    "FriendAlreadyDeleted"
                                )
                            } else {
                                val chatIntent = Intent(mainActivity, ChattingActivity::class.java)
                                chatIntent.putExtra(
                                    "chatting_room_id",
                                    friendList[position].chatting_room_id
                                )
                                chatIntent.putExtra("friend_nickname", friendList[position].name)
                                chatIntent.putExtra("user_nickname", userName)
                                if (friendList[position].user == "user1") { // 친구가 user1이면 내가 user2
                                    chatIntent.putExtra("user", "user2")
                                } else { // 친구가 user2이면 내가 user1
                                    chatIntent.putExtra("user", "user1")
                                }
                                startActivity(chatIntent)

                                // 채팅 상태 초기화
                                friendList[position].state = 0
                                adapter.notifyItemChanged(position)
                            }
                        }
                    })
                }
            } else {
                Log.d(TAG, "initData: 친구 목록 없음")
            }
        }
    }


    private fun deleteFirebase(roomId: Int) {
        // 현재 채팅방 id로 된 채팅방을 Firebase에서 삭제한다.
        var thisChattingRoomRef: DatabaseReference =
            Firebase.database.getReference("chattingRoomId").child(roomId.toString())
        thisChattingRoomRef.removeValue();
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = adapter.pos
        val chattingRoomId = friendList[position].chatting_room_id
        lateinit var chattingRoom: ChattingRoom
        when (item.itemId) {
            R.id.context_menu_delete -> { // 채팅창 나가기 버튼
                val dialog = ConfirmDialog(object : ConfirmDialogInterface {
                        override fun onYesButtonClick(id: String) {
                            Log.d(TAG, "onContextItemSelected: 채팅창 나가기 클릭")
                            // 채팅리스트에서 내 아이디 삭제
                            CoroutineScope(Dispatchers.IO).launch {
                                chattingRoom = retrofitChatroomService.getChattingRoom(chattingRoomId)
                                if (chattingRoom.deleted == 1) { // 이미 한쪽 나감
                                    Log.d(TAG, "onContextItemSelected: 이미 친구가 날 삭제함 (삭제한 채팅방 번호): ${chattingRoom.id}")
                                    retrofitChatroomService.deleteChattingRoom(chattingRoomId) // 완전 삭제
                                } else { // 내 아이디만 채팅창에서 삭제하기
                                    chattingRoom.deleted = 1 // 삭제된 채팅방임을 표시하기
                                    retrofitChatroomService.updateChattingRoom(chattingRoom) // 채팅창 id 상태 업데이트
                                    // 친구 수 줄이기
                                    val friendsNum = retrofitChatroomService.getFriendsNum(userId)
                                    friendsNum.friendsNum = friendsNum.friendsNum - 1 // 친구 수 하나 빼기
                                    retrofitChatroomService.updateFriendsNum(friendsNum) // DB 업데이트
                                }
                                // Firebase DB 삭제
                                deleteFirebase(chattingRoomId)
                            }
                            CoroutineScope(Dispatchers.Main).launch {
                                // 그리고 리스트, Adapter에서 삭제하기
                                friendList.removeAt(position)
                                adapter.notifyItemRemoved(position)
                            }
                        }
                    }, "정말로 채팅방을 나가시겠습니까?",
                    "다음에는 이 친구와 못 만날지도 몰라요!\n그래도 나가시겠어요?", ""
                )
                dialog.isCancelable = false
                dialog.show(mainActivity.supportFragmentManager, "networkUnAvailable")
            }
        }
        return super.onContextItemSelected(item)
    }


    // 파라미터로 넘어온 id를 가진 친구를 friendList에 추가한다.
    suspend fun addFriendInfo(friendId: Int, chatId: String) {
        // coroutine으로 호출
        CoroutineScope(Dispatchers.IO).launch {
            val friend = retrofitUserService.getFriend(userId!!, friendId)
            Log.d(TAG, "friend: $friend")

            val gender: String = if (friend.gender == "M") {
                "(남)"
            } else if (friend.gender == "F") {
                "(여)"
            } else {
                "(비공개)"
            }

            var interest = "#${friend.interest1} #${friend.interest2} #${friend.interest3}"
            Log.d(TAG, "addFriendInfo: interest4: ${friend.interest4}, interest5: ${friend.interest5}")
            if (!friend.interest4.isNullOrEmpty()) {
                interest += " #${friend.interest4}"
            }
            if (!friend.interest5.isNullOrEmpty()) {
                interest += " #${friend.interest5}"
            }
            var state = 0 // default
            // 내가 유저 1, 친구가 유저 2 이고, 내가 채팅 시작 했을때
            if (friend.friend == "user2" && friend.user1_last_visited_time == 0L) { // 새로 추가된 경우 (아직 user2가 채팅 시작 안함)
                state = 1 // 새로 추가된 친구
            } else if (friend.friend == "user1" && friend.user2_last_visited_time == 0L) { // 나:유저2, 친구:유저1, 내가 아직 접속 안 한 경우
                state = 1 // 새로 추가된 친구
            } else {
                if (friend.friend == "user2" && (friend.user1_last_visited_time < friend.user2_last_chatting_time)) { // 내가 user1 <= 친구가 user2인경우
                    state = 2 // 새 메시지 온 경우
                } else if (friend.friend == "user1" && (friend.user2_last_visited_time < friend.user1_last_chatting_time)) { // 내가 user2
                    state = 2 // 새 메시지 온 경우
                }
            }
            // 친구 정보 담기
            friendList.add(
                FriendItemDto(
                    friend.friend_id,
                    chatId.toInt(),
                    friend.nickname,
                    gender,
                    friend.mbti,
                    interest,
                    state,
                    friend.friend
                )
            )
            Log.d(TAG, "initData: 추가되었는지 확인) ${friendList[friendList.size - 1]}")
        }
    }
}