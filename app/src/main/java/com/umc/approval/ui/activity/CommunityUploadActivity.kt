package com.umc.approval.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.approval.databinding.ActivityCommunityUploadBinding
import com.umc.approval.ui.adapter.community_upload_activity.CommunityUploadVPAdapter

class CommunityUploadActivity : AppCompatActivity() {

    private val binding: ActivityCommunityUploadBinding by lazy {
        ActivityCommunityUploadBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityCommunityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val communityUploadVPAdapter = CommunityUploadVPAdapter(this)
        binding.uploadTabVp.adapter = communityUploadVPAdapter

        val tabTitleArray = arrayOf(
            "결재톡톡",
            "결재보고서"
        )

        TabLayoutMediator(binding.uploadCommunityTab, binding.uploadTabVp){ tab,position->
            tab.text = tabTitleArray[position]
        }.attach()

    }

    fun changeTitle(s: String) {
        binding.uploadTitleTv.text = s
    }
}