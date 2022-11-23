package com.ssafy.guffy.dto

data class FriendItemDto(
    val friend_id:Int,
    val chatting_room_id: Int,
    val name:String,
    val mbti:String,
    val interest:String,
    var state:Int,
    val user:String, // user1인지 user2인지 저장
) {
    constructor(): this(0,0,"", "","",0, "")

    override fun toString(): String {
        return "친구 닉네임: $name, friend_id: $friend_id, chattingroom_id:$chatting_room_id, state: $state"
    }
}