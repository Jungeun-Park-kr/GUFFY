package com.ssafy.guffy.Adapter

import android.app.Activity
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.guffy.R
import com.ssafy.guffy.databinding.RecentContactListItemBinding
import com.ssafy.guffy.dto.FriendItemDto

private const val TAG = "FriendAdapter_구피"
class FriendAdapter(var list:MutableList<FriendItemDto>, val nickname: String)
    :RecyclerView.Adapter<FriendAdapter.FriendHolder>() {

    var pos = -1

    interface OnItemClickListener { // 리스너 인터페이스
        fun onClick(view:View, position: Int)
    }

    fun setItemClickListener(OnItemClickListener: OnItemClickListener) {
        this.itemClickListener = OnItemClickListener
    }

    private lateinit var itemClickListener: OnItemClickListener

    inner class FriendHolder(var binding:RecentContactListItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener {
        init {
            binding.root.setOnCreateContextMenuListener(this) // 아이템이 눌리면 onCreateContextMenu() 호출됨
            // 짱 중요 : 길게 클릭하면 position을 넣어줌 (context menu 에서 쓸거임)
            binding.root.setOnLongClickListener {
                pos = layoutPosition
                return@setOnLongClickListener false
            }
        }

        fun bindingInfo(item: FriendItemDto) {

            binding.root.setOnClickListener { itemClickListener.onClick(it, layoutPosition) }
            binding.contactListItemNameTv.text = item.name
            binding.contactListItemMbtiTv.text = item.mbti
            binding.contactListItemInterestTv.text = item.interest
            binding.contactListItemGenderTv.text = item.gender

            when(item.state) { // 친구와의 상태
                0 -> { // dafult : 아무것도 없음
                    Log.d(TAG, "bindingInfo: 디폴트 상태")
                    binding.contactListItemInformTv.visibility = View.GONE
                    binding.contactListItemIcon.visibility = View.INVISIBLE
                    //binding.contactListItemIcon.setImageIcon()
                }
                1-> { // 새로 추가
                    Log.d(TAG, "bindingInfo: 새로 추가 상태")
                    binding.contactListItemIcon.visibility = View.VISIBLE
                    binding.contactListItemIcon.setImageResource(R.drawable.ic_new_releases_24)
                    binding.contactListItemInformTv.text = "새로운 친구가 등록되었습니다. 대화를 나눠보아요."
                    binding.contactListItemInformTv.visibility = View.VISIBLE

                }
                2-> { // 새 메시지
                    Log.d(TAG, "bindingInfo: 새 메시지 상태")
                    binding.contactListItemInformTv.visibility = View.GONE
                    binding.contactListItemIcon.setImageResource(R.drawable.ic_notifications_active_24)
                    binding.contactListItemIcon.visibility = View.VISIBLE

                } 3-> { // 삭제됨
                Log.d(TAG, "bindingInfo: 삭제 상태")
                    binding.contactListItemInformTv.text = "채팅이 불가합니다. 채팅방을 삭제해주세요."
                    binding.contactListItemInformTv.visibility = View.VISIBLE
                    binding.contactListItemIcon.setImageResource(R.drawable.ic_priority_high_24)
                    binding.contactListItemIcon.visibility = View.VISIBLE
                }
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu,
            v: View,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val activity = v.context as Activity
            activity.menuInflater.inflate(R.menu.menu_context, menu)
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


