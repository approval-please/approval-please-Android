package com.umc.approval.ui.fragment.approval

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.databinding.FragmentApprovalAllCategoryViewBinding
import com.umc.approval.ui.activity.DocumentActivity
import com.umc.approval.ui.adapter.approval_fragment.ApprovalPaperListRVAdapter
import com.umc.approval.ui.viewmodel.approval.ApprovalViewModel

class ApprovalAllCategoryViewFragment: Fragment() {

    private var _binding : FragmentApprovalAllCategoryViewBinding? = null
    private val binding get() = _binding!!

    /**mypage view model*/
    private val viewModel by viewModels<ApprovalViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentApprovalAllCategoryViewBinding.inflate(inflater, container, false)
        val view = binding.root

        //live data
        live_data()

        viewModel.get_all_documents("0")

        viewModel.init_all_category_approval()

        /**부서 선택마다 로직 설정*/
        binding.cgAllCategory.setOnCheckedStateChangeListener { _, checkedIds ->
            viewModel.get_all_documents(checkedIds.get(0).toString())
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

    /**live data*/
    private fun live_data() {

        viewModel.approval_all_list.observe(viewLifecycleOwner) {

            val dataRVAdapter = ApprovalPaperListRVAdapter(it)
            val spaceDecoration = VerticalSpaceItemDecoration(20)
            binding.rvApprovalPaper.addItemDecoration(spaceDecoration)
            binding.rvApprovalPaper.adapter = dataRVAdapter
            binding.rvApprovalPaper.layoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

            // 클릭 이벤트 처리
            dataRVAdapter.setOnItemClickListener(object :
                ApprovalPaperListRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: ApprovalPaper, pos: Int) {
                    startActivity(Intent(requireContext(), DocumentActivity::class.java))
                }
            })
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