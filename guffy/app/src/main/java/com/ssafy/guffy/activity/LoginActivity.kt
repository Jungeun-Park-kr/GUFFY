package com.ssafy.guffy.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.guffy.R
import com.ssafy.guffy.databinding.ActivityLoginBinding
import com.ssafy.guffy.fragment.JoinFragment

private lateinit var binding : ActivityLoginBinding
class LoginActivity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        // 로그인버튼 클릭하면 메인 화면으로 전환
//        binding.loginBtn.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
//        }
//
//
//        // 회원가입 버튼 누르면 프래그먼트 생김
//        binding.joinBtn.setOnClickListener {
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id., JoinFragment())
//                .commit()
//        }
    }
}