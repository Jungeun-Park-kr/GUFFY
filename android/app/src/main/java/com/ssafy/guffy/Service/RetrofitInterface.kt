package com.ssafy.guffy.Service

import com.ssafy.guffy.models.Friend
import com.ssafy.guffy.models.FriendListItem
import com.ssafy.guffy.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitInterface {

    // @GET( EndPoint-자원위치(URI) )
    @GET("/user/friend/friendsIds")
    fun getFriendIdList(@Query("email") email: String): Call<List<FriendListItem>>

    @GET("/user")
    suspend fun getUser(@Query("email") email : String):User

    @GET("/user/friend")
    suspend fun getFriend(@Query("friend_id") friendId:String):Friend

    @POST("/chatroom")
    suspend fun createChattingRoom(@Query("user_id") user_id:String):Int
}