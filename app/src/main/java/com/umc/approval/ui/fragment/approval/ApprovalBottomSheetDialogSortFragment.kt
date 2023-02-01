package com.umc.approval.ui.fragment.approval

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.umc.approval.R
import com.umc.approval.databinding.FragmentApprovalBottomSheetDialogSortBinding
import org.jsoup.select.Evaluator.Id
import kotlin.reflect.typeOf

class ApprovalBottomSheetDialogSortFragment(private val preSelect: String) : BottomSheetDialogFragment() {
    private var _binding : FragmentApprovalBottomSheetDialogSortBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApprovalBottomSheetDialogSortBinding.inflate(inflater, container, false)
        val view = binding.root

        // 선택되어있는 항목의 텍스트 색깔만 메인 컬러로
        when (preSelect) {
            binding.dialogItemLatest.text -> {
                binding.dialogItemLatest.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
                binding.dialogItemPopular.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_black_color))
            }

            binding.dialogItemPopular.text -> {
                binding.dialogItemLatest.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_black_color))
                binding.dialogItemPopular.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            }
        }

        binding.rgSelector.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.dialog_item_latest ->  {
                    val result = "최신순"
                    setFragmentResult("sort", bundleOf("result" to result))
                    dismiss()
                }
                R.id.dialog_item_popular-> {
                    val result = "인기순"
                    setFragmentResult("sort", bundleOf("result" to result))
                    dismiss()
                }
            }
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