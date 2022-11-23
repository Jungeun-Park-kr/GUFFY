package com.ssafy.guffy.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.guffy.R
import com.ssafy.guffy.databinding.ActivitySplashBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private lateinit var binding : ActivitySplashBinding
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        GlobalScope.launch {
            openLoginActivity()
        }

    }

    private suspend fun openLoginActivity() {
        delay(1000)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}