package com.ssafy.guffy.models

data class FriendListItem(
    val chat_id: String,
    val friend_id: Int,
    var deleted:Int
) {
    override fun toString(): String {
        return "chat_id: $chat_id, friend_id:$friend_id, deleted:$deleted"
    }
}