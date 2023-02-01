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
import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.databinding.FragmentMypageCommunityBinding
import com.umc.approval.databinding.FragmentMypageDocumentBinding
import com.umc.approval.databinding.FragmentMypageRecordBinding
import com.umc.approval.ui.activity.CommunityReportActivity
import com.umc.approval.ui.activity.CommunityTokActivity
import com.umc.approval.ui.activity.DocumentActivity
import com.umc.approval.ui.adapter.approval_fragment.ApprovalPaperListRVAdapter
import com.umc.approval.ui.adapter.community_fragment.CommunityReportItemRVAdapter
import com.umc.approval.ui.adapter.community_fragment.CommunityTalkItemRVAdapter
import com.umc.approval.ui.viewmodel.mypage.MyPageApprovalViewModel
import com.umc.approval.ui.viewmodel.mypage.MyPageCommunityViewModel
import com.umc.approval.ui.viewmodel.mypage.MypageViewModel
import com.umc.approval.util.Utils

/**
 * MyPage 커뮤니티 tab View
 * */
class MypageCommunityFragment : Fragment() {

    private var _binding : FragmentMypageCommunityBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MyPageCommunityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMypageCommunityBinding.inflate(inflater, container, false)
        val view = binding.root

//        binding.cgFilter.setOnCheckedStateChangeListener { _, checkedIds ->
//            Log.d("로그", "서류 종류 선택, $checkedIds")
//
//            // checkedIds에 따라 API 호출, 리사이클러뷰 갱신
//        }

        //버튼 선택시 뷰모델에 데이터 저장하는 이벤트
        binding.tok.setOnClickListener {
            viewModel.setState(1)
        }

        binding.report.setOnClickListener {
            viewModel.setState(0)
        }

        live_data()

        return view
    }

    override fun onStart() {
        super.onStart()

        // 시작 시 로직
        viewModel.get_community(viewModel.state.value)
    }

    override fun onResume() {
        super.onResume()

        //다른 뷰 갔다 왔을때 재 시작 로직
        viewModel.get_community(viewModel.state.value)
    }

    //라이브 데이터
    private fun live_data() {

        //state(postType) 상태 변화시 실행되는 라이브 데이터
        viewModel.state.observe(viewLifecycleOwner) {
            viewModel.get_community(viewModel.state.value)
        }

        //서버에서 데이터 받아오면 뷰에 적용하는 라이브 데이터
        viewModel.community.observe(viewLifecycleOwner) {

            if (it.communityReport == null || it.communityReport!!.isEmpty()) {
                val communityTok = CommunityTokDto(it.toktokCount!!, it.communityTok!!)
                val dataRVAdapter = CommunityTalkItemRVAdapter(communityTok)
                binding.rvMypageCommunity.adapter = dataRVAdapter
                binding.rvMypageCommunity.layoutManager =
                    LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

                // 클릭 이벤트 처리
                dataRVAdapter.itemClick = object : CommunityTalkItemRVAdapter.ItemClick {
                    override fun move_to_tok_activity() {
                        startActivity(Intent(requireContext(), CommunityReportActivity::class.java))
                    }
                }
            } else {
                val communityReport = CommunityReportDto(it.reportCount!!, it.communityReport!!)
                val dataRVAdapter = CommunityReportItemRVAdapter(communityReport)
                binding.rvMypageCommunity.adapter = dataRVAdapter
                binding.rvMypageCommunity.layoutManager =
                    LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

                // 클릭 이벤트 처리
                dataRVAdapter.itemClick = object : CommunityReportItemRVAdapter.ItemClick {
                    override fun move_to_report_activity() {
                        startActivity(Intent(requireContext(), CommunityReportActivity::class.java))
                    }
                    override fun move_to_document_activity() {
                        startActivity(Intent(requireContext(), DocumentActivity::class.java))
                    }
                }
            }

        }
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}