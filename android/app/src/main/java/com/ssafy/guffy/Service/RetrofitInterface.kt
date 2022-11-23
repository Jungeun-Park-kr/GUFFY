package com.ssafy.guffy.Service

import com.ssafy.guffy.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitInterface {

    // @GET( EndPoint-자원위치(URI) )
    @GET("/user/friend/friendsIds")
    suspend fun getFriendIdList(@Query("email") email: String): List<FriendListItem>

    @GET("/user")
    suspend fun getUser(@Query("email") email : String):User

    @GET("/user/friend")
    suspend fun getFriend(@Query("user_id")userId:Int, @Query("friend_id") friendId:Int):Friend

}