package com.umc.approval.ui.liz

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ApprovalVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> ApprovalAllCategoryViewFragment()
            1 -> ApprovalInterestingCategoryViewFragment()
            else -> ApprovalAllCategoryViewFragment()
        }
    }
}