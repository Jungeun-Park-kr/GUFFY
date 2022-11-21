package com.ssafy.guffy.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.guffy.R
import com.ssafy.guffy.databinding.ActivityChattingBinding
import com.ssafy.guffy.databinding.ItemChatMessageBinding
import com.ssafy.guffy.dto.ChattingItemDto
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "ChattingAdapter 구피"

class ChattingAdapter(val list:MutableList<ChattingItemDto>, val nickname: String) :
    RecyclerView.Adapter<ChattingAdapter.MessageHolder>() {

    var dataformat = SimpleDateFormat("MM/dd hh:mm", Locale("ko", "KR"))

    inner class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindingInfo(item: ChattingItemDto) {
            Log.d(TAG, "bindingInfo: ${item.nickname} , ${item.message}, ${dataformat.format(Date(item.time))}")
            if (item.nickname != nickname) { // 친구가 쓴 글.
                binding.otherSide.visibility = View.GONE
                binding.mySide.visibility = View.VISIBLE
                binding.myMessageTv.text = item.message
                binding.myTimeTv.text = dataformat.format(Date(item.time)).toString()
            } else { // 내가 쓴 글.
                binding.otherSide.visibility = View.VISIBLE
                binding.mySide.visibility = View.GONE
                binding.otherMessageTv.text = item.message
                binding.otherTimeTv.text = dataformat.format(Date(item.time)).toString()
            }
        }
    }

    // 뷰홀더 생성
    private lateinit var binding: ItemChatMessageBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        binding = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageHolder(binding.root)
    }

    // 뷰 재활용 될 때
    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.bindingInfo(list[position])
    }

    override fun getItemCount(): Int = list.size
}