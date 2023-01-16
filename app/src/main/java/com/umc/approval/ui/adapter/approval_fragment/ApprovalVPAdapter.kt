package com.umc.approval.ui.adapter.approval_fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umc.approval.ui.fragment.approval.ApprovalAllCategoryViewFragment
import com.umc.approval.ui.fragment.approval.ApprovalInterestingCategoryViewFragment

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