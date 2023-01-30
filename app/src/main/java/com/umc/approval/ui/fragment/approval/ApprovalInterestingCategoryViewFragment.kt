package com.umc.approval.ui.fragment.approval

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.databinding.FragmentApprovalInterestingCategoryViewBinding
import com.umc.approval.ui.activity.InterestingDepartmentActivity
import com.umc.approval.ui.activity.LoginActivity
import com.umc.approval.ui.viewmodel.approval.ApprovalViewModel
import com.umc.approval.ui.adapter.approval_fragment.ApprovalPaperListRVAdapter
import com.umc.approval.ui.adapter.approval_fragment.CategoryRVAdapter
import com.umc.approval.util.InterestingCategory

class ApprovalInterestingCategoryViewFragment: Fragment() {
    private var _binding : FragmentApprovalInterestingCategoryViewBinding? = null
    private val binding get() = _binding!!

    /**approval view model 초기화*/
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

        //서버로부터 데이터를 받아옴, 데모데이용 나중에 삭제
        viewModel.init_interest_category_approval()

        binding.addInterestCategoryButton.setOnClickListener {
            Log.d("로그", "관심 부서 추가 버튼 클릭")
            val intent = Intent(requireContext(), InterestingDepartmentActivity::class.java)
            startActivity(intent)
        }
        
        setInterestingCategoryList()

        //모든 관심 서류 목록 조회
        viewModel.get_interesting_documents(null)

        //엑세스 토큰이 없으면 로그인으로 이동
        not_has_access_token()

        return view
    }

    /**엑세스 토큰이 없으면 로그인 엑티비티로 이동*/
    private fun not_has_access_token() {
        viewModel.has_accessToken.observe(viewLifecycleOwner) {
            if (!it) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
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

    //live data
    private fun live_data() {

        viewModel.approval_interest_list.observe(viewLifecycleOwner) {
            val dataRVAdapter = ApprovalPaperListRVAdapter(it)
            val heightPx = dpToPx(requireContext(), 9)
            val spaceDecoration = VerticalSpaceItemDecoration(heightPx)
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

    private fun setInterestingCategoryList() {
        val interestingCategory: ArrayList<InterestingCategory> = arrayListOf()  // 샘플 데이터

        interestingCategory.apply{
            add(InterestingCategory("관심 부서 전체", true))
            add(InterestingCategory("디지털 기기", false))
            add(InterestingCategory("생활가전", false))
            add(InterestingCategory("생활용품", false))
            add(InterestingCategory("미용", false))
        }

        val categoryRVAdapter = CategoryRVAdapter(interestingCategory)
        val widthPx = dpToPx(requireContext(), 11)
        val spaceDecoration = HorizontalSpaceItemDecoration(widthPx)
        binding.rvCategory.addItemDecoration(spaceDecoration)
        binding.rvCategory.adapter = categoryRVAdapter
        binding.rvCategory.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        binding.addInterestCategoryButton.setOnClickListener {
            Log.d("로그", "관심 부서 추가 버튼 클릭")
            val intent = Intent(requireContext(), InterestingDepartmentActivity::class.java)
            startActivity(intent)
        }

        // 클릭 이벤트 처리
        categoryRVAdapter.setOnItemClickListener(object: CategoryRVAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: InterestingCategory, pos: Int) {
                Log.d("로그", "카테고리 선택, pos: $pos, data: $data")

                // API 호출하여 ApprovalPaperList 갱신
            }
        })
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

    inner class HorizontalSpaceItemDecoration(private val width: Int) :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.left = width
        }
    }

    // dp -> pixel 단위로 변경
    private fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}