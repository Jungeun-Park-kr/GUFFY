package com.ssafy.guffy.models

data class ChattingRoom(
    val id: Int,
    val user1Id: Int,
    val user1LastChattingTime: Int,
    val user1_last_visited_time: Int,
    val user2Id: Int,
    val user2LastChattingTime: Int,
    val user2LastVisitedTime: Int,
    var deleted:Int,
)