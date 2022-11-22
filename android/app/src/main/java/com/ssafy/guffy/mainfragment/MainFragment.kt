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
import com.ssafy.guffy.R
import com.ssafy.guffy.Service.RetrofitInterface
import com.ssafy.guffy.activity.ChattingActivity
import com.ssafy.guffy.activity.MainActivity
import com.ssafy.guffy.databinding.FragmentMainBinding
import com.ssafy.guffy.dto.FriendItemDto
import com.ssafy.guffy.models.FriendListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    
    private var username: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
        arguments?.let {
            username = it.getString(ARG_PARAM1) // MainFragment() 호출시 유저 네임 같이 넘기기
            param2 = it.getString(ARG_PARAM2)
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

            // 테스트용 : 채팅 액티비티로 이동
            moveFragment(3, "", 0)

            // 친구 자리 있는 경우
//            GlobalScope.launch {
//                mainActivity.showFindingFriendDialog()
//            }

            // 친구 꽉 찬 경우
            // showAlertWithMessageDialog(mainActivity, "더이상 추가할 수 없습니다", "친구는 최대 3명까지 추가할 수 있습니다.", "FriendsFull")
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
        val retrofitInterface = ApplicationClass.wRetrofit.create(RetrofitInterface::class.java)

        // coroutine으로 호출 (코드 훨씬 짧아짐)
        CoroutineScope(Dispatchers.IO).launch {
            val result = retrofitInterface.getUser(userEmail)
            username = result.nickname
            Log.d(TAG, "result: $result")
        }
        CoroutineScope(Dispatchers.IO).launch {
            val result = retrofitInterface.getFriendIdList(userEmail).awaitResponse()
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
                        val friendId = i.friend_id
                        Log.d(TAG, "initData: asdfasdf")
                        // coroutine으로 호출
                        CoroutineScope(Dispatchers.IO).launch {
                            val friend = retrofitInterface.getFriend(friendId)
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
                            friendList.add(FriendItemDto(friend.friend_id, i.chat_id.toInt(),friend.nickname, friend.mbti, interest, state, friend.friend))
                            Log.d(TAG, "initData: 추가되었는지 확인) ${friendList[friendList.size-1]}")
                        }
                    }


                }
                CoroutineScope(Dispatchers.Main).launch {
                    Log.d(TAG, "onViewCreated: friendlist: $friendList")
                    username = "가나다라마바사" // 나중에 로그인 한 뒤의 이름으로 바꾸기
                    adapter = FriendAdapter(friendList, username!!)
                    adapter.setItemClickListener(object:FriendAdapter.OnItemClickListener{
                        override fun onClick(view: View, position: Int) {
                            val chatIntent = Intent(mainActivity, ChattingActivity::class.java)
                            chatIntent.putExtra("chatting_room_id", friendList[position].chatting_room_id)
                            chatIntent.putExtra("friend_nickname", friendList[position].name)
                            chatIntent.putExtra("my_nickname", username)
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
}