package com.ssafy.guffy.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.guffy.databinding.RecentContactListItemBinding
import com.ssafy.guffy.dto.FriendItemDto

class FriendAdapter(var list:MutableList<FriendItemDto>, val nickname: String)
    :RecyclerView.Adapter<FriendAdapter.FriendHolder>() {

    inner class FriendHolder(var binding:RecentContactListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindingInfo(item: FriendItemDto) {
            binding.contactListItemNameTv.text = item.name
            binding.contactListItemMbtiTv.text = item.mbti
            binding.contactListItemInterestTv.text = item.interest

            when(item.state) { // 친구와의 상태
                0 -> { // dafult : 아무것도 없음
                    //binding.contactListItemIcon.setImageIcon()
                }
                1-> { // 새로 추가

                }
                2-> { // 새 메시지

                } 3-> { // 삭제됨

                }
            }

            if(true) { // 새로 추가된 경우
                binding.contactListItemInformTv.visibility = View.VISIBLE
            } else {
                binding.contactListItemInformTv.visibility = View.GONE
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendHolder {
        val binding = RecentContactListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendHolder, position: Int) {
        holder.bindingInfo(list[position])
    }

    override fun getItemCount(): Int = list.size
}


