package com.ssafy.guffy.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.ssafy.guffy.Adapter.ChattingAdapter
import com.ssafy.guffy.databinding.ActivityChattingBinding
import com.ssafy.guffy.dto.ChattingItemDto
import java.util.Calendar

private lateinit var binding: ActivityChattingBinding
private const val TAG = "ChattingActivity 구피"

class ChattingActivity : AppCompatActivity() {
    var messageList = mutableListOf<ChattingItemDto>()
    lateinit var chattingAdapter: ChattingAdapter
    lateinit var thisChattingRoomRef : DatabaseReference
    lateinit var firebase: FirebaseDatabase // firebase 데이터베이스 관리 객체참조변수
    lateinit var friendNickname: String
    lateinit var chattingRoomId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // main acitivity 에서 친구이름 받아오기
        friendNickname = "지워니"
        chattingRoomId = "1"
        var myNickName = "쩡으니" // 내 닉네임은 저장해두자. String에

        // 액션바 보이게 해주고
        supportActionBar?.show()
        //액션바에 친구 이름 넣어주기
        supportActionBar?.title = friendNickname

        // 어댑터 생성 및 연결
        chattingAdapter = ChattingAdapter(messageList, friendNickname)
        binding.chatRecyclerView.adapter = chattingAdapter

        // firebase 데이터베이스 관리 객체 얻어오기
        firebase = FirebaseDatabase.getInstance()
        thisChattingRoomRef = firebase.reference.child("chattingRoomId").child(chattingRoomId)

        binding.chattingSendBtn.setOnClickListener {
            var message = binding.chattingMessageEt.text.toString()
            if(!TextUtils.isEmpty(message)){
                var time = System.currentTimeMillis()
                var chattingItem = ChattingItemDto(myNickName, message, time)


                // Realtime database에 전송
                Log.d(TAG, "onCreate: db에 새로운 메세지 전송")
                thisChattingRoomRef.push().setValue(chattingItem)

                // edit text 지우기
                binding.chattingMessageEt.setText("")
            }

        }

        lateinit var childEventListener:ChildEventListener
        childEventListener = object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded: $snapshot")
                val chattingItem = snapshot.getValue(ChattingItemDto::class.java)
                if (chattingItem != null) {
                    messageList.add(chattingItem)
                }
                //notifyDatasetChanged() 사용 지양
                chattingAdapter.notifyItemInserted(messageList.size)
                binding.chatRecyclerView.smoothScrollToPosition(messageList.size)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                chattingAdapter.notifyDataSetChanged()
                binding.chatRecyclerView.smoothScrollToPosition(messageList.size)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }

        thisChattingRoomRef.addChildEventListener(childEventListener)



//        thisChattingRoomRef.addValueEventListener(object :ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                messageList.clear()
//                for (chat in snapshot.children){
//                    messageList.add(chat.getValue(ChattingItemDto::class.java)!!)
//                }
//                Log.d(TAG, "onDataChange: ${messageList}")
//
//                chattingAdapter.insertList(messageList)
//                chattingAdapter.notifyItemInserted(messageList.size)
//                binding.chatRecyclerView.smoothScrollToPosition(messageList.size)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.d(TAG, "onCancelled: Fail to read value")
//            }
//        })
    }
}