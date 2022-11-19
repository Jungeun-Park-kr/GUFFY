package com.ssafy.guffy.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.ssafy.guffy.R
import com.ssafy.guffy.databinding.ActivityChattingBinding
import com.ssafy.guffy.databinding.ActivityLoginBinding

private const val TAG = "ChattingActivity 구피"
private lateinit var binding : ActivityChattingBinding
class ChattingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()
        var nickname = "경찰서에 잡혀온 티모"

        // 앱 실행중에 액션바 title을 바꾸려면
        supportActionBar?.title = nickname

        binding.chattingSendMessageBtn.setOnClickListener {
            // 서버로 전송

            // 입력창 비워주기
           binding.chattingSendMessageTv.setText("")
        }
    }

    // optionsMenu 생성
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chatting_options_menu, menu)
        return true
    }

    // options 메뉴 아이템 선택시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 친구 연결 끊기

        // 메인 화면으로 이동
        startActivity(Intent(this, MainActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}