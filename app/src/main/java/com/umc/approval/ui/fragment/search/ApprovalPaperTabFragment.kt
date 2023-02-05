package com.umc.approval.ui.fragment.search

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.community.get.CommunityTok
import com.umc.approval.databinding.FragmentSearchApprovalPaperTabBinding
import com.umc.approval.ui.activity.CommunityTokActivity
import com.umc.approval.ui.activity.DocumentActivity
import com.umc.approval.ui.adapter.approval_fragment.ApprovalPaperListRVAdapter
import com.umc.approval.ui.adapter.community_fragment.CommunityTalkItemRVAdapter
import com.umc.approval.ui.fragment.approval.ApprovalBottomSheetDialogSortFragment
import com.umc.approval.ui.fragment.approval.ApprovalBottomSheetDialogStatusFragment
import com.umc.approval.ui.viewmodel.search.SearchDocumentViewModel
import com.umc.approval.ui.viewmodel.search.SearchKeywordViewModel
import com.umc.approval.util.Utils

class ApprovalPaperTabFragment: Fragment() {

    private var _binding : FragmentSearchApprovalPaperTabBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SearchDocumentViewModel>()
    private val keywordViewModel by activityViewModels<SearchKeywordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchApprovalPaperTabBinding.inflate(inflater, container, false)
        val view = binding.root

        setApprovalPaperList()  // 리사이클러뷰 데이터 & 어댑터 설정

        binding.categorySelect.setOnClickListener {
            val bottomSheetDialog = SearchBottomSheetDialogCategoryFragment(binding.categoryText.text.toString())
            bottomSheetDialog.setStyle(
                DialogFragment.STYLE_NORMAL,
                R.style.RoundCornerBottomSheetDialogTheme
            )
            bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
        }

        binding.stateSelect.setOnClickListener {
            val bottomSheetDialog = ApprovalBottomSheetDialogStatusFragment(binding.stateText.text.toString())
            bottomSheetDialog.setStyle(
                DialogFragment.STYLE_NORMAL,
                R.style.RoundCornerBottomSheetDialogTheme
            )
            bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
        }

        binding.sortSelect.setOnClickListener {
            val bottomSheetDialog = ApprovalBottomSheetDialogSortFragment(binding.sortText.text.toString())
            bottomSheetDialog.setStyle(
                DialogFragment.STYLE_NORMAL,
                R.style.RoundCornerBottomSheetDialogTheme
            )
            bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
        }

        childFragmentManager
            .setFragmentResultListener("category", this) { requestKey, bundle ->
                val result = bundle.getString("result")
                binding.categoryText.text = result
                if (result == "부서 전체") {
                    viewModel.setCategory(null)
                } else {
                    viewModel.setCategory(Utils.categoryMapReverse[result]!!)
                }
            }

        childFragmentManager
            .setFragmentResultListener("sort", this) { requestKey, bundle ->
                val result = bundle.getString("result")
                binding.sortText.text = result
                viewModel.setSort(Utils.searchSortMap[result]!!)
            }

        childFragmentManager
            .setFragmentResultListener("status", this) { _, bundle ->
                val result = bundle.getString("result")
                binding.stateText.text = result
                viewModel.setState(Utils.statusMap[result]!!)
            }

        return view
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setApprovalPaperList() {

        // category 상태 변화시
        viewModel.category.observe(viewLifecycleOwner) {
            viewModel.get_documents()
        }

        // sortBy(정렬) 상태 변화시
        viewModel.state.observe(viewLifecycleOwner) {
            viewModel.get_documents()
        }

        // sortBy(정렬) 상태 변화시
        viewModel.sort.observe(viewLifecycleOwner) {
            viewModel.get_documents()
        }

        // query(검색어) 상태 변화시
        keywordViewModel.search_keyword.observe(viewLifecycleOwner) {
            viewModel.get_documents()
        }


        viewModel.report.observe(viewLifecycleOwner) {
            val dataRVAdapter = it?.let { it1 -> ApprovalPaperListRVAdapter(it1) }
            binding.rvSearchResultApprovalPaper.adapter = dataRVAdapter
            binding.rvSearchResultApprovalPaper.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

            // 클릭 이벤트 처리
            dataRVAdapter?.setOnItemClickListener(object: ApprovalPaperListRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: ApprovalPaper, pos: Int) {
                    startActivity(Intent(requireContext(), DocumentActivity::class.java))
                }
            })
        }
    }

}