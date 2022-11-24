package com.ssafy.guffy.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.guffy.ApplicationClass
import com.ssafy.guffy.ApplicationClass.Companion.retrofitUserService
import com.ssafy.guffy.R
import com.ssafy.guffy.activity.LoginActivity
import com.ssafy.guffy.activity.MainActivity
import com.ssafy.guffy.databinding.FragmentLoginBinding
import com.ssafy.guffy.dialog.ConfirmNoCancelDialog
import com.ssafy.guffy.models.User
import com.ssafy.guffy.util.Common
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

private const val TAG = "LoginFragment 구피"
private lateinit var binding : FragmentLoginBinding
class LoginFragment : Fragment() {

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
}