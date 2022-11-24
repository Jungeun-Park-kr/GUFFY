package com.ssafy.guffy.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ssafy.guffy.R
import com.ssafy.guffy.databinding.ActivityLoginBinding
import com.ssafy.guffy.dialog.ConfirmDialog
import com.ssafy.guffy.dialog.ConfirmNoCancelDialog
import com.ssafy.guffy.fragment.LoginFragment
import com.ssafy.guffy.util.CheckNetwork
import com.ssafy.guffy.util.Common
import com.ssafy.guffy.util.Common.Companion.isNetworkConnected
import com.ssafy.guffy.util.Common.Companion.showAlertWithMessageDialog

private const val TAG = "LoginActivity_구피"
private lateinit var binding : ActivityLoginBinding
class LoginActivity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // 네트워크 연결 상태 확인 후 인터넷 없으면 앱 종료
        if(!Common.isNetworkConnected) {
            Log.d(TAG, "onCreate: 네트워크 연결 없음!! 로그인 !!!!")
            val dialog = ConfirmNoCancelDialog(object:ConfirmNoCancelDialog.ConfirmNoCancelDialogInterface {
                override fun onYesButtonClick(id: String) {
                    finishAndRemoveTask() // 앱 종료
                }
            }, "네트워크에 연결상태를 확인해주세요",
                "서비스를 정상적으로 이용할 수 없어 앱을 종료합니다", "")
            dialog.isCancelable = false
            dialog.show(this.supportFragmentManager, "networkUnAvailable")
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_frame_container, LoginFragment())
                .commit()
        }

    }
}