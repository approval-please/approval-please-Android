package com.umc.approval.check.collie
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
import com.umc.approval.data.dto.community.get.CommunityReport
import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTok
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.databinding.FragmentOtherpageCommunityBinding
import com.umc.approval.ui.activity.CommunityReportActivity
import com.umc.approval.ui.activity.CommunityTokActivity
import com.umc.approval.ui.activity.DocumentActivity
import com.umc.approval.ui.adapter.community_fragment.CommunityReportItemRVAdapter
import com.umc.approval.ui.adapter.community_fragment.CommunityTalkItemRVAdapter
import com.umc.approval.ui.adapter.community_fragment.CommunityVPAdapter
import com.umc.approval.ui.viewmodel.otherpage.OtherpageCommunityViewModel

/*
 MyPage View
 */
class OtherpageCommunityFragment(val userId : Int) : Fragment() {

    private var _binding : FragmentOtherpageCommunityBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<OtherpageCommunityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOtherpageCommunityBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel.get_other_community(userId, null)
        community_live_data()
        return view
    }

    override fun onStart() {
        super.onStart()
        binding.cgFilter.setOnCheckedStateChangeListener { chipGroup, checkedIds ->
            Log.d("로그", "서류 종류 선택, $checkedIds")
            // checkedIds에 따라 API 호출, 리사이클러뷰 갱신
            when(chipGroup.checkedChipId){
                binding.toktok.id -> {
                    viewModel.get_other_community(userId, null)
                }
                binding.report.id -> {
                    viewModel.get_other_community(userId, 0)
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

    /* community 라이브 데이터 받아오는 함수 */
    fun community_live_data(){
        viewModel.community.observe(viewLifecycleOwner){
            binding.rvOtherpageCommunity.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            if (it.toktokCount != null) {
                val communityTok = CommunityTokDto(it.toktokCount!!, it.toktokContent!!)
                val communityRVAdapter =
                    CommunityTalkItemRVAdapter(communityTok)
                binding.rvOtherpageCommunity.adapter = communityRVAdapter
                communityRVAdapter.itemClick = object : CommunityTalkItemRVAdapter.ItemClick{
                    override fun move_to_tok_activity(v: View, data: CommunityTok, pos: Int) {
                        val intent = Intent(requireContext(), CommunityTokActivity::class.java)
                        intent.putExtra("toktokId", data.toktokId.toString())
                        startActivity(intent)
                    }
                }
            } else {
                val communityReport = CommunityReportDto(it.reportCount!!, it.reportContent!!)
                val communityRVAdapter = CommunityReportItemRVAdapter(communityReport)
                binding.rvOtherpageCommunity.adapter = communityRVAdapter
                communityRVAdapter.itemClick = object : CommunityReportItemRVAdapter.ItemClick{
                    override fun move_to_report_activity(
                        v: View,
                        data: CommunityReport,
                        pos: Int
                    ) {
                        val intent = Intent(requireContext(), CommunityReportActivity::class.java)
                        intent.putExtra("reportId", data.reportId.toString())
                        startActivity(intent)
                    }

                    override fun move_to_document_activity(
                        v: View,
                        data: CommunityReport,
                        pos: Int
                    ) {
                        val intent = Intent(requireContext(), DocumentActivity::class.java)
                        intent.putExtra("documentId", data.document.documentId.toString())
                        startActivity(intent)
                    }
                }
            }
        }
    }
}