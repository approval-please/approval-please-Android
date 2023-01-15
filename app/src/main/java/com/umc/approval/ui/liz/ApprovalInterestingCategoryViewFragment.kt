package com.umc.approval.ui.liz

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
import com.umc.approval.databinding.FragmentApprovalInterestingCategoryViewBinding

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

        binding.cgInterestingCategory.setOnCheckedStateChangeListener { group, checkedIds ->
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
            add(ApprovalPaper(true, true, "스타벅스 텀블러 1", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 3, 2, "디지털기기", "5시간 전"))
            add(ApprovalPaper(false, false, "스타벅스 텀블러 2", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 4, 3, 2, "디지털기기", "5시간 전"))
            add(ApprovalPaper(true, true, "스타벅스 텀블러 3", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 1, 10, "디지털기기", "5시간 전"))
            add(ApprovalPaper(false, false, "스타벅스 텀블러 4", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 3, 2, "디지털기기", "5시간 전"))
            add(ApprovalPaper(true, false, "스타벅스 텀블러 5", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 3, 5, "디지털기기", "5시간 전"))
            add(ApprovalPaper(false, false, "스타벅스 텀블러 6", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 3, 2, "디지털기기", "5시간 전"))
            add(ApprovalPaper(true, true, "스타벅스 텀블러 7", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 3, 2, "디지털기기", "5시간 전"))
        }

        val dataRVAdapter = ApprovalPaperListRVAdapter(approvalPaperList)
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