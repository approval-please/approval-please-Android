package com.umc.approval.ui.fragment.search

import android.content.Intent
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
import com.umc.approval.data.dto.community.get.CommunityReport
import com.umc.approval.databinding.FragmentSearchReportTabBinding
import com.umc.approval.ui.activity.CommunityReportActivity
import com.umc.approval.ui.activity.DocumentActivity
import com.umc.approval.ui.adapter.community_fragment.CommunityReportItemRVAdapter
import com.umc.approval.ui.adapter.search_fragment.NoSearchResultRVAdapter
import com.umc.approval.ui.fragment.approval.ApprovalBottomSheetDialogSortFragment
import com.umc.approval.ui.viewmodel.search.SearchKeywordViewModel
import com.umc.approval.ui.viewmodel.search.SearchReportViewModel
import com.umc.approval.util.Utils

class ReportTabFragment: Fragment() {

    private var _binding : FragmentSearchReportTabBinding? = null
    private val binding get() = _binding!!

    //Community Image RV Adapter
    private lateinit var communityReportItemRVAdapter: CommunityReportItemRVAdapter

    private val viewModel by viewModels<SearchReportViewModel>()
    private val keywordViewModel by activityViewModels<SearchKeywordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchReportTabBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.setTag(keywordViewModel.search_keyword.value.toString())
        viewModel.setSort(0)

        live_data()

        binding.categorySelect.setOnClickListener {
            val bottomSheetDialog = SearchBottomSheetDialogCategoryFragment(binding.categoryText.text.toString())
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
                    viewModel.setCategory(18)
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

        return view
    }

    override fun onStart() {
        super.onStart()

        viewModel.setQuery(keywordViewModel.search_keyword.value!!)
        viewModel.get_reports(keywordViewModel.search_keyword.value.toString(), viewModel.tag.value!!, viewModel.category.value, viewModel.sort.value!!)
    }

    private fun live_data() {

        // category 상태 변화시
        viewModel.category.observe(viewLifecycleOwner) {
            noResult()
        }

        // sortBy(정렬) 상태 변화시
        viewModel.sort.observe(viewLifecycleOwner) {
            viewModel.get_reports(keywordViewModel.search_keyword.value.toString(), viewModel.tag.value!!, viewModel.category.value, viewModel.sort.value!!)
        }

        // query(검색어) 상태 변화시
        keywordViewModel.search_keyword.observe(viewLifecycleOwner) {
            viewModel.get_reports(it, viewModel.tag.value!!, viewModel.category.value, viewModel.sort.value!!)
        }

        // 서버에서 데이터를 받아오면 뷰에 적용하는 라이브 데이터
        viewModel.report.observe(viewLifecycleOwner) {
            if(it?.reportCount==0) {
                noResult()
            }else{
                communityReportItemRVAdapter = CommunityReportItemRVAdapter(it)

                val community_item_rv: RecyclerView = binding.rvSearchResultApprovalPaper

                community_item_rv.adapter = communityReportItemRVAdapter
                community_item_rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

                communityReportItemRVAdapter.itemClick = object : CommunityReportItemRVAdapter.ItemClick {
                    override fun move_to_report_activity(v: View, data: CommunityReport, pos: Int) {
                        //report Id 전달
                        val intent = Intent(requireContext(), CommunityReportActivity::class.java)
                        intent.putExtra("reportId", data.reportId.toString())
                        startActivity(intent)
                    }
                    override fun move_to_document_activity(v: View, data: CommunityReport, pos: Int) {
                        //report Id 전달
                        val intent = Intent(requireContext(), DocumentActivity::class.java)
                        intent.putExtra("documentId", data.document.documentId.toString())
                        startActivity(intent)
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

    private fun noResult() {
        val dataRVAdapter = NoSearchResultRVAdapter(listOf(keywordViewModel.search_keyword.value))
        binding.rvSearchResultApprovalPaper.adapter = dataRVAdapter
        binding.rvSearchResultApprovalPaper.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }
}