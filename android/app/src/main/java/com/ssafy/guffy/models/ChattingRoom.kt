package com.ssafy.guffy.models

data class ChattingRoom(
    val id: Int,
    val user1Id: String,
    val user1LastChattingTime: Int,
    val user1_last_visited_time: Int,
    val user2Id: String,
    val user2LastChattingTime: Int,
    val user2LastVisitedTime: Int
)