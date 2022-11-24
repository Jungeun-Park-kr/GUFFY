package com.ssafy.guffy.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.edit
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ssafy.guffy.ApplicationClass
import com.ssafy.guffy.ApplicationClass.Companion.editor
import com.ssafy.guffy.ApplicationClass.Companion.sharedPreferences
import com.ssafy.guffy.dialog.ConfirmDialog
import com.ssafy.guffy.dialog.ConfirmDialogInterface
import com.ssafy.guffy.R
import com.ssafy.guffy.databinding.ActivityMainBinding
import com.ssafy.guffy.dialog.ConfirmNoCancelDialog
import com.ssafy.guffy.dialog.FindingFriendDialog
import com.ssafy.guffy.fragment.LoginFragment
import com.ssafy.guffy.mainfragment.MainFragment
import com.ssafy.guffy.mainfragment.SettingsFragment
import com.ssafy.guffy.util.Common
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "MainActivity_구피1"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        // 네트워크 연결 상태 확인 후 인터넷 없으면 앱 종료
        if(!Common.isNetworkConnected) {
            Log.d(TAG, "onCreate: 네트워크 연결 없음!!")
            val dialog = ConfirmNoCancelDialog(object: ConfirmNoCancelDialog.ConfirmNoCancelDialogInterface {
                override fun onYesButtonClick(id: String) {
                    finishAndRemoveTask() // 앱 종료
                }
            }, "네트워크에 연결상태를 확인해주세요",
                "서비스를 정상적으로 이용할 수 없어 앱을 종료합니다", "")
            dialog.isCancelable = false
            dialog.show(this.supportFragmentManager, "networkUnAvailable")
        } else {
            // 첫 시작은 메인 화면
            openFragment(1)
        }
    }


    suspend fun showFindingFriendDialog() {
        CoroutineScope(Dispatchers.Main).launch {
            // 끝날때까지 기다리기
            val dialog = FindingFriendDialog(this@MainActivity)
            dialog.isCancelable = false // 배경 클릭 막기
            dialog.show(this@MainActivity.supportFragmentManager, "FindingFriendDialog")

            // 여기서 친구 찾아서 추가해주는 작업 하기 (코루틴 쓰기)

                delay (2000)
            //findingFriend("CurrentUserID")
            dialog.dismiss()
        }.join()
    }


    fun logout(){
        // 여기 로그아웃 로직 구현 (ex. preference 지우기)
        Log.d(TAG, "logout: 삭제 전 autoLogin = ${sharedPreferences.getBoolean("autoLogin",false)}")
        sharedPreferences.edit {
            remove("autoLogin")
            apply()
        }

        Log.d(TAG, "logout: 로그아웃 성공")
        Log.d(TAG, "logout: 삭제 후 autoLogin = ${sharedPreferences.getBoolean("autoLogin", false)}")

        //화면이동
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(intent)
    }

    fun openFragment(index:Int) {
        val transaction = supportFragmentManager.beginTransaction()
        when (index) {
            1 -> { // 메인으로 이동
                transaction.replace(R.id.frame_layout_main, MainFragment())
            }
            2 -> { // 설정으로 이동
                transaction.replace(R.id.frame_layout_main, SettingsFragment())
                    .addToBackStack(null)
            }
        }
        transaction.commit()
    }

}