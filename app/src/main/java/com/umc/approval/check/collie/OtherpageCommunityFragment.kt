package com.umc.approval.check.collie
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.FragmentOtherpageCommunityBinding
import com.umc.approval.ui.adapter.community_fragment.CommunityReportItemRVAdapter
import com.umc.approval.ui.adapter.community_fragment.CommunityTalkItemRVAdapter
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
        community_live_data(null)
        return view
    }

    override fun onStart() {
        super.onStart()
        viewModel.init_other_community()
        binding.cgFilter.setOnCheckedStateChangeListener { chipGroup, checkedIds ->
            Log.d("로그", "서류 종류 선택, $checkedIds")
            // checkedIds에 따라 API 호출, 리사이클러뷰 갱신
            when(chipGroup.checkedChipId){
                binding.toktok.id -> {
                    viewModel.get_other_community(userId, null)
                    community_live_data(null)
                }
                binding.report.id -> {
                    viewModel.get_other_community(userId, 0)
                    community_live_data(0)
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
    fun community_live_data(postType : Int?){
        viewModel.community.observe(viewLifecycleOwner){
            binding.rvOtherpageCommunity.layoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            when(postType){
                null -> {
                    val communityRVAdapter = CommunityTalkItemRVAdapter(it.toktokContent!!)
                    binding.rvOtherpageCommunity.adapter = communityRVAdapter
                }
                0 -> {
                    val communityRVAdapter = CommunityReportItemRVAdapter(it.reportContent!!)
                    binding.rvOtherpageCommunity.adapter = communityRVAdapter
                }
            }
        }
    }
}