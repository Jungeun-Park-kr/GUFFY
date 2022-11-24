package com.ssafy.guffy.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.guffy.ApplicationClass.Companion.retrofitUserService
import com.ssafy.guffy.R
import com.ssafy.guffy.activity.LoginActivity
import com.ssafy.guffy.databinding.FragmentFindPwBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

private const val TAG = "FindPwFragment 구피"

class FindPwFragment : Fragment() {
    private lateinit var binding: FragmentFindPwBinding
    private lateinit var loginActivity: LoginActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginActivity = context as LoginActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFindPwBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.findpwTempPwSendBtn.setOnClickListener {
            val email = binding.joinEmailEditText.text.toString().trim()

            CoroutineScope(Dispatchers.Main).launch {

                if (email.isNotEmpty()) {

                    var isValidUser = retrofitUserService.getEmailIsUsed(email).awaitResponse().body() as String
                    Log.d(TAG, "onViewCreated: DB에 있는지 검사한 결과 body >> ${isValidUser}")
                    if (isValidUser == "no"){
                        Log.d(TAG, "onViewCreated: DB에 없는 이메일")
                        val builder = AlertDialog.Builder(requireContext())
                        builder
                            .setTitle("회원가입을 진행해주세요")
                            .setMessage("가입한 기록이 없습니다.")
                            .setPositiveButton("OK") { dialog, which ->
                                Log.d(TAG, "showDialog: 확인버튼 누름")
                            }
                            .create()
                            .show()
                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.login_frame_container, JoinFragment())
                            .commit()
                    }
                    else{ // 디비에 있는 이메일임
                        var result = retrofitUserService.changePw(email).awaitResponse().body() ?: "null"
                        Log.d(TAG, "onViewCreated: 새로운 비밀번호 발송 성공 + ${result}")
                        if (result == "success") {
                            Log.d(TAG, "onViewCreated: 새로운 비밀번호 발송 성공 + $result")
                            showDialog()
                            requireActivity().supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.login_frame_container, LoginFragment())
                                .commit()
                        }
                    }
                    
                } else {
                    // 다이어로그 생성 함수
                    val builder = AlertDialog.Builder(requireContext())
                    builder
                        .setTitle("이메일을 입력해주세요")
                        .setPositiveButton("OK") { dialog, which ->
                            Log.d(TAG, "showDialog: 확인버튼 누름")
                        }
                        .create()
                        .show()
                }
            }
        }
    }

    // 다이어로그 생성 함수
    fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder
            .setTitle("비밀번호 찾기")
            .setMessage("회원가입한 이메일로 임시 비밀번호가 전송되었습니다")
            .setPositiveButton("OK") { dialog, which ->
                Log.d(TAG, "showDialog: 확인버튼 누름")
            }
            .create()
            .show()

    }
}