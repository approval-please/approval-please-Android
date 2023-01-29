package com.umc.approval.ui.adapter.search_fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umc.approval.ui.fragment.search.ApprovalPaperTabFragment
import com.umc.approval.ui.fragment.search.CommunityTabFragment
import com.umc.approval.ui.fragment.search.ReportTabFragment
import com.umc.approval.ui.fragment.search.UserTabFragment

class SearchResultVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ApprovalPaperTabFragment()
            1 -> CommunityTabFragment()
            2 -> ReportTabFragment()
            3 -> UserTabFragment()
            else -> ApprovalPaperTabFragment()
        }
    }
}