package com.ssafy.guffy.dto

data class ChattingItemDto (
    var uid: String,
    val nickname : String,
    val message : String,
    val time : Long = 0
) {
    constructor() : this("", "", "", 1L)

    override fun toString(): String {
        return "nickname : $nickname, message : $message, time : $time"
    }

}