package com.ssafy.guffy.Service

import com.ssafy.guffy.models.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitUserService {

    // @GET( EndPoint-자원위치(URI) )
    @GET("/user/friend/friendsIds")
    fun getFriendIdList(@Query("email") email: String): Call<List<FriendListItem>>

    @GET("/user/friend/friendsIds")
    suspend fun getFriendIdList2(@Query("email") email: String): List<FriendListItem>

    @GET("/user")
    suspend fun getUserSuspend(@Query("email") email : String):User

    @GET("/user")
    fun getUser(@Query("email") email: String): Call<User>

    @PUT("/user")
    fun updateUser(@Body user: User): Call<String>

    @GET("/user/friend")
    suspend fun getFriend(@Query("user_id")userId:Int, @Query("friend_id") friendId:Int):Friend

    @POST("/user/login")
    fun getLoginResult(@Body user: User): Call<User>

    @GET("/user/isUsed")
    fun getEmailIsUsed(@Query("email") email: String) : Call<String>

    @POST("/mail/sendAuth")
    fun getMainAuth(@Query("email") email: String) : Call<String>

    @POST("/user")
    fun addUser(@Body user: User) : Call<String>

    @POST("/mail/sendTempPw")
    fun changePw(@Query("email") email :String) : Call<String>

    @GET("/friendsNum")
    fun getMyFriendsNum(@Query("id") id : String) : Call<Int>

    @PUT("/user/password")
    fun updatePW(@Body user : User) : Call<String>
}