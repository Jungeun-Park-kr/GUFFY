package com.ssafy.guffy.settingsfragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.guffy.ApplicationClass
import com.ssafy.guffy.ApplicationClass.Companion.retrofitUserInterface
import com.ssafy.guffy.R
import com.ssafy.guffy.Service.RetrofitUserInterface
import com.ssafy.guffy.activity.ChattingActivity
import com.ssafy.guffy.activity.MainActivity
import com.ssafy.guffy.databinding.FragmentTabItemInterestBinding
import com.ssafy.guffy.databinding.FragmentTabItemPwBinding
import com.ssafy.guffy.dialog.ConfirmNoCancelDialog
import com.ssafy.guffy.mainfragment.MainFragment
import com.ssafy.guffy.mainfragment.SettingsFragment
import com.ssafy.guffy.models.User
import com.ssafy.guffy.util.Common
import com.ssafy.guffy.util.Common.Companion.passwordRegex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

private const val TAG = "TabItemPwFragment 구피"

class TabItemPwFragment : Fragment() {


    private lateinit var mainActivity: MainActivity
    private lateinit var binding: FragmentTabItemPwBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabItemPwBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = ApplicationClass.sharedPreferences.getString("email", "").toString()
        binding.tabItemPwEmailEt.setText(email)
        binding.tabItemPwEmailEt.isEnabled = false

        val passwordListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!passwordRegex(s.toString())) {
                    binding.tabItemPwNewPwLayout.error = "적절한 비밀번호가 아닙니다."
                } else {
                    binding.tabItemPwNewPwLayout.hint = "알파벳, 숫자, 특수문자 : 5 ~ 15자"
                    binding.tabItemPwNewPwLayout.error = ""
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    when {
                        s.isEmpty() -> {
                            binding.tabItemPwNewPwLayout
                            binding.tabItemPwNewPwLayout.error = "비밀번호를 입력해주세요."
                        }
                    }
                }
            }
        }

        binding.tabItemPwNewPwLayout.editText?.addTextChangedListener(passwordListener)
        binding.tabItemPwNewPwLayout.hint = "알파벳, 숫자, 특수문자 : 5 ~ 15자"
        binding.tabItemPwNewPwLayout.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.tabItemPwNewPwEt.hint = "비밀번호를 입력해주세요"
                if (!passwordRegex(binding.tabItemPwNewPwEt.text.toString())) {
                    binding.tabItemPwNewPwLayout.hint = "알파벳, 숫자, 특수문자 : 5 ~ 15자"
                }
            } else {
                binding.tabItemPwNewPwEt.hint = "알파벳, 숫자, 특수문자 : 5 ~ 15자"
                if (!passwordRegex(binding.tabItemPwNewPwEt.text.toString())) {
                    binding.tabItemPwNewPwLayout.hint = "알파벳, 숫자, 특수문자 : 5 ~ 15자"
                }
            }
        }

        val passwordListener2 = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!passwordRegex(s.toString())) {
                    binding.tabItemPwNewPwConfirmLayout.error = "적절한 비밀번호가 아닙니다."
                } else {
                    binding.tabItemPwNewPwConfirmLayout.hint = "알파벳, 숫자, 특수문자 : 5 ~ 15자"
                    binding.tabItemPwNewPwConfirmLayout.error = ""
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    when {
                        s.isEmpty() -> {
                            binding.tabItemPwNewPwConfirmLayout
                            binding.tabItemPwNewPwConfirmLayout.error = "비밀번호를 입력해주세요."
                        }
                    }
                }
            }
        }


        binding.tabItemPwNewPwConfirmLayout.editText?.addTextChangedListener(passwordListener2)
        binding.tabItemPwNewPwConfirmLayout.hint = "알파벳, 숫자, 특수문자 : 5 ~ 15자"
        binding.tabItemPwNewPwConfirmLayout.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.tabItemPwNewPwConfirmEt.hint = "비밀번호를 입력해주세요"
                if (!passwordRegex(binding.tabItemPwNewPwConfirmEt.text.toString())) {
                    binding.tabItemPwNewPwConfirmLayout.hint = "알파벳, 숫자, 특수문자 : 5 ~ 15자"
                }
            } else {
                binding.tabItemPwNewPwConfirmEt.hint = "알파벳, 숫자, 특수문자 : 5 ~ 15자"
                if (!passwordRegex(binding.tabItemPwNewPwConfirmEt.text.toString())) {
                    binding.tabItemPwNewPwConfirmLayout.hint = "알파벳, 숫자, 특수문자 : 5 ~ 15자"
                }
            }
        }

        binding.tabItemPwSaveBtn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val currentPW = binding.tabItemPwOriginPwEt.text.toString()
                val newPW1 = binding.tabItemPwNewPwEt.text.toString()
                val newPW2 = binding.tabItemPwNewPwConfirmEt.text.toString()
                Log.d(TAG, "onViewCreated: newPW1 = $newPW1, newPW2 = $newPW2")


                val user = retrofitUserInterface.getUser(email).awaitResponse().body() as User
                val dbId = user.id
                var nowAndNewPwIsSame = false
                Log.d(TAG, "디비 저장 id : ${user.id}")

                user.pw = currentPW
                var result = retrofitUserInterface.getLoginResult(user).awaitResponse().body()
                if (result == null) {
                    Log.d(TAG, "잘못된 현재 비밀번호: result == null")
                    nowAndNewPwIsSame = false
                } else {
                    if (result.id == dbId) {
                        Log.d(TAG, " 잘 입력된 현재 비밀번호")
                        nowAndNewPwIsSame = true
                    }
                }
 
                Log.d(TAG, "onViewCreated: $nowAndNewPwIsSame")
                Log.d(TAG, "onViewCreated: $user")
                if (currentPW.isEmpty()) {
                    Common.showAlertDialog(mainActivity, "현재 비밀번호를 입력해주세요", "")
                } else if (newPW1.isEmpty() || newPW2.isEmpty()) {
                    Common.showAlertDialog(mainActivity, "새로운 비밀번호를 입력해주세요", "")
                } else if (newPW1 != newPW2) {
                    Common.showAlertDialog(mainActivity, "비밀번호가 일치하지 않습니다", "")
                } else if (!nowAndNewPwIsSame) {
                    Common.showAlertDialog(mainActivity, "현재 비밀번호가 일치하지 않습니다.", "")
                } else {
                    user.pw = newPW1
                    val result = retrofitUserInterface.updatePW(user).awaitResponse().body() as String
                    if (result == "success") {
                        val dialog = ConfirmNoCancelDialog(
                            object : ConfirmNoCancelDialog.ConfirmNoCancelDialogInterface {
                                override fun onYesButtonClick(id: String) {
                                    mainActivity.openFragment(1)
                                }
                            }, "비밀번호를 변경했습니다",
                            "", ""
                        )
                        dialog.isCancelable = false
                        dialog.show(mainActivity.supportFragmentManager, "태그")
                    }
                }
            }
        }
    }



}