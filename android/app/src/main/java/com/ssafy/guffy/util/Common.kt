package com.ssafy.guffy.util

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.guffy.ApplicationClass
import com.ssafy.guffy.dialog.AlertDialog
import com.ssafy.guffy.dialog.AlertWithMessageDialog
import com.ssafy.guffy.models.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "Common 구피"
class Common {

    companion object {
        var helloList = listOf("안녕하세요 ~", "반갑습니다 !", "좀 더 자주 오세요 ~~ ", "자주 오시네요 !! ", "오랜만이네요..", "또 뵙네요  !")
        var interstList = listOf(
            "안드로이드",
            "iOS",
            "백엔드",
            "프론트엔드",
            "JAVA",
            "C",
            "JS",
            "Kotlin",
            "기획",
            "풀스택",
            "DB",

            "K-POP",
            "J-POP",
            "POP",
            "힙합",
            "재즈",
            "클래식",
            "EDM",
            "발라드",
            "포크",
            "디스코",
            "트로트",

            "액션 영화",
            "범죄 영화",
            "SF 영화",
            "코미디 영화",
            "로맨스 코미디 영화",
            "공포 영화",
            "스릴러 영화",
            "판타지 영화",
            "뮤지컬 영화",
            "멜로 영화",
            "애니메이션",
            "마블시리즈",
            "DC시리즈",
            "PC방",
            "노래방",
            "먹방",
            "페스티벌",
            "방탈출 카페",
            "캠핑",
            "아이스 스케이트",
            "요리",
            "보드게임",

            "주짓수",
            "크로스핏",
            "스키",
            "스노우 보드",
            "수영",
            "탁구",
            "러닝",
            "체조",
            "헬스",
            "요가",
            "배구",
            "등산",
            "야구",
            "축구",
            "낚시",
            "백패킹",
            "볼링",

            "수제 맥주",
            "와인",
            "피크닉",
            "넷플릭스",
            "국내여행",
            "해외여행"
        )

        var mbtiList = listOf(
            "ISFP", "ISFJ", "ISTP", "ISTJ",
            "INTJ", "INTP", "ESTP", "ESTJ",
            "ENTP", "ENTJ", "INFP", "INFJ",
            "ESFP", "ESFJ", "ENFP", "ENFJ",
            "모름", "싫어함", "비공개"
        )

        var genderList = listOf(
            "남자", "여자", "비공개"
        )

        fun passwordRegex(password: String): Boolean {
            // 영문, 특수문자, 숫자 포함 8자 이상
            var regExp = """^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%*^+\-=])(?=\S+$).*$"""
            if (password.length > 15 || password.length < 5) return false
            return password.matches(regExp.toRegex())
        }

        fun emailRegex(email: String): Boolean {
            // 영문, 특수문자, 숫자 포함 8자 이상
            var regExp = """^[a-zA-Z0-9+-\_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+${'$'}"""
            return email.matches(regExp.toRegex())
        }

        fun showAlertDialog(context: AppCompatActivity, title:String, tag:String) {
            val dialog = AlertDialog(context, title)
            // 알림창이 띄워져있는 동안 배경 클릭 막기
            dialog.isCancelable = false
            dialog.show(context.supportFragmentManager, tag)
        }

        fun showAlertWithMessageDialog(context:AppCompatActivity, title:String, message:String, tag:String) {
            val dialog = AlertWithMessageDialog(context, title, message)
            // 알림창이 띄워져있는 동안 배경 클릭 막기
            dialog.isCancelable = false
            dialog.show(context.supportFragmentManager, tag)

        }

        // Global variable used to store network state
        var isNetworkConnected = false

        // FCM Channel Id
        const val channel_id = "guffy_channel"

        // ratrofit  수업 후 network 에 업로드 할 수 있도록 구성
        fun uploadToken(token:String, userId:String){
            // 새로운 토큰 수신 시 서버로 전송
            ApplicationClass.retrofitFcmService.uploadToken(token,userId).enqueue(object :
                Callback<Token> {
                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    if(response.isSuccessful){
                        val res = response.body()
                        Log.d(TAG, "onResponse: $res")
                    } else {
                        Log.d(TAG, "onResponse: Error Code ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Log.d(TAG, t.message ?: "토큰 정보 등록 중 통신오류")
                }
            })
        }
    }


}