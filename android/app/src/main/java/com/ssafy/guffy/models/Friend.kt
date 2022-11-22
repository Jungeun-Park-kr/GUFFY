package com.ssafy.guffy.models

data class Friend(
    val chat_id: Int,
    val email: String,
    val friend: String,
    val friend_id: Int,
    val gender: String,
    val interest1: String,
    val interest2: String,
    val interest3: String,
    val interest4: String,
    val interest5: String,
    val mbti: String,
    val nickname: String,
    val user1_last_chatting_time: Long,
    val user1_last_visited_time: Long,
    val user2_last_chatting_time: Long,
    val user2_last_visited_time: Long
) {
    override fun toString(): String {
        return "id:${chat_id}, email:${email}, friend:$friend, friend_id:$friend_id," +
                "nickname:$nickname, mbti:$mbti"
    }
}