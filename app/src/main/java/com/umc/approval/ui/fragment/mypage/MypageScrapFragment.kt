package com.umc.approval.ui.fragment.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.databinding.FragmentMypageScrapBinding
import com.umc.approval.ui.activity.DocumentActivity
import com.umc.approval.ui.adapter.approval_fragment.ApprovalPaperListRVAdapter
import com.umc.approval.ui.adapter.community_fragment.CommunityTalkItemRVAdapter
import com.umc.approval.ui.viewmodel.mypage.MypageViewModel

/*
 MyPage 스크랩 tab View
*/
class MypageScrapFragment : Fragment() {

    private var _binding : FragmentMypageScrapBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MypageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMypageScrapBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.cgFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            Log.d("로그", "서류 종류 선택, $checkedIds")
            // checkedIds에 따라 API 호출, 리사이클러뷰 갱신
        }

//        val paperRVAdapter = ApprovalPaperListRVAdapter()
//        binding.rvMypageComment.adapter = paperRVAdapter
//        binding.rvMypageComment.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

//        val talkRVAdapter = CommunityTalkItemRVAdapter()
//        binding.rvMypageCommunity.adapter = talkRVAdapter
//        binding.rvMypageCommunity.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
//
//        val reportRVAdapter = CommunityReportItemRVAdapter()
//        binding.rvMypageCommunity.adapter = reportRVAdapter
//        binding.rvMypageCommunity.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        return view
    }

    override fun onStart() {
        super.onStart()
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}