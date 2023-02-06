package com.umc.approval.ui.fragment.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.umc.approval.R
import com.umc.approval.databinding.FragmentSearchBottomSheetDialogCategoryBinding

class SearchBottomSheetDialogCategoryFragment(private val preSelect: String): BottomSheetDialogFragment(){
    private var _binding : FragmentSearchBottomSheetDialogCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBottomSheetDialogCategoryBinding.inflate(inflater, container, false)
        val view = binding.root

        // 선택되어있는 항목의 텍스트 색깔만 메인 컬러로
        when (preSelect) {
            binding.dialogAll.text -> binding.dialogAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog0.text -> binding.dialog0.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog1.text -> binding.dialog1.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog2.text -> binding.dialog2.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog3.text -> binding.dialog3.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog4.text -> binding.dialog4.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog5.text -> binding.dialog5.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog6.text -> binding.dialog6.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog7.text -> binding.dialog7.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog8.text -> binding.dialog8.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog9.text -> binding.dialog9.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog10.text -> binding.dialog10.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog11.text -> binding.dialog11.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog12.text -> binding.dialog12.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog13.text -> binding.dialog13.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog14.text -> binding.dialog14.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog15.text -> binding.dialog15.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog16.text -> binding.dialog16.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            binding.dialog17.text -> binding.dialog17.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
        }

        binding.rgSelector.setOnCheckedChangeListener { _, checkedId ->
            var result: String

            when (checkedId) {
                R.id.dialog_all -> result = "부서 전체"
                R.id.dialog_0 -> result = "디지털기기"
                R.id.dialog_1 -> result = "생활가전"
                R.id.dialog_2 -> result = "생활용품"
                R.id.dialog_3 -> result = "가구/인테리어"
                R.id.dialog_4 -> result = "주방/건강"
                R.id.dialog_5 -> result = "출산/유아동"
                R.id.dialog_6 -> result = "패션의류/잡화"
                R.id.dialog_7 -> result = "뷰티/미용"
                R.id.dialog_8 -> result = "스포츠/헬스/레저"
                R.id.dialog_9 -> result = "취미/게임/완구"
                R.id.dialog_10 -> result = "문구/오피스"
                R.id.dialog_11 -> result = "도서/음악"
                R.id.dialog_12 -> result = "티켓/교환권"
                R.id.dialog_13 -> result = "식품"
                R.id.dialog_14 -> result = "동물/식물"
                R.id.dialog_15 -> result = "영화/공연"
                R.id.dialog_16 -> result = "자동차/공구"
                R.id.dialog_17 -> result = "기타 물품"
                else -> result = "부서 전체"
            }

            setFragmentResult("category", bundleOf("result" to result))
            dismiss()

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
}