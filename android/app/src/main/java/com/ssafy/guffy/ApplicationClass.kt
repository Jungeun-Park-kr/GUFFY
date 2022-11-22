package com.ssafy.guffy;

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 앱이 실행될 때 1번만 실행이 됨
class ApplicationClass : Application() {

//    val SERVER_URL = "http://guffy.ssaverytime.kr:9999"
    val SERVER_URL = "http://192.168.80.193:8080"

    companion object {

        // 전역변수 문법을 통해 Retrofit 인스턴스를 앱 실행 시 1번만 생성하여 사용 (싱글톤 객체)
        lateinit var wRetrofit : Retrofit
        //lateinit var retrofitService:RetrofitService
    }

    override fun onCreate() {
        super.onCreate()

        // 앱이 처음 생성되는 순간, retrofit 인스턴스를 생성
        wRetrofit = Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        //retrofitService = wRetrofit.create(RetrofitService::class.java)
    }
}