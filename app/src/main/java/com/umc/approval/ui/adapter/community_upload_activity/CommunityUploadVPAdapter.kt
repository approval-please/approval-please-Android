package com.umc.approval.ui.adapter.community_upload_activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umc.approval.ui.activity.CommunityUploadActivity
import com.umc.approval.ui.fragment.community.CommunityUploadReportFragment
import com.umc.approval.ui.fragment.community.CommunityUploadTalkFragment

class CommunityUploadVPAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){

    lateinit var communityUploadActivity: CommunityUploadActivity

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                CommunityUploadTalkFragment()
            }
            1 -> {
                CommunityUploadReportFragment()
            }
            else -> CommunityUploadReportFragment()
        }
    }
}