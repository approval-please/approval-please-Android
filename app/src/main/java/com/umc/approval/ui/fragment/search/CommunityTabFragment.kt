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
import com.umc.approval.data.dto.community.get.CommunityReport
import com.umc.approval.data.dto.community.get.CommunityTok
import com.umc.approval.databinding.FragmentSearchCommunityTabBinding
import com.umc.approval.ui.activity.CommunityTokActivity
import com.umc.approval.ui.adapter.community_fragment.CommunityTalkItemRVAdapter
import com.umc.approval.ui.fragment.approval.ApprovalBottomSheetDialogSortFragment
import com.umc.approval.ui.fragment.approval.ApprovalBottomSheetDialogStatusFragment
import com.umc.approval.ui.viewmodel.community.CommunityTokViewModel

class CommunityTabFragment: Fragment() {

    private var _binding : FragmentSearchCommunityTabBinding? = null
    private val binding get() = _binding!!

    private lateinit var communityTalkItemRVAdapter: CommunityTalkItemRVAdapter

    private val viewModel by viewModels<CommunityTokViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchCommunityTabBinding.inflate(inflater, container, false)
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

//        binding.sortSelect.setOnClickListener {
//            val bottomSheetDialog = ApprovalBottomSheetDialogSortFragment()
//            bottomSheetDialog.setStyle(
//                DialogFragment.STYLE_NORMAL,
//                R.style.RoundCornerBottomSheetDialogTheme
//            )
//            bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
//        }

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

        viewModel.get_all_toks()
    }

    override fun onResume() {
        super.onResume()

        viewModel.get_all_toks()
    }

    private fun live_data() {

        viewModel.tok_list.observe(viewLifecycleOwner) {
            communityTalkItemRVAdapter = CommunityTalkItemRVAdapter(it)

            val community_item_rv: RecyclerView = binding.rvSearchResultApprovalPaper

            community_item_rv.adapter = communityTalkItemRVAdapter
            community_item_rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            communityTalkItemRVAdapter.itemClick = object : CommunityTalkItemRVAdapter.ItemClick {
                override fun move_to_tok_activity(v: View, data: CommunityTok, pos: Int) {

                    //toktok Id 전달
                    val intent = Intent(requireContext(), CommunityTokActivity::class.java)
                    intent.putExtra("toktokId", data.toktokId.toString())
                    startActivity(intent)
                }
            }
        }
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