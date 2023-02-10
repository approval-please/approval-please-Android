package com.umc.approval.ui.fragment.mypage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.databinding.FragmentMypageScrapBinding
import com.umc.approval.ui.adapter.approval_fragment.ApprovalPaperListRVAdapter
import com.umc.approval.ui.adapter.community_fragment.CommunityReportItemRVAdapter
import com.umc.approval.ui.adapter.community_fragment.CommunityTalkItemRVAdapter
import com.umc.approval.ui.viewmodel.mypage.MyPageScrapViewModel

/*
 MyPage 스크랩 tab View
*/
class MypageScrapFragment : Fragment() {

    private var _binding : FragmentMypageScrapBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MyPageScrapViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMypageScrapBinding.inflate(inflater, container, false)
        val view = binding.root

        var type : Int? = null

        viewModel.init_my_scraps()
        getApproval(type)

        binding.cgFilter.setOnCheckedStateChangeListener { chipGroup, checkedIds ->
            Log.d("로그", "서류 종류 선택, $checkedIds")
            // checkedIds에 따라 API 호출, 리사이클러뷰 갱신
            when(chipGroup.checkedChipId){
                binding.chipApproval.id -> {
                    type = null
                    getApproval(type)
                }
                binding.chipTok.id -> {
                    type = 0
                    getTok(type)
                }
                binding.chipReport.id -> {
                    type = 1
                    getReport(type)
                }
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
    private fun getApproval(type : Int?){
        viewModel.get_my_scraps(type, null)
        viewModel.scrap.observe(viewLifecycleOwner){
            binding.rvMypageScrap.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            if(it.documentContent is ApprovalPaperDto){
                val paperRVAdapter = ApprovalPaperListRVAdapter(it.documentContent)
                paperRVAdapter?.notifyDataSetChanged()
                binding.rvMypageScrap.adapter = paperRVAdapter
            }
            else{
                Log.d("error", "approval_content data 없음")
            }
        }
    }
    /* 결재 톡톡 불러 오는 함수 */
    private fun getTok(type : Int?){
        viewModel.get_my_scraps(type, null)
        viewModel.scrap.observe(viewLifecycleOwner){
            binding.rvMypageScrap.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            if(it.toktokContent is CommunityTokDto){
                val talkRVAdapter = CommunityTalkItemRVAdapter(it.toktokContent)
                talkRVAdapter?.notifyDataSetChanged()
                binding.rvMypageScrap.adapter = talkRVAdapter
            }
            else{
                Log.d("error", "tok_content data 없음")
            }
        }
    }
    /* 결재 보고서 불러 오는 함수 */
    private fun getReport(type : Int?){
        viewModel.get_my_scraps(type, null)
        viewModel.scrap.observe(viewLifecycleOwner){
            binding.rvMypageScrap.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            if(it.reportContent is CommunityReportDto){
                val reportRVAdapter = CommunityReportItemRVAdapter(it.reportContent)
                reportRVAdapter?.notifyDataSetChanged()
                binding.rvMypageScrap.adapter = reportRVAdapter
            }
            else{
                Log.d("error", "report_content data 없음")
            }
        }
    }
}