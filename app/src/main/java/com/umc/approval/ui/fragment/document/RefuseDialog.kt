package com.umc.approval.ui.fragment.document

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.umc.approval.R
import com.umc.approval.databinding.ApproveDialogBinding
import com.umc.approval.databinding.ProfileShareDialogBinding
import com.umc.approval.databinding.RefuseDialogBinding
import com.umc.approval.ui.activity.DocumentActivity

/**
 * DocumentActivity에서 반려 버튼 누르는 경우 나타나는 Dialog
 * */
    class RefuseDialog : DialogFragment() {

    private var _binding : RefuseDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = RefuseDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onStart() {
        super.onStart()
        binding.noButton.setOnClickListener {
            dismiss()
        }
        binding.yesButton.setOnClickListener {
            if(activity is DocumentActivity){
                (activity as DocumentActivity).changeRefuseButton()
            }
            dismiss()
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