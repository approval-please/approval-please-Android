package com.umc.approval.ui.fragment.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.approval.databinding.FragmentCommunityBinding
import com.umc.approval.ui.adapter.approval_fragment.ApprovalVPAdapter
import com.umc.approval.ui.adapter.community_fragment.CommunityVPAdapter

/**
 * Community View
 * */
class CommunityFragment : Fragment() {

    private var _binding : FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    //view pager RV Adapter
    private lateinit var communityVPAdapter : CommunityVPAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        val view = binding.root

        //view pager와 탭 레이아웃 연결
        connect_view_pager()

        return view
    }

    /**ViewPager Connect*/
    private fun connect_view_pager() {

        communityVPAdapter = CommunityVPAdapter(this)
        binding.viewPager.adapter = communityVPAdapter

        //탭 레이아웃 제목
        val tabTitleArray = arrayOf("전체", "결제톡톡", "결제보고서")

        //탭 레이아웃과 뷰페이저 연결
        TabLayoutMediator(binding.communityTab, binding.viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}