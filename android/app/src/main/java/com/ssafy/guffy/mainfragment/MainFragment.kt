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
import com.ssafy.guffy.dialog.FindingFriendDialog
import com.ssafy.guffy.dto.FriendItemDto
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
    private var userId: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity

        val retrofitInterface = ApplicationClass.wRetrofit.create(RetrofitInterface::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            val result = retrofitInterface.getUser("je991025@gmail.com")
            username = result.nickname
            userId = result.id
        }

        arguments?.let {
            //username = it.getString(ARG_PARAM1) // MainFragment() 호출시 유저 네임 같이 넘기기
            //userId = it.getString(ARG_PARAM2)
            userId = 1
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
                    Log.d(TAG, "onViewCreated: 33333")
                    // 여기서 친구 추가 작업 해주기
                    val retrofit = wRetrofit.create(RetrofitChatroomInterface::class.java)
                    val newFriendChat = retrofit.createChattingRoom(userId)
                    Log.d(TAG, "onViewCreated: 새로생긴 채팅룸id: ${newFriendChat.chat_id}")
                    if(newFriendChat.chat_id > 0) {
                        Log.d(TAG, "onViewCreated: 44444")
                        Log.d(TAG, "onViewCreated: 새 채팅창 생성 성공 + $newFriendChat")

                        launch(Dispatchers.Main) {
                            // 새 친구 데이터 불러와서 리스트에 추가하기
                            addFriendInfo(newFriendChat.friend_id, newFriendChat.chat_id.toString())
                            adapter.notifyItemInserted(friendList.size) // 어댑터 갱신

                            showAlertWithMessageDialog(
                                mainActivity,
                                "새로운 친구를 추가했습니다.",
                                "친구와 새로운 채팅을 시작해보세요!",
                                "FriendsAddSucceeded"
                            )
                            Log.d(TAG, "onViewCreated: 55555")
                        }

                    } else {
                        launch(Dispatchers.Main) {
                            showAlertWithMessageDialog(mainActivity, "친구 추가를 실패했습니다.", "다시 시도해주세요.", "FriendsAddFailed")
                        }

                    }

                } else { // 친구 3명 꽉 찬 경우
                    //launch(Dispatchers.Main) {
                        showAlertWithMessageDialog(mainActivity, "더이상 추가할 수 없습니다", "친구는 최대 3명까지 추가할 수 있습니다.", "FriendsFull")
                    //}

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
        var userEmail = "je991025@gmail.com"
        //var list = ApplicationClass.retrofitService.getFriendIdList(userEmail)
        //Log.d(TAG, "initData: list: $list")

        // coroutine으로 호출 (코드 훨씬 짧아짐)
        CoroutineScope(Dispatchers.IO).launch {
            val myInfo = ApplicationClass.retrofitService.getUser(userEmail)
            username = myInfo.nickname
            Log.d(TAG, "내 닉네임: ${myInfo.nickname}")

            val result = ApplicationClass.retrofitService.getFriendIdList(userEmail).awaitResponse()
            if(result.body() != null) {
                friendsIdList = result.body() as MutableList<FriendListItem>
                Log.d(TAG, "friendsIdList: $${friendsIdList}")
                /*result.enqueue(object:Callback<List<FriendListItem>> {
                    override fun onResponse(
                        call: Call<List<FriendListItem>>,
                        response: Response<List<FriendListItem>>
                    ) {
                        val response = response.body() as List<FriendListItem>
                        Log.d(TAG, "onResponse: response: $response")
                    }
    
                    override fun onFailure(call: Call<List<FriendListItem>>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })*/

                for(i in friendsIdList) {
                    if(i.friend_id == null) { // 친ㄹ구가 날 삭제했음
                        friendList.add(FriendItemDto(-1,-1,"대화 불가능한 사용자", "", "", 3, ""))
                    } else {
                        Log.d(TAG, "initData: FriendId: ${i.friend_id}, chatId: ${i.chat_id}")
                        addFriendInfo(i.friend_id.toInt(), i.chat_id)
                    }
                }
                CoroutineScope(Dispatchers.Main).launch {
                    Log.d(TAG, "onViewCreated: friendlist: $friendList")
                    adapter = FriendAdapter(friendList, username)
                    adapter.setItemClickListener(object:FriendAdapter.OnItemClickListener{
                        override fun onClick(view: View, position: Int) {
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
                    })
                    binding.contactListRecyclerView.adapter = adapter
                    binding.contactListRecyclerView.layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false)
                }
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

        when(item.itemId) {
            R.id.context_menu_delete -> { // 채팅창 나가기 버튼
                Log.d(TAG, "onContextItemSelected: 채팅창 나가기 클릭")
                // 채팅리스트에서 내 아이디 삭제

                // 친구 아이디 남아있는 경우 => 내 아이디만 채팅창에서 삭제
                // 친구 아이디도 null인 경우 => DB 테이블에서 이 채팅방 아예 삭제

                // 친구 수 줄이기
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
            if(friend.interest4 != null) {
                interest += " #${friend.interest4}"
            }
            if(friend.interest5 != null) {
                interest += " #${friend.interest5}"
            }
            var state = 0 // default
            if(friend.user2_last_chatting_time == 0L) { // 새로 추가된 경우 (아직 user2가 채팅 시작 안함)
                state = 1 // 새로 추가된 친구
            } else {
                if(friend.friend == "user2" && (friend.user1_last_visited_time < friend.user2_last_chatting_time)) { // 내가 user1 <= 친구가 user2인경우
                    state = 2 // 새 메시지 온 경우
                } else if (friend.user2_last_visited_time < friend.user1_last_chatting_time) { // 내가 user2
                    state = 2 // 새 메시지 온 경우
                }
            }
            // 친구 정보 담기
            friendList.add(FriendItemDto(friend.friend_id, chatId.toInt(),friend.nickname, friend.mbti, interest, state, friend.friend))
            Log.d(TAG, "initData: 추가되었는지 확인) ${friendList[friendList.size-1]}")
        }
    }
}