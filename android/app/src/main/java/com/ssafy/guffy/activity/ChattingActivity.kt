package com.ssafy.guffy.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ssafy.guffy.Adapter.ChattingAdapter
import com.ssafy.guffy.ApplicationClass
import com.ssafy.guffy.ApplicationClass.Companion.retrofitChatroomInterface
import com.ssafy.guffy.Service.RetrofitInterface
import com.ssafy.guffy.databinding.ActivityChattingBinding
import com.ssafy.guffy.databinding.ItemChatMessageBinding
import com.ssafy.guffy.dto.ChattingItemDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


private lateinit var binding: ActivityChattingBinding
private const val TAG = "ChattingActivity 구피"

class ChattingActivity : AppCompatActivity() {
    private var messageList = mutableListOf<ChattingItemDto>()
    private lateinit var chattingAdapter: ChattingAdapter
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var thisChattingRoomRef : DatabaseReference
    private lateinit var childEventListener:ChildEventListener
    //lateinit var firebase: FirebaseDatabase // firebase 데이터베이스 관리 객체참조변수
    lateinit var friendNickname: String
    var myNickName = "" // 내 닉네임은 저장해두자. String에
    lateinit var chattingRoomId: String

    private var lastChatTime:Long = 0
    
    private var myUser = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // mainFragment에서 넘어온 채팅방 아이디
        chattingRoomId = intent.getIntExtra("chatting_room_id", -1).toString()
        friendNickname = intent.getStringExtra("friend_nickname").toString()
        myNickName = intent.getStringExtra("user_nickname").toString()
        myUser = intent.getStringExtra("user").toString() // 내가 user1인지 user2인지 저장

        Log.d(TAG, "onCreate: 채팅방 아이디 : $chattingRoomId, 친구 이름:$friendNickname, 내이름:$myNickName")

        initView()
        initFirebase()
    }

    private fun initView() {
        // 액션바 보이게 해주고
        supportActionBar?.show()
        //액션바에 친구 이름 넣어주기
        supportActionBar?.title = friendNickname

        // 어댑터 생성 및 연결
        chattingAdapter = ChattingAdapter(messageList, friendNickname)
        chatRecyclerView = binding.chatRecyclerView
        chatRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        chatRecyclerView.adapter = chattingAdapter

        // 전송 버튼 클릭
        binding.chattingSendBtn.setOnClickListener {
            var message = binding.chattingMessageEt.text.toString()
            if(!TextUtils.isEmpty(message)){
                var time = System.currentTimeMillis()
                var chattingItem = ChattingItemDto("", myNickName, message, time)

                // Realtime database에 전송
                Log.d(TAG, "onCreate: db에 새로운 메세지 전송")
                thisChattingRoomRef.push().setValue(chattingItem).addOnSuccessListener {
                    // 성공시 edit text 지우기
                    binding.chattingMessageEt.setText("")
                    lastChatTime = time // 가장 최근에 채팅 보낸 시간 저장
                }

            }

        }
    }

    override fun onStop() {
        super.onStop()
        // 마지막에 채팅창에 머무른 시간 가져오기
        val lastVisitedTime = System.currentTimeMillis()

        // 서버에 마지막 채팅시간, 마지막 방문시간 저장하기
        CoroutineScope(Dispatchers.IO).launch {
            var chattingRoom = retrofitChatroomInterface.getChattingRoom(chattingRoomId.toInt())
            if(myUser == "user1") { // 내가 user1
                chattingRoom.user1LastChattingTime = lastChatTime
                chattingRoom.user1LastVisitedTime = lastVisitedTime
            } else { // 내가 user2
                chattingRoom.user2LastChattingTime = lastChatTime
                chattingRoom.user2LastVisitedTime = lastVisitedTime
            }
            retrofitChatroomInterface.updateChattingRoom(chattingRoom)
            Log.d(TAG, "onStop: 시간 갱신 완료! lastVisitedTime : $lastVisitedTime")
        }

    }


    private fun initFirebase() {
        // firebase 데이터베이스 관리 객체 얻어오기
        thisChattingRoomRef = Firebase.database.getReference("chattingRoomId").child(chattingRoomId)

        childEventListener = object : ChildEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded: $snapshot")
                var chattingItem = snapshot.getValue(ChattingItemDto::class.java)!!
                chattingItem.uid = snapshot.key ?: ""
                Log.d(TAG, "id: ${chattingItem.uid}")

                messageList.add(chattingItem)
                //notifyDatasetChanged() 사용 지양
                chattingAdapter.notifyItemInserted(messageList.size-1)
                binding.chatRecyclerView.smoothScrollToPosition(messageList.size-1)
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            @RequiresApi(Build.VERSION_CODES.N)
            @SuppressLint("NotifyDataSetChanged")
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        thisChattingRoomRef.addChildEventListener(childEventListener)
    }
}