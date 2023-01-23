package com.umc.approval.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umc.approval.databinding.ActivityCommunityReportBinding

class CommunityReportActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCommunityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityReportBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}