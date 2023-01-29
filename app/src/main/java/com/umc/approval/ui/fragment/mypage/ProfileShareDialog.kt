package com.umc.approval.ui.fragment.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.umc.approval.R
import com.umc.approval.databinding.ProfileShareDialogBinding

/**
 * MyPage 프로필 공유 Dialog
 * */
    class ProfileShareDialog : BottomSheetDialogFragment() {

    private var _binding : ProfileShareDialogBinding? = null
    private val binding get() = _binding!!

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = ProfileShareDialogBinding.inflate(inflater, container, false)
        val view = binding.root
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
}