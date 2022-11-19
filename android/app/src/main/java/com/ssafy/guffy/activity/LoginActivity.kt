package com.ssafy.guffy.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.guffy.R
import com.ssafy.guffy.databinding.ActivityLoginBinding
import com.ssafy.guffy.fragment.LoginFragment

private lateinit var binding : ActivityLoginBinding
class LoginActivity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 액션바 숨기기
        supportActionBar?.hide()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.login_frame_container, LoginFragment())
            .commit()
    }
}