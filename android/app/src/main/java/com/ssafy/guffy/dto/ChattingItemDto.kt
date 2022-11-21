package com.ssafy.guffy.dto

class ChattingItemDto {
    var nickname : String = ""
    var time : Long = 0
    var message : String = ""

    constructor(nickname: String, message: String, time : Long){
        this.nickname = nickname
        this.message = message
        this.time = time
    }

    constructor(){
    }

    override fun toString(): String {
        return "nickname : $nickname, message : $message, time : $time"
    }

}