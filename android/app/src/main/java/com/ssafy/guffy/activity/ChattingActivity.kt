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
import com.ssafy.guffy.databinding.ActivityChattingBinding
import com.ssafy.guffy.databinding.ItemChatMessageBinding
import com.ssafy.guffy.dto.ChattingItemDto
import java.text.SimpleDateFormat
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // main acitivity 에서 친구이름 받아오기
/*        myNickName = "지워니"
        friendNickname = "쩡으니"
        chattingRoomId = "1"*/

        myNickName = "쩡으니" // 내 닉네임은 저장해두자. String에
        friendNickname = "지워니"
        chattingRoomId = "1"

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
                }

            }

        }
    }


    fun initFirebase() {
        // firebase 데이터베이스 관리 객체 얻어오기
        thisChattingRoomRef = Firebase.database.getReference("chattingRoomId").child(chattingRoomId)

        childEventListener = object : ChildEventListener{
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