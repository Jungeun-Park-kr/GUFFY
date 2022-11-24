package com.ssafy.firebase_2
import retrofit2.Call
import retrofit2.http.*

interface FirebaseTokenService {
    // Token정보 서버로 전송
    @POST("token")
    fun uploadToken(@Query("token") token: String): Call<String>

}