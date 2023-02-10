package com.umc.approval.ui.fragment.approval

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.umc.approval.databinding.FragmentApprovalBinding
import com.umc.approval.ui.activity.LoginActivity
import com.umc.approval.ui.activity.SearchActivity
import com.umc.approval.ui.activity.UploadActivity
import com.umc.approval.ui.viewmodel.approval.ApprovalViewModel

/**
 * Approval View
 * */
class ApprovalFragment : Fragment() {

    private var _binding : FragmentApprovalBinding? = null
    private val binding get() = _binding!!

    private val approvalViewModel by viewModels<ApprovalViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentApprovalBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.fabAddPost.setOnClickListener{
            // 게시글 작성 화면으로 이동
            startActivity(Intent(requireContext(), UploadActivity::class.java))
        }

        /**Login Activity로 이동*/
        binding.loginButton.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        /**Search Activity로 이동*/
        binding.searchButton.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        // 로그인 상태 확인해서 로그인 버튼 숨김 여부 설정
        approvalViewModel.checkAccessToken()
        approvalViewModel.accessToken.observe(viewLifecycleOwner) {
            binding.loginButton.isVisible = approvalViewModel.accessToken.value != true
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // FragmentManager 참조 가져오기
        childFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainer.id, ApprovalPaperListFragment())
            .commit()
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}