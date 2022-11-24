package com.ssafy.guffy.Service

import com.ssafy.guffy.models.Token
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitFcmService {

    // Token정보 서버로 전송
    @POST("/token")
    fun uploadToken(@Query("token") token: Token): Call<String>


}