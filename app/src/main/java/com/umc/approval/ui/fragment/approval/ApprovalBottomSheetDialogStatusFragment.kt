package com.umc.approval.ui.fragment.approval

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.umc.approval.R
import com.umc.approval.databinding.FragmentApprovalBottomSheetDialogStatusBinding

class ApprovalBottomSheetDialogStatusFragment(private val preSelect: String): BottomSheetDialogFragment() {
    private var _binding : FragmentApprovalBottomSheetDialogStatusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApprovalBottomSheetDialogStatusBinding.inflate(inflater, container, false)
        val view = binding.root

        // 선택되어있는 항목의 텍스트 색깔만 메인 컬러로
        when (preSelect) {
            binding.dialogItemAllStatus.text -> {
                binding.dialogItemAllStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
                binding.dialogItemApproved.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_black_color))
                binding.dialogItemRejected.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_black_color))
                binding.dialogItemPending.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_black_color))
            }

            binding.dialogItemApproved.text -> {
                binding.dialogItemAllStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_black_color))
                binding.dialogItemApproved.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
                binding.dialogItemRejected.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_black_color))
                binding.dialogItemPending.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_black_color))
            }

            binding.dialogItemRejected.text -> {
                binding.dialogItemAllStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_black_color))
                binding.dialogItemApproved.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_black_color))
                binding.dialogItemRejected.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
                binding.dialogItemPending.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_black_color))
            }

            binding.dialogItemPending.text -> {
                binding.dialogItemAllStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_black_color))
                binding.dialogItemApproved.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_black_color))
                binding.dialogItemRejected.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_black_color))
                binding.dialogItemPending.setTextColor(ContextCompat.getColor(requireContext(), R.color.approval_please_main_color))
            }
        }

        binding.rgSelector.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.dialog_item_all_status ->  {
                    val result = "상태 전체"
                    setFragmentResult("status", bundleOf("result" to result))
                    dismiss()
                }
                R.id.dialog_item_approved -> {
                    val result = "승인됨"
                    setFragmentResult("status", bundleOf("result" to result))
                    dismiss()
                }
                R.id.dialog_item_rejected -> {
                    val result = "반려됨"
                    setFragmentResult("status", bundleOf("result" to result))
                    dismiss()
                }
                R.id.dialog_item_pending -> {
                    val result = "결재 대기중"
                    setFragmentResult("status", bundleOf("result" to result))
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