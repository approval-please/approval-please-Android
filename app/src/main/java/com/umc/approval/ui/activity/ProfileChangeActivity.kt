package com.umc.approval.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umc.approval.databinding.ActivityProfileChangeBinding

class ProfileChangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileChangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileChangeBinding.inflate(layoutInflater)
        val view = binding.root

        /**mypage로 이동*/
        binding.backToProfile.setOnClickListener {
            finish()
        }

        setContentView(view)
    }
}