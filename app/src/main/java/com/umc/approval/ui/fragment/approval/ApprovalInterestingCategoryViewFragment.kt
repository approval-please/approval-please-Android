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
import com.umc.approval.databinding.FragmentApprovalInterestingCategoryViewBinding
import com.umc.approval.ui.adapter.approval_fragment.ApprovalPaperListRVAdapter
import com.umc.approval.ui.activity.InterestingDepartmentActivity
import com.umc.approval.ui.viewmodel.approval.ApprovalViewModel

class ApprovalInterestingCategoryViewFragment: Fragment() {
    private var _binding : FragmentApprovalInterestingCategoryViewBinding? = null
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
        _binding = FragmentApprovalInterestingCategoryViewBinding.inflate(inflater, container, false)
        val view = binding.root

        //live data
        live_data()

        //서버로부터 데이터를 받아옴
        viewModel.init_interest_category_approval()

        binding.cgInterestingCategory.setOnCheckedStateChangeListener { _, checkedIds ->
            Log.d("로그", "부서 선택, $checkedIds")
        }

        binding.addInterestCategoryButton.setOnClickListener {
            Log.d("로그", "관심 부서 추가 버튼 클릭")
            val intent = Intent(requireContext(), InterestingDepartmentActivity::class.java)
            startActivity(intent)
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

    //live data
    private fun live_data() {

        viewModel.approval_interest_list.observe(viewLifecycleOwner) {
            val dataRVAdapter = ApprovalPaperListRVAdapter(it)
            val spaceDecoration = VerticalSpaceItemDecoration(20)
            binding.rvApprovalPaper.addItemDecoration(spaceDecoration)
            binding.rvApprovalPaper.adapter = dataRVAdapter
            binding.rvApprovalPaper.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

            // 클릭 이벤트 처리
            dataRVAdapter.setOnItemClickListener(object: ApprovalPaperListRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: ApprovalPaper, pos: Int) {
                    Log.d("로그", "결재 서류 클릭, pos: $pos")
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