package com.umc.approval.ui.fragment.search

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.databinding.FragmentSearchReportTabBinding
import com.umc.approval.ui.activity.CommunityReportActivity
import com.umc.approval.ui.activity.CommunityTokActivity
import com.umc.approval.ui.activity.DocumentActivity
import com.umc.approval.ui.adapter.community_fragment.CommunityReportItemRVAdapter
import com.umc.approval.ui.adapter.community_fragment.CommunityTalkItemRVAdapter
import com.umc.approval.ui.fragment.approval.ApprovalBottomSheetDialogSortFragment
import com.umc.approval.ui.viewmodel.community.CommunityReportViewModel

class ReportTabFragment: Fragment() {

    private var _binding : FragmentSearchReportTabBinding? = null
    private val binding get() = _binding!!

    //Community Image RV Adapter
    private lateinit var communityReportItemRVAdapter: CommunityReportItemRVAdapter

    private val viewModel by viewModels<CommunityReportViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchReportTabBinding.inflate(inflater, container, false)
        val view = binding.root

        live_data()

        binding.categorySelect.setOnClickListener {
            val bottomSheetDialog = SearchBottomSheetDialogCategoryFragment()
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
            }

        childFragmentManager
            .setFragmentResultListener("categoryList", this) { requestKey, bundle ->
                val category = bundle.getIntegerArrayList("category")
                Log.d("로그", "카테고리 인덱스: $category")

                // 체크한 카테고리 정보(category)에 따라 리사이클러뷰 아이템 갱신
            }

        childFragmentManager
            .setFragmentResultListener("sort", this) { requestKey, bundle ->
                val result = bundle.getString("result")
                binding.sortText.text = result

                // 리사이클러뷰 아이템 갱신
            }

        return view
    }

    /**시작시 로그인 상태 확인*/
    override fun onStart() {
        super.onStart()

        /**AccessToken 확인해서 로그인 상태인지 아닌지 확인*/
        viewModel.checkAccessToken()

        viewModel.get_all_reports()
    }

    override fun onResume() {
        super.onResume()

        /**AccessToken 확인해서 로그인 상태인지 아닌지 확인*/
        viewModel.checkAccessToken()

        viewModel.get_all_reports()
    }

    private fun live_data() {

        viewModel.report_list.observe(viewLifecycleOwner) {

            communityReportItemRVAdapter = CommunityReportItemRVAdapter(it)

            val community_item_rv: RecyclerView = binding.rvSearchResultApprovalPaper

            community_item_rv.adapter = communityReportItemRVAdapter
            community_item_rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            communityReportItemRVAdapter.itemClick = object : CommunityReportItemRVAdapter.ItemClick {
                override fun move_to_report_activity() {
                    startActivity(Intent(requireContext(), CommunityReportActivity::class.java))
                }
                override fun move_to_document_activity() {
                    startActivity(Intent(requireContext(), DocumentActivity::class.java))
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

    // 아이템 간 간격 조절 기능
    inner class VerticalSpaceItemDecoration(private val height: Int) :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = height
        }
    }
}