package com.ssafy.guffy.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ssafy.guffy.dialog.ConfirmDialog
import com.ssafy.guffy.dialog.ConfirmDialogInterface
import com.ssafy.guffy.R
import com.ssafy.guffy.databinding.ActivityMainBinding
import com.ssafy.guffy.dialog.FindingFriendDialog
import com.ssafy.guffy.fragment.LoginFragment
import com.ssafy.guffy.mainfragment.MainFragment
import com.ssafy.guffy.util.Common
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ConfirmDialogInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 첫 시작은 메인 화면
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout_main, MainFragment())
            .commit()
    }

    /*fun showAlertDialog(title:String, message:String) {
        MaterialAlertDialogBuilder(this@MainActivity)
            .setTitle(title)
            .setMessage(message)
            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to neutral button press
            }
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                // Respond to positive button press
            }
            .show()
    }*/

    fun deleteFriend(title:String, message:String) {
        val dialog = ConfirmDialog(this, "진짜로 삭제하시겠습니까?", "다시 못되돌립니당", "삭제할친구ID_asdf")
        // 알림창이 띄워져있는 동안 배경 클릭 막기
        dialog.isCancelable = false
        dialog.show(this.supportFragmentManager, "ConfirmDialog")
    }

    override fun onYesButtonClick(id: String) {
        TODO("id로 넘어온 친구 삭제하기")
        // id : 삭제할 친구의 id
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

    private suspend fun findingFriend(id: String) { // suspend : CoroutineScope 안에서만 가능함!!
        // TODO : 친구 찾는 작업 여기서 하기 (일단은 2초 딜레이 해둠)
        delay (2000)
    }

    fun logout(){
        // 여기 로그아웃 로직 구현 (ex. preference 지우기)
        //ApplicationClass.sharedPreferencesUtil.deleteUser()

        //화면이동
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent)
    }

}