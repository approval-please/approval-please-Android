package com.umc.approval.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.approval.data.dto.upload.post.TalkUploadDto
import com.umc.approval.databinding.ActivityCommunityUploadBinding
import com.umc.approval.ui.adapter.community_upload_activity.CommunityUploadVPAdapter
import com.umc.approval.ui.viewmodel.community.CommunityUploadViewModel

class CommunityUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityUploadBinding

    val viewModel by viewModels<CommunityUploadViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityUploadBinding.inflate(layoutInflater)
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

        /**액티비티 종료 버튼*/
        binding.uploadCancelBtn.setOnClickListener {
            finish()
        }

        binding.uploadSubmitBtn.setOnClickListener {

            if (viewModel.tabId.value.toString() == "0") {
                val talkUploadDto = TalkUploadDto(0,"hi", link = viewModel.opengraph_list.value)
                viewModel.post_tok(talkUploadDto)
            }
        }
    }

    fun changeTitle(s: String) {
        binding.uploadTitleTv.text = s
    }
}