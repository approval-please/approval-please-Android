package com.umc.approval.ui.fragment.mypage.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayout
import com.umc.approval.R
import com.umc.approval.databinding.FragmentFollowBinding
import com.umc.approval.databinding.FragmentNotificationBinding
import com.umc.approval.databinding.FragmentSettingBinding
import com.umc.approval.ui.activity.MainActivity
import com.umc.approval.ui.viewmodel.login.LoginFragmentViewModel

/**
 * 내 정보 > 설정 Fragment View
 * */
class SettingFragment : Fragment() {

    private var _binding : FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginFragmentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.logout.setOnClickListener {
            viewModel.deleteAccessToken()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.backToMypage.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_settingFragment_to_fragment_mypage)
        }

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