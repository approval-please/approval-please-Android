package com.umc.approval.ui.fragment.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.community.get.CommunityReport
import com.umc.approval.data.dto.community.get.CommunityReportDocumentDto
import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.databinding.FragmentMypageCommentBinding
import com.umc.approval.ui.activity.DocumentActivity
import com.umc.approval.ui.adapter.approval_fragment.ApprovalPaperListRVAdapter
import com.umc.approval.ui.adapter.community_fragment.CommunityReportItemRVAdapter
import com.umc.approval.ui.adapter.community_fragment.CommunityTalkItemRVAdapter
import com.umc.approval.ui.fragment.approval.ApprovalBottomSheetDialogStatusFragment
import com.umc.approval.ui.viewmodel.mypage.MyPageCommentViewModel
import com.umc.approval.ui.viewmodel.mypage.MypageViewModel

/*
  MyPage 댓글 tab View
 */
class MypageCommentFragment : Fragment() {

    private var _binding : FragmentMypageCommentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MyPageCommentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMypageCommentBinding.inflate(inflater, container, false)
        val view = binding.root

        var state : Int? = null
        var type : Int? = null

        getApproval(type, state)

        binding.cgFilter.setOnCheckedStateChangeListener { chipGroup, checkedIds ->
            Log.d("로그", "서류 종류 선택, $checkedIds")
            // checkedIds에 따라 API 호출, 리사이클러뷰 갱신
            when(chipGroup.checkedChipId){
                binding.chipApproval.id -> {
                    type = null
                    getApproval(type, state)
                }
                binding.chipTok.id -> {
                    type = 0
                    getTok(type, state)
                }
                binding.chipReport.id -> {
                    type = 1
                    getReport(type, state)
                }
            }
        }

        binding.stateSelect.setOnClickListener {
            val bottomSheetDialog = ApprovalBottomSheetDialogStatusFragment(binding.stateText.text.toString())
            bottomSheetDialog.setStyle(
                DialogFragment.STYLE_NORMAL,
                R.style.RoundCornerBottomSheetDialogTheme
            )
            bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
        }

        childFragmentManager
            .setFragmentResultListener("status", this) { _, bundle ->
                val result = bundle.getString("result")
                binding.stateText.text = result

                // 리사이클러뷰 아이템 갱신
                Log.d("status", result.toString())
                when(result){
                    "상태 전체" -> { state = null }
                    "승인됨" -> { state = 0 }
                    "반려됨" -> { state = 1 }
                    "결재 대기중" -> { state = 2 }
                }
                when(type){
                    null -> { getApproval(type, state) }
                    0 -> { getTok(type, state) }
                    1 -> { getReport(type, state) }
                }
            }
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

    /* 결재 서류 불러 오는 함수 */
    private fun getApproval(type : Int?, state : Int?){
        viewModel.init_my_comments()
        viewModel.get_my_comments(type, state)
        viewModel.comment.observe(viewLifecycleOwner){
            binding.rvMypageComment.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            if(it.documentContent is ApprovalPaperDto){
                val paperRVAdapter = ApprovalPaperListRVAdapter(it.documentContent)
                paperRVAdapter?.notifyDataSetChanged()
                binding.rvMypageComment.adapter = paperRVAdapter
            }
            else{
                Log.d("error", "approval_content data 없음")
            }
        }
    }
    /* 결재 톡톡 불러 오는 함수 */
    private fun getTok(type : Int?, state : Int?){
        viewModel.get_my_comments(type, state)
        viewModel.comment.observe(viewLifecycleOwner){
            binding.rvMypageComment.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            if(it.toktokContent is CommunityTokDto){
                val talkRVAdapter = CommunityTalkItemRVAdapter(it.toktokContent)
                talkRVAdapter?.notifyDataSetChanged()
                binding.rvMypageComment.adapter = talkRVAdapter
            }
            else{
                Log.d("error", "tok_content data 없음")
            }
        }
    }
    /* 결재 보고서 불러 오는 함수 */
    private fun getReport(type : Int?, state : Int?){
        viewModel.get_my_comments(type, state)
        viewModel.comment.observe(viewLifecycleOwner){
            binding.rvMypageComment.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            if(it.reportContent is CommunityReportDto){
                val reportRVAdapter = CommunityReportItemRVAdapter(it.reportContent)
                reportRVAdapter?.notifyDataSetChanged()
                binding.rvMypageComment.adapter = reportRVAdapter
            }
            else{
                Log.d("error", "report_content data 없음")
            }
        }
    }
}