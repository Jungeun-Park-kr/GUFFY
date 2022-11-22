package com.ssafy.guffy.Service

import com.ssafy.guffy.models.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitChatroomInterface {

    @GET("/friendsNum")
    suspend fun getFriendsNum(@Query("user_id") userId:Int): FriendsNum

    @PUT("/friendsNum")
    suspend fun updateFriendsNum(@Body friendsNum:FriendsNum):Int

    @POST("/chatroom")
    suspend fun createChattingRoom(@Query("user_id") user_id:Int): FriendChat

    @PUT("/chatroom")
    suspend fun updateChattingRoom(@Body chattingRoom:ChattingRoom):Int

    @GET("/chatroom")
    suspend fun getChattingRoom(@Query("id")chattingRoomId:Int): ChattingRoom

    @DELETE("/chatroom")
    suspend fun deleteChattingRoom(@Query("id") chattingRoomId: Int):Int
}