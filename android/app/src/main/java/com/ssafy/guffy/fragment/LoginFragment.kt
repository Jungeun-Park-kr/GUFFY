package com.ssafy.guffy.fragment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.guffy.ApplicationClass
import com.ssafy.guffy.ApplicationClass.Companion.retrofitFcmService
import com.ssafy.guffy.ApplicationClass.Companion.retrofitUserService
import com.ssafy.guffy.R
import com.ssafy.guffy.activity.LoginActivity
import com.ssafy.guffy.activity.MainActivity
import com.ssafy.guffy.databinding.FragmentLoginBinding
import com.ssafy.guffy.dialog.ConfirmNoCancelDialog
import com.ssafy.guffy.models.Token
import com.ssafy.guffy.models.User
import com.ssafy.guffy.util.Common
import com.ssafy.guffy.util.Common.Companion.channel_id
import com.ssafy.guffy.util.Common.Companion.uploadToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

private const val TAG = "LoginFragment 구피"
private lateinit var binding : FragmentLoginBinding
class LoginFragment : Fragment() {

    private var user_id = ""
    private lateinit var loginActivity: LoginActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivity = context as LoginActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.loginJoinBtn.setOnClickListener{
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_frame_container, JoinFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.loginFindPwBtn.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_frame_container, FindPwFragment())
                .addToBackStack(null)
                .commit()
        }


        binding.loginLoginbtn.setOnClickListener {
            val email = binding.joinEmailEditText.text.toString().trim()
            val pw = binding.loginPwEditText.text.toString()

            if (email.isNotEmpty() && pw.isNotEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    var user = User(email, pw)
                    var result = retrofitUserService.getLoginResult(user).awaitResponse().body()
                    if (result == null) { // 로그인 실패
                        Log.d(TAG, "로그인 실패")
                        Common.showAlertDialog(
                            loginActivity,
                            "로그인 실패했습니다",
                            ""
                        )
                    } else {  // 로그인 성공
                        result = result as User

                        Log.d(TAG, "받아온 user = ${user}")
                        ApplicationClass.editor.putString("nickname", result.nickname)
                        ApplicationClass.editor.putString("email", result.email)

                        Log.d(TAG, "sharedPreference에 저장된 값 불러오기")
                        Log.d(TAG, "nickname : ${ApplicationClass.sharedPreferences.getString("nickname", "").toString()}")
                        Log.d(TAG, "email : ${ApplicationClass.sharedPreferences.getString("email", "").toString()}")

                        val user = retrofitUserService.getUser(result.email).awaitResponse().body() as User

                        Log.d(TAG, "사용자 불러옵니다: $user")

                        ApplicationClass.editor.putString("id", user.id.toString())
                        ApplicationClass.editor.putBoolean("autoLogin", true)
                        ApplicationClass.editor.apply()
                        ApplicationClass.editor.commit()

                        Log.d(TAG, "sh) 사용자 id : ${ApplicationClass.sharedPreferences.getString("id", "")}")
                        Log.d(TAG, "sh) 사용자 autoLogin : ${ApplicationClass.sharedPreferences.getBoolean("autoLogin", false)}")


                        // 여기에서 토큰 생성해서 서버에 토큰 쏴주기
                        if(user.id > 0) {
                            Log.d(TAG, "onViewCreated: 로그인 성공 => 토큰 생성 가능!")
                            user_id = user.id.toString()
                            createFcmToken()
                        } else {
                            Log.d(TAG, "onViewCreated: 로그인 후 유저 id값 이상함 => 토큰 생성 실패...")
                        }
                        



                        val dialog = ConfirmNoCancelDialog(object: ConfirmNoCancelDialog.ConfirmNoCancelDialogInterface {
                            override fun onYesButtonClick(id: String) {
                                // 로그인 성공 후 확인 버튼 누를시 로그인 액티비티로 이동 및 로그인 화면 종료
                                startActivity(Intent(requireActivity(), MainActivity::class.java))
                                loginActivity.finish()
                            }
                        }, "로그인을 성공했습니다",
                            "", "")
                        dialog.isCancelable = false
                        dialog.show(loginActivity.supportFragmentManager, "networkUnAvailable")

                    }
                }
            } else if (email.isEmpty()) {
                Common.showAlertDialog(
                    loginActivity,
                    "아이디를 입력해주세요",
                    ""
                )
            } else if (pw.isEmpty()) {
                Common.showAlertDialog(
                    loginActivity,
                    "비밀번호를 입력해주세요",
                    ""
                )
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createFcmToken() {
        // FCM 토큰 수신
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "FCM 토큰 얻기에 실패하였습니다.", task.exception)
                return@OnCompleteListener
            }
            // token log 남기기
            Log.d(TAG, "createFcmToken: user_id : $user_id")
            Log.d(TAG, "token: ${task.result?:"task.result is null"}")
            if(task.result != null){
                uploadToken(task.result!!, user_id)
            }
        })
        createNotificationChannel(channel_id, "guffy")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    // Notification 수신을 위한 체널 추가
    private fun createNotificationChannel(id: String, name: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)

        val notificationManager: NotificationManager
                = loginActivity.getSystemService(Context .NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}