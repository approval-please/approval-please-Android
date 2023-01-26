package com.umc.approval.ui.fragment.approval

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.umc.approval.R
import com.umc.approval.databinding.FragmentApprovalBottomSheetDialogStatusBinding

class ApprovalBottomSheetDialogStatusFragment: BottomSheetDialogFragment() {
    private var _binding : FragmentApprovalBottomSheetDialogStatusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApprovalBottomSheetDialogStatusBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.rgSelector.setOnCheckedChangeListener { group, checkedId ->
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