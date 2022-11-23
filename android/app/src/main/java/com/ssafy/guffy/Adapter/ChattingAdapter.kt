package com.ssafy.guffy.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ssafy.guffy.databinding.ItemChatMessageBinding
import com.ssafy.guffy.dto.ChattingItemDto
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "ChattingAdapter 구피"

class ChattingAdapter(var list:MutableList<ChattingItemDto>, val nickname: String) :
    RecyclerView.Adapter<ChattingAdapter.MessageHolder>() {

    val formatter = SimpleDateFormat("MM/dd H:mm", Locale("ko", "KR"))

    inner class MessageHolder(var binding: ItemChatMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindingInfo(item: ChattingItemDto) {
            Log.d(TAG, "bindingInfo: ${item.nickname} , ${item.message}, ${formatter.format(Date(item.time))}")
            if (item.nickname != nickname) { // 친구가 쓴 글.
                binding.otherSide.visibility = View.GONE
                binding.mySide.visibility = View.VISIBLE
                binding.myMessageTv.text = item.message
                binding.myTimeTv.text = formatter.format(Date(item.time)).toString()
            } else { // 내가 쓴 글.
                binding.otherSide.visibility = View.VISIBLE
                binding.mySide.visibility = View.GONE
                binding.otherMessageTv.text = item.message
                binding.otherTimeTv.text = formatter.format(Date(item.time)).toString()
            }
        }
    }

    // 뷰홀더 생성

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        var binding = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageHolder(binding)
    }

    // 뷰 재활용 될 때
    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.bindingInfo(list[position])
    }

    override fun getItemCount(): Int = list.size
}