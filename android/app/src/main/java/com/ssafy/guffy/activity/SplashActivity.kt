package com.ssafy.guffy.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.ssafy.guffy.ApplicationClass
import com.ssafy.guffy.R
import com.ssafy.guffy.databinding.ActivitySplashBinding
import kotlinx.coroutines.*

private lateinit var binding : ActivitySplashBinding
private const val TAG = "SplashActivity 구피"
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        CoroutineScope(Dispatchers.Main).launch {
            openLoginActivity()
        }

    }

    private suspend fun openLoginActivity() {
        delay(1000)
        var autoLogin = ApplicationClass.sharedPreferences.getBoolean("autoLogin",false)
        Log.d(TAG, "openLoginActivity: 현재 sharedPreference에 저장된 autoLogin = ${autoLogin?:"업는디용"}")
        if(!autoLogin){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }else{
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }

    }
}