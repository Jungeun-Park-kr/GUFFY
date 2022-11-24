package com.ssafy.guffy.models

import com.google.gson.annotations.SerializedName

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
    val token: String, // 친구 토큰
    val mbti: String,
    val nickname: String,
    val user1_last_chatting_time: Long,
    val user1_last_visited_time: Long,
    val user2_last_chatting_time: Long,
    val user2_last_visited_time: Long
) {

    @SerializedName("name")
    private val bodyValue: String? = null

    override fun toString(): String {
        return "id:${chat_id}, email:${email}, friend:$friend, friend_id:$friend_id," +
                "nickname:$nickname, mbti:$mbti" +
                "i1:$interest1, i2:$interest2, i3:$interest3, i4:$interest4, i5:$interest5" +
                "visit_time_1:$user1_last_visited_time, chat_time_1: $user1_last_chatting_time" +
                "visit_time_2:$user2_last_visited_time, chat_time_2: $user2_last_chatting_time"
    }
}