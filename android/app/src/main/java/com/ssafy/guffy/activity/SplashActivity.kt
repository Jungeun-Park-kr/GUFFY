package com.ssafy.guffy.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.guffy.R
import com.ssafy.guffy.databinding.ActivitySplashBinding

private lateinit var binding : ActivitySplashBinding
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 액션바 숨기기
        supportActionBar?.hide()

        binding.logo.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }


    }
}