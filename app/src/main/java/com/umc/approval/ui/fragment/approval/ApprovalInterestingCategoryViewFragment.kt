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
import com.umc.approval.ui.adapter.approval_fragment.ApprovalPaperListRVAdapter
import com.umc.approval.ui.activity.InterestingDepartmentActivity

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

    private fun setApprovalPaperList() {
        val approvalPaperList: ArrayList<ApprovalPaper> = arrayListOf()  // 샘플 데이터

        approvalPaperList.apply{
            add(ApprovalPaper(0, 0, "30분전",
                mutableListOf("https://www.backmarket.co.kr/used-refurbished/iPhone-13-Pro-128GB-Gold-Unlocked/2"),
                "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                1000, 32, 12))

            add(ApprovalPaper(1, 0, "30분전",
                mutableListOf(),
                "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                1000, 32, 12))

            add(ApprovalPaper(0, 0, "30분전",
                mutableListOf("https://www.backmarket.co.kr/used-refurbished/iPhone-13-Pro-128GB-Gold-Unlocked/2"),
                "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                1000, 32, 12))

            add(ApprovalPaper(1, 0, "30분전",
                mutableListOf(),
                "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                1000, 32, 12))

            add(ApprovalPaper(2, 0, "30분전",
                mutableListOf("https://www.backmarket.co.kr/used-refurbished/iPhone-13-Pro-128GB-Gold-Unlocked/2"),
                "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
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