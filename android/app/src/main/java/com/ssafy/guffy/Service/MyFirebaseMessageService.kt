package com.ssafy.guffy.Service

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ssafy.guffy.ApplicationClass
import com.ssafy.guffy.R
import com.ssafy.guffy.activity.LoginActivity
import com.ssafy.guffy.activity.MainActivity
import com.ssafy.guffy.fragment.LoginFragment
import com.ssafy.guffy.models.Token
import com.ssafy.guffy.util.Common.Companion.channel_id
import com.ssafy.guffy.util.Common.Companion.uploadToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "MyFirebaseMsgSvc_싸피"

class MyFirebaseMessageService : FirebaseMessagingService() {
    // 새로운 토큰이 생성될 때 마다 해당 콜백이 호출된다.
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")
        // 새로운 토큰 수신 시 서버로 전송
        val userName = ApplicationClass.sharedPreferences.getString("nickname", "").toString()
        uploadToken(token, userName)
    }

    // Foreground, Background 모두 처리하기 위해서는 data에 값을 담아서 넘긴다.
    //https://firebase.google.com/docs/cloud-messaging/android/receive
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var messageTitle = ""
        var messageContent = ""

        if(remoteMessage.notification != null){ // notification이 있는 경우 foreground처리
            //foreground
            messageTitle= remoteMessage.notification!!.title.toString()
            messageContent = remoteMessage.notification!!.body.toString()

        }else{  // background 에 있을경우 혹은 foreground에 있을경우 두 경우 모두
            var data = remoteMessage.data
            Log.d(TAG, "data.message: ${data}")
            Log.d(TAG, "data.message: ${data.get("title")}")
            Log.d(TAG, "data.message: ${data.get("body")}")

            messageTitle = data.get("title").toString()
            messageContent = data.get("body").toString()
        }

        // TODO : 로그인 유무에 따라 noti 타고 들어왔을때 보이는 화면을 다르게 하기
        val isLogined = ApplicationClass.sharedPreferences.getBoolean("autoLogin", false)
        if(isLogined) { // 메인 화면으로
            val mainIntent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val mainPendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE)

            val builder1 = NotificationCompat.Builder(this, channel_id)
                .setSmallIcon(R.drawable.ic_guffy_launcher_round)
                .setContentTitle(messageTitle)
                .setContentText(messageContent)
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent)

            NotificationManagerCompat.from(this).apply {
                notify(101, builder1.build())
            }
        }
    }

}