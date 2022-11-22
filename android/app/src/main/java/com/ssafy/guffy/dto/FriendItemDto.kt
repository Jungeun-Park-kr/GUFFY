package com.ssafy.guffy.dto

data class FriendItemDto(
    val friend_id:Int,
    val chatting_room_id: Int,
    val name:String,
    val mbti:String,
    val interest:String,
    val state:Int,
) {
    constructor(): this(0,0,"", "","",0)
}