package com.umc.approval.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.umc.approval.databinding.ActivitySplashBinding
import com.umc.approval.ui.viewmodel.SplashViewModel
import com.umc.approval.ui.viewmodel.comment.CommentViewModel

/**
 * Splash 화면 구성 뷰
 * */
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.checkAccessToken()

        viewModel.accessToken.observe(this) {
            if (it == false) {
                Handler(Looper.myLooper()!!).postDelayed({
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }, 1500)
            } else {
                Handler(Looper.myLooper()!!).postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, 1500)
            }
        }
    }
}