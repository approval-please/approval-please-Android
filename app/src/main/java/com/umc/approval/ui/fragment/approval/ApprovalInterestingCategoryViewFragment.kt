package com.umc.approval.ui.fragment.approval

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.databinding.FragmentApprovalInterestingCategoryViewBinding
import com.umc.approval.ui.activity.InterestingDepartmentActivity
import com.umc.approval.ui.adapter.approval_fragment.ApprovalPaperListRVAdapter
import com.umc.approval.ui.adapter.approval_fragment.CategoryRVAdapter
import com.umc.approval.util.InterestingCategory

class ApprovalInterestingCategoryViewFragment: Fragment() {
    private var _binding : FragmentApprovalInterestingCategoryViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentApprovalInterestingCategoryViewBinding.inflate(inflater, container, false)
        val view = binding.root

        setApprovalPaperList()  // 리사이클러뷰 데이터 & 어댑터 설정
        setInterestingCategoryList()

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
        val approvalPaperList: ArrayList<ApprovalPaper> = arrayListOf()  // 샘플 데이터

        approvalPaperList.apply{
            add(ApprovalPaper(0, 0, "30분전",
                mutableListOf("https://www.backmarket.co.kr/used-refurbished/iPhone-13-Pro-128GB-Gold-Unlocked/2"),
                "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("가전", "환경"),
                1000, 32, 12))

            add(ApprovalPaper(1, 0, "30분전",
                mutableListOf(),
                "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "가구"),
                1000, 32, 12))

            add(ApprovalPaper(0, 0, "30분전",
                mutableListOf("https://www.backmarket.co.kr/used-refurbished/iPhone-13-Pro-128GB-Gold-Unlocked/2"),
                "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("환경"),
                1000, 32, 12))

            add(ApprovalPaper(1, 0, "30분전",
                mutableListOf(),
                "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계"),
                1000, 32, 12))

            add(ApprovalPaper(2, 0, "30분전",
                mutableListOf("https://www.backmarket.co.kr/used-refurbished/iPhone-13-Pro-128GB-Gold-Unlocked/2"),
                "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경"),
                1000, 32, 12))
        }

        val dataRVAdapter = ApprovalPaperListRVAdapter(approvalPaperList)
        val spaceDecoration = VerticalSpaceItemDecoration(40)
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
        val spaceDecoration = HorizontalSpaceItemDecoration(25)
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
}