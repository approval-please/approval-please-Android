package com.umc.approval.ui.fragment.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.community.get.CommunityTok
import com.umc.approval.data.dto.search.get.SearchUserDto
import com.umc.approval.data.dto.search.get.SearchUserInfoDto
import com.umc.approval.databinding.FragmentSearchUserTabBinding
import com.umc.approval.ui.activity.CommunityTokActivity
import com.umc.approval.ui.activity.DocumentActivity
import com.umc.approval.ui.adapter.approval_fragment.ApprovalPaperListRVAdapter
import com.umc.approval.ui.adapter.community_fragment.CommunityTalkItemRVAdapter
import com.umc.approval.ui.adapter.search_fragment.SearchUserRVAdapter
import com.umc.approval.ui.viewmodel.search.SearchKeywordViewModel
import com.umc.approval.ui.viewmodel.search.SearchUserViewModel
import com.umc.approval.util.Participant

class UserTabFragment: Fragment() {

    private var _binding : FragmentSearchUserTabBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SearchUserViewModel>()
    private val keywordViewModel by activityViewModels<SearchKeywordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchUserTabBinding.inflate(inflater, container, false)
        val view = binding.root

        setLikeList()  // 리사이클러뷰 아이템 & 어댑터 설정

        return view
    }

    private fun setLikeList() {
        val like: ArrayList<Participant> = arrayListOf()

        // query(검색어) 상태 변화시
        keywordViewModel.search_keyword.observe(viewLifecycleOwner) {
            viewModel.get_user()
        }

        // 서버에서 데이터를 받아오면 뷰에 적용하는 라이브 데이터
        viewModel.report.observe(viewLifecycleOwner) {
            val participantRVAdapter = SearchUserRVAdapter(it)
            binding.rvLike.adapter = participantRVAdapter
            binding.rvLike.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)


            // 클릭 이벤트 처리
            participantRVAdapter?.setOnItemClickListener(object: SearchUserRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: SearchUserInfoDto, pos: Int) {
                    Log.d("유저 선택",data.toString())
                }
            })
        }

    }
}