package com.ssafy.guffy.Service

import com.ssafy.guffy.models.Friend
import com.ssafy.guffy.models.FriendListItem
import com.ssafy.guffy.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {

    // @GET( EndPoint-자원위치(URI) )
    @GET("/user/friend/friendsIds")
    fun getFriendIdList(@Query("email") email: String): Call<List<FriendListItem>>

    @GET("/user")
    suspend fun getUser(@Query("email") email : String):User

    @GET("/user/friend")
    suspend fun getFriend(@Query("friend_id") friendId:String):Friend


}