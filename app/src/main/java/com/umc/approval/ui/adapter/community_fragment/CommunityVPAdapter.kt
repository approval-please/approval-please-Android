package com.umc.approval.ui.adapter.community_fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umc.approval.ui.fragment.community.CommunityAllFragment
import com.umc.approval.ui.fragment.community.CommunityReportFragment
import com.umc.approval.ui.fragment.community.CommunityTalkFragment

class CommunityVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> CommunityAllFragment()
            1 -> CommunityTalkFragment()
            else -> CommunityReportFragment()
        }
    }
}