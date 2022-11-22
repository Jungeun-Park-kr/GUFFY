package com.ssafy.guffy.mainfragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.guffy.Adapter.FriendAdapter
import com.ssafy.guffy.ApplicationClass
import com.ssafy.guffy.ApplicationClass.Companion.retrofitChatroomInterface
import com.ssafy.guffy.ApplicationClass.Companion.retrofitService
import com.ssafy.guffy.ApplicationClass.Companion.wRetrofit
import com.ssafy.guffy.R
import com.ssafy.guffy.Service.RetrofitChatroomInterface
import com.ssafy.guffy.Service.RetrofitInterface
import com.ssafy.guffy.activity.ChattingActivity
import com.ssafy.guffy.activity.MainActivity
import com.ssafy.guffy.databinding.FragmentMainBinding
import com.ssafy.guffy.dialog.AlertWithMessageDialog
import com.ssafy.guffy.dialog.ConfirmDialog
import com.ssafy.guffy.dialog.FindingFriendDialog
import com.ssafy.guffy.dto.FriendItemDto
import com.ssafy.guffy.models.ChattingRoom
import com.ssafy.guffy.models.FriendListItem
import com.ssafy.guffy.util.Common
import com.ssafy.guffy.util.Common.Companion.showAlertDialog
import com.ssafy.guffy.util.Common.Companion.showAlertWithMessageDialog
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val TAG = "MainFragment_구피"
class MainFragment : Fragment() {
    private lateinit var binding:FragmentMainBinding
    private lateinit var mainActivity: MainActivity

    private lateinit var adapter:FriendAdapter
    private var friendList = mutableListOf<FriendItemDto>()

    private var friendsIdList = mutableListOf<FriendListItem>()

    private var username: String = ""
    private var userId: Int = 0
    private var userEmail:String = "je991025@gmail.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity

        val retrofitInterface = ApplicationClass.wRetrofit.create(RetrofitInterface::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            val result = retrofitInterface.getUser(userEmail)
            username = result.nickname
            userId = result.id
        }

        arguments?.let {
            //username = it.getString(ARG_PARAM1) // MainFragment() 호출시 유저 네임 같이 넘기기
            //userId = it.getString(ARG_PARAM2)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()

        // 설정 버튼 추가
        binding.mainSettingBtn.setOnClickListener {
            moveFragment(1, "", 0)
        }

        // 친구 추가 버튼 클릭
        binding.mainFriendAddBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val result = retrofitChatroomInterface.getFriendsNum(userId)
                Log.d(TAG, "onViewCreated: 나의 친구 수: $result")

                if(result.friendsNum < 3) { // 친구 3명 미만
                    mainActivity.showFindingFriendDialog()
                    // 여기서 친구 추가 작업 해주기
                    val retrofit = wRetrofit.create(RetrofitChatroomInterface::class.java)
                    val newFriendChat = retrofit.createChattingRoom(userId)
                    if(newFriendChat.chat_id==-1 && newFriendChat.friend_id==-1) {
                        // 더 이상 추가 가능한 친구가 없음
                        showAlertWithMessageDialog(mainActivity, "친구 추가 실패", "더이상 추가 가능한 친구가 없습니다.\n친구들에게 GUFFY앱을 추천해서 사용자를 늘려주세요!ㅎㅎ", "FriendsFullFail")
                    } else { // 추가 가능한 친구가 있는 경우
                        Log.d(TAG, "onViewCreated: 새로생긴 채팅룸id: ${newFriendChat.chat_id}")
                        if(newFriendChat.chat_id > 0) {

                            // 리스트에 새 친구 담기
                            launch(Dispatchers.Main) { // 뷰 관련된 것만 메인에서
                                addFriendInfo(newFriendChat.friend_id, newFriendChat.chat_id.toString())

                                // 새 친구 데이터 불러와서 리스트에 추가하기
                                adapter.notifyItemInserted(friendList.size) // 어댑터 갱신

                                showAlertWithMessageDialog(
                                    mainActivity,
                                    "새로운 친구를 추가했습니다.",
                                    "친구와 새로운 채팅을 시작해보세요!",
                                    "FriendsAddSucceeded"
                                )
                            }

                        } else {
                            launch(Dispatchers.Main) {
                                showAlertWithMessageDialog(mainActivity, "친구 추가를 실패했습니다.", "다시 시도해주세요.", "FriendsAddFailed")
                            }
                        }
                    }
                } else { // 친구 3명 꽉 찬 경우
                    showAlertWithMessageDialog(mainActivity, "더이상 추가할 수 없습니다", "친구는 최대 3명까지 추가할 수 있습니다.", "FriendsFull")
                }
            }
        }
    }

    private fun moveFragment(index:Int, key:String, value:Int){
        val transaction = parentFragmentManager.beginTransaction()
        when(index){
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

    private fun initData() {
        // 서버에서 필요한 데이터 가져오기

        //var list = ApplicationClass.retrofitService.getFriendIdList(userEmail)
        //Log.d(TAG, "initData: list: $list")

        // coroutine으로 호출 (코드 훨씬 짧아짐)
        CoroutineScope(Dispatchers.Main).launch {
            val myInfo = retrofitService.getUser(userEmail)
            username = myInfo.nickname
            Log.d(TAG, "내 닉네임: ${myInfo.nickname}")
            binding.userNameTv.text = username

            val result = retrofitService.getFriendIdList(userEmail)
            if(result.isNotEmpty()) {
                friendsIdList = result as MutableList<FriendListItem>
                Log.d(TAG, "friendsIdList: $${friendsIdList}")

                for(i in friendsIdList) {
                    if(i.deleted == 1) { // 친구가 날 삭제했음
                        friendList.add(FriendItemDto(i.friend_id,i.chat_id.toInt(),"대화 불가능한 사용자", "", "", 3, ""))
                    } else {
                        Log.d(TAG, "initData: FriendId: ${i.friend_id}, chatId: ${i.chat_id}, deleted: ${i.deleted}")
                        addFriendInfo(i.friend_id, i.chat_id)
                    }
                }

                delay(100) // 이거 없으면 아래 코드가 먼저 실행돼버림..
                //launch(Dispatchers.Main) {
                Log.d(TAG, "onViewCreated: friendlist: $friendList")
                adapter = FriendAdapter(friendList, username)
                adapter.setItemClickListener(object:FriendAdapter.OnItemClickListener{
                    override fun onClick(view: View, position: Int) {
                        // 이미 삭제된 친구의 경우
                        if(friendList[position].state == 3) { // 삭제 알리기
                            showAlertWithMessageDialog(mainActivity, "이미 삭제된 친구입니다.", "꾹 눌러서 채팅방을 나가주세요.", "FriendAlreadyDeleted")
                        } else {
                            val chatIntent = Intent(mainActivity, ChattingActivity::class.java)
                            chatIntent.putExtra("chatting_room_id", friendList[position].chatting_room_id)
                            chatIntent.putExtra("friend_nickname", friendList[position].name)
                            chatIntent.putExtra("user_nickname", username)
                            if(friendList[position].user == "user1") { // 친구가 user1이면 내가 user2
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
                binding.contactListRecyclerView.adapter = adapter
                binding.contactListRecyclerView.layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false)

                //}
            } else {
                Log.d(TAG, "initData: 친구 목록 없음")
            }

        }


        // 일반 방법으로 호출
//        retrofitInterface.friendsList(userEmail)
        //http://192.168.80.193:8080/user?email=je991025@gmail.com

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        val position = adapter.pos
        val selectedItem = friendList[position]
        val chattingRoomId = friendList[position].chatting_room_id
        lateinit var chattingRoom: ChattingRoom
        when(item.itemId) {
            R.id.context_menu_delete -> { // 채팅창 나가기 버튼

                val dialog = ConfirmDialog(object: ConfirmDialog.ConfirmDialogInterface {
                    override fun onYesButtonClick(id: String) {
                        // 여기서 삭제하기
                        Log.d(TAG, "onContextItemSelected: 채팅창 나가기 클릭")
                        // 채팅리스트에서 내 아이디 삭제
                        CoroutineScope(Dispatchers.IO).launch {
                            chattingRoom=retrofitChatroomInterface.getChattingRoom(chattingRoomId)
                            if(chattingRoom.deleted == 1) { // 이미 한쪽 나감
                                Log.d(TAG, "onContextItemSelected: 이미 친구가 날 삭제함 (삭제한 채팅방 번호): ${chattingRoom.id}")
                                retrofitChatroomInterface.deleteChattingRoom(chattingRoomId) // 완전 삭제
                            } else { // 내 아이디만 채팅창에서 삭제하기
                                chattingRoom.deleted = 1 // 삭제된 채팅방임을 표시하기
                                retrofitChatroomInterface.updateChattingRoom(chattingRoom) // 채팅창 id 상태 업데이트
                            }
                            launch(Dispatchers.Main){ // 그리고 리스트, Adapter에서 삭제하기
                                friendList.removeAt(position)
                                adapter.notifyItemRemoved(position)
                            }
                            // 친구 수 줄이기 (공통)
                            val friendsNum = retrofitChatroomInterface.getFriendsNum(userId)
                            friendsNum.friendsNum = friendsNum.friendsNum-1 // 친구 수 하나 빼기
                            retrofitChatroomInterface.updateFriendsNum(friendsNum) // DB 업데이트
                        }
                    }
                }, "친구 삭제", "진짜로 삭제하시겠습니까?", "")
                // 알림창이 띄워져있는 동안 배경 클릭 막기
                dialog.isCancelable = false
                dialog.show(mainActivity.supportFragmentManager, "ConfirmDialog")
            }
        }
        return super.onContextItemSelected(item)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    // 파라미터로 넘어온 id를 가진 친구를 friendList에 추가한다.
    suspend fun addFriendInfo(friendId:Int, chatId:String) {
        // coroutine으로 호출
        CoroutineScope(Dispatchers.IO).launch {
            val friend = ApplicationClass.retrofitService.getFriend(userId!!, friendId)
            Log.d(TAG, "friend: $friend")

            var interest = "#${friend.interest1} #${friend.interest2} #${friend.interest3}"
            if(friend.interest4 != "") {
                interest += " #${friend.interest4}"
            }
            if(friend.interest5 != "") {
                interest += " #${friend.interest5}"
            }
            var state = 0 // default
            if (friend.friend == "user1" && friend.user2_last_visited_time == 0L) {
                Log.d(TAG, "addFriendInfo: 내가 user2이고 첫접속 알림")
                // 내가 user2 <= 친구가 user1 이고 내가 아직 채팅방 안 들어간 경우 : 첫 접속을 알리기
                state = 1
            } else if (friend.friend == "user2" && friend.user1_last_visited_time == 0L) {
                Log.d(TAG, "addFriendInfo: 내가 user1이고 첫접속 알림")
                // 내가 user1 이고, 내가 아직 접속 안 한 경우
                state = 1 // 새로 추가된 상태임을 알리기

            } else {
                if(friend.friend == "user2" && (friend.user1_last_visited_time < friend.user2_last_chatting_time)) { // 내가 user1 <= 친구가 user2인경우
                    Log.d(TAG, "addFriendInfo: 내가 user1이고 새채팅 알림")
                    state = 2 // 새 메시지 온 경우
                } else if (friend.friend == "user1" && (friend.user2_last_visited_time < friend.user1_last_chatting_time)) { // 내가 user2
                    Log.d(TAG, "addFriendInfo: 내가 user2이고 새채팅 알림")
                    state = 2 // 새 메시지 온 경우
                }
            }
            // 친구 정보 담기
            friendList.add(FriendItemDto(friend.friend_id, chatId.toInt(),friend.nickname, friend.mbti, interest, state, friend.friend))
            Log.d(TAG, "initData: 추가되었는지 확인) ${friendList[friendList.size-1]}")
        }
    }


}