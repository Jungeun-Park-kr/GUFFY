package com.ssafy.guffy.Service

import com.ssafy.guffy.models.*
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
    suspend fun getFriend(@Query("user_id")userId:Int, @Query("friend_id") friendId:Int):Friend

    @POST("/chatroom")
    suspend fun createChattingRoom(@Query("user_id") user_id:Int): FriendChat

    @GET("/friendsNum")
    suspend fun getFriendsNum(@Query("user_id") userId:Int): FriendsNum

}