package com.umc.approval.ui.aran

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umc.approval.R
import com.umc.approval.databinding.ActivityCommunityUploadBinding

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