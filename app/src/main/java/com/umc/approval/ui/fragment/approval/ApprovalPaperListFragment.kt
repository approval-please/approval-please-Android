package com.umc.approval.ui.fragment.approval

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.approval.R
import com.umc.approval.databinding.FragmentApprovalPaperListBinding
import com.umc.approval.ui.adapter.approval_fragment.ApprovalVPAdapter

class ApprovalPaperListFragment: Fragment() {
    private var _binding : FragmentApprovalPaperListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentApprovalPaperListBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val approvalTabVPAdapter = ApprovalVPAdapter(this)

        binding.vpApprovalPaperList.adapter = approvalTabVPAdapter

        val tabTitleArray = arrayOf(
            "전체",
            "관심",
        )

        // 탭 레이아웃과 뷰페이저 연결
        TabLayoutMediator(binding.tabLayout, binding.vpApprovalPaperList) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()

        binding.stateSelect.setOnClickListener {
            // bottomSheetDialog 객체 생성
            val bottomSheetDialog = ApprovalBottomSheetDialogStatusFragment(binding.stateText.text.toString())
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
            .setFragmentResultListener("status", this) { _, bundle ->
                val result = bundle.getString("result")
                binding.stateText.text = result

                // 리사이클러뷰 아이템 갱신
            }

        childFragmentManager
            .setFragmentResultListener("sort", this) { _, bundle ->
                val result = bundle.getString("result")
                binding.sortText.text = result

                // 리사이클러뷰 아이템 갱신
            }
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
