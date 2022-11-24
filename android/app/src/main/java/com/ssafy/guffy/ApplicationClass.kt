package com.ssafy.guffy;

import android.app.Application
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ssafy.guffy.Service.RetrofitChatroomService
import com.ssafy.guffy.Service.RetrofitFcmService
import com.ssafy.guffy.Service.RetrofitUserService
import com.ssafy.guffy.util.CheckNetwork
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

// 앱이 실행될 때 1번만 실행이 됨
class ApplicationClass : Application() {

    //    val SERVER_URL = "http://guffy.ssaverytime.kr:9999"
    val SERVER_URL = "http://192.168.100.141:9999"

    companion object {
        // 전역변수 문법을 통해 Retrofit 인스턴스를 앱 실행 시 1번만 생성하여 사용 (싱글톤 객체)
        lateinit var wRetrofit : Retrofit
        lateinit var retrofitUserService:RetrofitUserService
        lateinit var retrofitChatroomService: RetrofitChatroomService
        lateinit var retrofitFcmService: RetrofitFcmService

        // 로그인 정보를 담기 위한 sharedPreference
        lateinit var sharedPreferences: SharedPreferences
        lateinit var editor : SharedPreferences.Editor
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate() {
        super.onCreate()

        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.commit() // data 저장

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()
        // 앱이 처음 생성되는 순간, retrofit 인스턴스를 생성
        wRetrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofitUserService = wRetrofit.create(RetrofitUserService::class.java)
        retrofitChatroomService = wRetrofit.create(RetrofitChatroomService::class.java)
        retrofitFcmService = wRetrofit.create(RetrofitFcmService::class.java)

        // 네트워크에 연결되어있는지 확인 후 없으면 앱 종료 시키기위해 네트워크 연결상태 감지 콜백 생성시켜두기
        val network: CheckNetwork = CheckNetwork(applicationContext)
        network.registerNetworkCallback()
    }

    private val nullOnEmptyConverterFactory = object : Converter.Factory() {
        fun converterFactory() = this
        override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object :
            Converter<ResponseBody, Any?> {
            val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
            override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) {
                try{
                    nextResponseBodyConverter.convert(value)
                }catch (e:Exception){
                    e.printStackTrace()
                    null
                }
            } else{
                null
            }
        }
    }
}