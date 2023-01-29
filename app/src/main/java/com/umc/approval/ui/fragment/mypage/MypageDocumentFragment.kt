package com.umc.approval.ui.fragment.mypage

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.databinding.FragmentMypageDocumentBinding
import com.umc.approval.databinding.FragmentMypageRecordBinding
import com.umc.approval.ui.activity.DocumentActivity
import com.umc.approval.ui.adapter.approval_fragment.ApprovalPaperListRVAdapter
import com.umc.approval.ui.fragment.approval.ApprovalBottomSheetDialogSortFragment
import com.umc.approval.ui.fragment.approval.ApprovalBottomSheetDialogStatusFragment
import com.umc.approval.util.ApprovalPaper

/**
 * MyPage 결재 서류 tab View
 * */
class MypageDocumentFragment : Fragment() {

    private var _binding : FragmentMypageDocumentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMypageDocumentBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.cgFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            Log.d("로그", "서류 종류 선택, $checkedIds")
            // checkedIds에 따라 API 호출, 리사이클러뷰 갱신
        }

        binding.stateSelect.setOnClickListener {
            val bottomSheetDialog = ApprovalBottomSheetDialogStatusFragment()
            bottomSheetDialog.setStyle(
                DialogFragment.STYLE_NORMAL,
                R.style.RoundCornerBottomSheetDialogTheme
            )
            bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
        }

        childFragmentManager
            .setFragmentResultListener("status", this) { requestKey, bundle ->
                val result = bundle.getString("result")
                binding.stateText.text = result

                // 리사이클러뷰 아이템 갱신
            }

        setDocumentList()

        return view
    }

    override fun onStart() {
        super.onStart()
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    /**
     * 결재 서류 리사이클러뷰 아이템 설정
     */
    private fun setDocumentList() {
        val approvalPaperList: ArrayList<com.umc.approval.data.dto.approval.get.ApprovalPaper> = arrayListOf()  // 샘플 데이터

        approvalPaperList.apply{
            add(
                com.umc.approval.data.dto.approval.get.ApprovalPaper(
                    0, 0, "30분전",
                    mutableListOf("https://www.backmarket.co.kr/used-refurbished/iPhone-13-Pro-128GB-Gold-Unlocked/2"),
                    "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("가전", "환경"),
                    1000, 32, 12
                )
            )

            add(
                com.umc.approval.data.dto.approval.get.ApprovalPaper(
                    1, 0, "30분전",
                    mutableListOf(),
                    "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "가구"),
                    1000, 32, 12
                )
            )

            add(
                com.umc.approval.data.dto.approval.get.ApprovalPaper(
                    0, 0, "30분전",
                    mutableListOf("https://www.backmarket.co.kr/used-refurbished/iPhone-13-Pro-128GB-Gold-Unlocked/2"),
                    "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("환경"),
                    1000, 32, 12
                )
            )

            add(
                com.umc.approval.data.dto.approval.get.ApprovalPaper(
                    1, 0, "30분전",
                    mutableListOf(),
                    "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계"),
                    1000, 32, 12
                )
            )

            add(
                com.umc.approval.data.dto.approval.get.ApprovalPaper(
                    2, 0, "30분전",
                    mutableListOf("https://www.backmarket.co.kr/used-refurbished/iPhone-13-Pro-128GB-Gold-Unlocked/2"),
                    "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경"),
                    1000, 32, 12
                )
            )
        }

        val dataRVAdapter = ApprovalPaperListRVAdapter(approvalPaperList)
        val spaceDecoration = VerticalSpaceItemDecoration(40)
        binding.rvMypageDocument.addItemDecoration(spaceDecoration)
        binding.rvMypageDocument.adapter = dataRVAdapter
        binding.rvMypageDocument.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        // 클릭 이벤트 처리
        dataRVAdapter.setOnItemClickListener(object: ApprovalPaperListRVAdapter.OnItemClickListner {
            override fun onItemClick(v: View, data: com.umc.approval.data.dto.approval.get.ApprovalPaper, pos: Int) {
                startActivity(Intent(requireContext(), DocumentActivity::class.java))
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