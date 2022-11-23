package com.ssafy.guffy.models

data class ChattingRoom(
    val id: Int,
    val user1Id: Int,
    val user1LastChattingTime: Long,
    val user1LastVisitedTime: Long,
    val user2Id: Int,
    var user2LastChattingTime: Long,
    var user2LastVisitedTime: Long,
    var deleted:Int,
)